package org.royllo.explorer.batch.test.core.claim;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.batch.batch.request.ClaimAssetOwnershipRequestBatch;
import org.royllo.explorer.core.dto.request.ClaimAssetOwnershipRequestDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("Claim ownership request batch test")
@ActiveProfiles({"scheduler-disabled"})
public class ClaimAssetOwnershipRequestBatchTest extends TestWithMockServers {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Autowired
    ClaimAssetOwnershipRequestBatch claimAssetOwnershipRequestBatch;

    @Autowired
    AssetService assetService;

    @Test
    @DisplayName("Process requests")
    public void processRequests() {
        // For our test, we add some assets in our database.
        final String TRICKY_ROYLLO_COIN_1_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNLIMITED_ROYLLO_COIN_1_RAW_PROOF = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNLIMITED_ROYLLO_COIN_2_RAW_PROOF = UNLIMITED_ROYLLO_COIN_2_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_1_RAW_PROOF);
        requestService.createAddProofRequest(UNLIMITED_ROYLLO_COIN_1_RAW_PROOF);
        requestService.createAddProofRequest(UNLIMITED_ROYLLO_COIN_2_RAW_PROOF);
        addProofBatch.processRequests();

        // =============================================================================================================
        // Invalid proof used for ownership claim.
        final ClaimAssetOwnershipRequestDTO request1 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, "INVALID_PROOF");
        assertNotNull(request1);
        assertEquals(OPENED, request1.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request1.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertTrue(request.getErrorMessage().contains("proto: (line 1:17): invalid value for bytes type: \"INVALID_PROOF\""));
                });

        // =============================================================================================================
        // Adding a proof of an asset not yet in our database.
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final ClaimAssetOwnershipRequestDTO request2 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(request2);
        assertEquals(OPENED, request2.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request2.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertTrue(request.getErrorMessage().contains("This asset is not in our database"));
                });

        // =============================================================================================================
        // Adding a proof that does not concern an issuance state.
        final String TRICKY_ROYLLO_COIN_2_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(1).getRawProof();

        final ClaimAssetOwnershipRequestDTO request3 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, TRICKY_ROYLLO_COIN_2_RAW_PROOF);
        assertNotNull(request3);
        assertEquals(OPENED, request3.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request3.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertTrue(request.getErrorMessage().contains("This proof does not concern the asset issuance"));
                });

        // =============================================================================================================
        // Adding a valid proof concerning an issuance state but ownership verification fails.
        final ClaimAssetOwnershipRequestDTO request4 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, TRICKY_ROYLLO_COIN_1_RAW_PROOF);
        assertNotNull(request4);
        assertEquals(OPENED, request4.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request4.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertTrue(request.getErrorMessage().contains("Ownership proof is not valid"));
                });

        // =============================================================================================================
        // Adding a valid proof concerning an issuance state but impossible to decode.
        final ClaimAssetOwnershipRequestDTO request5 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, UNLIMITED_ROYLLO_COIN_1_RAW_PROOF);
        assertNotNull(request5);
        assertEquals(OPENED, request5.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request5.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertTrue(request.getErrorMessage().contains("Invalid!"));
                });

        // =============================================================================================================
        // Adding a valid proof concerning an issuance state and ownership verification is successful!s
        final ClaimAssetOwnershipRequestDTO request6 = requestService.createClaimAssetOwnershipRequest(STRAUMAT_USER_USER_ID, UNLIMITED_ROYLLO_COIN_2_RAW_PROOF);
        assertNotNull(request6);
        assertEquals(OPENED, request6.getStatus());

        // Running the batch, we should have an error.
        claimAssetOwnershipRequestBatch.processRequests();
        assertThat(requestService.getRequest(request6.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    // For security reasons, we remove the proof value in the request at the end of the process.
                    assertNull(((ClaimAssetOwnershipRequestDTO) request).getProofWithWitness());
                });

        // =============================================================================================================
        // We check that the id has been updated.
        assertThat(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_1_ASSET_ID))
                .isPresent()
                .get()
                .matches(asset -> asset.getCreator().getUserId().equals(ANONYMOUS_USER_ID));

        assertThat(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_2_ASSET_ID))
                .isPresent()
                .get()
                .matches(asset -> asset.getCreator().getUserId().equals(STRAUMAT_USER_USER_ID));
    }

}
