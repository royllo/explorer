package org.royllo.explorer.web.util.base;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.royllo.explorer.web.util.page.Page;

/**
 * Base controller.
 */
public class BaseController {

    /** HTMX request header. */
    public static final String HTMX_REQUEST = "HX-Request";

    /**
     * Get page to display (page or page fragment) based on the request.
     *
     * @param request request
     * @param page    page
     * @return page to display
     */
    protected final String getPage(@NonNull final HttpServletRequest request,
                                   @NonNull final Page page) {
        if (isHtmxRequest(request)) {
            return page.getPageFragment();
        } else {
            return page.getPageName();
        }
    }

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
