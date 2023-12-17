package org.royllo.explorer.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.service.statistics.StatisticService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.web.util.constants.HomePagesConstants.GLOBAL_STATISTICS_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.HomePagesConstants.HOME_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;

/**
 * Home page controller.
 */
@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

    /** Statistic service. */
    private final StatisticService statisticService;

    /**
     * Page displaying home.
     *
     * @param model   model
     * @param request request
     * @param query   query
     * @return page to display
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String home(final Model model,
                       final HttpServletRequest request,
                       @RequestParam(required = false) final String query) {
        // Update the model with the query.
        model.addAttribute(QUERY_ATTRIBUTE, query);

        // Add the statistics to the model.
        model.addAttribute(GLOBAL_STATISTICS_ATTRIBUTE, statisticService.getGlobalStatistics());

        return getPageOrFragment(request, HOME_PAGE);
    }

}
