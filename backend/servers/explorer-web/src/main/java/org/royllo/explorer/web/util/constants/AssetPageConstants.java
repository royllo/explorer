package org.royllo.explorer.web.util.constants;

import lombok.experimental.UtilityClass;
import org.royllo.explorer.web.util.page.Page;

/**
 * Asset page files.
 */
@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class AssetPageConstants {

    /** Asset page. */
    public static final String ASSET_PAGE = "asset/genesis";

    /** Asset genesis page. */
    public static final Page ASSET_GENESIS_PAGE = new Page("asset/genesis");

    /** Asset group page. */
    public static final Page ASSET_GROUP_PAGE = new Page("asset/group");

    /** Asset states page. */
    public static final Page ASSET_STATES_TAB = new Page("asset/states");

    /** Asset owner page. */
    public static final Page ASSET_OWNER_TAB = new Page("asset/owner");

    /** Asset proofs page. */
    public static final Page ASSET_PROOFS_TAB = new Page("asset/proofs");

}
