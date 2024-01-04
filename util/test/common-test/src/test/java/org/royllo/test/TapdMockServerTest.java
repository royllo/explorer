package org.royllo.test;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.royllo.test.tapd.asset.AssetValue;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_VOUT;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;

/**
 * Tapd mock server test.
 */
@DisplayName("TAPD mock server test")
public class TapdMockServerTest {

    /** TAPD mock server port. */
    private static final int MOCK_SERVER_PORT = 9092;

    @Test
    @DisplayName("Mock server decode response")
    public void decodeResponse() {
        final ClientAndServer tapdMockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
        TapdData.setMockServerRules(tapdMockServer);
        var client = new OkHttpClient();

        // Testing with Royllo coin.
        AssetValue roylloCoin = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);
        Request request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(roylloCoin.getDecodedProofValuesWithoutMetaReveal().get(0).getJSONRequest(), MediaType.parse("application/json; charset=utf-8")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"genesis_point\" : \"" + ROYLLO_COIN_GENESIS_TXID + ":" + ROYLLO_COIN_GENESIS_VOUT + "" + "\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing with tricky royllo coin.
        AssetValue trickyRoylloCoin = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertNotNull(trickyRoylloCoin);
        request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(trickyRoylloCoin.getDecodedProofValuesWithoutMetaReveal().get(2).getJSONRequest(), MediaType.parse("application/json; charset=utf-8")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"script_key\" : \"023a8d9bc352eb3f5f69b798a941f06244b3633a8fd2cf82406132504ff08e23ff\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing with tricky royllo coin and meta-reveal.
        trickyRoylloCoin = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertNotNull(trickyRoylloCoin);
        request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(trickyRoylloCoin.getDecodedProofValuesWithMetaReveal().get(0).getJSONRequest(), MediaType.parse("application/json; charset=utf-8")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"data\" : \"747269636b79526f796c6c6f436f696e20627920526f796c6c6f\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        tapdMockServer.stop();
    }

}
