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
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_2_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_3_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_GENESIS_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_GENESIS_VOUT;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("setOfRoylloNFT test")
@ActiveProfiles({"scheduler-disabled"})
public class SetOfRoylloNFTIntegrationTest extends TestWithMockServers {

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
        // The setOfRoylloNFT (3 of them) are not in our database, we add them.

        // =============================================================================================================
        // We retrieve the data.
        final String SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();

        // NFT1.
        final String SET_OF_ROYLLO_NFT_1_RAW_PROOF = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_1_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_1_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // NFT2.
        final String SET_OF_ROYLLO_NFT_2_RAW_PROOF = SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_2_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_2_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // NFT3.
        final String SET_OF_ROYLLO_NFT_3_RAW_PROOF = SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_3_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_3_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetGroupService.getAssetGroupByAssetGroupId(SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY).isPresent());

        assertFalse(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_1_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_2_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_3_ASSET_ID).isPresent());

        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID).isPresent());

        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add the three proofs.
        AddProofRequestDTO addRoylloNFT1Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_1_RAW_PROOF);
        assertNotNull(addRoylloNFT1Request);
        assertEquals(OPENED, addRoylloNFT1Request.getStatus());

        AddProofRequestDTO addRoylloNFT2Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_2_RAW_PROOF);
        assertNotNull(addRoylloNFT2Request);
        assertEquals(OPENED, addRoylloNFT1Request.getStatus());

        AddProofRequestDTO addRoylloNFT3Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_3_RAW_PROOF);
        assertNotNull(addRoylloNFT3Request);
        assertEquals(OPENED, addRoylloNFT3Request.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();

        assertThat(requestService.getRequest(addRoylloNFT1Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertNotNull(((AddProofRequestDTO) request).getAsset());
                    assertEquals(SET_OF_ROYLLO_NFT_1_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        assertThat(requestService.getRequest(addRoylloNFT2Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertNotNull(((AddProofRequestDTO) request).getAsset());
                    assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        assertThat(requestService.getRequest(addRoylloNFT3Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertNotNull(((AddProofRequestDTO) request).getAsset());
                    assertEquals(SET_OF_ROYLLO_NFT_3_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT).isPresent());

        assertTrue(assetGroupService.getAssetGroupByAssetGroupId(SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY).isPresent());

        assertTrue(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_1_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_2_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_3_ASSET_ID).isPresent());

        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID).isPresent());

        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_2_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check what have been created.
        assertEquals(assetGroupCountBefore + 1, assetGroupRepository.count());
        assertEquals(assetCountBefore + 3, assetRepository.count());
        assertEquals(assetStateCountBefore + 3, assetStateRepository.count());

        // =============================================================================================================
        // We check the value of what has been created.

        // Royllo NFT 1.
        assertThat(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_1_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    var bto1 = bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto1, SET_OF_ROYLLO_NFT_GENESIS_TXID);
                    var bto2 = bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto2, SET_OF_ROYLLO_NFT_ANCHOR_1_TXID);
                    var assetCreated = assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_1_ASSET_ID)
                            .orElseThrow(() -> new AssertionError("Asset not found"));
                    verifyAsset(assetCreated, SET_OF_ROYLLO_NFT_1_ASSET_ID);
                    var assetStateCreated = assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state not found"));
                    verifyAssetState(assetStateCreated,
                            SET_OF_ROYLLO_NFT_1_ASSET_ID,
                            SET_OF_ROYLLO_NFT_ANCHOR_1_TXID,
                            SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT,
                            SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("setOfRoylloNFT by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(SET_OF_ROYLLO_NFT_1_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(SET_OF_ROYLLO_NFT_1_RAW_PROOF, response.body().string());
                            });
                });

        // Royllo NFT 2.
        assertThat(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_2_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    var bto1 = bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto1, SET_OF_ROYLLO_NFT_ANCHOR_2_TXID);
                    var assetCreated = assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_2_ASSET_ID)
                            .orElseThrow(() -> new AssertionError("Asset not found"));
                    verifyAsset(assetCreated, SET_OF_ROYLLO_NFT_2_ASSET_ID);
                    var assetStateCreated = assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state not found"));
                    verifyAssetState(assetStateCreated,
                            SET_OF_ROYLLO_NFT_2_ASSET_ID,
                            SET_OF_ROYLLO_NFT_ANCHOR_2_TXID,
                            SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT,
                            SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("setOfRoylloNFT2 by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(SET_OF_ROYLLO_NFT_2_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(SET_OF_ROYLLO_NFT_2_RAW_PROOF, response.body().string());
                            });
                });

        // Royllo NFT 3.
        assertThat(assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_3_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    var bto1 = bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto1, SET_OF_ROYLLO_NFT_ANCHOR_3_TXID);
                    var assetCreated = assetService.getAssetByAssetIdOrAlias(SET_OF_ROYLLO_NFT_3_ASSET_ID)
                            .orElseThrow(() -> new AssertionError("Asset not found"));
                    verifyAsset(assetCreated, SET_OF_ROYLLO_NFT_3_ASSET_ID);
                    var assetStateCreated = assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state not found"));
                    verifyAssetState(assetStateCreated,
                            SET_OF_ROYLLO_NFT_3_ASSET_ID,
                            SET_OF_ROYLLO_NFT_ANCHOR_3_TXID,
                            SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT,
                            SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("setOfRoylloNFT3 by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(SET_OF_ROYLLO_NFT_3_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(SET_OF_ROYLLO_NFT_3_RAW_PROOF, response.body().string());
                            });
                });
    }

}
