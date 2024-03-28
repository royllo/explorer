package org.royllo.explorer.web.controller.util;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.util.parameters.RoylloExplorerParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.SITEMAP_URLS_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.SITEMAP_PAGE;

/**
 * Sitemap controller.
 */
@Controller
@RequiredArgsConstructor
public class SitemapController {

    /** Royllo explorer parameters. */
    private final RoylloExplorerParameters roylloExplorerParameters;

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /**
     * Page displaying the sitemap.
     *
     * @param model model
     * @return asset genesis page
     */
    @SuppressWarnings("SameReturnValue")
    // @GetMapping(value = {"/sitemap.xml"})
    public String sitemap(final Model model) {

        // Get all asset ids and generate the corresponding urls.
        final String baseUrl = roylloExplorerParameters.getWeb().getBaseUrl() + "/asset/";
        List<String> urls = assetRepository.findAll()
                .stream()
                .map(Asset::getAssetId)
                .map(assetId -> baseUrl + assetId)
                .toList();
        model.addAttribute(SITEMAP_URLS_ATTRIBUTE, urls);

        return SITEMAP_PAGE;
    }

}
