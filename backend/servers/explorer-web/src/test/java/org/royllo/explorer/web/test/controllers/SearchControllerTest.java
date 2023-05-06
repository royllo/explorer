package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
public class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Search page without query parameter")
    void searchPageWithoutQueryParameter() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))));
    }

    @Test
    @DisplayName("Search page with empty parameter")
    void searchPageWithEmptyParameter() throws Exception {
        mockMvc.perform(get("/search").queryParam(QUERY_ATTRIBUTE, ""))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))));

        // With space!
        mockMvc.perform(get("/search").queryParam(QUERY_ATTRIBUTE, " "))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.noQuery"))));
    }

    @Test
    @DisplayName("Search page with no result")
    void searchPageWithNoResult() throws Exception {
        // We remove the parameter from the message.
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("search.noResult")).replace("\"{0}\"", "");
        mockMvc.perform(get("/search").param(QUERY_ATTRIBUTE, "NO_RESULT_QUERY"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Search page with results & blank spaces")
    void searchPageWithResults() throws Exception {
        // Results - Page 1 (page not specified).
        mockMvc.perform(get("/search").param(QUERY_ATTRIBUTE, "coin"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(containsString(">Coin 001<")))
                .andExpect(content().string(containsString(">Coin 002<")))
                .andExpect(content().string(containsString(">Coin 003<")))
                .andExpect(content().string(containsString(">Coin 004<")))
                .andExpect(content().string(containsString(">Coin 005<")))
                .andExpect(content().string(containsString(">Coin 006<")))
                .andExpect(content().string(containsString(">Coin 007<")))
                .andExpect(content().string(containsString(">Coin 008<")))
                .andExpect(content().string(containsString(">Coin 009<")))
                .andExpect(content().string(containsString(">Coin 010<")))
                .andExpect(content().string(not(containsString(">Coin 011<"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 1 (page specified).
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, "coin")
                        .param(PAGE_ATTRIBUTE, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(containsString(">Coin 001<")))
                .andExpect(content().string(containsString(">Coin 002<")))
                .andExpect(content().string(containsString(">Coin 003<")))
                .andExpect(content().string(containsString(">Coin 004<")))
                .andExpect(content().string(containsString(">Coin 005<")))
                .andExpect(content().string(containsString(">Coin 006<")))
                .andExpect(content().string(containsString(">Coin 007<")))
                .andExpect(content().string(containsString(">Coin 008<")))
                .andExpect(content().string(containsString(">Coin 009<")))
                .andExpect(content().string(containsString(">Coin 010<")))
                .andExpect(content().string(not(containsString(">Coin 011<"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 2 - Added spaces to test trim().
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, " coin ")
                        .param(PAGE_ATTRIBUTE, "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(not(containsString(">Coin 010<"))))
                .andExpect(content().string(containsString(">Coin 011<")))
                .andExpect(content().string(containsString(">Coin 012<")))
                .andExpect(content().string(containsString(">Coin 013<")))
                .andExpect(content().string(containsString(">Coin 014<")))
                .andExpect(content().string(containsString(">Coin 015<")))
                .andExpect(content().string(containsString(">Coin 016<")))
                .andExpect(content().string(containsString(">Coin 017<")))
                .andExpect(content().string(containsString(">Coin 018<")))
                .andExpect(content().string(containsString(">Coin 019<")))
                .andExpect(content().string(containsString(">Coin 020<")))
                .andExpect(content().string(not(containsString(">Coin 021<"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 3.
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, "coin")
                        .param(PAGE_ATTRIBUTE, "3"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(not(containsString(">Coin 020<"))))
                .andExpect(content().string(containsString(">Coin 021<")))
                .andExpect(content().string(containsString(">Coin 022<")))
                .andExpect(content().string(not(containsString(">Coin 023<"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));
    }

    @Test
    @DisplayName("Search page with invalid page number")
    void searchPageWithInvalidPageNumber() throws Exception {
        mockMvc.perform(get("/search")
                        .param(QUERY_ATTRIBUTE, "coin")
                        .param(PAGE_ATTRIBUTE, "4"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.invalidPage"))));
    }

}
