package org.royllo.explorer.web.test.controllers.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GENESIS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GROUP_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_OWNER_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PROOFS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_README_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_STATES_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_URL_ATTRIBUTE;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Display trickyRoylloCoin page tests")
@AutoConfigureMockMvc
public class DisplayTrickyRoylloCoinTest extends BaseWebTest {

    // Asset tested.
    String assetId = TRICKY_ROYLLO_COIN_ASSET_ID;
    String assetIdAlias = TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS;
    DecodedProofValueResponse.DecodedProof assetFromTest = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check default page")
    void assetDefaultPage(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId).headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GENESIS_PAGE)))
                .andExpect(model().attribute(ASSET_URL_ATTRIBUTE, containsString("http://localhost:8080/asset/" + assetIdAlias)))
                // Checking tab header.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getAssetId() + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check genesis page")
    void assetPageGenesis(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/genesis").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GENESIS_PAGE)))
                // Checking tab header.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getAssetId() + "<")))
                // Checking genesis tab data.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getAssetId() + "<")))
                .andExpect(content().string(containsString(">" + assetIdAlias + "<")))
                .andExpect(content().string(containsString(">" + getMessage(messages, "asset.data.assetType.normal") + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAmount() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getMetaDataHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getVersion() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getGenesisPoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getOutputIndex() + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check asset readme")
    void assetReadme(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/readme").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_README_PAGE)))
                // Checking readme page.
                .andExpect(content().string(containsString(getMessage(messages, "asset.data.readme"))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.data.readme.explanation"))))
                .andExpect(content().string(containsString("This is a tricky coin!")))
                // No error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check group page")
    void assetPageGroup(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/group").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GROUP_PAGE)))
                // Checking group tab data.
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.data.tweakedGroupKey")))))
                // There should be no pagination.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(not(containsString("currentPage"))))
                .andExpect(content().string(not(containsString("nextPage"))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check states page")
    void assetPageStates(final HttpHeaders headers) throws Exception {

        final DecodedProofValueResponse.DecodedProof.Asset TRICKY_ROYLLO_COIN_1_ASSET_STATE = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset();
        final DecodedProofValueResponse.DecodedProof.Asset TRICKY_ROYLLO_COIN_2_ASSET_STATE = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(1).getAsset();
        final DecodedProofValueResponse.DecodedProof.Asset TRICKY_ROYLLO_COIN_3_ASSET_STATE = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(3).getAsset();
        final DecodedProofValueResponse.DecodedProof.Asset TRICKY_ROYLLO_COIN_4_ASSET_STATE = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(4).getAsset();

        mockMvc.perform(get("/asset/" + assetId + "/states").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_STATES_PAGE)))
                // Checking states tab data (Four asset states).
                // First asset state.
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getChainAnchor().getTapscriptSibling() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_1_ASSET_STATE.getScriptKey() + "<")))
                // Second asset state.
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getChainAnchor().getTapscriptSibling() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_2_ASSET_STATE.getScriptKey() + "<")))
                // Third asset state.
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getChainAnchor().getTapscriptSibling() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_3_ASSET_STATE.getScriptKey() + "<")))
                // Fourth asset state.
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getChainAnchor().getTapscriptSibling() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_4_ASSET_STATE.getScriptKey() + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check owner page")
    void assetPageOwner(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/owner").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_OWNER_PAGE)))
                // Checking owner tab data.
                .andExpect(content().string(containsString(">" + STRAUMAT_USER_USER_ID + "<")))
                .andExpect(content().string(containsString(">" + STRAUMAT_USER_USERNAME + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "user.data.fullName"))))
                .andExpect(content().string(containsString(">" + STRAUMAT_USER_FULL_NAME + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "user.data.biography"))))
                .andExpect(content().string(containsString(">" + STRAUMAT_USER_BIOGRAPHY + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "user.data.website"))))
                .andExpect(content().string(containsString(">" + STRAUMAT_USER_WEBSITE + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check proofs page")
    void assetPageProofs(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/proofs").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs tab data.
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getProofId() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(1).getProofId() + "<")))
                .andExpect(content().string(containsString(">" + TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(3).getProofId() + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

}
