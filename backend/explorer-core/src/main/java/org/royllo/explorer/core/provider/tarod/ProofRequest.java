package org.royllo.explorer.core.provider.tarod;

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
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofRequest {

    /**
     * Raw proof.
     */
    @JsonProperty("raw_proof")
    String rawProof;

    /**
     * Proof index.
     */
    @JsonProperty("proof_at_depth")
    long proofIndex;

}
