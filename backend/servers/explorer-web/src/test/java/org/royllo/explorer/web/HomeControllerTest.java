package org.royllo.explorer.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.PagesConstants.HOME_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Home controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Display home page")
    void homePage() throws Exception {
        // Web page.
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE))
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


}
