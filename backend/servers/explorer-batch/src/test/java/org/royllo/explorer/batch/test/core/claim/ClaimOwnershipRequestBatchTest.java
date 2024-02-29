package org.royllo.explorer.batch.test.core.claim;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.batch.batch.request.ClaimOwnershipRequestBatch;
import org.royllo.explorer.core.dto.request.ClaimOwnershipRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
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
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("Claim ownership request batch test")
@ActiveProfiles({"scheduler-disabled"})
public class ClaimOwnershipRequestBatchTest extends TestWithMockServers {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Autowired
    ClaimOwnershipRequestBatch claimOwnershipRequestBatch;

    @Autowired
    AssetService assetService;

    @Test
    @DisplayName("Process requests")
    public void processRequests() {
        final String STRAUMAT_USED_ID = "22222222-2222-2222-2222-222222222222";

        // For our test, we add some assets in our database.
        final String TRICKY_ROYLLO_COIN_1_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_1_RAW_PROOF);
        addProofBatch.processRequests();

        // =============================================================================================================
        // Invalid proof used for ownership claim.
        final ClaimOwnershipRequestDTO request1 = requestService.createClaimOwnershipRequest(STRAUMAT_USED_ID, "INVALID_PROOF");
        assertNotNull(request1);
        assertEquals(OPENED, request1.getStatus());

        // Running the batch, we should have an error.
        claimOwnershipRequestBatch.processRequests();
        final Optional<RequestDTO> request1Treated = requestService.getRequest(request1.getId());
        assertTrue(request1Treated.isPresent());
        assertFalse(request1Treated.get().isSuccessful());
        assertEquals(FAILURE, request1Treated.get().getStatus());
        assertEquals("proto: (line 1:17): invalid value for bytes type: \"INVALID_PROOF\"", request1Treated.get().getErrorMessage());

        // =============================================================================================================
        // Adding a proof of an asset not yet in our database.
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final ClaimOwnershipRequestDTO request2 = requestService.createClaimOwnershipRequest(STRAUMAT_USED_ID, UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(request2);
        assertEquals(OPENED, request2.getStatus());

        // Running the batch, we should have an error.
        claimOwnershipRequestBatch.processRequests();
        final Optional<RequestDTO> request2Treated = requestService.getRequest(request2.getId());
        assertTrue(request2Treated.isPresent());
        assertFalse(request2Treated.get().isSuccessful());
        assertEquals(FAILURE, request2Treated.get().getStatus());
        assertEquals("This asset is not in our database, use 'Add proof' menu first", request2Treated.get().getErrorMessage());

        // =============================================================================================================
        // Adding a proof that does not concern an issuance state.
        final String TRICKY_ROYLLO_COIN_2_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(1).getRawProof();

        final ClaimOwnershipRequestDTO request3 = requestService.createClaimOwnershipRequest(STRAUMAT_USED_ID, TRICKY_ROYLLO_COIN_2_RAW_PROOF);
        assertNotNull(request3);
        assertEquals(OPENED, request3.getStatus());

        // Running the batch, we should have an error.
        claimOwnershipRequestBatch.processRequests();
        final Optional<RequestDTO> request3Treated = requestService.getRequest(request3.getId());
        assertTrue(request3Treated.isPresent());
        assertFalse(request3Treated.get().isSuccessful());
        assertEquals(FAILURE, request3Treated.get().getStatus());
        assertEquals("This proof does not concern the asset issuance", request3Treated.get().getErrorMessage());

        // =============================================================================================================
        // Adding an invalid proof.


    }

}
