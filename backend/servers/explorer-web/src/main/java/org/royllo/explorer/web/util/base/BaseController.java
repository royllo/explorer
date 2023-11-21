package org.royllo.explorer.web.util.base;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

/**
 * Base controller.
 */
public class BaseController {

    /** HTMX request header. */
    public static final String HTMX_REQUEST = "HX-Request";

    /** Page and fragment separator. */
    private static final String PAGE_AND_FRAGMENT_SEPARATOR = " :: ";

    /** Page fragment suffix. */
    private static final String PAGE_FRAGMENT_SUFFIX = "-fragment";

    /**
     * Get page to display (page or page fragment) based on the request.
     *
     * @param request request
     * @param page    page
     * @return page to display
     */
    protected final String getPageOrFragment(@NonNull final HttpServletRequest request,
                                             @NonNull final String page) {

        if (request.getHeader(HTMX_REQUEST) != null) {
            // HTMX_REQUEST header is present, return only the page fragment.
            return page
                    + PAGE_AND_FRAGMENT_SEPARATOR
                    + page.replace("/", "-")
                    + PAGE_FRAGMENT_SUFFIX;

        } else {
            return page;
        }

    }

    /**
     * Returns true is the request is an HTMX request.
     *
     * @param request request
     * @return true is HTMX request
     */
    @Deprecated
    protected final boolean isHtmxRequest(final HttpServletRequest request) {
        return request != null && request.getHeader(HTMX_REQUEST) != null;
    }

}
