package org.royllo.explorer.web.test.controllers.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static java.math.BigInteger.ONE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Search controller tests")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@PropertySource("classpath:messages.properties")
public class SearchControllerTest extends BaseTest {

    @Autowired
    BitcoinService bitcoinService;

    @Autowired
    AssetService assetService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page without query parameter")
    void searchPageWithoutQueryParameter(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/search").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with empty parameter")
    void searchPageWithEmptyParameter(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/search").headers(headers).queryParam(QUERY_ATTRIBUTE, ""))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // With space.
        mockMvc.perform(get("/search").queryParam(QUERY_ATTRIBUTE, " "))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with no result")
    void searchPageWithNoResult(final HttpHeaders headers) throws Exception {
        // We remove the parameter from the message.
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("search.noResult")).replace("\"{0}\"", "");
        mockMvc.perform(get("/search").headers(headers).param(QUERY_ATTRIBUTE, "NO_RESULT_QUERY"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Error message.
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.noQuery")))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Search page with results & blank spaces")
    void searchPageWithResults(final HttpHeaders headers) throws Exception {
        // Create fake assets.
        createFakeAssets(11);

        // Results - Page 1 (page not specified).
        mockMvc.perform(get("/search").headers(headers).param(QUERY_ATTRIBUTE, "FAKE"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_01")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_02")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_03")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_04")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_05")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_06")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_07")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_08")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_09")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_10")))
                .andExpect(content().string(not(containsString("/FAKE_ASSET_ID_11"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(not(containsString(">3</a>"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.noQuery")))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;coin&quot;")))));

        // Results - Page 1 (page specified).
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, "FAKE")
                        .param(PAGE_ATTRIBUTE, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_01")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_02")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_03")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_04")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_05")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_06")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_07")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_08")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_09")))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_10")))
                .andExpect(content().string(not(containsString("/FAKE_ASSET_ID_11"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(not(containsString(">3</a>"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.noQuery")))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;coin&quot;")))));

        // Results - Page 2 - Added spaces to test trim().
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, " FAKE ")
                        .param(PAGE_ATTRIBUTE, "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(not(containsString("/FAKE_ASSET_ID_10"))))
                .andExpect(content().string(containsString("/FAKE_ASSET_ID_11")))
                .andExpect(content().string(not(containsString("/FAKE_ASSET_ID_12"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(not(containsString(">3</a>"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.noQuery")))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;coin&quot;")))));
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
                .andExpect(content().string(containsString(environment.getProperty("search.error.invalidPage"))))
                .andExpect(content().string(not(containsString(environment.getProperty("search.error.noQuery")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("search.noResult"))
                        .replace("\"{0}\"", "&quot;coin&quot;")))));
    }

    /**
     * Create fake assets starting with FAKE_ASSET_%1
     *
     * @param numberOfAssets number of assets to generate
     */
    private void createFakeAssets(final int numberOfAssets) {
        // Transaction used.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());

        // Create fake assets.
        for (int i = 1; i <= numberOfAssets; i++) {
            final Optional<AssetDTO> assetFound = assetService.getAssetByAssetId("FAKE_ASSET_ID_" + String.format("%02d", i));
            if (assetFound.isEmpty()) {
                assetService.addAsset(
                        AssetDTO.builder()
                                .creator(ANONYMOUS_USER_DTO)
                                .assetId("FAKE_ASSET_ID_" + String.format("%02d", i))
                                .genesisPoint(bto.get())
                                .metaDataHash("metadata")
                                .name("FAKE_ASSET_NAME_" + String.format("%02d", i))
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
