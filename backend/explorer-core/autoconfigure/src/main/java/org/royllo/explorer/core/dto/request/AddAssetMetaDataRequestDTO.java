package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.asset.AssetDTO;

/**
 * Request to add asset meta data to an asset already in royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetMetaDataRequestDTO extends RequestDTO {

    /** Taproot asset id. */
    @NotBlank(message = "Asset id is required")
    String assetId;

    /** Metadata corresponding to the meta hash stored in the genesis information. */
    @NotBlank(message = "Metadata is required")
    String metaData;

    /** The asset created/updated by this request. */
    AssetDTO asset;

    /**
     * Set the asset created/updated by this request (Cannot be used to update the asset).
     *
     * @param newAsset new asset
     */
    public void setAsset(final AssetDTO newAsset) {
        assert asset == null : "You can't update the target asset, it's already set";
        asset = newAsset;
    }

}
