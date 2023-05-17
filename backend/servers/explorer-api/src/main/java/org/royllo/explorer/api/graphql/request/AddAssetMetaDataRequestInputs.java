package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * createAddAssetMetaDataRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddAssetMetaDataRequestInputs {

    /** Taproot asset id. */
    private String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    private String metaData;

}
