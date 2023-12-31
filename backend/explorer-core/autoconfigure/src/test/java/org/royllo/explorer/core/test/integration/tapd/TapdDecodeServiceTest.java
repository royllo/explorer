package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.test.tapd.asset.DecodedProofValue;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;

@SpringBootTest(properties = {"tapd.api.base-url=https://testnet.universe.royllo.org:8089"})
@DirtiesContext
@DisplayName("TAPD decode proof service test")
public class TapdDecodeServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("decode()")
    public void decodeTest() {
        final String ROYLLO_COIN_RAW_PROOF = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();

        // Value coming from the server.
        final DecodedProofResponse response = tapdService.decode(ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(response);

        // Value coming from our test data.
        final DecodedProofValue decodedProofValueFromTest = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofValuesWithoutMetaReveal().get(0);
        assertNotNull(decodedProofValueFromTest);

        // =============================================================================================================
        // Testing all the value from the response with what we have in test data.

        // Errors.
        assertNull(response.getErrorCode());
        assertNull(response.getErrorMessage());

        // Asset.
        final DecodedProofResponse.DecodedProof.Asset assetFromServer = response.getDecodedProof().getAsset();
        final DecodedProofValueResponse.DecodedProof.Asset assetFromTestData = decodedProofValueFromTest.getResponse().getDecodedProof().getAsset();
        assertEquals(assetFromTestData.getAssetGenesis().getAssetType(), assetFromServer.getAssetGenesis().getAssetType());
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
        assertEquals(assetGroupFromTestData.getRawGroupKey(), assetGroupFromServer.getRawGroupKey());
        assertEquals(assetGroupFromTestData.getTweakedGroupKey(), assetGroupFromServer.getTweakedGroupKey());
        assertEquals(assetGroupFromTestData.getAssetWitness(), assetGroupFromServer.getAssetWitness());

        // Chain anchor.
        final DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchorFromServer = response.getDecodedProof().getAsset().getChainAnchor();
        final DecodedProofValueResponse.DecodedProof.Asset.ChainAnchor chainAnchorFromTestData = assetFromTestData.getChainAnchor();
        assertEquals(chainAnchorFromTestData.getAnchorTx(), chainAnchorFromServer.getAnchorTx());
        assertEquals(chainAnchorFromTestData.getAnchorTxId(), chainAnchorFromServer.getAnchorTxId());
        assertEquals(chainAnchorFromTestData.getAnchorBlockHash(), chainAnchorFromServer.getAnchorBlockHash());
        assertEquals(chainAnchorFromTestData.getAnchorOutpoint(), chainAnchorFromServer.getAnchorOutpoint());
        assertEquals(chainAnchorFromTestData.getInternalKey(), chainAnchorFromServer.getInternalKey());
        assertEquals(chainAnchorFromTestData.getMerkleRoot(), chainAnchorFromServer.getMerkleRoot());
        assertEquals(chainAnchorFromTestData.getTapscriptSibling(), chainAnchorFromServer.getTapscriptSibling());
    }

    @Test
    @DisplayName("decode() with proofAtDepth parameter")
    public void proofAtDepthTest() {
        final String TRICKY_ROYLLO_COIN_3_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(2).getRawProof();

        final DecodedProofResponse response1 = tapdService.decode(TRICKY_ROYLLO_COIN_3_RAW_PROOF, 0, false).block();
        assertNotNull(response1);
        assertEquals(2, response1.getDecodedProof().getNumberOfProofs());
        assertEquals(0, response1.getDecodedProof().getProofAtDepth());

        final DecodedProofResponse response2 = tapdService.decode(TRICKY_ROYLLO_COIN_3_RAW_PROOF, 1, false).block();
        assertNotNull(response2);
        assertEquals(2, response1.getDecodedProof().getNumberOfProofs());
        assertEquals(1, response2.getDecodedProof().getProofAtDepth());
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

    @Test
    @DisplayName("decode() on tapd with meta reveal")
    public void decodeMetaRevealTest() {
        // Case tests on tricky coin.

        // Proof file 1.
        // Issuance proof with meta reveal ==> We should be able to get the meta information.
        final String issuanceProof1 = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        DecodedProofResponse response = tapdService.decode(issuanceProof1, 0, true).block();
        assertNotNull(response);
        assertNull(response.getErrorCode());
        assertNotNull(response.getDecodedProof().getMetaReveal());
        assertEquals("747269636b79526f796c6c6f436f696e20627920526f796c6c6f", response.getDecodedProof().getMetaReveal().getData());
        assertEquals("META_TYPE_OPAQUE", response.getDecodedProof().getMetaReveal().getType());
        assertEquals("13a16f12767ad6fbafcfd69fd12fc19747ac35749b1b6806c74dd4a07d2e4b41", response.getDecodedProof().getMetaReveal().getMetaHash());
        assertTrue(response.getDecodedProof().isIssuance());

        // If we do the same without asking for meta reveal, we should not get it.
        response = tapdService.decode(issuanceProof1, 0, false).block();
        assertNotNull(response);
        assertNull(response.getErrorCode());
        assertNull(response.getDecodedProof().getMetaReveal());
        assertFalse(response.getDecodedProof().isIssuance());

        // Proof file 2.
        // We get the proof file 2 (with two proofs) and we decode the last one, which is the issuance proof.
        final String issuanceProof2 = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(1).getRawProof();
        response = tapdService.decode(issuanceProof2, 1, true).block();
        assertNotNull(response);
        assertNull(response.getErrorCode());
        assertNotNull(response.getDecodedProof().getMetaReveal());
        assertEquals("747269636b79526f796c6c6f436f696e20627920526f796c6c6f", response.getDecodedProof().getMetaReveal().getData());
        assertEquals("META_TYPE_OPAQUE", response.getDecodedProof().getMetaReveal().getType());
        assertEquals("13a16f12767ad6fbafcfd69fd12fc19747ac35749b1b6806c74dd4a07d2e4b41", response.getDecodedProof().getMetaReveal().getMetaHash());

        // If we get a transfer proof, we should not get the meta reveal and have a code error instead.
        response = tapdService.decode(issuanceProof2, 0, true).block();
        assertNotNull(response);
        assertNotNull(response.getErrorCode());

        // But we should not have a problem if we set meta reveal to false.
        response = tapdService.decode(issuanceProof2, 1, false).block();
        assertNotNull(response);
        assertNull(response.getErrorCode());
    }

}
