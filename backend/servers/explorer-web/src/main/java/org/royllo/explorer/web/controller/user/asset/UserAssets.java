package org.royllo.explorer.web.controller.user.asset;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetDTOCreatorUpdate;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.royllo.explorer.web.configuration.WebConfiguration.USER_ASSETS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_NAME_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSETS_PAGE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSET_FORM_PAGE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * User assets controller.
 */
@Controller
@RequiredArgsConstructor
public class UserAssets extends BaseController {

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
        model.addAttribute(RESULT_ATTRIBUTE, assetService.getAssetsByUserId(currentUser.getUsername(),
                page,
                USER_ASSETS_DEFAULT_PAGE_SIZE));

        return USER_ASSETS_PAGE;
    }

    /**
     * Display asset form.
     *
     * @param model       model
     * @param currentUser current user
     * @param assetId     asset id
     * @return asset form page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/account/asset/{assetId}/edit", "/account/asset/{assetId}/edit"})
    public String assetFormShow(final Model model,
                                final @AuthenticationPrincipal UserDetails currentUser,
                                @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        // Retrieving the asset.
        Optional<AssetDTO> asset = assetService.getAssetByAssetIdOrAlias(assetId);
        if (asset.isPresent()) {
            // If the user tries to access an asset he doesn't own, we throw an exception.
            if (!asset.get().getCreator().getUserId().equals(currentUser.getUsername())) {
                logger.error("User {} tried to access asset {} he doesn't own", currentUser.getUsername(), assetId);
                throw new ResponseStatusException(UNAUTHORIZED, "You don't own this asset: " + assetId);
            }

            // Ok, it exists and the user owns it, we can show the form.
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId);
            model.addAttribute(ASSET_NAME_ATTRIBUTE, asset.get().getName());
            UserAssetForm userAssetForm = new UserAssetForm();
            userAssetForm.setAssetIdAlias(asset.get().getAssetIdAlias());
            userAssetForm.setReadme(asset.get().getReadme());
            model.addAttribute(FORM_ATTRIBUTE, asset.get());
        } else {
            // If the asset doesn't exist, we throw an exception.
            logger.error("User {} tried to access asset {} but it doesn't exists", currentUser.getUsername(), assetId);
            throw new ResponseStatusException(NOT_FOUND, "This assert doesn't exists");
        }
        return USER_ASSET_FORM_PAGE;
    }

    /**
     * Save asset form.
     *
     * @param model         model
     * @param currentUser   current user
     * @param form          form
     * @param bindingResult binding result
     * @return asset form page
     */
    @SuppressWarnings("SameReturnValue")
    @PostMapping(value = {"/account/asset/save", "/account/asset/save/"})
    public String assetFormSave(final Model model,
                                final @AuthenticationPrincipal UserDetails currentUser,
                                @Valid @ModelAttribute(FORM_ATTRIBUTE) final UserAssetForm form,
                                final BindingResult bindingResult) {

        // Getting the asset
        Optional<AssetDTO> asset = assetService.getAssetByAssetIdOrAlias(form.getAssetId());
        if (asset.isPresent()) {

            // If the user tries to access an asset he doesn't own, we throw an exception.
            if (!asset.get().getCreator().getUserId().equals(currentUser.getUsername())) {
                logger.error("User {} tried to access asset {} he doesn't own", currentUser.getUsername(), form.getAssetId());
                throw new ResponseStatusException(UNAUTHORIZED, "You don't own this asset: " + form.getAssetId());
            }

            // We set the value we are displaying.
            model.addAttribute(ASSET_ID_ATTRIBUTE, form.getAssetId());
            model.addAttribute(ASSET_NAME_ATTRIBUTE, asset.get().getName());

            if (bindingResult.hasErrors()) {
                // If we have errors in the form data validation.
                return USER_ASSET_FORM_PAGE;
            } else {

                // If the user attempts to change the assetId, we first check if the assetIdAlias is unique.
                if (asset.get().getAssetIdAlias() != null
                        && !asset.get().getAssetIdAlias().equals(form.getAssetIdAlias())) {
                    Optional<AssetDTO> existingAsset = assetService.getAssetByAssetIdOrAlias(form.getAssetIdAlias());
                    if (existingAsset.isPresent()) {
                        bindingResult.rejectValue("assetIdAlias", "validation.asset.assetIdAlias.unique");
                        return USER_ASSET_FORM_PAGE;
                    }
                }

                // Ok, we can update and save.
                assetService.updateAssetCreatorData(form.getAssetId(),
                        AssetDTOCreatorUpdate.builder()
                                .assetIdAlias(form.getAssetIdAlias())
                                .readme(form.getReadme())
                                .build());
                return "redirect:/account/assets?assetUpdated=" + form.getAssetId();
            }
        } else {
            // If the asset doesn't exist, we throw an exception.
            logger.error("User {} tried to access asset {} but it doesn't exists", currentUser.getUsername(), form.getAssetId());
            throw new ResponseStatusException(NOT_FOUND, "This assert doesn't exists");
        }
    }

}
