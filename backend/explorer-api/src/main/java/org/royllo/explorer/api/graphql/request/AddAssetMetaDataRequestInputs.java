package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * addAssetMetaDataRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddAssetMetaDataRequestInputs {

    /** Taro asset id. */
    private String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    private String metaData;


}
