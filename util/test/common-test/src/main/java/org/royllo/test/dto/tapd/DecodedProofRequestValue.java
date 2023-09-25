package org.royllo.test.dto.tapd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Decoded proof request (for tests).
 */
@Data
@Builder
@Jacksonized
@SuppressWarnings({"checkstyle:VisibilityModifier", "unused"})
public class DecodedProofRequestValue {

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

}
