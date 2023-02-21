package org.royllo.explorer.core.provider.tarod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Decoded proof response.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class DecodedProofResponse {

    /** Proof index. */
    @JsonProperty("proof_index")
    long proofIndex;

    /** Number of proofs. */
    @JsonProperty("number_of_proofs")
    long numberOfProofs;


}
