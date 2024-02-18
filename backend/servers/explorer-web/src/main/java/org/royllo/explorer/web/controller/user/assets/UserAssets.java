package org.royllo.explorer.web.controller.user.assets;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.web.configuration.WebConfiguration.USER_ASSETS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSETS_PAGE;

/**
 * User assets controller.
 */
@Controller
@RequiredArgsConstructor
public class UserAssets {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Display user assets.
     *
     * @param model       model
     * @param currentUser current user
     * @param page        page
     * @return user assets page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/account/assets"})
    public String userAssets(final Model model,
                             final @AuthenticationPrincipal UserDetails currentUser,
                             @RequestParam(defaultValue = "1") final int page) {
        // We set back the page number to the model.
        model.addAttribute(PAGE_ATTRIBUTE, page);

        // If the query is present, we make the search and add result to the page.
        model.addAttribute(RESULT_ATTRIBUTE, assetService.getAssetsByUsername(currentUser.getUsername(),
                page,
                USER_ASSETS_DEFAULT_PAGE_SIZE));

        return USER_ASSETS_PAGE;
    }

}
