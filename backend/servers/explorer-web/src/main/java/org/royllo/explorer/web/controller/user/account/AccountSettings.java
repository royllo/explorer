package org.royllo.explorer.web.controller.user.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.util.base.BaseController;
import org.royllo.explorer.web.util.exception.AccessForbiddenException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.AccountSettingsPageConstants.ACCOUNT_SETTINGS_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.SUCCESSFUL_OPERATION_ATTRIBUTE;

/**
 * Account settings.
 */
@Controller
@RequiredArgsConstructor
public class AccountSettings extends BaseController {

    /** User service. */
    private final UserService userService;

    /**
     * Display account settings.
     *
     * @param model       model
     * @param currentUser current user
     * @return account settings
     */
    @GetMapping("/account/settings")
    @PreAuthorize("isAuthenticated()")
    public String displayAccountSettings(final Model model,
                                         final @AuthenticationPrincipal UserDetails currentUser) {
        Optional<UserDTO> connectedUser = userService.getUserByUsername(currentUser.getUsername());
        if (connectedUser.isPresent()) {
            logger.info("Displaying account settings for user {}: {}", connectedUser.get().getUsername(), connectedUser.get());
            AccountSettingsForm accountSettingsForm = new AccountSettingsForm();
            accountSettingsForm.setFullName(connectedUser.get().getFullName());
            accountSettingsForm.setBiography(connectedUser.get().getBiography());
            accountSettingsForm.setWebsite(connectedUser.get().getWebsite());
            model.addAttribute(FORM_ATTRIBUTE, accountSettingsForm);
            return ACCOUNT_SETTINGS_PAGE;
        } else {
            logger.error("User not found: {}", currentUser);
            throw new AccessForbiddenException();
        }
    }

    /**
     * Account settings update.
     *
     * @param model         model
     * @param currentUser   current user
     * @param form          form
     * @param bindingResult binding result
     * @return account settings
     */
    @PostMapping("/account/settings")
    @PreAuthorize("isAuthenticated()")
    public String updateAccountSettings(final Model model,
                                        final @AuthenticationPrincipal UserDetails currentUser,
                                        @Valid @ModelAttribute(FORM_ATTRIBUTE) final AccountSettingsForm form,
                                        final BindingResult bindingResult) {
        Optional<UserDTO> connectedUser = userService.getUserByUsername(currentUser.getUsername());
        if (connectedUser.isPresent()) {
            logger.info("Displaying account settings for user {}: {}", connectedUser.get().getUsername(), connectedUser.get());
            if (!bindingResult.hasErrors()) {
                // Calling the service to update the user.
                connectedUser.get().setFullName(form.getFullName());
                connectedUser.get().setBiography(form.getBiography());
                connectedUser.get().setWebsite(form.getWebsite());
                userService.updateUser(connectedUser.get().getUsername(), connectedUser.get());
                // Add an indicator to the model to indicate the update was successful.
                model.addAttribute(SUCCESSFUL_OPERATION_ATTRIBUTE, true);
            }
            return ACCOUNT_SETTINGS_PAGE;
        } else {
            logger.error("User not found: {}", currentUser);
            throw new AccessForbiddenException();
        }
    }

}
