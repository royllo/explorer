package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("roylloCoin integration test")
@ActiveProfiles({"scheduler-disabled"})
public class RoylloCoinIntegrationTest extends TestWithMockServers {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Autowired
    AssetService assetService;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        // roylloCoin already exists in our database, so we should not be able to add it again.
        final String ROYLLO_COIN_RAW_PROOF = ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();

        // We create the request
        AddProofRequestDTO myRoylloCoinRequest = requestService.createAddProofRequest(ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // We process the request - We should have a failure as the proof already exists.
        addProofBatch.processRequests();
        assertThat(requestService.getRequest(myRoylloCoinRequest.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertFalse(request.isSuccessful());
                    assertEquals(FAILURE, request.getStatus());
                    assertThat(request.getErrorMessage()).contains("Proof already exists in royllo");
                });

        // We should have the asset metadata in our database and files.
        Optional<AssetDTO> asset = assetService.getAssetByAssetIdOrAlias(ROYLLO_COIN_ASSET_ID);
        assertTrue(asset.isPresent());
        assertThat(getFileFromContentServer(asset.get().getMetaDataFileName()))
                .isNotNull()
                .satisfies(response -> {
                    assertEquals(200, response.code());
                    assertEquals("roylloCoin on mainnet by Royllo", response.body().string());
                });

        // We should have the proof file on our content service.
        assertThat(getFileFromContentServer(sha256(ROYLLO_COIN_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                .isNotNull()
                .satisfies(response -> {
                    assertEquals(200, response.code());
                    assertEquals(ROYLLO_COIN_RAW_PROOF, response.body().string());
                });
    }

}
