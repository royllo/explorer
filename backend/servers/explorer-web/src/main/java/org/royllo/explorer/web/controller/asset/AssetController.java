package org.royllo.explorer.web.controller.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;

/**
 * Asset controller.
 */
@Controller
@RequiredArgsConstructor
public class AssetController {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Page displaying an asset.
     *
     * @param model   model
     * @param assetId asset id
     * @return view asset page
     */
    @GetMapping(value = {"/asset/", "/asset/{assetId}"})
    public String getAssetByAssetId(final Model model,
                                    @PathVariable("assetId") final Optional<String> assetId) {
        // If assetId is present, we retrieve it.
        if (assetId.isPresent() && !assetId.get().trim().isEmpty()) {
            // Value the user asked for.
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId.get());

            // We retrieve the asset to display it.
            assetService.getAssetByAssetId(assetId.get()).ifPresent(asset -> model.addAttribute(RESULT_ATTRIBUTE, asset));
        } else {
            // If the user just typed "/asset/".
            model.addAttribute(ASSET_ID_ATTRIBUTE, "");
        }
        return ASSET_PAGE;
    }

}
