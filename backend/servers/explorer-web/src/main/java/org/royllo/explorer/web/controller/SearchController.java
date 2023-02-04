package org.royllo.explorer.web.controller;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.royllo.explorer.web.configuration.WebConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE;

/**
 * Search controller.
 */
@Controller
@RequiredArgsConstructor
public class SearchController {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Search result.
     *
     * @param model model
     * @param query query
     * @param page  page number
     * @return page to display
     */
    @GetMapping("/search")
    public String home(final Model model,
                       @RequestParam(required = false) final Optional<String> query,
                       @RequestParam(defaultValue = "1") final int page) {
        // If the query is present, we make the search.
        if (query.isPresent() && !query.get().trim().isEmpty()) {
            // Value the user searched for and the page.
            model.addAttribute(QUERY_ATTRIBUTE, query.get().trim());
            model.addAttribute(PAGE_ATTRIBUTE, page);

            // Adding result to the page.
            model.addAttribute(RESULT_ATTRIBUTE, assetService.queryAssets(query.get(),
                    page,
                    DEFAULT_PAGE_SIZE));
        }
        return SEARCH_PAGE;
    }

}
