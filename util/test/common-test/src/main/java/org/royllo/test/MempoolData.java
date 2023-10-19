package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Test transactions data.
 * TODO Refactor transaction names.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "checkstyle:MagicNumber"})
public class MempoolData {

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

    /** Transaction used by unknown royllo coin. */
    public static final String BITCOIN_TRANSACTION_FOR_UNKNOWN_ROYLLO_COIN = "4155275d5468dc2f116e3ea4aaddc4783a559933c7bef08fd3029fde40daf26d";

    /** Transaction for unknown royllo. */
    private static final String UNKNOWN_ROYLLO_COIN_1 = "6b16776eb0a23a473b89f050fae17d07c2a4c437b0f3edaf2f0f0316f5e72d6b";

    /** Transaction used by test coin. */
    private static final String TX_FOR_TEST_COIN_1 = "45d5846bfcce2a3232cbe018d98ede66f49db738389dc5186abda57ad5480677";

    /** Transaction used by test coin. */
    private static final String TX_FOR_TEST_COIN_2 = "cb12bb112c0211e3610300b2a93c1a2d0228b54ed49c58ca637ccb2ea0dc6f7d";

    /** Transaction used by test coin. */
    private static final String TX_FOR_TEST_COIN_3 = "db5c1530de5943a8f9e0f29cb2a0989cca2a3edea508dd1332d44d529e8b1408";

    /** Contains all transactions. */
    private static final Map<String, TransactionValue> TRANSACTIONS = new LinkedHashMap<>();


    static {
        try {
            // TODO find files automatically instead of specifying them manually.

            // Adding all transactions.
            TRANSACTIONS.put(ROYLLO_COIN_GENESIS_POINT_TXID, getTransactionValueFromFile(ROYLLO_COIN_GENESIS_POINT_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_1_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_1_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_2_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_2_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_3_TXID, getTransactionValueFromFile(BITCOIN_TRANSACTION_3_TXID));
            TRANSACTIONS.put(BITCOIN_TAPROOT_TRANSACTION_2_TXID, getTransactionValueFromFile(BITCOIN_TAPROOT_TRANSACTION_2_TXID));
            TRANSACTIONS.put(BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID, getTransactionValueFromFile(BITCOIN_TESTNET_TAPROOT_ASSET_TRANSACTION_1_TXID));
            TRANSACTIONS.put(BITCOIN_TRANSACTION_FOR_UNKNOWN_ROYLLO_COIN, getTransactionValueFromFile(BITCOIN_TRANSACTION_FOR_UNKNOWN_ROYLLO_COIN));
            TRANSACTIONS.put(UNKNOWN_ROYLLO_COIN_1, getTransactionValueFromFile(UNKNOWN_ROYLLO_COIN_1));
            TRANSACTIONS.put(TX_FOR_TEST_COIN_1, getTransactionValueFromFile(TX_FOR_TEST_COIN_1));
            TRANSACTIONS.put(TX_FOR_TEST_COIN_2, getTransactionValueFromFile(TX_FOR_TEST_COIN_2));
            TRANSACTIONS.put(TX_FOR_TEST_COIN_3, getTransactionValueFromFile(TX_FOR_TEST_COIN_3));

            // Transactions for coinWithFixedSupply.
            TRANSACTIONS.put("21da01524e9d41f20d2fd51b76acfe0468ac0954addeb314bfd8ca63cc276a69",
                    getTransactionValueFromFile("21da01524e9d41f20d2fd51b76acfe0468ac0954addeb314bfd8ca63cc276a69"));
            TRANSACTIONS.put("ab75d04579905832c8a4ab2d6936a096496f3f0b658354324cb15594308a5e06",
                    getTransactionValueFromFile("ab75d04579905832c8a4ab2d6936a096496f3f0b658354324cb15594308a5e06"));

            // Transactions for coinWithEmission.
            TRANSACTIONS.put("49748296309e13a71f7ed14404a66a8bb09cb6b81ec8248586d09a96e3478082",
                    getTransactionValueFromFile("49748296309e13a71f7ed14404a66a8bb09cb6b81ec8248586d09a96e3478082"));
            TRANSACTIONS.put("f5cb0a137c8f5cdad6e1ba78aa34dee74b9808e33bc81b45d26d0198efd1ea95",
                    getTransactionValueFromFile("f5cb0a137c8f5cdad6e1ba78aa34dee74b9808e33bc81b45d26d0198efd1ea95"));
            TRANSACTIONS.put("69a9ae015c9fbd947e348839efe8ae5f77fcc7b13b740ccaeadd1d664f38dae9",
                    getTransactionValueFromFile("69a9ae015c9fbd947e348839efe8ae5f77fcc7b13b740ccaeadd1d664f38dae9"));

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
     * Set mock server rules.
     *
     * @param mockServer mock server
     */
    public static void setMockServerRules(final ClientAndServer mockServer) {
        // Adding all transactions to the mock server.
        for (Map.Entry<String, TransactionValue> entry : TRANSACTIONS.entrySet()) {
            // Mock the request.
            mockServer.when(request().withPath(".*/api/tx/" + entry.getKey() + ".*"))
                    .respond(response().withStatusCode(200)
                            .withContentType(APPLICATION_JSON)
                            .withBody(entry.getValue().getJSONResponse()));
        }
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
        InputStream inputStream = TapdData.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, GetTransactionValueResponse.class);
    }

}
