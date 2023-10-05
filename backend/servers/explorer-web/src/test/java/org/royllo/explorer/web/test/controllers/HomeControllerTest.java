package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.explorer.web.util.base.BaseController.HTMX_REQUEST;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.HOME_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.HOME_PAGE_FRAGMENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Home controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class HomeControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Display home page")
    void homePage() throws Exception {
        // Web page.
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE))
                // Checking the button in the header is here.
                .andExpect(content().string(containsString(environment.getProperty("request.addData"))))
                // Checking the search form is here.
                .andExpect(content().string(containsString("form")))
                .andExpect(content().string(containsString("input")))
                .andExpect(content().string(containsString("query")))
                // Checking the footer is here.
                .andExpect(content().string(containsString("https://www.royllo.org")))
                .andExpect(content().string(containsString("https://github.com/royllo/explorer")))
                .andExpect(content().string(containsString("https://twitter.com/royllo_org")))
                .andExpect(content().string(containsString("mailto:contact@royllo.org")));

        // Images on the home page.
        mockMvc.perform(get("/images/logo/royllo_logo_homepage.png"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/images/logo/royllo_logo_horizontal_with_title.png"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Display partial home page")
    void partialHomePage() throws Exception {
        final HttpHeaders htmxHeaders = new HttpHeaders();
        htmxHeaders.add(HTMX_REQUEST, "true");

        // Web page.
        mockMvc.perform(get("/").headers(htmxHeaders))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE_FRAGMENT))
                // Checking the button in the header is here.
//                .andExpect(content().string(not(containsString(environment.getProperty("request.proof.add")))))
                // Checking the search form is here.
                .andExpect(content().string(containsString("form")))
                .andExpect(content().string(containsString("input")))
                .andExpect(content().string(containsString("query")))
                // Checking the footer is not here.
                .andExpect(content().string(not(containsString("https://www.royllo.org"))))
                .andExpect(content().string(not(containsString("https://github.com/royllo/explorer"))))
                .andExpect(content().string(not(containsString("https://twitter.com/royllo_org"))))
                .andExpect(content().string(not(containsString("mailto:contact@royllo.org"))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Display home page with query parameter")
    void homePageWithQueryParameter(final HttpHeaders headers) throws Exception {
        // Web page.
        mockMvc.perform(get("/").headers(headers).queryParam(QUERY_ATTRIBUTE, "MyQuery"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(HOME_PAGE)))
                // Checking the search form is already filled.
                .andExpect(content().string(containsString("value=\"MyQuery\"")));
    }

}
