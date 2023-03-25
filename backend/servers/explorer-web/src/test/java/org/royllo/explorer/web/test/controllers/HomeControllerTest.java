package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.HOME_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Home controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class HomeControllerTest {

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
                .andExpect(content().string(containsString(environment.getProperty("request.proof.add"))))
                // Checking the search form is here.
                .andExpect(content().string(containsString("input type=\"search\"")))
                // Checking the footer is here.
                .andExpect(content().string(containsString("License")));

        // Images on the home page.
        mockMvc.perform(get("/images/logo/royllo_logo_homepage.png"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/images/logo/royllo_logo_horizontal_with_title.png"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Display home page with query parameter")
    void homePagWithQueryParameter() throws Exception {
        // Web page.
        mockMvc.perform(get("/").queryParam(QUERY_ATTRIBUTE, "MyQuery"))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE))
                // Checking the search form is already filled.
                .andExpect(content().string(containsString("value=\"MyQuery\"")));
    }

}
