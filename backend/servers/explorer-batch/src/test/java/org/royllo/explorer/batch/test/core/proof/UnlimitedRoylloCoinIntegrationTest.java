package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_ALIAS_LENGTH;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
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

        assertFalse(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_1_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_2_ASSET_ID).isPresent());

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

        assertThat(requestService.getRequest(unlimitedRoylloCoin1Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset()).isNotNull();
                    assertEquals(UNLIMITED_ROYLLO_COIN_1_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        assertThat(requestService.getRequest(unlimitedRoylloCoin2Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset()).isNotNull();
                    assertEquals(UNLIMITED_ROYLLO_COIN_2_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_1_GENESIS_TXID, UNLIMITED_ROYLLO_COIN_1_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_TXID, UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_2_GENESIS_TXID, UNLIMITED_ROYLLO_COIN_2_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_TXID, UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_GENESIS_VOUT).isPresent());

        assertTrue(assetGroupService.getAssetGroupByAssetGroupId(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY).isPresent());

        assertTrue(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_1_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_2_ASSET_ID).isPresent());

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
        assertThat(assetGroupService.getAssetGroupByAssetGroupId(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY))
                .isPresent()
                .get()
                .satisfies(assetGroup -> {
                    assertNotNull(assetGroup.getTweakedGroupKey());
                    assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, assetGroup.getTweakedGroupKey());
                });

        assertThat(assetStateService.getAssetStateByAssetStateId(UNLIMITED_ROYLLO_COIN_1_ASSET_STATE_ID))
                .isPresent()
                .get()
                .satisfies(assetState -> {
                    assertNotNull(assetState.getAsset());
                    assertNotNull(assetState.getAsset().getAssetGroup());
                    assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, assetState.getAsset().getAssetGroup().getTweakedGroupKey());
                });

        assertThat(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_1_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAssetGroup());
                    assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, asset.getAssetGroup().getTweakedGroupKey());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    // Content verification.
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("unlimitedRoylloCoin emission 1 by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(UNLIMITED_ROYLLO_COIN_1_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(UNLIMITED_ROYLLO_COIN_1_RAW_PROOF, response.body().string());
                            });
                });

        assertThat(assetService.getAssetByAssetIdOrAlias(UNLIMITED_ROYLLO_COIN_2_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAssetGroup());
                    assertEquals(UNLIMITED_ROYLLO_COIN_TWEAKED_GROUP_KEY, asset.getAssetGroup().getTweakedGroupKey());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    // Content verification.
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("unlimitedRoylloCoin emission 2 by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(UNLIMITED_ROYLLO_COIN_2_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(UNLIMITED_ROYLLO_COIN_2_RAW_PROOF, response.body().string());
                            });
                });
    }

}
