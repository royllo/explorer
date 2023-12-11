package org.royllo.explorer.web.test.controllers.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.web.test.util.BaseTest;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GENESIS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GROUP_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_OWNER_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PROOFS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_STATES_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_URL_ATTRIBUTE;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_FROM_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Display setRoylloNFT1 page tests")
@AutoConfigureMockMvc
public class DisplaySetRoylloNFT1Test extends BaseTest {

    // Asset tested.
    String assetId = SET_OF_ROYLLO_NFT_1_ASSET_ID;
    String assetIdAlias = SET_OF_ROYLLO_NFT_1_ASSET_ID_ALIAS;
    DecodedProofValueResponse.DecodedProof assetFromTest = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0);

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
                .andExpect(content().string(containsString(">" + getMessage(messages, "asset.data.assetType.collectible") + "<")))
                .andExpect(content().string(not(containsString(">" + assetFromTest.getAsset().getAmount() + "<"))))
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
    @DisplayName("Check group page")
    void assetPageGroup(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/asset/" + assetId + "/group").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GROUP_PAGE)))
                // Checking group tab data.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.data.tweakedGroupKey") + ": " + assetFromTest.getAsset().getAssetGroup().getTweakedGroupKey())))
                .andExpect(content().string(containsString("/asset/" + SET_OF_ROYLLO_NFT_1_ASSET_ID)))
                .andExpect(content().string(containsString("/asset/" + SET_OF_ROYLLO_NFT_2_ASSET_ID)))
                .andExpect(content().string(containsString("/asset/" + SET_OF_ROYLLO_NFT_3_ASSET_ID)))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
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

        mockMvc.perform(get("/asset/" + assetId + "/states").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_STATES_PAGE)))
                // Checking states tab data.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getScriptKey() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getTapscriptSibling() + "<")))
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
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_ID + "<")))
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_USERNAME + "<")))
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
                .andExpect(content().string(containsString(">" + SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofRequest(0).getProofId() + "<")))
                .andExpect(content().string(not(containsString(">" + SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofRequest(0).getProofId() + "<"))))
                .andExpect(content().string(not(containsString(">" + SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofRequest(0).getProofId() + "<"))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

}
