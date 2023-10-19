package org.royllo.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.test.tapd.asset.AssetValue;
import org.royllo.test.tapd.asset.DecodedProofValue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TEST_COIN_ASSET_ID;

@DisplayName("Test asset tests")
public class TapdDataTest {

    @Test
    @DisplayName("findAssetValueByAssetId()")
    public void findAssetValueByAssetId() {
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
        // Test coin.
        final AssetValue testCoin = TapdData.findAssetValueByAssetId(TEST_COIN_ASSET_ID);
        assertNotNull(testCoin);
        assertEquals(5, testCoin.getDecodedProofValues().size());
    }

}
