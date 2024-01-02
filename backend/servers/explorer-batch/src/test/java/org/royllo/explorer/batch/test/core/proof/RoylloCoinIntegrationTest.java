package org.royllo.explorer.batch.test.core.proof;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;
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
        final String ROYLLO_COIN_PROOF_ID = sha256(ROYLLO_COIN_RAW_PROOF);

        // We create the request
        AddProofRequestDTO myRoylloCoinRequest = requestService.createAddProofRequest(ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // WeProcess the request - We should have a failure as the proof already exists.
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreated = requestService.getRequest(myRoylloCoinRequest.getId());
        assertTrue(myRoylloCoinRequestTreated.isPresent());
        assertFalse(myRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.get().getStatus());
        assertEquals("This proof is already registered with proof id: " + ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.get().getErrorMessage());

        // We should have the asset metadata in our database and files.
        var client = new OkHttpClient();
        Optional<AssetDTO> asset = assetService.getAssetByAssetId(ROYLLO_COIN_ASSET_ID);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getMetaDataFileName());
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("roylloCoin on mainnet by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

    }

}
