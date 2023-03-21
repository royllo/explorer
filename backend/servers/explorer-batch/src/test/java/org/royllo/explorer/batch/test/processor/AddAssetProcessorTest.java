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
        // TODO Do this test.

        // =============================================================================================================
        // "My Royllo coin": The asset is already in our database and MY_ROYLLO_COIN_RAW_PROOF also.
        // If we add a request to add this proof, we should get an error because the proof already exists.

        // Add the proof
        AddProofRequestDTO myRoylloCoinRequest = requestService.addProof(MY_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertNotNull(myRoylloCoinRequest.getRequestId());
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // Process the request.
        final RequestDTO myRoylloCoinRequestTreated = requestProcessorService.processRequest(myRoylloCoinRequest);
        assertFalse(myRoylloCoinRequestTreated.isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.getStatus());
        assertEquals("This proof is already registered with proof id: " + MY_ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.getErrorMessage());

        // =============================================================================================================
        // "Unknown Royllo coin": The asset and the proof are not in out database.
        // After processing the request, the asset and the proof must be present in database.
        // We try to add again the same proof, we should get an error.

        // Add the proof
        AddProofRequestDTO unknownRoylloCoinRequest = requestService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(unknownRoylloCoinRequest);
        assertNotNull(unknownRoylloCoinRequest.getRequestId());
        String unknownRoylloCoinRequestId = unknownRoylloCoinRequest.getRequestId();

        // Check that the asset and the proof does not exist.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

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

        // Known Royllo coin.
        // String myRoylloCoinRequestId = myRoylloCoinRequest.getRequestId();
//        AddProofRequestDTO knownRoylloCoinRequest = requestService.addProof(KNOWN_ROYLLO_COIN_RAW_PROOF);
//        assertNotNull(knownRoylloCoinRequest);
//        assertNotNull(knownRoylloCoinRequest.getRequestId());
//        String knownRoylloCoinRequestId = knownRoylloCoinRequest.getRequestId();
//
//        // Unknown Royllo coin.
//        AddProofRequestDTO unknownRoylloCoinRequest = requestService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
//        assertNotNull(unknownRoylloCoinRequest);
//        assertNotNull(unknownRoylloCoinRequest.getRequestId());
//        String unknownRoylloCoinRequestId = unknownRoylloCoinRequest.getRequestId();
//
//        // Active Royllo coin - Proof 1.
//        AddProofRequestDTO activeRoylloCoinRequest1 = requestService.addProof(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF);
//        assertNotNull(activeRoylloCoinRequest1);
//        assertNotNull(activeRoylloCoinRequest1.getRequestId());
//        String activeRoylloCoinRequestId = activeRoylloCoinRequest1.getRequestId();
//
//        // =============================================================================================================
//        // My Royllo coin.
//        final RequestDTO myRoylloCoinRequestTreated = requestProcessorService.processRequest(myRoylloCoinRequest);
//        assertNotNull(myRoylloCoinRequestTreated);
//        assertTrue(myRoylloCoinRequestTreated.isSuccessful());

        // =============================================================================================================
        // Known Royllo coin.

        // =============================================================================================================
        // Unknown Royllo coin.

        // =============================================================================================================
        // Active Royllo coin.

    }

}
