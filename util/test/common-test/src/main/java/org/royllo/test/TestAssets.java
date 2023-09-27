package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.royllo.test.tapd.AssetValue;
import org.royllo.test.tapd.DecodedProofValue;
import org.royllo.test.tapd.DecodedProofValueRequest;
import org.royllo.test.tapd.DecodedProofValueResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
     * Returns an asset state by raw proof sha256.
     *
     * @param rawProofSha256 raw proof sha256
     * @return asset state
     */
    public static Optional<DecodedProofValueResponse.DecodedProof> findAssetStateByRawProofSha256(final String rawProofSha256) {
        return ASSETS.values().stream()
                .flatMap(assetValue -> assetValue.getDecodedProofValues().stream())
                .filter(decodedProofValue -> decodedProofValue.getRawProofSha256().equals(rawProofSha256))
                .map(DecodedProofValue::getResponse)
                .map(DecodedProofValueResponse::getDecodedProof)
                .findFirst();
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
