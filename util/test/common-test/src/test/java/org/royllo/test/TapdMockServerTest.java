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
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), roylloCoin.getDecodedProofValues().get(0).getJSONRequest()))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"genesis_point\" : \"04feaf85babeeb5662e1139edd48b889ec178880cc69bbe38b5820dae322c75b:0"));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing with tricky royllo coin.
        AssetValue testCoin = TapdData.findAssetValueByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertNotNull(testCoin);
        request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), testCoin.getDecodedProofValues().get(2).getJSONRequest()))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"script_key\" : \"023a8d9bc352eb3f5f69b798a941f06244b3633a8fd2cf82406132504ff08e23ff\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        tapdMockServer.stop();
    }

}
