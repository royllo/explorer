package org.royllo.explorer.api.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * A request to add asset meta data to an asset already in Royllo database.
 */
@Getter
@SuperBuilder
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetMetaRequestDTO extends RequestDTO {

    /** Taro asset id. */
    String taroAssetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    String meta;

}
