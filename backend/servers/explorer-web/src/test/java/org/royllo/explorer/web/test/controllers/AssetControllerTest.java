package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofFileRepository;
import org.royllo.explorer.web.test.util.BaseTest;
import org.royllo.test.TapdData;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_PROOF_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_RAW_PROOF;
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
    AssetRepository assetRepository;

    @Autowired
    ProofFileRepository proofFileRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page")
    void assetPage(final HttpHeaders headers) throws Exception {
        // Retrieving asset data from test values.
        final DecodedProofValueResponse.DecodedProof assetFromTestData = TapdData.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID).getDecodedProof(0);

        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID).headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking header.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getAssetId() + "<")))
                // Asset.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getAssetId() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getGenesisPoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getMetaDataHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getOutputIndex() + "<")))
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAmount() + "<")))
                // Asset group.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGroup().getAssetIdSig() + "<")))
                // TODO Find a worling example
                //.andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGroup().getTweakedGroupKey() + "<")))
                // Asset states.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getScriptKey() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getInternalKey() + "<")))
                // TODO Find a working example
                // .andExpect(content().string(containsString(">" + ROYLLO_COIN_MERKLE_ROOT + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getTapscriptSibling() + "<")))
                // Owner.
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_ID + "<")))
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_USERNAME + "<")))
                // Proof files.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_PROOF + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));

        // Trim test with spaces
        mockMvc.perform(get("/asset/ " + ROYLLO_COIN_ASSET_ID + " ").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking header.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getAssetId() + "<")))
                // Asset.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getAssetId() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getGenesisPoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getMetaDataHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGenesis().getOutputIndex() + "<")))
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAmount() + "<")))
                // Asset group.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGroup().getAssetIdSig() + "<")))
                // TODO Find a worling example
                //.andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getAssetGroup().getTweakedGroupKey() + "<")))
                // Asset states.
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getScriptKey() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getInternalKey() + "<")))
                // TODO Find a working example
                // .andExpect(content().string(containsString(">" + ROYLLO_COIN_MERKLE_ROOT + "<")))
                .andExpect(content().string(containsString(">" + assetFromTestData.getAsset().getChainAnchor().getTapscriptSibling() + "<")))
                // Owner.
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_ID + "<")))
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_USERNAME + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page without parameter")
    void assetPageWithoutParameter(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
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

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page with an empty group asset")
    void assetPageWithEmptyGroupAsset(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/NO_GROUP_ASSET_ASSET_ID").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Should display asset but not the asset group tab.
                .andExpect(content().string(containsString(">NoGroupAsset<")))
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.tabs.assetGroup")))));
    }

    @Test
    @DisplayName("Download proof file")
    void downloadProofFile() throws Exception {
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proof_file/" + ROYLLO_COIN_PROOF_ID))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Invalid asset id")
    void invalidAssetId(final HttpHeaders headers) throws Exception {
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("asset.view.error.assetNotFound")).replace("\"{0}\"", "&quot;NON_EXISTING&quot;");
        mockMvc.perform(get("/asset/NON_EXISTING").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

}
