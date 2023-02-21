package org.royllo.explorer.core.provider.tarod;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * Proof used by the decode method.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /**
     * Raw proof.
     */
    @JsonProperty("raw_index")
    String rawProof;

    /**
     * Proof index.
     */
    @JsonProperty("proof_index")
    long proofIndex;

}
