package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.web.test.util.BaseTest;
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
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Search controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class SearchControllerTest extends BaseTest {

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
        // Results - Page 1 (page not specified).
        mockMvc.perform(get("/search").headers(headers).param(QUERY_ATTRIBUTE, "coin"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString(">TestPaginationCoin01<")))
                .andExpect(content().string(containsString(">TestPaginationCoin02<")))
                .andExpect(content().string(containsString(">TestPaginationCoin03<")))
                .andExpect(content().string(containsString(">TestPaginationCoin04<")))
                .andExpect(content().string(containsString(">TestPaginationCoin05<")))
                .andExpect(content().string(containsString(">TestPaginationCoin06<")))
                .andExpect(content().string(containsString(">TestPaginationCoin07<")))
                .andExpect(content().string(containsString(">TestPaginationCoin08<")))
                .andExpect(content().string(containsString(">TestPaginationCoin09<")))
                .andExpect(content().string(containsString(">TestPaginationCoin10<")))
                .andExpect(content().string(not(containsString(">TestPaginationCoin11<"))))
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
                        .param(QUERY_ATTRIBUTE, "coin")
                        .param(PAGE_ATTRIBUTE, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(containsString(">TestPaginationCoin01<")))
                .andExpect(content().string(containsString(">TestPaginationCoin02<")))
                .andExpect(content().string(containsString(">TestPaginationCoin03<")))
                .andExpect(content().string(containsString(">TestPaginationCoin04<")))
                .andExpect(content().string(containsString(">TestPaginationCoin05<")))
                .andExpect(content().string(containsString(">TestPaginationCoin06<")))
                .andExpect(content().string(containsString(">TestPaginationCoin07<")))
                .andExpect(content().string(containsString(">TestPaginationCoin08<")))
                .andExpect(content().string(containsString(">TestPaginationCoin09<")))
                .andExpect(content().string(containsString(">TestPaginationCoin10<")))
                .andExpect(content().string(not(containsString(">TestPaginationCoin11<"))))
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
                        .param(QUERY_ATTRIBUTE, " coin ")
                        .param(PAGE_ATTRIBUTE, "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(SEARCH_PAGE)))
                // Checking results.
                .andExpect(content().string(not(containsString(">TestPaginationCoin10<"))))
                .andExpect(content().string(containsString(">TestPaginationCoin11<")))
                .andExpect(content().string(not(containsString(">TestPaginationCoin12<"))))
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

}
