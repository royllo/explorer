package org.royllo.explorer.web.test.controllers.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PAGE;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Asset controller tests")
@AutoConfigureMockMvc
public class AssetControllerTest extends BaseTest {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    ProofRepository proofRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @Test
    @DisplayName("Asset page without parameter")
    void assetPageWithoutParameter() throws Exception {

        mockMvc.perform(get("/asset/"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @Test
    @DisplayName("Asset page without parameter and slash")
    void assetPageWithoutParameterAndSlash() throws Exception {

        mockMvc.perform(get("/asset"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @Test
    @DisplayName("Invalid asset id")
    void invalidAssetId() throws Exception {

        mockMvc.perform(get("/asset/NON_EXISTING_ASSET_ID"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.assetNotFound"))));

    }

    @Test
    @DisplayName("Download proof")
    void downloadProof() throws Exception {

        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proof/" + ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getProofId()))
                .andExpect(status().isOk());

    }

}
