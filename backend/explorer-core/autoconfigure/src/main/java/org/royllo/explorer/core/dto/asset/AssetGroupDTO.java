package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.user.UserDTO;

import static lombok.AccessLevel.PRIVATE;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;

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
    UserDTO creator;

    /** Asset group id (=TWEAKED_GROUP_KEY). */
    @NotNull(message = "{validation.assetGroup.assetGroupId.required}")
    String assetGroupId;

    /** The raw group key which is a normal public key. */
    String rawGroupKey;

    /** The tweaked group key, which is derived based on the genesis point and also asset type. */
    @NotNull(message = "{validation.assetGroup.tweakedGroupKey.required}")
    String tweakedGroupKey;

    /** A witness that authorizes a specific asset to be part of the asset group specified by the above key. */
    String assetWitness;

    /**
     * Getter creator.
     *
     * @return creator
     */
    public UserDTO getCreator() {
        if (creator == null) {
            return ANONYMOUS_USER_DTO;
        } else {
            return creator;
        }
    }

}
