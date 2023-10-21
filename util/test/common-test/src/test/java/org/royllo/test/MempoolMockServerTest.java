package org.royllo.test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.MempoolData.ROYLLO_NFT_GENESIS_TXID;

@DisplayName("Mempool mock server test")
public class MempoolMockServerTest {

    /** Mempool mock server port. */
    private static final int MOCK_SERVER_PORT = 9091;

    @Test
    @DisplayName("Mock server data")
    public void mockServerData() {
        final ClientAndServer mempoolMockServer = startClientAndServer(MOCK_SERVER_PORT);
        MempoolData.setMockServerRules(mempoolMockServer);
        var client = new OkHttpClient();

        // Testing a transaction existing on the bitcoin testnet.
        Request request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/api/tx/" + ROYLLO_COIN_GENESIS_TXID)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"txid\" : \"04feaf85babeeb5662e1139edd48b889ec178880cc69bbe38b5820dae322c75b\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing a transaction existing on the bitcoin testnet.
        request = new Request.Builder()
                .url("http://localhost:" + MOCK_SERVER_PORT + "/testnet/api/tx/" + ROYLLO_NFT_GENESIS_TXID)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"txid\" : \"c28a42586b36ac499c6d36da792d98176572573124dbc82526d02bbad5b3d9c7\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        mempoolMockServer.stop();
    }

}
