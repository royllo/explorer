package org.royllo.explorer.web.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.search.SearchService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_SEARCH_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.SearchPageConstants.SEARCH_PAGE;

/**
 * Search controller.
 */
@Controller
@RequiredArgsConstructor
public class SearchController extends BaseController {

    /** Search service. */
    private final SearchService searchService;

    /**
     * Page displaying search results.
     *
     * @param model   model
     * @param request request
     * @param query   query
     * @param page    page number
     * @return page to display
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/search")
    public String search(final Model model,
                         final HttpServletRequest request,
                         @RequestParam(required = false) final String query,
                         @RequestParam(defaultValue = "1") final int page) {
        // Update the model with the query and page number.
        if (StringUtils.isNotBlank(query)) {
            model.addAttribute(QUERY_ATTRIBUTE, query.trim());
            model.addAttribute(PAGE_ATTRIBUTE, page);

            // If the query is present, we make the search and add result to the page.
            model.addAttribute(RESULT_ATTRIBUTE, searchService.queryAssets(query.trim(), page, ASSET_SEARCH_DEFAULT_PAGE_SIZE));
        }

        return getPageOrFragment(request, SEARCH_PAGE);
    }

}
