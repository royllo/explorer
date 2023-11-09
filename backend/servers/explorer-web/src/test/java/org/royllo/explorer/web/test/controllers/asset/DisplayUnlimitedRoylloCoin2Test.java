package org.royllo.explorer.web.test.controllers.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.web.test.util.BaseTest;
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
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_FROM_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Display unlimitedRoylloCoin2 page tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class DisplayUnlimitedRoylloCoin2Test extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Check unlimitedRoylloCoin2 asset page")
    void assetPage(final HttpHeaders headers) throws Exception {
        final DecodedProofValueResponse.DecodedProof assetFromTest = UNLIMITED_ROYLLO_COIN_2_FROM_TEST.getDecodedProofResponse(0);

        mockMvc.perform(get("/asset/" + UNLIMITED_ROYLLO_COIN_2_ASSET_ID).headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking tab header.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getAssetId() + "<")))
                // Asset definition.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getAssetId() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getGenesisPoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getName() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getMetaDataHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getOutputIndex() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGenesis().getVersion() + "<")))
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAmount() + "<")))
                // Asset group.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getAssetGroup().getTweakedGroupKey() + "<")))
                // One asset states.
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorTx() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorBlockHash() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getAnchorOutpoint() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getInternalKey() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getMerkleRoot() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getChainAnchor().getTapscriptSibling() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getScriptVersion() + "<")))
                .andExpect(content().string(containsString(">" + assetFromTest.getAsset().getScriptKey() + "<")))
                // Owner.
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_ID + "<")))
                .andExpect(content().string(containsString(">" + ANONYMOUS_USER_USERNAME + "<")))
                // Proof files.
                .andExpect(content().string(containsString(">" + UNLIMITED_ROYLLO_COIN_2_FROM_TEST.getDecodedProofRequest(0).getRawProof() + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + UNLIMITED_ROYLLO_COIN_2_ASSET_ID + "&quot;")))));
    }

}
