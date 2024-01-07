package org.royllo.explorer.web.test.controllers.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@DisplayName("Analytics - no analytics tests")
@AutoConfigureMockMvc
@TestPropertySource(properties = {"royllo.explorer.analytics.piwik.trackingId="})
public class NoAnalyticsTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("No analytics configured")
    void noAnalyticsConfigured() throws Exception {

        // We test on each layout (home, asset).
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("(window, document, 'dataLayer'"))));

        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("(window, document, 'dataLayer'"))));

    }

}
