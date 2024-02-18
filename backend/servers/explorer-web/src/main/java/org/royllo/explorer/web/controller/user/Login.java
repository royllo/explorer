package org.royllo.explorer.web.controller.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.SHORT_USERNAME;
import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.USERNAME;
import static org.royllo.explorer.web.util.constants.AuthenticationSessionConstants.USER_ID;
import static org.royllo.explorer.web.util.constants.HomePagesConstants.HOME_PAGE;

/**
 * Login controller.
 */
@Controller
@RequiredArgsConstructor
public class Login extends BaseController {

    /** User service. */
    private final UserService userService;

    /**
     * Called on successful login.
     *
     * @param session     session
     * @param currentUser current
     * @return home page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/login/success"})
    public String loginSuccess(final HttpSession session,
                               final @AuthenticationPrincipal UserDetails currentUser) {

        // We will set useful information in the session.
        Optional<UserDTO> connectedUser = userService.getUserByUsername(currentUser.getUsername());
        if (connectedUser.isPresent()) {
            logger.info("User {} has logged in successfully.", connectedUser.get().getUsername());
            session.setAttribute(USER_ID, connectedUser.get().getUserId());
            session.setAttribute(USERNAME, connectedUser.get().getUsername());
            session.setAttribute(SHORT_USERNAME, connectedUser.get().getShortenedUsername());

            // If user did not set its settings, we send it to settings.
            if (!connectedUser.get().hasSettingsSet()) {
                logger.info("User {} has not set its settings yet. Redirecting to account settings", connectedUser.get().getUsername());
                return "redirect:/account/settings";
            }
        }
        return HOME_PAGE;

    }

}
