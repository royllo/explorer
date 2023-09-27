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
import static org.royllo.test.TestTransactions.BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID;
import static org.royllo.test.TestTransactions.BITCOIN_TRANSACTION_1_TXID;
import static org.royllo.test.TestTransactions.MEMPOOL_MOCK_SERVER_PORT;

@DisplayName("Mempool mock server test")
public class MempoolMockServerTest {

    @Test
    @DisplayName("Mock server data")
    public void mockServerData() {
        final ClientAndServer mockServer = TestTransactions.getMockServer();
        var client = new OkHttpClient();

        // Testing a transaction existing on the bitcoin mainnet.
        Request request = new Request.Builder()
                .url("http://localhost:" + MEMPOOL_MOCK_SERVER_PORT + "/api/tx/" + BITCOIN_TRANSACTION_1_TXID)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"txid\" : \"2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        // Testing a transaction existing on the bitcoin testnet.
        request = new Request.Builder()
                .url("http://localhost:" + MEMPOOL_MOCK_SERVER_PORT + "/testnet/api/tx/" + BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("\"txid\" : \"d8a8016095b9fcd1f63c57342d375026ecbc72c885a54b676c6e62b216e15365\""));
        } catch (IOException e) {
            fail("Error while calling the mock server");
        }

        mockServer.stop();
    }

}
