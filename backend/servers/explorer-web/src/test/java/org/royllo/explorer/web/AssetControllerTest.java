package org.royllo.explorer.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
public class AssetControllerTest {

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
        mockMvc.perform(get("/asset/b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking each field.
                .andExpect(content().string(containsString(">starbackrcoin<")))
                .andExpect(content().string(containsString(">b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e<")))
                .andExpect(content().string(containsString(">anonymous<")))
                .andExpect(content().string(containsString(">737461726261636b72206d6f6e6579<")))
                .andExpect(content().string(containsString(">2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea:0<")))
                .andExpect(content().string(containsString(">1<")))
                .andExpect(content().string(containsString(">410415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004ac<")))
                .andExpect(content().string(containsString(">OP_PUSHBYTES_65 0415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004 OP_CHECKSIG<")))
                .andExpect(content().string(containsString(">p2pk<")))
                .andExpect(content().string(containsString(">19oAxAJhAKmEFb2pcA9qiX8sV8hxKeXjzd<")));
    }

}
