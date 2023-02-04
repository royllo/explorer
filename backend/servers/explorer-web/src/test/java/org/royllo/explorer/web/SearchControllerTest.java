package org.royllo.explorer.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.CoreMatchers.not;
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
        mockMvc.perform(get("/search").param("query", ""))
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
        mockMvc.perform(get("/search").param("query", "NO_RESULT_QUERY"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Search page with results")
    void searchPageWithResults() throws Exception {
        // Results - Page 1 (page not specified).
        mockMvc.perform(get("/search")
                        .param("query", "coin"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(containsString(">Coin 001</a>")))
                .andExpect(content().string(containsString(">Coin 002</a>")))
                .andExpect(content().string(containsString(">Coin 003</a>")))
                .andExpect(content().string(containsString(">Coin 004</a>")))
                .andExpect(content().string(containsString(">Coin 005</a>")))
                .andExpect(content().string(containsString(">Coin 006</a>")))
                .andExpect(content().string(containsString(">Coin 007</a>")))
                .andExpect(content().string(containsString(">Coin 008</a>")))
                .andExpect(content().string(containsString(">Coin 009</a>")))
                .andExpect(content().string(containsString(">Coin 010</a>")))
                .andExpect(content().string(not(containsString(">Coin 011</a>"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 1 (page specified).
        mockMvc.perform(get("/search")
                        .param("query", "coin")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(containsString(">Coin 001</a>")))
                .andExpect(content().string(containsString(">Coin 002</a>")))
                .andExpect(content().string(containsString(">Coin 003</a>")))
                .andExpect(content().string(containsString(">Coin 004</a>")))
                .andExpect(content().string(containsString(">Coin 005</a>")))
                .andExpect(content().string(containsString(">Coin 006</a>")))
                .andExpect(content().string(containsString(">Coin 007</a>")))
                .andExpect(content().string(containsString(">Coin 008</a>")))
                .andExpect(content().string(containsString(">Coin 009</a>")))
                .andExpect(content().string(containsString(">Coin 010</a>")))
                .andExpect(content().string(not(containsString(">Coin 011</a>"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 2.
        mockMvc.perform(get("/search")
                        .param("query", "coin")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(not(containsString(">Coin 010</a>"))))
                .andExpect(content().string(containsString(">Coin 011</a>")))
                .andExpect(content().string(containsString(">Coin 012</a>")))
                .andExpect(content().string(containsString(">Coin 013</a>")))
                .andExpect(content().string(containsString(">Coin 014</a>")))
                .andExpect(content().string(containsString(">Coin 015</a>")))
                .andExpect(content().string(containsString(">Coin 016</a>")))
                .andExpect(content().string(containsString(">Coin 017</a>")))
                .andExpect(content().string(containsString(">Coin 018</a>")))
                .andExpect(content().string(containsString(">Coin 019</a>")))
                .andExpect(content().string(containsString(">Coin 020</a>")))
                .andExpect(content().string(not(containsString(">Coin 021</a>"))))
                // Checking pages.
                .andExpect(content().string(not(containsString(">0</a>"))))
                .andExpect(content().string(containsString(">1</a>")))
                .andExpect(content().string(containsString(">2</a>")))
                .andExpect(content().string(containsString(">3</a>")))
                .andExpect(content().string(not(containsString(">4</a>"))));

        // Results - Page 3.
        mockMvc.perform(get("/search")
                        .param("query", "coin")
                        .param("page", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking results.
                .andExpect(content().string(not(containsString(">Coin 020</a>"))))
                .andExpect(content().string(containsString(">Coin 021</a>")))
                .andExpect(content().string(containsString(">Coin 022</a>")))
                .andExpect(content().string(containsString(">starbackrcoin</a>")))
                .andExpect(content().string(not(containsString(">Coin 023</a>"))))
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
                        .param("query", "coin")
                        .param("page", "4"))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("search.error.invalidPage"))));
    }

}
