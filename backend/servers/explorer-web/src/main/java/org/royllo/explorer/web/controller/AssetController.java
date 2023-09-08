package org.royllo.explorer.web.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.proof.ProofFileService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.core.service.asset.AssetStateServiceImplementation.SEARCH_PARAMETER_ASSET_ID;
import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_PROOFS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_STATES_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_STATES_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE_FRAGMENT;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PROOFS_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PROOFS_PAGE_FRAGMENT;

/**
 * Asset controller.
 */
@Controller
@RequiredArgsConstructor
public class AssetController extends BaseController {

    /** Asset service. */
    private final AssetService assetService;

    /** Asset state service. */
    private final AssetStateService assetStateService;

    /** Proof file service. */
    private final ProofFileService proofFileService;

    /**
     * Page displaying an asset.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @return view asset page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset", "/asset/", "/asset/{assetId}"})
    public String getAssetByAssetId(final Model model,
                                    final HttpServletRequest request,
                                    @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        // If assetId is present, we retrieve it.
        if (StringUtils.isNotBlank(assetId)) {
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId);

            // =========================================================================================================
            // We retrieve the asset and the assert group.
            assetService.getAssetByAssetId(assetId.trim()).ifPresent(asset -> model.addAttribute(RESULT_ATTRIBUTE, asset));

            // =========================================================================================================
            // We retrieve the asset states.
            final Page<AssetStateDTO> assetStates = assetStateService.queryAssetStates(SEARCH_PARAMETER_ASSET_ID + assetId.trim(),
                    1,
                    ASSET_STATES_DEFAULT_PAGE_SIZE);
            model.addAttribute(ASSET_STATES_LIST_ATTRIBUTE, assetStates.getContent());
        }
        // If it's an HTMX request, we return the fragment.
        if (isHtmxRequest(request)) {
            return ASSET_PAGE_FRAGMENT;
        }
        return ASSET_PAGE;
    }

    /**
     * Page displaying the proofs of an asset.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @param page    page number
     * @return proofs
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/proofs", "/asset/{assetId}/proofs/"})
    public String getProofsByAssetId(final Model model,
                                     final HttpServletRequest request,
                                     @PathVariable(name = ASSET_ID_ATTRIBUTE, required = false) final String assetId,
                                     @RequestParam(defaultValue = "1") final int page) {
        // If assetId is present, we retrieve it.
        if (StringUtils.isNotBlank(assetId)) {
            // Value the user asked for.
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId.trim());
            model.addAttribute(PAGE_ATTRIBUTE, page);

            // We retrieve the proofs to display them IF the asset is found.
            assetService.getAssetByAssetId(assetId.trim()).ifPresent(assetDTO -> model.addAttribute(RESULT_ATTRIBUTE,
                    proofFileService.getProofFilesByAssetId(assetId.trim(),
                            page,
                            ASSET_PROOFS_DEFAULT_PAGE_SIZE)));
        }
        // If it's an HTMX request, we return the fragment.
        if (isHtmxRequest(request)) {
            return ASSET_PROOFS_PAGE_FRAGMENT;
        }
        return ASSET_PROOFS_PAGE;
    }

}
