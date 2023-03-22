package org.royllo.explorer.batch.test.processor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.service.RequestProcessorService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Disabled
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tarodProofServiceMock", "scheduler-disabled"})
public class AddAssetProcessorTest extends BaseTest {

    @Autowired
    RequestService requestService;

    @Autowired
    AssetService assetService;

    @Autowired
    ProofService proofService;

    @Autowired
    RequestProcessorService requestProcessorService;

    @Test
    @DisplayName("Process")
    public void process() {
        // =============================================================================================================
        // "Invalid Royllo coin": we add a proof that can be decoded.
        // TODO test if raw proof returns an error.

        // =============================================================================================================
        // "My Royllo coin": The asset is already in our database and MY_ROYLLO_COIN_RAW_PROOF also.
        // If we add a request to add this proof, we should get an error because the proof already exists.

        // Add the proof
        AddProofRequestDTO myRoylloCoinRequest = requestService.addProof(MY_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // Process the request.
        final RequestDTO myRoylloCoinRequestTreated = requestProcessorService.processRequest(myRoylloCoinRequest);
        assertFalse(myRoylloCoinRequestTreated.isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.getStatus());
        assertEquals("This proof is already registered with proof id: " + MY_ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.getErrorMessage());

        // =============================================================================================================
        // "Unknown Royllo coin": The asset and the proof are not in our database.
        // After processing the request, the asset and the proof must be present in database.
        // We try to add again the same proof, we should get an error.

        // Check that the asset and the proof does not exist.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // Add the request
        AddProofRequestDTO unknownRoylloCoinRequest = requestService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(unknownRoylloCoinRequest);

        // Process the request.
        final RequestDTO unknownRoylloCoinRequestTreated = requestProcessorService.processRequest(unknownRoylloCoinRequest);
        assertTrue(unknownRoylloCoinRequest.isSuccessful());
        assertEquals(SUCCESS, unknownRoylloCoinRequestTreated.getStatus());

        // Check that the asset and the proof now exists.
        assertTrue(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // We add again the same proof, we should get an error.
        final RequestDTO myRoylloCoinRequestTreatedBis = requestProcessorService.processRequest(requestService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF));
        assertFalse(myRoylloCoinRequestTreatedBis.isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreatedBis.getStatus());
        assertEquals("This proof is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID, myRoylloCoinRequestTreatedBis.getErrorMessage());

        // =============================================================================================================
        // "Active Royllo coin" : The asset and the proofs are not in our database.
        // After processing the request, the asset and the proof must be present in database.
        // We should be able to add two other proofs.

        // Check that the asset and the proofs does not exist.
        assertFalse(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 1 and process it.
        AddProofRequestDTO activeRoylloCoinRequest1 = requestService.addProof(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF);
        RequestDTO activeRoylloCoinRequest1Treated = requestProcessorService.processRequest(activeRoylloCoinRequest1);
        assertTrue(activeRoylloCoinRequest1Treated.isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest1Treated.getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 3 and process it.
        AddProofRequestDTO activeRoylloCoinRequest2 = requestService.addProof(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF);
        RequestDTO activeRoylloCoinRequest2Treated = requestProcessorService.processRequest(activeRoylloCoinRequest2);
        assertTrue(activeRoylloCoinRequest2Treated.isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest2Treated.getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 2 and process it.
        AddProofRequestDTO activeRoylloCoinRequest3 = requestService.addProof(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF);
        RequestDTO activeRoylloCoinRequest3Treated = requestProcessorService.processRequest(activeRoylloCoinRequest3);
        assertTrue(activeRoylloCoinRequest3Treated.isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest3Treated.getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());
    }

}
