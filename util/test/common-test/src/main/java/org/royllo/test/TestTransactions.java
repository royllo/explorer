package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.mockserver.integration.ClientAndServer;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Test transactions data.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "checkstyle:MagicNumber"})
public class TestTransactions {

    /** Royllo genesis point tx id. */
    public static final String ROYLLO_COIN_GENESIS_POINT_TXID = "57faede9e107f3af74bb93e730bc1dfd07f81b166a974068f823d30d9eb10111";

    /** txid of the first bitcoin transaction in database - one output. */
    public static final String BITCOIN_TRANSACTION_1_TXID = "2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea";

    /** txid of the second bitcoin transaction in database - two outputs. */
    public static final String BITCOIN_TRANSACTION_2_TXID = "46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946";

    /** txid of a third transaction not in our database but in the blockchain - three outputs. */
    public static final String BITCOIN_TRANSACTION_3_TXID = "117ad75a79af2e7fdb2908baee9171fde4d6fb80c7322dcb895a2429f84f4d4a";

    /** txid of a taproot transaction NOT in our database, but IN the blockchain. */
    public static final String BITCOIN_TAPROOT_TRANSACTION_2_TXID = "d61a4957e5e756a7631246b1a00d685e4854f98f8c2835bafafed8b1d1e26be5";

    /** txid of a taproot transaction containing a Taproot asset on the testnet (output 1). */
    public static final String BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID = "d8a8016095b9fcd1f63c57342d375026ecbc72c885a54b676c6e62b216e15365";

    /** Mock server port. */
    public static final int MEMPOOL_MOCK_SERVER_PORT = 9091;

    /** Mock server. */
    @Getter
    private static final ClientAndServer MOCK_SERVER = startClientAndServer(MEMPOOL_MOCK_SERVER_PORT);

    /** Contains all transactions. */
    private static final Map<String, TransactionValue> TRANSACTIONS = new LinkedHashMap<>();

    static {
        try {
            // Adding all transactions.
            TRANSACTIONS.put(ROYLLO_COIN_GENESIS_POINT_TXID, getTransactionValueFromFile(ROYLLO_COIN_GENESIS_POINT_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_1_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_1_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_2_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_2_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_3_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_3_TXID));
            TRANSACTIONS.put(BITCOIN_TAPROOT_TRANSACTION_2_TXID, getTransactionValueFromFile(BITCOIN_TAPROOT_TRANSACTION_2_TXID));
            TRANSACTIONS.put(BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID, getTransactionValueFromFile(BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID));

            // Adding all transactions to the mock server.
            for (Map.Entry<String, TransactionValue> entry : TRANSACTIONS.entrySet()) {
                // Mock the request.
                MOCK_SERVER.when(request().withPath(".*/api/tx/" + entry.getKey() + ".*"))
                        .respond(response().withStatusCode(200)
                                .withContentType(APPLICATION_JSON)
                                .withBody(entry.getValue().getJSONResponse()));
            }
        } catch (IOException e) {
            throw new RuntimeException("TestTransactions loading error: " + e);
        }
    }

    /**
     * Returns a transaction value.
     *
     * @param transactionId transaction id
     * @return transaction value
     */
    public static TransactionValue findTransactionByTransactionId(final String transactionId) {
        return TRANSACTIONS.get(transactionId);
    }

    /**
     * Returns a transaction value.
     *
     * @param transactionId transaction id
     * @return transaction value
     * @throws IOException file not found
     */
    private static TransactionValue getTransactionValueFromFile(final String transactionId) throws IOException {
        GetTransactionValueResponse response = getTransactionValueResponse("/mempool/" + transactionId + "-response.json");
        return new TransactionValue(response);
    }

    /**
     * Returns a transaction response loaded from a file.
     *
     * @param filePath file path
     * @return transaction response
     * @throws IOException file not found
     */
    private static GetTransactionValueResponse getTransactionValueResponse(final String filePath) throws IOException {
        InputStream inputStream = TestAssets.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, GetTransactionValueResponse.class);
    }

}
