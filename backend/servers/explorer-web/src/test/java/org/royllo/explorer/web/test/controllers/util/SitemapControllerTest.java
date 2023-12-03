package org.royllo.explorer.web.test.controllers.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Sitemap controller tests")
@AutoConfigureMockMvc
public class SitemapControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Sitemap")
    void sitemap() throws Exception {

        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + ROYLLO_NFT_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + SET_OF_ROYLLO_NFT_1_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + SET_OF_ROYLLO_NFT_2_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + SET_OF_ROYLLO_NFT_3_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + TRICKY_ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + UNLIMITED_ROYLLO_COIN_1_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">http://localhost:8080/asset/" + UNLIMITED_ROYLLO_COIN_2_ASSET_ID + "<")));

    }

}
