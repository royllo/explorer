package org.royllo.explorer.web.controller.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE;

/**
 * Add universe server request controller.
 */
@Controller
@RequiredArgsConstructor
public class AddUniverseServerRequestController {

    /**
     * Request service.
     */
    private final RequestService requestService;

    /**
     * Requests - Add universe server form.
     *
     * @param model model
     * @return universe server form
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/request/universe_server/add")
    public String displayForm(final Model model) {
        model.addAttribute(FORM_ATTRIBUTE, new AddUniverseServerRequestForm());
        return ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE;
    }

    /**
     * Requests - Add universe server post.
     *
     * @param model         model
     * @param form          form
     * @param bindingResult binding result
     * @return proof success or error
     */
    @PostMapping("/request/universe_server/add")
    public String saveForm(final Model model,
                           @Valid @ModelAttribute(FORM_ATTRIBUTE) final AddUniverseServerRequestForm form,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If we have errors in the form data validation.
            return ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE;
        } else {
            // Calling the service to create the request.
            model.addAttribute(RESULT_ATTRIBUTE, requestService.createAddUniverseServerRequest(form.getServerAddress()));
            return ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE;
        }
    }

}
