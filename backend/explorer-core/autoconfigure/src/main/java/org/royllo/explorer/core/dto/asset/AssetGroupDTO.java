package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.domain.user.User;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset chain anchor.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetGroupDTO {

    /** Unique identifier. */
    Long id;

    /** Asset creator. */
    @NotNull(message = "Asset group creator is required")
    User creator;

    /** A signature over the genesis point using the above key. */
    String assetIdSig;

    /** The raw group key which is a normal public key. */
    String rawGroupKey;

    /** The tweaked group key, which is derived based on the genesis point and also asset type. */
    String tweakedGroupKey;

}
