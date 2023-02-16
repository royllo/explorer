package org.royllo.explorer.web.controller;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.REQUEST_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;

/**
 * Request controller.
 */
@Controller
@RequiredArgsConstructor
public class RequestController {

    /** Request service. */
    private final RequestService requestService;

    /**
     * Page displaying a request.
     *
     * @param model     model
     * @param requestId request id
     * @return page to display
     */
    @GetMapping(value = {"/request", "/request/", "/request/{requestId}"})
    public String getRequestByRequestId(final Model model,
                                        @PathVariable(REQUEST_ID_ATTRIBUTE) final Optional<String> requestId) {
        // TODO Add a test to see if trim is necessary ??
        // If requestId is present, we retrieve it.
        if (requestId.isPresent() && !requestId.get().trim().isEmpty()) {
            // Value the user asked for.
            model.addAttribute(REQUEST_ID_ATTRIBUTE, requestId.get().trim());

            // We retrieve the request to display it.
            requestService.getRequestByRequestId(requestId.get().trim()).ifPresent(request -> model.addAttribute(RESULT_ATTRIBUTE, request));
        } else {
            // If the user just typed "/request" or "/request/".
            model.addAttribute(REQUEST_ID_ATTRIBUTE, "");
        }
        return REQUEST_PAGE;
    }

}
