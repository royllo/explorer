package org.royllo.explorer.core.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.royllo.explorer.core.domain.user.User;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset chain anchor.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetGroupDTO {

    // TODO Add a link to a bitcoin transaction output DTO

    /** Unique identifier. */
    Long id;

    /** Asset creator. */
    User creator;

    /** A signature over the genesis point using the above key. */
    String assetIdSig;

    /** The raw group key which is a normal public key. */
    String rawGroupKey;

    /** The tweaked group key, which is derived based on the genesis point and also asset type. */
    String tweakedGroupKey;

    /** Assets in this group. */
    @ToString.Exclude
    Set<AssetDTO> assets;

}
