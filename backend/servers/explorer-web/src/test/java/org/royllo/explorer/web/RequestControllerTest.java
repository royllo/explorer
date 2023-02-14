package org.royllo.explorer.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Request controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Calling '/request'")
    void requestPageWithoutParameterAndSlash() throws Exception {
        mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("request.view.error.noRequestId"))));
    }

    @Test
    @DisplayName("Calling '/request/'")
    void requestPageWithoutParameter() throws Exception {
        mockMvc.perform(get("/request/"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("request.view.error.noRequestId"))));
    }

}
