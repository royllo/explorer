package org.royllo.test.dto.tapd;

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
    DecodedProofRequestValue request;

    /** Decoded proof response. */
    DecodedProofResponseValue response;

    /** Raw proof sha256. */
    String rawProofSha256;

    /** Proof at depth. */
    long proofAtDepth;

    /**
     * Constructor.
     *
     * @param newRequest  decoded proof request
     * @param newResponse decoded proof response
     */
    public DecodedProofValue(final DecodedProofRequestValue newRequest,
                             final DecodedProofResponseValue newResponse) {
        this.request = newRequest;
        this.response = newResponse;
        // "Calculated field".
        this.rawProofSha256 = sha256(newRequest.getRawProof());
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
     * Returns the sha256 value calculated with the parameter.
     *
     * @param input input
     * @return sha256 of input
     */
    private String sha256(final String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return bytesToHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

}
