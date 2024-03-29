package org.royllo.explorer.web.controller.asset;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_GROUP_ASSETS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_PROOFS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_STATES_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GENESIS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GROUP_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_OWNER_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PROOFS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_README_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_README_VALUE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_STATES_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSETS_IN_GROUP_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_STATES_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_URL_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_TITLE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PROOF_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.WEB_BASE_URL_ATTRIBUTE;

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
    private final ProofService proofService;

    /**
     * Page displaying the asset genesis (which is the default page).
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @return asset genesis page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset", "/asset/", "/asset/{assetId}", "/asset/{assetId}/genesis"})
    public String assetGenesis(final Model model,
                               final HttpServletRequest request,
                               @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        addAssetToModel(model, assetId);

        return getPageOrFragment(request, ASSET_GENESIS_PAGE);
    }

    /**
     * Page displaying the readme.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @return asset group page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/readme"})
    public String assetReadme(final Model model,
                              final HttpServletRequest request,
                              @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        final Optional<AssetDTO> assetDTO = addAssetToModel(model, assetId);

        // Parsing readme to html.
        if (assetDTO.isPresent() && assetDTO.get().getReadme() != null) {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(assetDTO.get().getReadme());
            HtmlRenderer renderer = HtmlRenderer.builder().sanitizeUrls(true).build();
            model.addAttribute(ASSET_README_VALUE, renderer.render(document));
        }

        return getPageOrFragment(request, ASSET_README_PAGE);
    }

    /**
     * Page displaying the asset group.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @param page    page number
     * @return asset group page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/group"})
    public String assetGroup(final Model model,
                             final HttpServletRequest request,
                             @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId,
                             @RequestParam(defaultValue = "1") final int page) {
        final Optional<AssetDTO> asset = addAssetToModel(model, assetId);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        // We search for the assets in the same asset group.
        if (asset.isPresent() && asset.get().getAssetGroup() != null) {
            model.addAttribute(ASSETS_IN_GROUP_LIST_ATTRIBUTE, assetService.getAssetsByAssetGroupId(asset.get().getAssetGroup().getAssetGroupId(),
                    page,
                    ASSET_GROUP_ASSETS_DEFAULT_PAGE_SIZE));
        }

        return getPageOrFragment(request, ASSET_GROUP_PAGE);
    }

    /**
     * Page displaying the asset states.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @param page    page number
     * @return asset states page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/states"})
    public String assetStates(final Model model,
                              final HttpServletRequest request,
                              @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId,
                              @RequestParam(defaultValue = "1") final int page) {
        addAssetToModel(model, assetId);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        // We retrieve the asset states.
        model.addAttribute(ASSET_STATES_LIST_ATTRIBUTE, assetStateService.getAssetStatesByAssetId(assetId,
                page,
                ASSET_STATES_DEFAULT_PAGE_SIZE));

        return getPageOrFragment(request, ASSET_STATES_PAGE);
    }

    /**
     * Page displaying the asset owner.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @return asset owner page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/owner"})
    public String assetOwner(final Model model,
                             final HttpServletRequest request,
                             @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        addAssetToModel(model, assetId);

        return getPageOrFragment(request, ASSET_OWNER_PAGE);
    }

    /**
     * Page displaying the asset proofs.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @param page    page number
     * @return proofs owner page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset/{assetId}/proofs"})
    public String assetProofs(final Model model,
                              final HttpServletRequest request,
                              @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId,
                              @RequestParam(defaultValue = "1") final int page) {
        addAssetToModel(model, assetId);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        // We retrieve the proof files.
        model.addAttribute(PROOF_LIST_ATTRIBUTE, proofService.getProofsByAssetId(assetId.trim(),
                page,
                ASSET_PROOFS_DEFAULT_PAGE_SIZE));

        return getPageOrFragment(request, ASSET_PROOFS_PAGE);
    }

    /**
     * Add asset data to the model.
     *
     * @param model   model
     * @param assetId asset id
     * @return asset
     */
    private Optional<AssetDTO> addAssetToModel(final Model model, final String assetId) {
        model.addAttribute(ASSET_ID_ATTRIBUTE, assetId);
        final Optional<AssetDTO> asset = assetService.getAssetByAssetIdOrAlias(assetId);
        if (asset.isPresent()) {
            model.addAttribute(ASSET_ATTRIBUTE, asset.get());
            // We also set the url to share the asset.
            model.addAttribute(ASSET_URL_ATTRIBUTE, model.getAttribute(WEB_BASE_URL_ATTRIBUTE) + "/asset/" + asset.get().getAssetIdAlias());
            model.addAttribute(PAGE_TITLE_ATTRIBUTE, asset.get().getName() + " - " + asset.get().getAssetId());
        }
        return asset;
    }

}
