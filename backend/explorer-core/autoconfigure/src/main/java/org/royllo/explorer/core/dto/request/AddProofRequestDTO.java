package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.util.enums.ProofType;

/**
 * Request to add a proof to royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddProofRequestDTO extends RequestDTO {

    /** Proof that validates the asset information. */
    @NotBlank(message = "{validation.request.proof.required}")
    String proof;

    /** Proof type. */
    @NotNull(message = "{validation.request.type.required}")
    ProofType type;

    /** The asset created/updated by this request. */
    AssetDTO asset;

    /**
     * Set the asset created/updated by this request (Cannot be used to update the asset).
     *
     * @param newAsset new asset
     */
    public void setAsset(final AssetDTO newAsset) {
        if (asset != null) {
            throw new IllegalStateException("You can't update the target asset, it's already set");
        }
        asset = newAsset;
    }

}
