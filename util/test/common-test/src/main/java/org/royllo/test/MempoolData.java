package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Mempool transactions data.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "checkstyle:MagicNumber", "checkstyle:JavadocVariable"})
public class MempoolData {

    // =================================================================================================================
    // For RoylloCoin
    public static final String ROYLLO_COIN_GENESIS_TXID = "04feaf85babeeb5662e1139edd48b889ec178880cc69bbe38b5820dae322c75b";
    public static final String ROYLLO_COIN_ANCHOR_1_TXID = "c28a42586b36ac499c6d36da792d98176572573124dbc82526d02bbad5b3d9c7";
    // =================================================================================================================
    // For roylloNFT
    public static final String ROYLLO_NFT_GENESIS_TXID = "c28a42586b36ac499c6d36da792d98176572573124dbc82526d02bbad5b3d9c7";
    public static final String ROYLLO_NFT_ANCHOR_1_TXID = "6db79f5af2ba65bfb4044ced690f3acb4a791a6fc6a7450664e15559ad770b90";
    // =================================================================================================================
    // For setOfRoylloNFT
    public static final String SET_OF_ROYLLO_NFT_GENESIS_TXID = "6db79f5af2ba65bfb4044ced690f3acb4a791a6fc6a7450664e15559ad770b90";
    public static final String SET_OF_ROYLLO_NFT_ANCHOR_1_TXID = "ca93a44b534f410914c18ed4563c97b0a6c31194af236d789a79683c9ec76526";
    // =================================================================================================================
    // For trickyRoylloCoin
    public static final String TRICKY_ROYLLO_COIN_GENESIS_TXID = "2777e306451f6d5231f4b17b8286412e1e13ef153800086cf879baae89d9df76";
    public static final String TRICKY_ROYLLO_COIN_ANCHOR_1_TXID = "632d0c3935fff230aa3718e268dce5517786d7976f7aa33efb615b408737b0ad";
    public static final String TRICKY_ROYLLO_COIN_ANCHOR_2_TXID = "f2acf1235882a7683bad5baeb1b84c2f1dbf33f0fc4a7c85f2191aa8d49ce0d3";
    public static final String TRICKY_ROYLLO_COIN_ANCHOR_3_TXID = "2727229cc771efa552f9232a04b0cd8d16df6d83bd217523888feee6a8553ec8";
    // =================================================================================================================
    // For unknownRoylloCoin
    public static final String UNKNOWN_ROYLLO_COIN_GENESIS_TXID = "2727229cc771efa552f9232a04b0cd8d16df6d83bd217523888feee6a8553ec8";
    public static final String UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID = "77b550de10ad347daf314f4982a979db6d101573afe6fbf20c35935ce4bb0413";
    // =================================================================================================================
    // For unlimitedRoylloCoin
    public static final String UNLIMITED_ROYLLO_COIN_1_GENESIS_TXID = "77b550de10ad347daf314f4982a979db6d101573afe6fbf20c35935ce4bb0413";
    public static final String UNLIMITED_ROYLLO_COIN_1_ANCHOR_1_TXID = "2debb29de64ed7aafaf79c034f1ba22c41f484fafc814cab7b22a97bacfa6825";
    public static final String UNLIMITED_ROYLLO_COIN_2_GENESIS_TXID = "2debb29de64ed7aafaf79c034f1ba22c41f484fafc814cab7b22a97bacfa6825";
    public static final String UNLIMITED_ROYLLO_COIN_2_ANCHOR_1_TXID = "89cd5498584f217ca77d50e740e56490a710ab7b6988b3cd73aac95b9e077b1f";

    /** Static logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MempoolData.class);

    /** Contains all transactions. */
    private static final Map<String, TransactionValue> TRANSACTIONS = new LinkedHashMap<>();

    // Initialisation - We load transaction from files.
    static {
        // We retrieve all transactions declared in MempoolData class.
        LOGGER.info("Loading MempoolData...");
        Arrays.stream(MempoolData.class.getFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))
                .map(txid -> {
                    try {
                        return (String) txid.get(null);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Error while loading transaction {}: {}", txid, e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .peek(transactionValue -> LOGGER.info("Loading transaction {}", transactionValue))
                .map(MempoolData::getTransactionValueFromFile)
                .filter(Objects::nonNull)
                .forEach(transactionValue -> TRANSACTIONS.put(transactionValue.getTxId(), transactionValue));
        LOGGER.info("MempoolData loaded.");
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
     */
    private static TransactionValue getTransactionValueFromFile(final String transactionId) {
        try {
            GetTransactionValueResponse response = getTransactionValueResponse("/mempool/" + transactionId + "-response.json");
            return new TransactionValue(response);
        } catch (IOException e) {
            LOGGER.error("Error while loading transaction {} from file: {}", transactionId, e.getMessage());
        }
        return null;
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
