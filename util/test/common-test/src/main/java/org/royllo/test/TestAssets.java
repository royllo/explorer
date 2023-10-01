package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.JsonBody;
import org.royllo.test.tapd.AssetValue;
import org.royllo.test.tapd.DecodedProofValue;
import org.royllo.test.tapd.DecodedProofValueRequest;
import org.royllo.test.tapd.DecodedProofValueResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Test assets data.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class TestAssets {

    /** Royllo coin asset id (exists in royllo database). */
    public static final String ROYLLO_COIN_ASSET_ID = "f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32";

    /** Unknown royllo coin asset id (exists on testnet). */
    public static final String UNKNOWN_ROYLLO_COIN_ASSET_ID = "9eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c15";

    /** Test coin asset id (several proof files). */
    public static final String TEST_COIN_ASSET_ID = "981dd27089b3bebc0ea0cd17e78e69ca8d582e7b9695ac53f97ea10723a852f8";

    /** Contains all assets and contains decoded proofs (request and response). */
    private static final Map<String, AssetValue> ASSETS = new LinkedHashMap<>();

    static {
        try {
            // =========================================================================================================
            // RoylloCoin.
            AssetValue roylloCoin = new AssetValue();
            // Decoded proof 1.
            roylloCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/roylloCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/roylloCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            // Adding the asset
            ASSETS.put(roylloCoin.getAssetId(), roylloCoin);

            // =========================================================================================================
            // UnknownRoylloCoin.
            AssetValue unknownRoylloCoin = new AssetValue();
            // Decoded proof 1.
            unknownRoylloCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/unknownRoylloCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/unknownRoylloCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            // Adding the asset
            ASSETS.put(unknownRoylloCoin.getAssetId(), unknownRoylloCoin);

            // =========================================================================================================
            // TestCoin.
            AssetValue testCoin = new AssetValue();
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth1-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth1-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth1-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth1-response.json")));
            ASSETS.put(testCoin.getAssetId(), testCoin);

        } catch (IOException e) {
            throw new RuntimeException("TestAssets loading error: " + e);
        }
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
     * Returns an asset state by asset state id.
     *
     * @param assetStateId asset state id
     * @return asset state
     */
    public static Optional<DecodedProofValueResponse.DecodedProof> findAssetStateByAssetStateId(final String assetStateId) {
        return ASSETS.values().stream()
                .flatMap(assetValue -> assetValue.getDecodedProofValues().stream())
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
            entry.getValue().getDecodedProofValues().forEach(decodedProofValue -> {
                int index = i.getAndIncrement();

//                System.out.println("===========================================");
//                System.out.println("Asset => " + entry.getValue().getDecodedProofValues().get(index).getResponse().getDecodedProof().getAsset().getAssetGenesis().getName());
//                System.out.println("i => " + index);
//                System.out.println("Raw proof: " + entry.getValue().getDecodedProofValues().get(index).getRequest().getRawProof());
//                System.out.println("Raw proof at depth => " + entry.getValue().getDecodedProofValues().get(index).getRequest().getProofAtDepth());
//                System.out.println("Response: " + entry.getValue().getDecodedProofValues().get(index).getJSONResponse());

                //System.out.println("Returns: " + entry.getValue().getDecodedProofValues().get(index).getJSONResponse());


                mockServer.when(request().withBody(
                                JsonBody.json(
                                        "{"
                                                + "\"raw_proof\" : \"" + entry.getValue().getDecodedProofValues().get(index).getRequest().getRawProof() + "\","
                                                + "\"proof_at_depth\" : " + entry.getValue().getDecodedProofValues().get(index).getRequest().getProofAtDepth()
                                                + "}",
                                        MatchType.ONLY_MATCHING_FIELDS
                                )
                        ))
                        .respond(response().withStatusCode(200)
                                .withContentType(APPLICATION_JSON)
                                .withBody(entry.getValue().getDecodedProofValues().get(index).getJSONResponse()));
            });
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
        InputStream inputStream = TestAssets.class.getResourceAsStream(filePath);
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
        InputStream inputStream = TestAssets.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, DecodedProofValueResponse.class);
    }

}
