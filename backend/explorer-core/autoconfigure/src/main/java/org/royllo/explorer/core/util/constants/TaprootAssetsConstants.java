package org.royllo.explorer.core.util.constants;

import lombok.experimental.UtilityClass;

/**
 * Taproot assets constants.
 */
@UtilityClass
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "unused"})
public class TaprootAssetsConstants {

    /** Tweaked group key size is always 33 bytes (66 characters) and hexadecimal. */
    public static final int TWEAKED_GROUP_KEY_LENGTH = 66;

    /** Asset id length is always 32 bytes (64 characters) and hexadecimal. */
    public static final int ASSET_ID_LENGTH = 64;

    /** The length of the asset id alias. */
    public static final int ASSET_ID_ALIAS_LENGTH = 8;

}
