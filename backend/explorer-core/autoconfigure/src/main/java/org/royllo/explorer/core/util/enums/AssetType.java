package org.royllo.explorer.core.util.enums;

/**
 * Asset type.
 */
public enum AssetType {

    /**
     * Indicates that an asset is capable of being split/merged, with each of the units being fungible, even across a
     * key asset ID boundary (assuming the key group is the same).
     */
    NORMAL,

    /**
     * Indicates that an asset is a collectible, meaning that each of the other items under the same key group are not
     * fully fungible with each other.
     * Collectibles also cannot be split or merged.
     */
    COLLECTIBLE

}
