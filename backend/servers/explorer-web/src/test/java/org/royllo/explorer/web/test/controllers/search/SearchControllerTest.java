package org.royllo.explorer.web.test.controllers.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.math.BigInteger.ONE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.SearchPageConstants.SEARCH_PAGE;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Search controller tests")
@AutoConfigureMockMvc
public class SearchControllerTest extends BaseWebTest {

    @Autowired
    BitcoinService bitcoinService;

    @Autowired
    AssetGroupService assetGroupService;

    @Autowired
    AssetService assetService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page without query parameter")
    void searchPageWithoutQueryParameter(final HttpHeaders headers) throws Exception {

        // Attribute not set.
        mockMvc.perform(get("/search").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(getMessage(messages, "search.error.noQuery"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

        // Attribute set to "".
        mockMvc.perform(get("/search").headers(headers).queryParam(QUERY_ATTRIBUTE, ""))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(getMessage(messages, "search.error.noQuery"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

        // Attribute set to " ".
        mockMvc.perform(get("/search").headers(headers).queryParam(QUERY_ATTRIBUTE, " "))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(getMessage(messages, "search.error.noQuery"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with no result")
    void searchPageWithNoResult(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/search").headers(headers).param(QUERY_ATTRIBUTE, "NO_RESULT_QUERY"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(containsString(getMessage(messages, "search.error.noResult"))));

    }

    @DirtiesContext
    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with results on several pages")
    void searchPageWithResultsOnSeveralPages(final HttpHeaders headers) throws Exception {
        // Create fake assets.
        createFakeAssets();

        // Results - Page 1 (page not specified).
        mockMvc.perform(get("/search").headers(headers).param(QUERY_ATTRIBUTE, "FAKE"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000002")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000003")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000004")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000005")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000006")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000007")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000008")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000009")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000010")))
                .andExpect(content().string(not(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000011"))))
                // Checking pages.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(containsString(">1/10</")))
                .andExpect(content().string(containsString("nextPage")))
                // Checking there is no error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

        // Results - Page 1 (page specified).
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, "FAKE")
                        .param(PAGE_ATTRIBUTE, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000002")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000003")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000004")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000005")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000006")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000007")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000008")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000009")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000010")))
                .andExpect(content().string(not(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000011"))))
                // Checking pages.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(containsString(">1/10</")))
                .andExpect(content().string(containsString("nextPage")))
                // Checking there is no error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

        // Results - Page 2 - Added spaces to test trim().
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, " FAKE ")
                        .param(PAGE_ATTRIBUTE, "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(not(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000010"))))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000011")))
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000020")))
                .andExpect(content().string(not(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000021"))))
                // Checking pages.
                .andExpect(content().string(containsString("previousPage")))
                .andExpect(content().string(containsString(">2/10</")))
                .andExpect(content().string(containsString("nextPage")))
                // Checking there is no error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

        // Results - Page 10 - Last page
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, " FAKE ")
                        .param(PAGE_ATTRIBUTE, "10"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000099")))
                // Checking pages.
                .andExpect(content().string(containsString("previousPage")))
                .andExpect(content().string(containsString(">10/10</")))
                .andExpect(content().string(not(containsString("nextPage"))))
                // Checking there is no error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with invalid page number")
    void searchPageWithInvalidPageNumber(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/search")
                        .headers(headers)
                        .param(QUERY_ATTRIBUTE, "coin")
                        .param(PAGE_ATTRIBUTE, "4"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messages, "search.error.invalidPage"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

    }


    @DirtiesContext
    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with images on results")
    void searchPageWithImageInResults(final HttpHeaders headers) throws Exception {

        // We check in the result.
        mockMvc.perform(get("/search")
                        .headers(headers)
                        .param(QUERY_ATTRIBUTE, "roylloNFT")
                        .param(PAGE_ATTRIBUTE, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Check image link
                .andExpect(content().string(containsString("/89c9d3ff7cb9dbc4615f854f7127e94db10edd430f8bcf82d7309d0c8b750051.png")))
                // Checking error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noQuery")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "search.error.noResult")))));

    }

    /**
     * Create fake assets.
     */
    private void createFakeAssets() {
        // Transaction used.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 4);
        assertTrue(bto.isPresent());

        // Create a fake asset group.
        final AssetGroupDTO assetGroupDTO = assetGroupService.getAssetGroupByAssetGroupId("FAKE_ASSET_GROUP_ID")
                .orElseGet(() -> assetGroupService.addAssetGroup(
                        AssetGroupDTO.builder()
                                .assetGroupId("FAKE_ASSET_GROUP_ID")
                                .tweakedGroupKey("FAKE_TWEAKED_GROUP_KEY")
                                .creator(ANONYMOUS_USER_DTO)
                                .build()
                ));

        // Create fake assets.
        for (int i = 1; i <= 99; i++) {
            final Optional<AssetDTO> assetFound = assetService.getAssetByAssetIdOrAlias("FAKE_ASSET_ID_000000000000000000000000000000000000000000000000" + String.format("%02d", i));
            if (assetFound.isEmpty()) {
                assetService.addAsset(
                        AssetDTO.builder()
                                .creator(ANONYMOUS_USER_DTO)
                                .assetGroup(assetGroupDTO)
                                .assetId("FAKE_ASSET_ID_000000000000000000000000000000000000000000000000" + String.format("%02d", i))
                                .genesisPoint(bto.get())
                                .metaDataHash("metadata")
                                .name("FAKE_ASSET_NAME_000000000000000000000000000000000000000000000000" + String.format("%02d", i))
                                .outputIndex(0)
                                .version(0)
                                .type(NORMAL)
                                .amount(ONE)
                                .build()
                );
            }
        }
    }

}
