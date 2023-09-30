package org.royllo.test.tapd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Decoded proof value for the server (for tests).
 * Contains the request and the response.
 */
@Value
@SuppressWarnings("checkstyle:VisibilityModifier")
public class DecodedProofValue {

    /** Decoded proof request. */
    DecodedProofValueRequest request;

    /** Decoded proof response. */
    DecodedProofValueResponse response;

    /** Asset state id. */
    String assetStateId;

    /** Proof at depth. */
    long proofAtDepth;

    /**
     * Constructor.
     *
     * @param newRequest  decoded proof request
     * @param newResponse decoded proof response
     */
    public DecodedProofValue(final DecodedProofValueRequest newRequest,
                             final DecodedProofValueResponse newResponse) {
        this.request = newRequest;
        this.response = newResponse;
        // "Calculated field".
        String uniqueValue = newResponse.getDecodedProof().getAsset().getAssetGenesis().getAssetId()
                + "_" + newResponse.getDecodedProof().getAsset().getChainAnchor().getAnchorOutpoint()
                + "_" + newResponse.getDecodedProof().getAsset().getScriptKey();
        this.assetStateId = sha256(uniqueValue);
        this.proofAtDepth = newRequest.getProofAtDepth();
    }

    /**
     * Returns the bytesToHex value calculated with the parameter.
     *
     * @param value value
     * @return sha256 of value
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private static String bytesToHex(final byte[] value) {
        StringBuilder hexString = new StringBuilder(2 * value.length);
        for (byte b : value) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Returns the request in JSON.
     *
     * @return json request
     */
    public String getJSONRequest() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Impossible to transform to JSON" + e);
        }
    }

    /**
     * Returns the response in JSON.
     *
     * @return json response
     */
    public String getJSONResponse() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Impossible to transform to JSON" + e);
        }
    }

    /**
     * Returns the sha256 value calculated with the parameter.
     *
     * @param input input
     * @return sha256 of input
     */
    private String sha256(final String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return bytesToHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

}
