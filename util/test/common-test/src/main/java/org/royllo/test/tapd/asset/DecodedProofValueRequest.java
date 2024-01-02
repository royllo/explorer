package org.royllo.test.tapd.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Decoded proof request (for tests).
 */
@Data
@Builder
@Jacksonized
@SuppressWarnings({"checkstyle:VisibilityModifier", "unused"})
public class DecodedProofValueRequest {

    /**
     * Raw proof.
     */
    @JsonProperty("raw_proof")
    String rawProof;

    /**
     * The index depth of the decoded proof, with 0 being the latest proof.
     */
    @JsonProperty("proof_at_depth")
    long proofAtDepth;

    /**
     * With prev witnesses.
     */
    @JsonProperty("with_prev_witnesses")
    boolean withPrevWitnesses;

    /**
     * With meta reveal.
     */
    @JsonProperty("with_meta_reveal")
    boolean withMetaReveal;

    /**
     * Returns proof id.
     *
     * @return proof id
     */
    public final String getProofId() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawProof.getBytes(UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
