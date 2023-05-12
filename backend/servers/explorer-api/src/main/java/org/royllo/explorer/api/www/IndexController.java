package org.royllo.explorer.api.www;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Index controller - redirect to graphiql.
 */
@Controller
public class IndexController {

    /**
     * Redirect homepage.
     *
     * @return graphiql page
     */
    @SuppressWarnings("SameReturnValue")
    @RequestMapping(value = "/", method = GET)
    public String redirect() {
        return "redirect:/graphiql";
    }

}
