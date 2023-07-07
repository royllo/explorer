package org.royllo.explorer.core.provider.tapd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * Proof request used by the decode method.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings({"checkstyle:VisibilityModifier", "unused"})
public class ProofRequest {

    /**
     * Raw proof.
     */
    @JsonProperty("raw_proof")
    String rawProof;

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
