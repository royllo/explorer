package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.dto.tapd.AssetValue;
import org.royllo.test.dto.tapd.DecodedProofValue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.TestAssets.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.TEST_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.UNKNOWN_ROYLLO_COIN_ASSET_ID;

/**
 * Test for test assets.
 */
@DisplayName("Test asset tests")
public class TestAssetsTestData {

    @Test
    @DisplayName("findAssetValueByAssetId()")
    public void findAssetValueByAssetId() {
        // Coin that does not exist.
        assertNull(TestAssets.findAssetValueByAssetId("COIN_THAT_DOES_NOT_EXISTS"));

        // =============================================================================================================
        // Royllo coin.
        final AssetValue roylloCoin = TestAssets.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);
        final List<DecodedProofValue> decodedProofValue = roylloCoin.getDecodedProofValue();
        assertEquals(1, decodedProofValue.size());
        // Decoded proof 1.
        final DecodedProofValue decodedProofValue1 = decodedProofValue.get(0);
        assertNotNull(decodedProofValue1.getRawProofSha256());
        assertNotNull(decodedProofValue1.getRequest());
        assertNotNull(decodedProofValue1.getRequest().getRawProof());
        assertNotNull(decodedProofValue1.getResponse());
        assertNotNull(decodedProofValue1.getResponse().getDecodedProof().getAsset().getAssetType());

        // =============================================================================================================
        // Unknown royllo coin.
        assertNull(TestAssets.findAssetValueByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID));

        // =============================================================================================================
        // Test coin.
        final AssetValue testCoin = TestAssets.findAssetValueByAssetId(TEST_COIN_ASSET_ID);
        assertNotNull(testCoin);
        assertEquals(5, testCoin.getDecodedProofValue().size());
    }

}
