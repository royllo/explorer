package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.util.enums.ProofType;

/**
 * Request to add proof to royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddProofRequestDTO extends RequestDTO {

    /** Proof that validates the asset information. */
    @NotBlank(message = "Proof is required")
    String proof;

    /** Proof type. */
    @NotNull(message = "Proof type is required")
    ProofType proofType;

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
