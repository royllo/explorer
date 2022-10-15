package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * addAssetRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddAssetRequestInputs {

    /** The full genesis information encoded in a portable manner, so it can be easily copied and pasted for address creation. */
    private String genesisBootstrapInformation;

    /** Proof that validates the asset information. */
    private String proof;

}
