package org.royllo.explorer.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.royllo.explorer.web.util.constants.PagesConstants.HOME_PAGE;

/**
 * Homepage controller.
 */
@Controller
public class HomeController {

    /**
     * Home page.
     *
     * @param model model
     * @param query query
     * @return page to display
     */
    @GetMapping("/")
    public String home(final Model model,
                       @RequestParam(required = false) final Optional<String> query) {
        query.ifPresent(queryValue -> model.addAttribute("query", queryValue));
        return HOME_PAGE;
    }

}
