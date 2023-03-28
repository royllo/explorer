package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Request to add asset meta data to an asset already in royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetMetaDataRequestDTO extends RequestDTO {

    /** Taro asset id. */
    @NotBlank(message = "Asset id is required")
    String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    @NotBlank(message = "Metadata is required")
    String metaData;

}
