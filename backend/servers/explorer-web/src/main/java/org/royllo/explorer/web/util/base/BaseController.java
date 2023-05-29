package org.royllo.explorer.web.util.base;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Base controller.
 */
public class BaseController {

    /** HTMX request header. */
    public static final String HTMX_REQUEST = "HX-Request";

    /**
     * Returns true is the request is an HTMX request.
     *
     * @param request request
     * @return true is HTMX request
     */
    protected final boolean isHtmxRequest(final HttpServletRequest request) {
        return request != null && request.getHeader(HTMX_REQUEST) != null;
    }

}
