package org.royllo.explorer.batch.test.core.proof;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.service.proof.ProofService;
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
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.explorer.core.util.mapper.AssetMapperDecorator.ALIAS_LENGTH;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_GENESIS_VOUT;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_1_GENESIS_TXID;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_1_GENESIS_VOUT;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_GENESIS_VOUT;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_2_GENESIS_TXID;
import static org.royllo.test.MempoolData.UNLIMITED_ROYLLO_COIN_2_GENESIS_VOUT;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("unlimitedRoylloCoin test")
@ActiveProfiles({"scheduler-disabled"})
public class UnlimitedRoylloCoinIntegrationTest extends TestWithMockServers {

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetStateRepository assetStateRepository;

    @Autowired
    AssetGroupService assetGroupService;

    @Autowired
    AssetService assetService;

    @Autowired
    BitcoinService bitcoinService;

    @Autowired
    AssetStateService assetStateService;

    @Autowired
    ProofService proofService;

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        // unlimitedRoylloCoin. You will have an asset group and two emissions.
        final String UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();

        // First emission.
        final String UNLIMITED_ROYLLO_COIN_1_RAW_PROOF = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNLIMITED_ROYLLO_COIN_1_PROOF_ID = sha256(UNLIMITED_ROYLLO_COIN_1_RAW_PROOF);
        final String UNLIMITED_ROYLLO_COIN_1_ASSET_STATE_ID = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // Second emission.
        final String UNLIMITED_ROYLLO_COIN_2_RAW_PROOF = UNLIMITED_ROYLLO_COIN_2_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNLIMITED_ROYLLO_COIN_2_PROOF_ID = sha256(UNLIMITED_ROYLLO_COIN_2_RAW_PROOF);
        final String UNLIMITED_ROYLLO_COIN_2_ASSET_STATE_ID = UNLIMITED_ROYLLO_COIN_2_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetGroupService.getAssetGroupByAssetGroupId(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY).isPresent());

        assertFalse(assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_1_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_2_ASSET_ID).isPresent());

        assertFalse(assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());

        assertFalse(proofService.getProofByProofId(UNLIMITED_ROYLLO_COIN_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(UNLIMITED_ROYLLO_COIN_2_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add the three proofs.
        AddProofRequestDTO unlimitedRoylloCoin1Request = requestService.createAddProofRequest(UNLIMITED_ROYLLO_COIN_1_RAW_PROOF);
        assertNotNull(unlimitedRoylloCoin1Request);
        assertEquals(OPENED, unlimitedRoylloCoin1Request.getStatus());

        AddProofRequestDTO unlimitedRoylloCoin2Request = requestService.createAddProofRequest(UNLIMITED_ROYLLO_COIN_2_RAW_PROOF);
        assertNotNull(unlimitedRoylloCoin2Request);
        assertEquals(OPENED, unlimitedRoylloCoin1Request.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();

        final Optional<RequestDTO> unlimitedRoylloCoin1RequestTreated = requestService.getRequest(unlimitedRoylloCoin1Request.getId());
        assertTrue(unlimitedRoylloCoin1RequestTreated.isPresent());
        assertTrue(unlimitedRoylloCoin1RequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, unlimitedRoylloCoin1RequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) unlimitedRoylloCoin1RequestTreated.get()).getAsset());
        assertEquals(UNLIMITED_ROYLLO_COIN_1_ASSET_ID, ((AddProofRequestDTO) unlimitedRoylloCoin1RequestTreated.get()).getAsset().getAssetId());

        final Optional<RequestDTO> unlimitedRoylloCoin2RequestTreated = requestService.getRequest(unlimitedRoylloCoin2Request.getId());
        assertTrue(unlimitedRoylloCoin2RequestTreated.isPresent());
        assertTrue(unlimitedRoylloCoin2RequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, unlimitedRoylloCoin2RequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) unlimitedRoylloCoin2RequestTreated.get()).getAsset());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_1_GENESIS_TXID, UNLIMITED_ROYLLO_COIN_1_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_TXID, UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_2_GENESIS_TXID, UNLIMITED_ROYLLO_COIN_2_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_TXID, UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_GENESIS_VOUT).isPresent());

        assertTrue(assetGroupService.getAssetGroupByAssetGroupId(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY).isPresent());

        assertTrue(assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_1_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_2_ASSET_ID).isPresent());

        assertTrue(assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());

        assertTrue(proofService.getProofByProofId(UNLIMITED_ROYLLO_COIN_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(UNLIMITED_ROYLLO_COIN_2_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertEquals(assetGroupCountBefore + 1, assetGroupRepository.count());
        assertEquals(assetCountBefore + 2, assetRepository.count());
        assertEquals(assetStateCountBefore + 2, assetStateRepository.count());

        // =============================================================================================================
        // Focus on group.
        final Optional<AssetStateDTO> assetState1 = assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_1_ASSET_STATE_ID);
        assertTrue(assetState1.isPresent());
        assertNotNull(assetState1.get().getAsset().getAssetGroup());
        assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, assetState1.get().getAsset().getAssetGroup().getTweakedGroupKey());

        final Optional<AssetStateDTO> assetState2 = assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_2_ASSET_STATE_ID);
        assertTrue(assetState2.isPresent());
        assertNotNull(assetState2.get().getAsset().getAssetGroup());
        assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, assetState2.get().getAsset().getAssetGroup().getTweakedGroupKey());

        final Optional<AssetDTO> asset1 = assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_1_ASSET_ID);
        assertTrue(asset1.isPresent());
        assertNotNull(asset1.get().getAssetIdAlias());
        assertEquals(ALIAS_LENGTH, asset1.get().getAssetIdAlias().length());
        assertNotNull(asset1.get().getAssetGroup());
        assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, asset1.get().getAssetGroup().getTweakedGroupKey());

        final Optional<AssetDTO> asset2 = assetService.getAssetByAssetId(UNLIMITED_ROYLLO_COIN_2_ASSET_ID);
        assertTrue(asset2.isPresent());
        assertNotNull(asset2.get().getAssetIdAlias());
        assertEquals(ALIAS_LENGTH, asset2.get().getAssetIdAlias().length());
        assertNotNull(asset2.get().getAssetGroup());
        assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, asset2.get().getAssetGroup().getTweakedGroupKey());

        // We check if we have the meta-data file for UNLIMITED_ROYLLO_COIN_1_ASSET_ID.
        var client = new OkHttpClient();
        assertNotNull(asset1.get().getMetaDataFileName());
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset1.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("unlimitedRoylloCoin emission 1 by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // And then for UNLIMITED_ROYLLO_COIN_2_ASSET_ID.
        assertNotNull(asset2.get().getMetaDataFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset2.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("unlimitedRoylloCoin emission 2 by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }
    }

}
