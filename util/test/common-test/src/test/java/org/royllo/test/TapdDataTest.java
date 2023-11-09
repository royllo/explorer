package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.tapd.asset.AssetValue;
import org.royllo.test.tapd.asset.DecodedProofValue;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID;

@DisplayName("Test assets data")
public class TapdDataTest {

    @Test
    @DisplayName("findAssetValueByAssetId()")
    public void findAssetValueByAssetId() {
        // =============================================================================================================
        // Coin that does not exist.
        assertNull(TapdData.findAssetValueByAssetId("COIN_THAT_DOES_NOT_EXISTS"));

        // =============================================================================================================
        // Royllo coin.
        final AssetValue roylloCoin = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);
        final List<DecodedProofValue> decodedProofValue = roylloCoin.getDecodedProofValues();
        assertEquals(1, decodedProofValue.size());
        // Decoded proof 1.
        final DecodedProofValue decodedProofValue1 = decodedProofValue.get(0);
        assertNotNull(decodedProofValue1.getAssetStateId());
        assertNotNull(decodedProofValue1.getRequest());
        assertNotNull(decodedProofValue1.getRequest().getRawProof());
        assertNotNull(decodedProofValue1.getResponse());
        assertNotNull(decodedProofValue1.getResponse().getDecodedProof().getAsset().getAssetType());

