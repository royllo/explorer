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

    /** Home page fragment. */
    public static final String HOME_PAGE_FRAGMENT = "home :: home-content";

    /** Search page. */
    public static final String SEARCH_PAGE = "search";

    /** Search page fragment. */
    public static final String SEARCH_PAGE_FRAGMENT = "search :: search-results";

    /** Asset page. */
    public static final String ASSET_PAGE = "asset/view";

    /** Asset page fragment. */
    public static final String ASSET_PAGE_FRAGMENT = "asset/view :: asset-view";

    /** Asset proofs page. */
    public static final String ASSET_PROOFS_PAGE = "asset/proof/list";

    /** Asset proofs page fragment. */
    public static final String ASSET_PROOFS_PAGE_FRAGMENT = "asset/proof/list :: asset-proofs-list";

    /** Choose request type page. */
    public static final String CHOOSE_REQUEST_TYPE_PAGE = "request/choose_request_type";

    /** Choose request type page. */
    public static final String CHOOSE_REQUEST_TYPE_PAGE_FRAGMENT = "request/choose_request_type :: choose-request-type-view";

    /** Request page. */
    public static final String REQUEST_PAGE = "request/view";

    /** Request - Add proof form. */
    public static final String ADD_PROOF_REQUEST_FORM_PAGE = "request/proof/add_form";

    /** Request - Saved proof request with success. */
    public static final String ADD_PROOF_REQUEST_SUCCESS_PAGE = "request/proof/add_success";

    /** Request - Add universe server form. */
    public static final String ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE = "request/universe_server/add_form";

    /** Request - Saved universe server request with success. */
    public static final String ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE = "request/universe_server/add_success";

    /** Generic error page. */
    public static final String ERROR_PAGE = "util/errors/error";

    /** Error 404 page. */
    public static final String ERROR_404_PAGE = "util/errors/error-404";

    /** Error 500 page. */
    public static final String ERROR_500_PAGE = "util/errors/error-500";

}
