package org.royllo.explorer.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Homepage controller.
 */
@Controller
public class HomeController {

    /**
     * Home page.
     *
     * @param model model
     * @return page to display
     */
    @GetMapping("/")
    public String home(final Model model) {
        return "home";
    }

}
