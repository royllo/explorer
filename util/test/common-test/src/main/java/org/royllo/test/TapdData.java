package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;
import org.royllo.test.tapd.asset.AssetValue;
import org.royllo.test.tapd.asset.DecodedProofValue;
import org.royllo.test.tapd.asset.DecodedProofValueRequest;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.royllo.test.tapd.wallet.OwnershipVerifyRequest;
import org.royllo.test.tapd.wallet.OwnershipVerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockserver.matchers.MatchType.ONLY_MATCHING_FIELDS;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Test assets data.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "checkstyle:MagicNumber", "checkstyle:JavadocVariable", "unused"})
public class TapdData {

    // =================================================================================================================
    // For RoylloCoin
    public static final String ROYLLO_COIN_ASSET_ID = "24a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d";
    public static final String ROYLLO_COIN_ASSET_ALIAS = "roylloCoin";
    public static final AssetValue ROYLLO_COIN_FROM_TEST;

    // =================================================================================================================
    // For roylloNFT
    public static final String ROYLLO_NFT_ASSET_ID = "89c9d3ff7cb9dbc4615f854f7127e94db10edd430f8bcf82d7309d0c8b750051";
    public static final String ROYLLO_NFT_ASSET_ID_ALIAS = "alias001";
    public static final AssetValue ROYLLO_NFT_FROM_TEST;

    // =================================================================================================================
    // For setOfRoylloNFT
    public static final String SET_OF_ROYLLO_NFT_1_ASSET_ID = "f643450242bb302284bab17e097982ad89790f94a9ad53db34d1902dfcccae5b";
    public static final String SET_OF_ROYLLO_NFT_1_ASSET_ID_ALIAS = "alias002";
    public static final AssetValue SET_OF_ROYLLO_NFT_1_FROM_TEST;

    public static final String SET_OF_ROYLLO_NFT_2_ASSET_ID = "84be6e5954af62b40d1ea25f289e05414d579116e00b9fcaa76453efe8ee39ff";
    public static final String SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS = "alias003";
    public static final AssetValue SET_OF_ROYLLO_NFT_2_FROM_TEST;

    public static final String SET_OF_ROYLLO_NFT_3_ASSET_ID = "23577de9f41b99b03ad8ed10e09120bf949f956b0702133102a566f5d33f2bd7";
    public static final String SET_OF_ROYLLO_NFT_3_ASSET_ID_ALIAS = "alias004";
    public static final AssetValue SET_OF_ROYLLO_NFT_3_FROM_TEST;

    // =================================================================================================================
    // For trickyRoylloCoin
    public static final String TRICKY_ROYLLO_COIN_ASSET_ID = "e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e";
    public static final String TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS = "alias005";
    public static final AssetValue TRICKY_ROYLLO_COIN_FROM_TEST;

    // =================================================================================================================
    // For unknownRoylloCoin
    public static final String UNKNOWN_ROYLLO_COIN_ASSET_ID = "a8e1cd6961301e8ef91df4f902f7af0bf261e30c122d85ebf7abc32c15bee002";
    public static final AssetValue UNKNOWN_ROYLLO_COIN_FROM_TEST;

    // =================================================================================================================
    // For unlimitedRoylloCoin
    public static final String UNLIMITED_ROYLLO_COIN_1_ASSET_ID = "ce2d57a6bef7668a781bc149f5491aab7392b30a2f4368f013722f46a57abc70";
    public static final String UNLIMITED_ROYLLO_COIN_1_ASSET_ID_ALIAS = "alias006";
    public static final AssetValue UNLIMITED_ROYLLO_COIN_1_FROM_TEST;

    public static final String UNLIMITED_ROYLLO_COIN_2_ASSET_ID = "fdf6f8c41602ccdad237305a05e41868ba267947d152a1cacfaf3b5045a84a04";
    public static final String UNLIMITED_ROYLLO_COIN_2_ASSET_ID_ALIAS = "alias007";
    public static final AssetValue UNLIMITED_ROYLLO_COIN_2_FROM_TEST;

