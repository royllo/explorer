package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Add proof batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class AddProofRequestBatchTest extends BaseTest {

    @Autowired
    RequestService requestService;

    @Autowired
    AssetService assetService;

    @Autowired
    ProofService proofService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Add proof request processing")
    public void batch() {
        // TODO Review this test

        // =============================================================================================================
        // We add an invalid proof that can't be decoded ("INVALID_PROOF").

        // Add the proof
        AddProofRequestDTO invalidProofRequest = requestService.createAddProofRequest("INVALID_PROOF");
        assertNotNull(invalidProofRequest);
        assertEquals(OPENED, invalidProofRequest.getStatus());

        // Process the request.
        addProofBatch.processRequests();
        final Optional<RequestDTO> invalidProofRequestTreated = requestService.getRequest(invalidProofRequest.getId());
        assertTrue(invalidProofRequestTreated.isPresent());
        assertFalse(invalidProofRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, invalidProofRequestTreated.get().getStatus());
        assertEquals("An error occurred while decoding", invalidProofRequestTreated.get().getErrorMessage());

        // =============================================================================================================
        // "My Royllo coin": The asset is already in our database.
        // A request with "MY_ROYLLO_COIN_RAW_PROOF" is also in database (linked to the asset).
        // If we add a request to add this proof, we should get an error because the proof already exists.

        // Add the proof
        AddProofRequestDTO myRoylloCoinRequest = requestService.createAddProofRequest(ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // Process the request.
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreated = requestService.getRequest(myRoylloCoinRequest.getId());
        assertTrue(myRoylloCoinRequestTreated.isPresent());
        assertFalse(myRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.get().getStatus());
        assertEquals("This proof is already registered with proof id: " + ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.get().getErrorMessage());

        // =============================================================================================================
        // "Unknown Royllo coin": The asset and the proof are not in our database.
        // After processing the request, the asset and the proof must be present in database.
        // We try to add again the same proof, we should get an error.

        // Check that the asset and the proof does not exist.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // Add the request
        AddProofRequestDTO unknownRoylloCoinRequest = requestService.createAddProofRequest(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(unknownRoylloCoinRequest);

        // Process the request and test its values after.
        addProofBatch.processRequests();
        final Optional<RequestDTO> unknownRoylloCoinRequestTreated = requestService.getRequest(unknownRoylloCoinRequest.getId());
        assertTrue(unknownRoylloCoinRequestTreated.isPresent());
        assertTrue(unknownRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, unknownRoylloCoinRequestTreated.get().getStatus());
        assertNotNull(unknownRoylloCoinRequestTreated.get().getAsset());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, unknownRoylloCoinRequestTreated.get().getAsset().getAssetId());

        // Check that the asset and the proof now exists.
        assertTrue(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // We add again the same proof, we should get an error.
        AddProofRequestDTO unknownRoylloCoinRequestBis = requestService.createAddProofRequest(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreatedBis = requestService.getRequest(unknownRoylloCoinRequestBis.getId());
        assertTrue(myRoylloCoinRequestTreatedBis.isPresent());
        assertFalse(myRoylloCoinRequestTreatedBis.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreatedBis.get().getStatus());
        assertEquals("This proof is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID, myRoylloCoinRequestTreatedBis.get().getErrorMessage());

        // =============================================================================================================
        // "Active Royllo coin" : The asset and the proofs are not in our database.
        // After processing the request, the asset and the proof must be present in database.
        // We should be able to add two other proofs for the same asset.

        // Check that the asset and the proofs does not exist.
        assertFalse(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 1 and process it.
        AddProofRequestDTO activeRoylloCoinRequest1 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest1Treated = requestService.getRequest(activeRoylloCoinRequest1.getId());
        assertTrue(activeRoylloCoinRequest1Treated.isPresent());
        assertTrue(activeRoylloCoinRequest1Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest1Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 3 and process it.
        AddProofRequestDTO activeRoylloCoinRequest2 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest2Treated = requestService.getRequest(activeRoylloCoinRequest2.getId());
        assertTrue(activeRoylloCoinRequest2Treated.isPresent());
        assertTrue(activeRoylloCoinRequest2Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest2Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 2 and process it.
        AddProofRequestDTO activeRoylloCoinRequest3 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest3Treated = requestService.getRequest(activeRoylloCoinRequest3.getId());
        assertTrue(activeRoylloCoinRequest3Treated.isPresent());
        assertTrue(activeRoylloCoinRequest3Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest3Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());
    }

}
