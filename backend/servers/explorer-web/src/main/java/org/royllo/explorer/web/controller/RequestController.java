package org.royllo.explorer.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.REQUEST_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;

/**
 * Request controller.
 */
@Controller
public class RequestController {

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
        // If requestId is present, we retrieve it.
        if (requestId.isPresent() && !requestId.get().trim().isEmpty()) {
            // Value the user asked for.
            model.addAttribute(REQUEST_ID_ATTRIBUTE, requestId.get().trim());

            // We retrieve the asset to display it.
            // assetService.getAssetByAssetId(assetId.get().trim()).ifPresent(asset -> model.addAttribute(RESULT_ATTRIBUTE, asset));
        } else {
            // If the user just typed "/asset" or "/asset/".
            model.addAttribute(REQUEST_ID_ATTRIBUTE, "");
        }
        return REQUEST_PAGE;
    }

}
