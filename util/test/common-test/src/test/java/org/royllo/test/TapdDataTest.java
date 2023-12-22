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
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
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
        final List<DecodedProofValue> decodedProofValue = roylloCoin.getDecodedProofValuesWithoutMetaReveal();
        assertEquals(1, decodedProofValue.size());
        // Decoded proof 1.
        final DecodedProofValue decodedProofValue1 = decodedProofValue.get(0);
        assertNotNull(decodedProofValue1.getAssetStateId());
        assertNotNull(decodedProofValue1.getRequest());
        assertNotNull(decodedProofValue1.getRequest().getRawProof());
        assertNotNull(decodedProofValue1.getResponse());
        assertNotNull(decodedProofValue1.getResponse().getDecodedProof().getAsset().getAssetGenesis().getAssetType());

        // =============================================================================================================
        // Tricky royllo coin.
        final AssetValue trickyRoylloCoin = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertNotNull(trickyRoylloCoin);
        assertEquals(6, trickyRoylloCoin.getDecodedProofValuesWithoutMetaReveal().size());
    }

    @Test
    @DisplayName("Royllo coin value")
    public void roylloCoinValue() {
        final AssetValue roylloCoin = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);

        // Decoded proof 1.
        final DecodedProofValueResponse.DecodedProof roylloCoinProof = roylloCoin.getDecodedProofResponseWithMetaReveal(0);
        // Decoded proof.
        assertEquals(0, roylloCoinProof.getProofAtDepth());
        assertEquals(1, roylloCoinProof.getNumberOfProofs());
        // Asset.
        assertNotNull(roylloCoinProof.getAsset());
        assertEquals("ASSET_VERSION_V0", roylloCoinProof.getAsset().getVersion());
        // Asset genesis.
        assertEquals("d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb:4", roylloCoinProof.getAsset().getAssetGenesis().getGenesisPoint());
        assertEquals("roylloCoin", roylloCoinProof.getAsset().getAssetGenesis().getName());
        assertEquals("e08c74d75554e2c9d6d8452a3584d191f8727963b05e497bba84ada8ae829208", roylloCoinProof.getAsset().getAssetGenesis().getMetaDataHash());
        assertEquals("24a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d", roylloCoinProof.getAsset().getAssetGenesis().getAssetId());
        assertEquals(0, roylloCoinProof.getAsset().getAssetGenesis().getOutputIndex());
        assertEquals(0, roylloCoinProof.getAsset().getAssetGenesis().getVersion());
        // Asset (continue).
        assertEquals("NORMAL", roylloCoinProof.getAsset().getAssetGenesis().getAssetType());
        assertEquals(0, BigInteger.valueOf(100000).compareTo(roylloCoinProof.getAsset().getAmount()));
        assertEquals(0, roylloCoinProof.getAsset().getLockTime());
        assertEquals(0, roylloCoinProof.getAsset().getRelativeLockTime());
        assertEquals(0, roylloCoinProof.getAsset().getScriptVersion());
        assertEquals("02fe0898805795bd2b698c8819ea3bfcbf1c7aa2330d0f7228723899b825848ee6", roylloCoinProof.getAsset().getScriptKey());
        assertNull(roylloCoinProof.getAsset().getAssetGroup());
        // Asset chain anchor.
        assertEquals("02000000000101cb9f6b6266ba56d7bf85fbf3963c59caadb80045c1202c2a267c65dea2e92dd20400000000ffffffff02e8030000000000002251200e7b1c167645f8fea7d7d52d9fd2655822d53e9f56e7ce5261635955d18906f1542e040000000000225120350e820feb4e9ae48a0f6281f7aebc64434132302cd204f71bb0d53f5e8fdf600248304502210094efb25003c9ced0c59a50c486f34351f23fcf72d5994b788e21be14e0d7e91f02203b1101052c06ca423891bcef245857e0073137a4d9eb62fcc0cde6c28a5becfc012103a23aa11f7f890650feaf48f09006d9c3e800f37b5b48c47dcfef27e7554cd59000000000", roylloCoinProof.getAsset().getChainAnchor().getAnchorTx());
        assertEquals("00000000000000000002b9a667c668ebd344f5905c537ff2f6588368d7bcc9a4", roylloCoinProof.getAsset().getChainAnchor().getAnchorBlockHash());
        assertEquals("ca8d2eb13b25fd0b363d92de2655988b49bc5b519f282d41e10ce117beb97558:0", roylloCoinProof.getAsset().getChainAnchor().getAnchorOutpoint());
        assertEquals("0215305a361d0919c3be1a756ec45514feff098ddeb5b2731c4bf8068e19a69de9", roylloCoinProof.getAsset().getChainAnchor().getInternalKey());
        assertEquals("c52672ccc8f393eaa1f41c31786d560b3603c3af082fd68d4b4fe4190458a77f", roylloCoinProof.getAsset().getChainAnchor().getMerkleRoot());
        assertEquals("", roylloCoinProof.getAsset().getChainAnchor().getTapscriptSibling());
        // Asset (continue).
        assertNotNull(roylloCoinProof.getAsset().getPrevWitnesses());
        assertFalse(roylloCoinProof.getAsset().getIsSpent());
        assertEquals("", roylloCoinProof.getAsset().getLeaseOwner());
        assertEquals("0", roylloCoinProof.getAsset().getLeaseExpiry());
        assertFalse(roylloCoinProof.getAsset().getIsBurn());
        // End asset.
        assertEquals("0c6a7ccb693cd1d079239846914c0f6321988a3ab9efe6265191669bcb74c487f005e70b97110aa74310d5758156a887b8a1cfe6034ed8d2b4afba0ef89d3d0c1bb18c04cfe7fbc25a72fc5f9298c19f8e2d0ee5a127c5244f66a6edbc29df1c3764deda4f5dbcf2aad84cece706a48d1ead1aff3c3a54cf651c5803c710e697208b453cf2d5d65fb39948b24709616e6ce502ba78c72dc48f732608ec17af1109980f804c959933be7c02b6b1491a40a9182cf1b5f9b7bb3a8b5eeffad7dcc72bbc3c649ec93c31c5b0acff3a4275edf9e95046a9911276ec256ff3410bdfc513aeab02ad7ab1d0bda062fb9ff3828a367139363265ac7b96076018dcddc4c64f2ae2be52f40637b6c75d7d1b8d62316aabd2948da02e5e61988615faa014c78b1defa9f97f3897ae8d0f27a0ef87f87af2f153f15f27ba3dd35a65dc4743b7570bf824800d0451c3d7b7f0619176a6b13655380a272c06925ecf77508e70e578c6fb76a872f68b0832f60b124b9aedcee1596bc5692b1cf02c7fcd60dfd0bca5cf0b", roylloCoinProof.getTxMerkleProof());
        assertEquals("00040000000002210215305a361d0919c3be1a756ec45514feff098ddeb5b2731c4bf8068e19a69de903740149000100022024a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d04220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff022700010002220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", roylloCoinProof.getInclusionProof());
        assertEquals(1, roylloCoinProof.getExclusionProofs().size());
        assertEquals("00040000000102210238636cf0d229ba3c9c46e2d832db0f41a827de003ff5a16edc0611e1b71806f10503040101", roylloCoinProof.getExclusionProofs().get(0));
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

        // Meta reveal
        assertNotNull(roylloCoinProof.getMetaReveal());
        assertEquals("726f796c6c6f436f696e206f6e206d61696e6e657420627920526f796c6c6f", roylloCoinProof.getMetaReveal().getData());
        assertEquals("META_TYPE_OPAQUE", roylloCoinProof.getMetaReveal().getType());
        assertEquals("e08c74d75554e2c9d6d8452a3584d191f8727963b05e497bba84ada8ae829208", roylloCoinProof.getMetaReveal().getMetaHash());
    }

    @Test
    @DisplayName("Tricky coin value")
    public void trickyCoinValue() {
        // We test some fields that are not in royllo coin decoded proof.
        // We are using trickyRoylloCoin/decodeProof-proofFile3-proofAtDepth0-response.json
        final DecodedProofValueResponse.DecodedProof roylloCoinProof3 = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID).getDecodedProofResponse(3);
        assertEquals("000400000000022102bfca44f6bdfd7d54ce75e775049026015f7745ddef61610d4a6488c42f06f0a8037401490001000220e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e04220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff022700010002220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", roylloCoinProof3.getSplitRootProof());
    }

    @Test
    @DisplayName("Set of Royllo NTF value")
    public void setOfRoylloNFTValue() {
        // We test some fields that are not in royllo coin decoded proof.
        final DecodedProofValueResponse.DecodedProof setOfRoylloNFT1 = TapdData.findAssetValueByAssetId(SET_OF_ROYLLO_NFT_1_ASSET_ID).getDecodedProofResponse(0);
        assertNotNull(setOfRoylloNFT1.getAsset().getAssetGroup());
        assertEquals("039511fd8c1f51acfbd85d9d742268d1ca03ce3b69d0436e71130192c085394c99", setOfRoylloNFT1.getAsset().getAssetGroup().getTweakedGroupKey());
    }

}
