package org.royllo.explorer.web.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.REQUEST_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.CHOOSE_REQUEST_TYPE_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.CHOOSE_REQUEST_TYPE_PAGE_FRAGMENT;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;

/**
 * Request controller.
 */
@Controller
@RequiredArgsConstructor
public class RequestController extends BaseController {

    /** Request service. */
    private final RequestService requestService;

    /**
     * Page letting the user choose the type of request to make.
     *
     * @param model   model
     * @param request request
     * @return page to display
     */
    @GetMapping(value = {"/request/choose_request_type"})
    public String chooseRequestType(final Model model,
                                    final HttpServletRequest request) {
        // If it's an HTMX request, we return the fragment.
        if (isHtmxRequest(request)) {
            return CHOOSE_REQUEST_TYPE_PAGE_FRAGMENT;
        }
        return CHOOSE_REQUEST_TYPE_PAGE;
    }

    /**
     * Page displaying a request.
     *
     * @param model     model
     * @param requestId request id
     * @return page to display
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/request", "/request/", "/request/{requestId}"})
    public String getRequestByRequestId(final Model model,
                                        @PathVariable(value = REQUEST_ID_ATTRIBUTE, required = false) final String requestId) {
        // If requestId is present, we retrieve it.
        if (StringUtils.isNotBlank(requestId)) {
            // Value the user asked for.
            model.addAttribute(REQUEST_ID_ATTRIBUTE, requestId.trim());

            // We retrieve the request to display it.
            requestService.getRequestByRequestId(requestId.trim()).ifPresent(request -> model.addAttribute(RESULT_ATTRIBUTE, request));
        }
        return REQUEST_PAGE;
    }

}