        // =============================================================================================================
        // Tricky royllo coin.
        final AssetValue trickyRoylloCoin = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertNotNull(trickyRoylloCoin);
        assertEquals(6, trickyRoylloCoin.getDecodedProofValues().size());
    }

    @Test
    @DisplayName("Royllo coin value")
    public void roylloCoinValue() {
        final AssetValue roylloCoin = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);

        // Decoded proof 1.
        final DecodedProofValueResponse.DecodedProof roylloCoinProof = roylloCoin.getDecodedProofResponse(0);
        // Decoded proof.
        assertEquals(0, roylloCoinProof.getProofAtDepth());
        assertEquals(1, roylloCoinProof.getNumberOfProofs());
        // Asset.
        assertNotNull(roylloCoinProof.getAsset());
        assertEquals("ASSET_VERSION_V0", roylloCoinProof.getAsset().getVersion());
        // Asset genesis.
        assertEquals("04feaf85babeeb5662e1139edd48b889ec178880cc69bbe38b5820dae322c75b:0", roylloCoinProof.getAsset().getAssetGenesis().getGenesisPoint());
        assertEquals("roylloCoin", roylloCoinProof.getAsset().getAssetGenesis().getName());
        assertEquals("e342a303a484ac7fead42c7bc8590000c10fa801f1e7c6c59bd47b360171dfee", roylloCoinProof.getAsset().getAssetGenesis().getMetaDataHash());
        assertEquals("ce5a426ea282d2dee3a2eb48170231403ee4768be17f73fef8e6f925d30797af", roylloCoinProof.getAsset().getAssetGenesis().getAssetId());
        assertEquals(0, roylloCoinProof.getAsset().getAssetGenesis().getOutputIndex());
        assertEquals(0, roylloCoinProof.getAsset().getAssetGenesis().getVersion());
        // Asset (continue).
        assertEquals("NORMAL", roylloCoinProof.getAsset().getAssetType());
        assertEquals(0, BigInteger.valueOf(10000).compareTo(roylloCoinProof.getAsset().getAmount()));
        assertEquals(0, roylloCoinProof.getAsset().getLockTime());
        assertEquals(0, roylloCoinProof.getAsset().getRelativeLockTime());
        assertEquals(0, roylloCoinProof.getAsset().getScriptVersion());
        assertEquals("025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d211", roylloCoinProof.getAsset().getScriptKey());
        assertNull(roylloCoinProof.getAsset().getAssetGroup());
        // Asset chain anchor.
        assertEquals("020000000001015bc722e3da20588be3bb69cc808817ec89b848dd9e13e16256ebbeba85affe040000000000ffffffff02e8030000000000002251202aad3ee91b3a90c38bbbad0b5da98f1f85bda1cfd12c4bcb15f69e8e95842ffdc8bc100000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b06024730440220631ed9453ae9a4d5dbabbb44cb97cf5cbe9956176463c3cc6387282f522f5d3b022015456e56cabf586e3662b8be7a698abb654ce0f621bc2e5a7d8e8103abcd6402012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000", roylloCoinProof.getAsset().getChainAnchor().getAnchorTx());
        assertEquals("c28a42586b36ac499c6d36da792d98176572573124dbc82526d02bbad5b3d9c7", roylloCoinProof.getAsset().getChainAnchor().getAnchorTxId());
        assertEquals("0000000000000033e0a0580c592ed3c47df45ca147d569e2730fb3ca415289ab", roylloCoinProof.getAsset().getChainAnchor().getAnchorBlockHash());
        assertEquals("c28a42586b36ac499c6d36da792d98176572573124dbc82526d02bbad5b3d9c7:0", roylloCoinProof.getAsset().getChainAnchor().getAnchorOutpoint());
        assertEquals("026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6", roylloCoinProof.getAsset().getChainAnchor().getInternalKey());
        assertEquals("fb74e3e82f8d2700804ec05c6574b59e8f6828adb5fecc49e9d07a89f14548c8", roylloCoinProof.getAsset().getChainAnchor().getMerkleRoot());
        assertEquals("", roylloCoinProof.getAsset().getChainAnchor().getTapscriptSibling());
        assertEquals(2534237, roylloCoinProof.getAsset().getChainAnchor().getBlockHeight());
        // Asset (continue).
        assertNotNull(roylloCoinProof.getAsset().getPrevWitnesses());
        assertFalse(roylloCoinProof.getAsset().getIsSpent());
        assertEquals("", roylloCoinProof.getAsset().getLeaseOwner());
        assertEquals("0", roylloCoinProof.getAsset().getLeaseExpiry());
        assertFalse(roylloCoinProof.getAsset().getIsBurn());
        // End asset.
        assertEquals("060d6cca264dc5ae7811f5e30305b64021c4b6107325359dd2964afb464ede9d209af1a7478aa5e8704ed521214bfa5935f022a3282ca01ec378743f8f2b26e883d7c81784fdd7aa621d14239510ee5f06add858f8f7c262941b6642674650628793c0b475e851828cbbd4048d003279eb3617939dee343f2a0edc55c6eb694929ac0f81acc5d39a6dd1675e351a7c26c84a6636253d30525ed7f09e9aba7d472b9d9a0a166581b27a343859332d3bebc60736a221995481b9355a475bb52c468330", roylloCoinProof.getTxMerkleProof());
        assertEquals("0004000000000221026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae6037401490001000220ce5a426ea282d2dee3a2eb48170231403ee4768be17f73fef8e6f925d30797af04220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff022700010002220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", roylloCoinProof.getInclusionProof());
        assertEquals(1, roylloCoinProof.getExclusionProofs().size());
        assertEquals("00040000000102210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd110980503040101", roylloCoinProof.getExclusionProofs().get(0));
        assertEquals("", roylloCoinProof.getSplitRootProof());
        assertEquals(0, roylloCoinProof.getNumberOfAdditionalInputs());
        assertNotNull(roylloCoinProof.getChallengeWitness());
        assertFalse(roylloCoinProof.getIsBurn());

        // Testing roylloCoin missing fields on other assets.
        final AssetValue unlimitedRoylloCoin1 = TapdData.findAssetValueByAssetId(UNLIMITED_ROYLLO_COIN_1_ASSET_ID);
        assertNotNull(unlimitedRoylloCoin1);
        // Decoded proof 1.
        final DecodedProofValueResponse.DecodedProof unlimitedRoylloCoin1Proof = unlimitedRoylloCoin1.getDecodedProofResponse(0);
        // Test for "asset_group".
        assertNotNull(unlimitedRoylloCoin1Proof.getAsset().getAssetGroup());
        assertEquals("", unlimitedRoylloCoin1Proof.getAsset().getAssetGroup().getRawGroupKey());
        assertEquals("0349d60c6689bbbb2ffc9b30b45b96a3d6e5fd5fd01c867344c0054f6105048241", unlimitedRoylloCoin1Proof.getAsset().getAssetGroup().getTweakedGroupKey());
        assertEquals("", unlimitedRoylloCoin1Proof.getAsset().getAssetGroup().getAssetWitness());
    }

    @Test
    @DisplayName("Tricky coin value")
    public void trickyCoinValue() {
        // We test some fields that are not in royllo coin decoded proof.
        // We are using trickyRoylloCoin/decodeProof-proofFile3-proofAtDepth0-response.json
        final DecodedProofValueResponse.DecodedProof roylloCoinProof3 = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID).getDecodedProofResponse(3);
        assertEquals("000400000000022102bfca44f6bdfd7d54ce75e775049026015f7745ddef61610d4a6488c42f06f0a8037401490001000220e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e04220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff022700010002220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", roylloCoinProof3.getSplitRootProof());
    }

}
