package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A request to add asset meta data to an asset already in Royllo database.
 */
@Getter
@SuperBuilder
@ToString
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetMetaDataRequestDTO extends RequestDTO {

    /** Taro asset id. */
    String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    String metaData;

}
