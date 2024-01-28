package org.royllo.explorer.web.util.constants;

import lombok.experimental.UtilityClass;

/**
 * Authentication page constants.
 */
@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class AuthenticationPageConstants {

    /** The path to the login page. */
    public static final String LNURL_AUTH_LOGIN_PAGE_PATH = "/login";

    /** The path to the auth wallet login page. */
    public static final String LNURL_AUTH_WALLET_LOGIN_PATH = "/api/v1/lnurl-auth/login/wallet";

    /** The path to the auth session login page. */
    public static final String LNURL_AUTH_SESSION_LOGIN_PATH = "/api/v1/lnurl-auth/login/session?redirect=/";

    /** Authentication session K1 key. */
    public static final String LNURL_AUTH_SESSION_K1_KEY = "k1";

}
