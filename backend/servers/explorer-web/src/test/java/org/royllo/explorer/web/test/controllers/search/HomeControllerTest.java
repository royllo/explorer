package org.royllo.explorer.web.test.controllers.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.explorer.web.util.constants.HomePagesConstants.HOME_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.QUERY_ATTRIBUTE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Home controller tests")
@AutoConfigureMockMvc
public class HomeControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @Autowired
    UniverseServerRepository universeServerRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetStateRepository assetStateRepository;

    @Test
    @DisplayName("Display home page")
    void homePage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE))
                // Checking the button in the header.
                .andExpect(content().string(containsString(getMessage(messages, "request.view.addData"))))
                .andExpect(content().string(containsString("\"/request/proof/add\"")))
                .andExpect(content().string(containsString("\"/request/universe_server/add\"")))
                // Checking the search form is here and empty.
                .andExpect(content().string(containsString("form")))
                .andExpect(content().string(containsString("input")))
                .andExpect(content().string(containsString("query")))
                .andExpect(content().string(containsString("value=\"\"")))
                // Checking the message on the homepage.
                .andExpect(content().string(containsString(getMessage(messages, "home.message"))))
                // Checking the statistics are here.
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.universeCount"))))
                .andExpect(content().string(containsString(">" + universeServerRepository.count() + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.assetCount"))))
                .andExpect(content().string(containsString(">" + assetRepository.count() + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.assetStateCount"))))
                .andExpect(content().string(containsString(">" + assetStateRepository.count() + "<")))
                // Checking the footer is here.
                .andExpect(content().string(containsString("https://www.royllo.org")))
                .andExpect(content().string(containsString("http://localhost:9090/api")))
                .andExpect(content().string(containsString("https://github.com/royllo/explorer")))
                .andExpect(content().string(containsString("https://twitter.com/royllo_org")));

    }

    @Test
    @DisplayName("Display home page fragment")
    void homePageFragment() throws Exception {

        mockMvc.perform(get("/").headers(getHeaders()))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(HOME_PAGE)))
                // Checking the button is NOT RETURNED.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.addData")))))
                .andExpect(content().string(not(containsString("\"/request/proof/add\""))))
                .andExpect(content().string(not(containsString("\"/request/universe_server/add\""))))
                // Checking the search form is here and empty.
                .andExpect(content().string(containsString("form")))
                .andExpect(content().string(containsString("input")))
                .andExpect(content().string(containsString("query")))
                .andExpect(content().string(containsString("value=\"\"")))
                // Checking the message on the homepage.
                .andExpect(content().string(containsString(getMessage(messages, "home.message"))))
                // Checking the statistics are here.
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.universeCount"))))
                .andExpect(content().string(containsString(">" + universeServerRepository.count() + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.assetCount"))))
                .andExpect(content().string(containsString(">" + assetRepository.count() + "<")))
                .andExpect(content().string(containsString(getMessage(messages, "statistics.global.assetStateCount"))))
                .andExpect(content().string(containsString(">" + assetStateRepository.count() + "<")))
                // Checking the footer is NOT RETURNED.
                .andExpect(content().string(not(containsString("https://www.royllo.org"))))
                .andExpect(content().string(not(containsString("http://localhost:9090/api"))))
                .andExpect(content().string(not(containsString("https://github.com/royllo/explorer"))))
                .andExpect(content().string(not(containsString("https://twitter.com/royllo_org"))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Display home page with query parameter")
    void homePageWithQueryParameter(final HttpHeaders headers) throws Exception {

        mockMvc.perform(get("/").headers(headers).queryParam(QUERY_ATTRIBUTE, "MyQuery"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(HOME_PAGE)))
                // Checking the search form is filled with the parameter.
                .andExpect(content().string(containsString("value=\"MyQuery\"")));

    }

    @Test
    @DisplayName("Images available")
    void images() throws Exception {

        mockMvc.perform(get("/images/logo/royllo_logo_homepage.png")).andExpect(status().isOk());
        mockMvc.perform(get("/images/logo/royllo_logo_horizontal_with_title.png")).andExpect(status().isOk());
        mockMvc.perform(get("/svg/footer/api.svg")).andExpect(status().isOk());
        mockMvc.perform(get("/svg/footer/github.svg")).andExpect(status().isOk());
        mockMvc.perform(get("/svg/footer/twitter.svg")).andExpect(status().isOk());
        mockMvc.perform(get("/svg/type_collectible_asset.svg")).andExpect(status().isOk());
        mockMvc.perform(get("/svg/type_normal_asset.svg")).andExpect(status().isOk());

    }

}
