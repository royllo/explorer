package org.royllo.explorer.web.controller;

import io.github.wimdeblauwe.hsbt.mvc.HxRequest;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_SEARCH_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE_FRAGMENT;

/**
 * Search controller.
 */
@Controller
@RequiredArgsConstructor
public class SearchController {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Page displaying search results.
     *
     * @param model model
     * @param query query
     * @param page  page number
     * @return page to display
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/search")
    public String search(final Model model,
                         @RequestParam(required = false) final String query,
                         @RequestParam(defaultValue = "1") final int page) {
        if (StringUtils.isNotBlank(query)) {
            model.addAttribute(QUERY_ATTRIBUTE, query.trim());
            model.addAttribute(PAGE_ATTRIBUTE, page);

            // If the query is present, we make the search and add result to the page.
            model.addAttribute(RESULT_ATTRIBUTE, assetService.queryAssets(query.trim(), page, ASSET_SEARCH_DEFAULT_PAGE_SIZE));
        }
        return SEARCH_PAGE;
    }

    /**
     * Page displaying search results (HTMX access).
     *
     * @param model model
     * @param query query
     * @param page  page number
     * @return page to display
     */
    @SuppressWarnings("SameReturnValue")
    @HxRequest
    @GetMapping("/search")
    public String searchWithHTMX(final Model model,
                                 @RequestParam(required = false) final String query,
                                 @RequestParam(defaultValue = "1") final int page) {
        search(model, query, page);
        return SEARCH_PAGE_FRAGMENT;
    }

}
