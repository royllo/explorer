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

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.COMMAND_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_ADD_PROOF_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_ADD_PROOF_SUCCESS_PAGE;

/**
 * Proof request controller.
 */
@Controller
@RequiredArgsConstructor
public class ProofRequestController {

    /** Request service. */
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
        model.addAttribute(COMMAND_ATTRIBUTE, new ProofFormCommand());
        return REQUEST_ADD_PROOF_FORM_PAGE;
    }

    /**
     * Requests - Add proof post.
     *
     * @param model         model
     * @param command       form
     * @param bindingResult binding result
     * @return proof success or error
     */
    @PostMapping("/request/proof/add")
    public String saveForm(final Model model,
                           @Valid @ModelAttribute(COMMAND_ATTRIBUTE) final ProofFormCommand command,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if we have errors in the form data validation.
            return REQUEST_ADD_PROOF_FORM_PAGE;
        } else {
            // Calling the service to create the proof.
            model.addAttribute(RESULT_ATTRIBUTE, requestService.addProof(command.getRawProof()));
            return REQUEST_ADD_PROOF_SUCCESS_PAGE;
        }
    }

}
