package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.test.TapdData;
import org.royllo.test.tapd.asset.DecodedProofValue;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_RAW_PROOF;
import static org.royllo.test.TapdData.TESTCOIN_RAW_PROOF_2;

@SpringBootTest(properties = {"tapd.api.base-url=https://157.230.85.88:8089"})
@DisplayName("TAPD decode proof service test")
public class TapdDecodeServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("decode()")
    @SuppressWarnings("SpellCheckingInspection")
    public void decodeTest() {
        // Value coming from the server.
        final DecodedProofResponse response = tapdService.decode(ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(response);

        // Value coming from our test data.
        final DecodedProofValue decodedProofValueFromTest = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID).getDecodedProofValues().get(0);
        assertNotNull(decodedProofValueFromTest);

        // =============================================================================================================
        // Testing all the value from the response with what we have in test data.

        // Errots.
        assertNull(response.getErrorCode());
        assertNull(response.getErrorMessage());

        // Asset.
        final DecodedProofResponse.DecodedProof.Asset assetFromServer = response.getDecodedProof().getAsset();
        final DecodedProofValueResponse.DecodedProof.Asset assetFromTestData = decodedProofValueFromTest.getResponse().getDecodedProof().getAsset();
        assertEquals(assetFromTestData.getAssetType(), assetFromServer.getAssetType());
        assertEquals(0, assetFromTestData.getAmount().compareTo(assetFromServer.getAmount()));
        assertEquals(assetFromTestData.getLockTime(), assetFromServer.getLockTime());
        assertEquals(assetFromTestData.getRelativeLockTime(), assetFromServer.getRelativeLockTime());
        assertEquals(assetFromTestData.getScriptKey(), assetFromServer.getScriptKey());
        assertEquals(assetFromTestData.getScriptVersion(), assetFromServer.getScriptVersion());

        // Genesis point.
        final DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesisFromServer = assetFromServer.getAssetGenesis();
        final DecodedProofValueResponse.DecodedProof.Asset.AssetGenesis assetGenesisFromTestData = assetFromTestData.getAssetGenesis();
        assertEquals(assetGenesisFromTestData.getGenesisPoint(), assetGenesisFromServer.getGenesisPoint());
        assertEquals(assetGenesisFromTestData.getName(), assetGenesisFromServer.getName());
        assertEquals(assetGenesisFromTestData.getMetaDataHash(), assetGenesisFromServer.getMetaDataHash());
        assertEquals(assetGenesisFromTestData.getAssetId(), assetGenesisFromServer.getAssetId());
        assertEquals(assetGenesisFromTestData.getOutputIndex(), assetGenesisFromServer.getOutputIndex());
        assertEquals(assetGenesisFromTestData.getVersion(), assetGenesisFromServer.getVersion());

        // Asset group.
        final DecodedProofResponse.DecodedProof.Asset.AssetGroup assetGroupFromServer = response.getDecodedProof().getAsset().getAssetGroup();
        final DecodedProofValueResponse.DecodedProof.Asset.AssetGroup assetGroupFromTestData = assetFromTestData.getAssetGroup();
        // TODO Waiting for issue https://github.com/lightninglabs/taproot-assets/issues/407 to be fixed.
        assertEquals("", assetGroupFromServer.getRawGroupKey());
        assertEquals(assetGroupFromTestData.getTweakedGroupKey(), assetGroupFromServer.getTweakedGroupKey());
        assertEquals(assetGroupFromTestData.getAssetWitness(), assetGroupFromServer.getAssetIdSig());

        // Chain anchor.
        final DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchorFromServer = response.getDecodedProof().getAsset().getChainAnchor();
        final DecodedProofValueResponse.DecodedProof.Asset.ChainAnchor chainAnchorFromTestData = assetFromTestData.getChainAnchor();
        assertEquals(chainAnchorFromTestData.getAnchorTx(), chainAnchorFromServer.getAnchorTx());
        assertEquals(chainAnchorFromTestData.getAnchorTxId(), chainAnchorFromServer.getAnchorTxId());
        assertEquals(chainAnchorFromTestData.getAnchorBlockHash(), chainAnchorFromServer.getAnchorBlockHash());
        assertEquals(chainAnchorFromTestData.getAnchorOutpoint(), chainAnchorFromServer.getAnchorOutpoint());
        assertEquals(chainAnchorFromTestData.getInternalKey(), chainAnchorFromServer.getInternalKey());
        // TODO Waiting for issue https://github.com/lightninglabs/taproot-assets/issues/407 to be fixed.
        assertEquals("", chainAnchorFromServer.getMerkleRoot());
        assertEquals(chainAnchorFromTestData.getTapscriptSibling(), chainAnchorFromServer.getTapscriptSibling());
    }

    @Test
    @DisplayName("decode() with proofAtDepth parameter")
    public void proofAtDepthTest() {
        final DecodedProofResponse response1 = tapdService.decode(TESTCOIN_RAW_PROOF_2, 0).block();
        assertNotNull(response1);
        assertEquals(2, response1.getDecodedProof().getNumberOfProofs());
        assertEquals(1, response1.getDecodedProof().getProofAtDepth());

        final DecodedProofResponse response2 = tapdService.decode(TESTCOIN_RAW_PROOF_2, 1).block();
        assertNotNull(response2);
        assertEquals(2, response1.getDecodedProof().getNumberOfProofs());
        assertEquals(0, response2.getDecodedProof().getProofAtDepth());
    }

    @Test
    @DisplayName("decode() on tapd with an invalid proof")
    public void decodeErrorTest() {
        final DecodedProofResponse response = tapdService.decode("INVALID_PROOF").block();

        // Testing errors.
        assertNotNull(response);
        assertNotNull(response.getErrorCode());
        assertNotNull(response.getErrorMessage());
    }

}
