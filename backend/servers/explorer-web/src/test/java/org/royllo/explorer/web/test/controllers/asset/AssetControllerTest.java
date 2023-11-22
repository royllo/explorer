package org.royllo.explorer.web.test.controllers.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

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

    // TODO review this test

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    ProofRepository proofRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Autowired
    MessageSource messageSource;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page without parameter")
    void assetPageWithoutParameter(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messageSource, "asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(getMessage(messageSource, "asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page without parameter and slash")
    void assetPageWithoutParameterAndSlash(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @Test
    @DisplayName("Download proof")
    void downloadProof() throws Exception {
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proof_file/" + ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getProofId()))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Invalid asset id")
    void invalidAssetId(final HttpHeaders headers) throws Exception {
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("asset.view.error.assetNotFound")).replace("\"{0}\"", "&quot;NON_EXISTING_ASSET_ID&quot;");
        mockMvc.perform(get("/asset/NON_EXISTING_ASSET_ID").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

}
