package org.royllo.test;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.royllo.test.tapd.AssetValue;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.royllo.test.TestAssets.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.TEST_COIN_ASSET_ID;

/**
 * Tapd mock server test.
 */
@DisplayName("TAPD mock server test")
public class TapdMockServerTest {

    /** TAPD mock server port. */
    private static final int MOCK_SERVER_PORT = 9092;

    @Test
    @DisplayName("Mock server data")
    public void mockServerData() {
        final ClientAndServer tapdMockServer = startClientAndServer(MOCK_SERVER_PORT);
        TestAssets.setMockServerRules(tapdMockServer);
        var client = new OkHttpClient();

        // Testing with Royllo coin.
        AssetValue roylloCoin = TestAssets.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID);
        assertNotNull(roylloCoin);
        Request request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), roylloCoin.getDecodedProofValues().get(0).getJSONRequest()))
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"genesis_point\" : \"57faede9e107f3af74bb93e730bc1dfd07f81b166a974068f823d30d9eb10111:1"));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing with test coin.
        AssetValue testCoin = TestAssets.findAssetValueByAssetId(TEST_COIN_ASSET_ID);
        assertNotNull(testCoin);
        request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/v1/taproot-assets/proofs/decode")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), testCoin.getDecodedProofValues().get(2).getJSONRequest()))
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(">>>>>> " + response.body().string());
            // assertTrue(response.body().string().contains("\"script_key\" : \"02f02cc84b9f38f9596afd32542439d87fb8cd44e73ac4d94445044bb5e98331b1\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        tapdMockServer.stop();
    }

}
