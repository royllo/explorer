package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.web.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Asset controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class AssetControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Calling '/asset'")
    void assetPageWithoutParameterAndSlash() throws Exception {
        mockMvc.perform(get("/asset"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))));
    }

    @Test
    @DisplayName("Calling '/asset/'")
    void assetPageWithoutParameter() throws Exception {
        mockMvc.perform(get("/asset/"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))));
    }

    @Test
    @DisplayName("Invalid asset id")
    void invalidAssetId() throws Exception {
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("asset.view.error.assetNotFound")).replace("\"{0}\"", "&quot;NON_EXISTING&quot;");
        mockMvc.perform(get("/asset/NON_EXISTING"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Asset display")
    void assetDisplay() throws Exception {
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking each field.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                // Asset genesis.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + MY_ROYLLO_COIN_GENESIS_POINT_VOUT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_META + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_OUTPUT_INDEX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION + "<")))
                // Other data.
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_AMOUNT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_SCRIPT_KEY + "<")))
                // Anchor.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_OUTPOINT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHRO_INTERNAL_KEY + "<")));
    }

}
