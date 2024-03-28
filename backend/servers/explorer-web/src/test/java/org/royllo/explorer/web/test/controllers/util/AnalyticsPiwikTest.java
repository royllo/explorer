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
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@DisplayName("Analytics - Piwik tests")
@AutoConfigureMockMvc
@TestPropertySource(properties = {"royllo.explorer.analytics.piwik.trackingId=00000000-0000-0000-0000-000000000001"})
public class AnalyticsPiwikTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Piwik configured")
    void piwikConfigured() throws Exception {
        // We test on each layout (home, asset).
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("(window, document, 'dataLayer', '00000000-0000-0000-0000-000000000001');")));

        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("(window, document, 'dataLayer', '00000000-0000-0000-0000-000000000001');")));
    }

}
