package org.royllo.explorer.web.controller.user.account;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.dto.user.UserDTOSettings;
import org.royllo.explorer.core.provider.storage.ContentService;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.core.util.enums.FileType;
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

import java.io.IOException;
import java.util.Optional;

import static org.royllo.explorer.core.util.enums.FileType.IMAGE;
import static org.royllo.explorer.web.util.constants.AccountSettingsPageConstants.ACCOUNT_SETTINGS_PAGE;
import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.SHORT_USERNAME;
import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.USERNAME;
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

    /** Content service. */
    private final ContentService contentService;

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
        Optional<UserDTO> connectedUser = userService.getUserByUserId(currentUser.getUsername());
        if (connectedUser.isPresent()) {
            logger.info("Displaying account settings for user {}: {}", connectedUser.get().getUsername(), connectedUser.get());
            AccountSettingsForm accountSettingsForm = new AccountSettingsForm();
            accountSettingsForm.setUsername(connectedUser.get().getUsername());
            accountSettingsForm.setProfilePictureFilename(connectedUser.get().getProfilePictureFileName());
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
     * @param session       session
     * @param currentUser   current user
     * @param form          form
     * @param bindingResult binding result
     * @return account settings
     */
    @PostMapping("/account/settings")
    @PreAuthorize("isAuthenticated()")
    public String updateAccountSettings(final Model model,
                                        final HttpSession session,
                                        final @AuthenticationPrincipal UserDetails currentUser,
                                        @Valid @ModelAttribute(FORM_ATTRIBUTE) final AccountSettingsForm form,
                                        final BindingResult bindingResult) throws IOException, MimeTypeException {
        Optional<UserDTO> connectedUser = userService.getUserByUserId(currentUser.getUsername());
        if (connectedUser.isPresent()) {
            logger.info("Displaying account settings for user {}: {}", connectedUser.get().getUsername(), connectedUser.get());
            if (!bindingResult.hasErrors()) {
                if (!form.getUsername().trim().equals(connectedUser.get().getUsername())) {
                    // Check if the username is already taken.
                    if (userService.getUserByUsername(form.getUsername()).isPresent()) {
                        bindingResult.rejectValue("username", "validation.user.username.exists");
                        return ACCOUNT_SETTINGS_PAGE;
                    }
                }

                // Calling the service to update the user.
                final String previousUsername = connectedUser.get().getUsername();
                connectedUser.get().setUsername(form.getUsername());
                connectedUser.get().setFullName(form.getFullName());
                connectedUser.get().setBiography(form.getBiography());
                connectedUser.get().setWebsite(form.getWebsite());

                // If we have a picture set, we save it.
                if (form.getProfilePicture() != null && !form.getProfilePicture().isEmpty()) {
                    byte[] file = form.getProfilePicture().getBytes();

                    // We retrieve the type.
                    final String mimeType = new Tika().detect(file);
                    String extension = MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();

                    if (FileType.getTypeByExtension(extension) == IMAGE) {
                        String filename = connectedUser.get().getUserId() + extension;
                        contentService.storeFile(form.getProfilePicture().getBytes(), filename);
                        connectedUser.get().setProfilePictureFileName(filename);
                        form.setProfilePictureFilename(filename);
                    }
                    // TODO Create an error if the type is not an image.
                }
                userService.updateUser(currentUser.getUsername(),
                        UserDTOSettings.builder()
                                .username(form.getUsername())
                                .profilePictureFileName(form.getProfilePictureFilename())
                                .fullName(form.getFullName())
                                .biography(form.getBiography())
                                .website(form.getWebsite())
                                .build()
                );

                // Change session username.
                session.setAttribute(USERNAME, connectedUser.get().getUsername());
                session.setAttribute(SHORT_USERNAME, connectedUser.get().getShortenedUsername());

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