    /** Static logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TapdData.class);

    // =================================================================================================================

    /** Contains all assets and contains decoded proofs (request and response). */
    private static final Map<String, AssetValue> ASSETS = new LinkedHashMap<>();

    /** Contains all ownership verify requests. */
    public static final Map<OwnershipVerifyRequest, OwnershipVerifyResponse> OWNERSHIP_VERIFY_REQUESTS = new LinkedHashMap<>();

    // Initialisation - We load assets from files.
    static {
        LOGGER.info("Loading TapdData...");

        // =============================================================================================================
        // RoylloCoin.
        LOGGER.info("Loading RoylloCoin...");
        ROYLLO_COIN_FROM_TEST = new AssetValue();
        ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("roylloCoin", 1, 0, true));
        ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("roylloCoin", 1, 0, false));
        ASSETS.put(ROYLLO_COIN_ASSET_ID, ROYLLO_COIN_FROM_TEST);

        // =============================================================================================================
        // roylloNFT.
        LOGGER.info("Loading roylloNFT...");
        ROYLLO_NFT_FROM_TEST = new AssetValue();
        ROYLLO_NFT_FROM_TEST.addDecodedProofValue(getDecodedProofValue("roylloNFT", 1, 0, true));
        ROYLLO_NFT_FROM_TEST.addDecodedProofValue(getDecodedProofValue("roylloNFT", 1, 0, false));
        ASSETS.put(ROYLLO_NFT_ASSET_ID, ROYLLO_NFT_FROM_TEST);

        // =============================================================================================================
        // setOfRoylloNFT.
        LOGGER.info("Loading setOfRoylloNFT...");
        SET_OF_ROYLLO_NFT_1_FROM_TEST = new AssetValue();
        SET_OF_ROYLLO_NFT_1_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 1, 0, true));
        SET_OF_ROYLLO_NFT_1_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 1, 0, false));
        ASSETS.put(SET_OF_ROYLLO_NFT_1_ASSET_ID, SET_OF_ROYLLO_NFT_1_FROM_TEST);

        SET_OF_ROYLLO_NFT_2_FROM_TEST = new AssetValue();
        SET_OF_ROYLLO_NFT_2_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 2, 0, true));
        SET_OF_ROYLLO_NFT_2_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 2, 0, false));
        ASSETS.put(SET_OF_ROYLLO_NFT_2_ASSET_ID, SET_OF_ROYLLO_NFT_2_FROM_TEST);

        SET_OF_ROYLLO_NFT_3_FROM_TEST = new AssetValue();
        SET_OF_ROYLLO_NFT_3_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 3, 0, true));
        SET_OF_ROYLLO_NFT_3_FROM_TEST.addDecodedProofValue(getDecodedProofValue("setOfRoylloNFT", 3, 0, false));
        ASSETS.put(SET_OF_ROYLLO_NFT_3_ASSET_ID, SET_OF_ROYLLO_NFT_3_FROM_TEST);

        // =============================================================================================================
        // trickyRoylloCoin.
        LOGGER.info("Loading trickyRoylloCoin...");
        TRICKY_ROYLLO_COIN_FROM_TEST = new AssetValue();
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 1, 0, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 1, 0, false));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 2, 0, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 2, 0, false));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 2, 1, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 2, 1, false));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 0, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 0, false));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 1, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 1, false));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 2, true));
        TRICKY_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("trickyRoylloCoin", 3, 2, false));
        ASSETS.put(TRICKY_ROYLLO_COIN_ASSET_ID, TRICKY_ROYLLO_COIN_FROM_TEST);
        final String trickyRoylloCoinBase = "/tapd/assets/trickyRoylloCoin/";
        try {
            OWNERSHIP_VERIFY_REQUESTS.put(
                    getOwnershipVerifyRequestFromFile(trickyRoylloCoinBase + "ownership-verify-negative-request.json"),
                    getOwnershipVerifyResponseFromFile(trickyRoylloCoinBase + "ownership-verify-negative-response.json")
            );
            OWNERSHIP_VERIFY_REQUESTS.put(
                    getOwnershipVerifyRequestFromFile(trickyRoylloCoinBase + "ownership-verify-positive-request.json"),
                    getOwnershipVerifyResponseFromFile(trickyRoylloCoinBase + "ownership-verify-positive-response.json")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // =============================================================================================================
        // UnknownRoylloCoin.
        LOGGER.info("Loading unknownRoylloCoin...");
        UNKNOWN_ROYLLO_COIN_FROM_TEST = new AssetValue();
        UNKNOWN_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unknownRoylloCoin", 1, 0, true));
        UNKNOWN_ROYLLO_COIN_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unknownRoylloCoin", 1, 0, false));
        ASSETS.put(UNKNOWN_ROYLLO_COIN_ASSET_ID, UNKNOWN_ROYLLO_COIN_FROM_TEST);

        // =============================================================================================================
        // unlimitedRoylloCoin.
        LOGGER.info("Loading unlimitedRoylloCoin...");
        UNLIMITED_ROYLLO_COIN_1_FROM_TEST = new AssetValue();
        UNLIMITED_ROYLLO_COIN_1_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unlimitedRoylloCoin/firstEmission", 1, 0, true));
        UNLIMITED_ROYLLO_COIN_1_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unlimitedRoylloCoin/firstEmission", 1, 0, false));
        ASSETS.put(UNLIMITED_ROYLLO_COIN_1_ASSET_ID, UNLIMITED_ROYLLO_COIN_1_FROM_TEST);
        final String unlimitedRoylloCoinBaseFirstEmission = "/tapd/assets/unlimitedRoylloCoin/firstEmission/";
        try {
            OWNERSHIP_VERIFY_REQUESTS.put(
                    getOwnershipVerifyRequestFromFile(unlimitedRoylloCoinBaseFirstEmission + "ownership-verify-error-request.json"),
                    getOwnershipVerifyResponseFromFile(unlimitedRoylloCoinBaseFirstEmission + "ownership-verify-error-response.json")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UNLIMITED_ROYLLO_COIN_2_FROM_TEST = new AssetValue();
        UNLIMITED_ROYLLO_COIN_2_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unlimitedRoylloCoin/secondEmission", 1, 0, true));
        UNLIMITED_ROYLLO_COIN_2_FROM_TEST.addDecodedProofValue(getDecodedProofValue("unlimitedRoylloCoin/secondEmission", 1, 0, false));
        ASSETS.put(UNLIMITED_ROYLLO_COIN_2_ASSET_ID, UNLIMITED_ROYLLO_COIN_2_FROM_TEST);
        final String unlimitedRoylloCoinBaseSecondEmission = "/tapd/assets/unlimitedRoylloCoin/secondEmission/";
        try {
            OWNERSHIP_VERIFY_REQUESTS.put(
                    getOwnershipVerifyRequestFromFile(unlimitedRoylloCoinBaseSecondEmission + "ownership-verify-positive-request.json"),
                    getOwnershipVerifyResponseFromFile(unlimitedRoylloCoinBaseSecondEmission + "ownership-verify-positive-response.json")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // =============================================================================================================
        // Invalid proof.
        LOGGER.info("Loading invalidProof...");
        final AssetValue invalidProof = new AssetValue();
        invalidProof.addDecodedProofValue(getDecodedProofValue("invalidProof", 1, 0, true));
        invalidProof.addDecodedProofValue(getDecodedProofValue("invalidProof", 1, 0, false));
        ASSETS.put("invalidProof", invalidProof);

        // =============================================================================================================
        // OwnershipTest1.
        LOGGER.info("Loading ownershipTest1...");
        try {
            // Ownership verify - Working.
            OwnershipVerifyRequest request1 = getOwnershipVerifyRequestFromFile("/tapd/assets/ownershipTest1/ownership-verify-request.json");
            OwnershipVerifyResponse response1 = getOwnershipVerifyResponseFromFile("/tapd/assets/ownershipTest1/ownership-verify-response.json");
            OWNERSHIP_VERIFY_REQUESTS.put(request1, response1);

            // Ownership verify - Error.
            OwnershipVerifyRequest request2 = getOwnershipVerifyRequestFromFile("/tapd/assets/ownershipTest1/ownership-verify-request-with-error.json");
            OwnershipVerifyResponse response2 = getOwnershipVerifyResponseFromFile("/tapd/assets/ownershipTest1/ownership-verify-response-with-error.json");
            OWNERSHIP_VERIFY_REQUESTS.put(request2, response2);

        } catch (IOException e) {
            LOGGER.error("Error loading ownershipTest1: {}", e.getMessage());
        }

        LOGGER.info("TapdData loaded.");
    }

    /**
     * Returns an asset value by asset id.
     *
     * @param assetId asset id
     * @return asset value
     */
    public static AssetValue findAssetValueByAssetId(final String assetId) {
        return ASSETS.get(assetId);
    }

    /**
     * Returns the first decoded proof of an asset.
     *
     * @param assetId asset id
     * @return first decoded proof
     */
    public static DecodedProofValueResponse.DecodedProof findFirstDecodedProof(final String assetId) {
        return ASSETS.get(assetId).getDecodedProofResponse(1);
    }

    /**
     * Returns an asset state by asset state id.
     *
     * @param assetStateId asset state id
     * @return asset state
     */
    public static Optional<DecodedProofValueResponse.DecodedProof> findAssetStateByAssetStateId(final String assetStateId) {
        return ASSETS.values().stream()
                .flatMap(assetValue -> assetValue.getDecodedProofValuesWithoutMetaReveal().stream())
                .filter(decodedProofValue -> decodedProofValue.getAssetStateId().equals(assetStateId))
                .map(DecodedProofValue::getResponse)
                .map(DecodedProofValueResponse::getDecodedProof)
                .findFirst();
    }

    /**
     * Set mock server rules.
     *
     * @param mockServer mock server
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static void setMockServerRules(final ClientAndServer mockServer) {
        // Adding all assets to the mock server.
        for (Map.Entry<String, AssetValue> entry : ASSETS.entrySet()) {
            // For each decoded proof values, we mock the request.
            AtomicInteger i = new AtomicInteger(0);

            // Adding decoded proof values without meta reveal.
            entry.getValue().getDecodedProofValuesWithoutMetaReveal().forEach(decodedProofValue -> {
                int index = i.getAndIncrement();
                mockServer.when(request().withBody(
                                JsonBody.json(
                                        "{"
                                                + "\"raw_proof\" : \"" + entry.getValue().getDecodedProofValuesWithoutMetaReveal().get(index).getRequest().getRawProof() + "\","
                                                + "\"proof_at_depth\" : " + entry.getValue().getDecodedProofValuesWithoutMetaReveal().get(index).getRequest().getProofAtDepth() + ","
                                                + "\"with_meta_reveal\" : " + entry.getValue().getDecodedProofValuesWithoutMetaReveal().get(index).getRequest().isWithMetaReveal()
                                                + "}",
                                        ONLY_MATCHING_FIELDS
                                )
                        ))
                        .respond(response().withStatusCode(200)
                                .withContentType(APPLICATION_JSON)
                                .withBody(entry.getValue().getDecodedProofValuesWithoutMetaReveal().get(index).getJSONResponse()));
            });

            // Adding decoded proof values with meta reveal.
            i.set(0);
            entry.getValue().getDecodedProofValuesWithMetaReveal().forEach(decodedProofValue -> {
                int index = i.getAndIncrement();
                mockServer.when(request().withBody(
                                JsonBody.json(
                                        "{"
                                                + "\"raw_proof\" : \"" + entry.getValue().getDecodedProofValuesWithMetaReveal().get(index).getRequest().getRawProof() + "\","
                                                + "\"proof_at_depth\" : " + entry.getValue().getDecodedProofValuesWithMetaReveal().get(index).getRequest().getProofAtDepth() + ","
                                                + "\"with_meta_reveal\" : " + entry.getValue().getDecodedProofValuesWithMetaReveal().get(index).getRequest().isWithMetaReveal()
                                                + "}",
                                        ONLY_MATCHING_FIELDS
                                )
                        ))
                        .respond(response().withStatusCode(200)
                                .withContentType(APPLICATION_JSON)
                                .withBody(entry.getValue().getDecodedProofValuesWithMetaReveal().get(index).getJSONResponse()));
            });
        }

        // Adding all ownership verify requests to the mock server.
        OWNERSHIP_VERIFY_REQUESTS.forEach((request, response) -> {
            mockServer.when(request().withBody(
                            JsonBody.json(
                                    "{"
                                            + "\"proof_with_witness\" : \"" + request.getProofWithWitness() + "\""
                                            + "}",
                                    ONLY_MATCHING_FIELDS
                            )
                    ))
                    .respond(response().withStatusCode(200)
                            .withContentType(APPLICATION_JSON)
                            .withBody(response.getJSONResponse()));
        });

    }

    /**
     * Get decoded proof value.
     *
     * @param coinName       coin name
     * @param proofFileIndex proof file index
     * @param proofAtDepth   proof at depth
     * @param withMetaReveal with meta reveal
     * @return decoded proof value
     */
    private static DecodedProofValue getDecodedProofValue(final String coinName,
                                                          final int proofFileIndex,
                                                          final int proofAtDepth,
                                                          final boolean withMetaReveal) {
        String baseFileName = "/tapd/assets/" + coinName + "/decodeProof-proofFile" + proofFileIndex + "-proofAtDepth" + proofAtDepth;
        if (withMetaReveal) {
            baseFileName += "-withMetaReveal";
        } else {
            baseFileName += "-withoutMetaReveal";
        }
        try {
            return new DecodedProofValue(
                    getDecodedProofRequestFromFile(baseFileName + "-request.json"),
                    getDecodedProofResponseFromFile(baseFileName + "-response.json"));
        } catch (IOException e) {
            LOGGER.error("Error getting decoded proof value: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a proof request loaded from a file.
     *
     * @param filePath file path
     * @return proof request
     * @throws IOException file not found
     */
    private static DecodedProofValueRequest getDecodedProofRequestFromFile(final String filePath) throws IOException {
        InputStream inputStream = TapdData.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, DecodedProofValueRequest.class);
    }

    /**
     * Returns a decoded proof response loaded from a file.
     *
     * @param filePath file path
     * @return decoded proof
     * @throws IOException file not found
     */
    private static DecodedProofValueResponse getDecodedProofResponseFromFile(final String filePath) throws IOException {
        InputStream inputStream = TapdData.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, DecodedProofValueResponse.class);
    }

    /**
     * Returns an ownership verify request loaded from a file.
     *
     * @param filePath file path
     * @return ownership verify request
     * @throws IOException file not found
     */
    public static OwnershipVerifyRequest getOwnershipVerifyRequestFromFile(final String filePath) throws IOException {
        InputStream inputStream = TapdData.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, OwnershipVerifyRequest.class);
    }

    /**
     * Returns an ownership verify response loaded from a file.
     *
     * @param filePath file path
     * @return ownership verify response
     * @throws IOException file not found
     */
    private static OwnershipVerifyResponse getOwnershipVerifyResponseFromFile(final String filePath) throws IOException {
        InputStream inputStream = TapdData.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, OwnershipVerifyResponse.class);
    }

}
