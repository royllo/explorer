package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.domain.user.User;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset group.
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

    /** Asset group id (=RAW_GROUP_KEY). */
    @NotNull(message = "Asset group id is required")
    String assetGroupId;

    /** The raw group key which is a normal public key. */
    String rawGroupKey;

    /** The tweaked group key, which is derived based on the genesis point and also asset type. */
    @NotNull(message = "Tweaked group key is required")
    String tweakedGroupKey;

    /** A witness that authorizes a specific asset to be part of the asset group specified by the above key. */
    String assetWitness;

}
