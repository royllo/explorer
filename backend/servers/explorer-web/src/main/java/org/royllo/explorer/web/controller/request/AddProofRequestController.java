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
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_PROOF_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_PROOF_REQUEST_SUCCESS_PAGE;

/**
 * Add proof request controller.
 */
@Controller
@RequiredArgsConstructor
public class AddProofRequestController {

    /**
     * Request service.
     */
    private final RequestService requestService;

    /**
     * Requests - Add proof form.
     *
     * @param model model
     * @return proof form
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/request/proof/add")
    public String displayForm(final Model model) {
        model.addAttribute(FORM_ATTRIBUTE, new AddProofRequestForm());
        return ADD_PROOF_REQUEST_FORM_PAGE;
    }

    /**
     * Requests - Add proof post.
     *
     * @param model         model
     * @param form          form
     * @param bindingResult binding result
     * @return proof success or error
     */
    @PostMapping("/request/proof/add")
    public String saveForm(final Model model,
                           @Valid @ModelAttribute(FORM_ATTRIBUTE) final AddProofRequestForm form,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If we have errors in the form data validation.
            return ADD_PROOF_REQUEST_FORM_PAGE;
        } else {
            // Calling the service to create the request.
            model.addAttribute(RESULT_ATTRIBUTE, requestService.createAddProofRequest(form.getProof()));
            return ADD_PROOF_REQUEST_SUCCESS_PAGE;
        }
    }

}
