package org.royllo.explorer.web.controller;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_PROOFS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PROOFS_PAGE;

/**
 * Asset controller.
 */
@Controller
@RequiredArgsConstructor
public class AssetController {

    /** Asset service. */
    private final AssetService assetService;

    /** Proof service. */
    private final ProofService proofService;

    /**
     * Page displaying an asset.
     *
     * @param model   model
     * @param assetId asset id
     * @return view asset page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset", "/asset/", "/asset/{assetId}"})
    public String getAssetByAssetId(final Model model,
                                    @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        // If assetId is present, we retrieve it.
        if (StringUtils.isNotBlank(assetId)) {
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId);

            // We retrieve the asset to display it.
            assetService.getAssetByAssetId(assetId.trim()).ifPresent(asset -> model.addAttribute(RESULT_ATTRIBUTE, asset));
        }
        return ASSET_PAGE;
    }

    /**
     * Page display the proofs of an asset.
     *
     * @param model   model
     * @param assetId asset id
     * @return proofs
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/proofs", "/asset/{assetId}/proofs/"})
    public String getProofsByAssetId(final Model model,
                                     @PathVariable(name = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        // If assetId is present, we retrieve it.
        if (StringUtils.isNotBlank(assetId)) {
            // Value the user asked for.
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId.trim());

            // We retrieve the proofs to display them IF the asset is found.
            assetService.getAssetByAssetId(assetId.trim()).ifPresent(assetDTO -> model.addAttribute(RESULT_ATTRIBUTE,
                    proofService.getProofsByAssetId(assetId.trim(),
                            1,
                            ASSET_PROOFS_DEFAULT_PAGE_SIZE)));
        }
        return ASSET_PROOFS_PAGE;
    }

}
