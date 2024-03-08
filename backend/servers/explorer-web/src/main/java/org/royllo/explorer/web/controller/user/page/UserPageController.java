package org.royllo.explorer.web.controller.user.page;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import static org.royllo.explorer.web.configuration.WebConfiguration.USER_ASSETS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.USERNAME_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.USER_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_PUBLIC_PAGE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * User page controller.
 */
@Controller
@RequiredArgsConstructor
public class UserPageController extends BaseController {

    /** User service. */
    private final UserService userService;

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Returns a user's public page.
     *
     * @param model    model
     * @param username username parameter
     * @param page     page number
     * @return user public page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/user", "/user/", "/user/{username}", "/asset/{username}/"})
    public String assetGenesis(final Model model,
                               @PathVariable(value = USERNAME_ATTRIBUTE, required = false) final String username,
                               @RequestParam(defaultValue = "1") final int page) {
        // We set back the page number to the model.
        model.addAttribute(PAGE_ATTRIBUTE, page);

        // Getting the user by username.
        UserDTO user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        // Adding the user and the assets to the model.
        model.addAttribute(USER_ATTRIBUTE, user);
        model.addAttribute(RESULT_ATTRIBUTE, assetService.getAssetsByUsername(username,
                page,
                USER_ASSETS_DEFAULT_PAGE_SIZE));

        // Return the user public page.
        return USER_PUBLIC_PAGE;
    }

}
