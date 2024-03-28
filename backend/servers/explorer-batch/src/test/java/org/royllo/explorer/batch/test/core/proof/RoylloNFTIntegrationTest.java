package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
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
import static org.royllo.test.MempoolData.ROYLLO_NFT_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.ROYLLO_NFT_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.ROYLLO_NFT_GENESIS_TXID;
import static org.royllo.test.MempoolData.ROYLLO_NFT_GENESIS_VOUT;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("roylloNFT integration test")
@ActiveProfiles({"scheduler-disabled"})
public class RoylloNFTIntegrationTest extends TestWithMockServers {

    @Autowired
    BitcoinTransactionOutputRepository bitcoinTransactionOutputRepository;

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
        // roylloNFT is not in our database - We add it (No asset group).
        final String ROYLLO_NFT_RAW_PROOF = ROYLLO_NFT_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_NFT_PROOF_ID = sha256(ROYLLO_NFT_RAW_PROOF);
        final String ROYLLO_NFT_ASSET_STATE_ID = ROYLLO_NFT_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ROYLLO_NFT_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long bitcoinTransactionOutputCountBefore = bitcoinTransactionOutputRepository.count();
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add a request to add roylloNFT proof.
        AddProofRequestDTO addRoylloNFTRequest = requestService.createAddProofRequest(ROYLLO_NFT_RAW_PROOF);
        assertNotNull(addRoylloNFTRequest);
        assertEquals(OPENED, addRoylloNFTRequest.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();
        assertThat(requestService.getRequest(addRoylloNFTRequest.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertNotNull(((AddProofRequestDTO) request).getAsset());
                    assertEquals(ROYLLO_NFT_ASSET_ID, ((AddProofRequestDTO) request).getAsset().getAssetId());
                });

        // =============================================================================================================
        // We check that the asset now exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_GENESIS_TXID, ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_ANCHOR_1_TXID, ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ROYLLO_NFT_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertEquals(bitcoinTransactionOutputCountBefore + 2, bitcoinTransactionOutputRepository.count());
        assertEquals(assetGroupCountBefore, assetGroupRepository.count());
        assertEquals(assetCountBefore + 1, assetRepository.count());
        assertEquals(assetStateCountBefore + 1, assetStateRepository.count());

        // =============================================================================================================
        // We check the value of what has been created.
        assertThat(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID))
                .isPresent()
                .get()
                .satisfies(asset -> {
                    assertNotNull(asset.getAssetIdAlias());
                    assertEquals(ASSET_ID_ALIAS_LENGTH, asset.getAssetIdAlias().length());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    var bto1 = bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_GENESIS_TXID, ROYLLO_NFT_GENESIS_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto1, ROYLLO_NFT_GENESIS_TXID);
                    var bto2 = bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_ANCHOR_1_TXID, ROYLLO_NFT_ANCHOR_1_VOUT)
                            .orElseThrow(() -> new AssertionError("Bitcoin transaction output not found"));
                    verifyTransaction(bto2, ROYLLO_NFT_ANCHOR_1_TXID);
                    var assetCreated = assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID)
                            .orElseThrow(() -> new AssertionError("Asset not found"));
                    verifyAsset(assetCreated, ROYLLO_NFT_ASSET_ID);
                    var assetStateCreated = assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state not found"));
                    verifyAssetState(assetStateCreated,
                            ROYLLO_NFT_ASSET_ID,
                            ROYLLO_NFT_ANCHOR_1_TXID,
                            ROYLLO_NFT_ANCHOR_1_VOUT,
                            ROYLLO_NFT_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("roylloNFT by Royllo", response.body().string());
                            });
                    assertThat(getFileFromContentServer(sha256(ROYLLO_NFT_RAW_PROOF) + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(ROYLLO_NFT_RAW_PROOF, response.body().string());
                            });
                });
    }

}
