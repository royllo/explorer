package org.royllo.explorer.web.controller.user.request;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.USER_ID;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.CLAIM_ASSET_OWNERSHIP_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.CLAIM_ASSET_OWNERSHIP_REQUEST_SUCCESS_PAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Controller for handling claim asset ownership requests.
 */
@Controller
@RequiredArgsConstructor
public class ClaimAssetOwnershipRequestController {

    /** User service. */
    private final UserService userService;

    /** Request service. */
    private final RequestService requestService;

    /**
     * Requests - Add asset ownership claim request form.
     *
     * @param model model
     * @return proof form
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/account/request/claim_asset_ownership/add")
    public String forms(final Model model) {
        model.addAttribute(FORM_ATTRIBUTE, new ClaimAssetOwnershipRequestForm());
        return CLAIM_ASSET_OWNERSHIP_REQUEST_FORM_PAGE;
    }

    /**
     * Requests - Save asset ownership claim request form.
     *
     * @param model         model
     * @param session       session
     * @param currentUser   current user
     * @param form          form
     * @param bindingResult binding result
     * @return proof success or error
     */
    @PostMapping("/account/request/claim_asset_ownership/add")
    public String saveForm(final Model model,
                           final HttpSession session,
                           final @AuthenticationPrincipal UserDetails currentUser,
                           @Valid @ModelAttribute(FORM_ATTRIBUTE) final ClaimAssetOwnershipRequestForm form,
                           final BindingResult bindingResult) {

        // Getting user id from session or from the current user (Forced to do that for tests).
        String userId = (String) session.getAttribute(USER_ID);
        if (userId == null) {
            userId = userService.getUserByUserId(currentUser.getUsername())
                    .map(UserDTO::getUserId)
                    .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
        }

        if (bindingResult.hasErrors() || userId == null) {
            // If we have errors in the form data validation.
            return CLAIM_ASSET_OWNERSHIP_REQUEST_FORM_PAGE;
        } else {
            // Calling the service to create the request.
            model.addAttribute(RESULT_ATTRIBUTE, requestService.createClaimAssetOwnershipRequest(userId, form.getProofWithWitness()));
            return CLAIM_ASSET_OWNERSHIP_REQUEST_SUCCESS_PAGE;
        }
    }

}
