package org.royllo.explorer.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.royllo.explorer.web.util.constants.HomePagesConstants.HOME_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;

/**
 * Home page controller.
 */
@Controller
public class HomeController extends BaseController {

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

        return getPageOrFragment(request, HOME_PAGE);
    }

}
