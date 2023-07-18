package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.asset.AssetDTO;

/**
 * Request to add proof to royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddProofRequestDTO extends RequestDTO {

    /** Proof that validates the asset information. */
    @NotBlank(message = "Raw proof is required")
    String rawProof;


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
