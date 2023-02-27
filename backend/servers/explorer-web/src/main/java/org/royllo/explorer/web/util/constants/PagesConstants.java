package org.royllo.explorer.web.util.constants;

import lombok.experimental.UtilityClass;

/**
 * Pages constants.
 */
@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class PagesConstants {

    /** Home page. */
    public static final String HOME_PAGE = "home";

    /** Search page. */
    public static final String SEARCH_PAGE = "search";

    /** Asset page. */
    public static final String ASSET_PAGE = "asset/view";

    /** Request page. */
    public static final String REQUEST_PAGE = "request/view";

    /** Request - Add proof form. */
    public static final String REQUEST_ADD_PROOF_FORM_PAGE = "request/proof/add_form";

    /** Request - Saved proof with success. */
    public static final String REQUEST_ADD_PROOF_SUCCESS_PAGE = "request/proof/add_success";

    /** Error page. */
    public static final String ERROR_PAGE = "util/errors/error";

    /** Error 404 page. */
    public static final String ERROR_404_PAGE = "util/errors/error-404";

    /** Error 500 page. */
    public static final String ERROR_500_PAGE = "util/errors/error-500";

}
