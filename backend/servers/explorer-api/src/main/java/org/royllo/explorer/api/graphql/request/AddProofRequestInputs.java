package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * addProofRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddProofRequestInputs {

    /** Proof that validates the asset information. */
    private String rawProof;

}
