package org.royllo.explorer.web.test.controllers.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSETS_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("User assets controller tests")
@AutoConfigureMockMvc
public class UserAssetsTest extends BaseTest {

    @Autowired
    MessageSource messages;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    AssetService assetService;

    @Test
    @DisplayName("User list with no asset")
    @WithMockUser(username = "userwithoutasset")
    void userWithoutAssets() throws Exception {

        mockMvc.perform(get("/account/assets"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSETS_PAGE))
                .andExpect(model().attributeExists(PAGE_ATTRIBUTE))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                // Checking page content.
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.title"))))
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.explanation"))))
                // The user has no assets, so we should see a message saying it.
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.noAssets"))))
                // We did ask for a correct page, so no message error on this point.
                .andExpect(content().string(not(containsString(getMessage(messages, "user.assets.wrongPageNumber")))));

    }

    @Test
    @DisplayName("User assets list with two assets")
    @WithMockUser(username = "straumat")
    void userWithAssets() throws Exception {

        // Straumat has two assets.
        mockMvc.perform(get("/account/assets"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSETS_PAGE))
                .andExpect(model().attributeExists(PAGE_ATTRIBUTE))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                // Checking page content.
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.title"))))
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.explanation"))))
                // The user no assets, so we should not see a message saying it doesn't have any.
                .andExpect(content().string(not(containsString(getMessage(messages, "user.assets.noAssets")))))
                // We did ask for a correct page, so no message error on this point.
                .andExpect(content().string(not(containsString(getMessage(messages, "user.assets.wrongPageNumber")))))
                // Assets are displayed.
                .andExpect(content().string(containsString("setOfRoylloNFT2")))
                .andExpect(content().string(containsString("trickyRoylloCoin")));

        // We are asking for a page that doesn't exist.
        mockMvc.perform(get("/account/assets").param(PAGE_ATTRIBUTE, "100"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSETS_PAGE))
                .andExpect(model().attributeExists(PAGE_ATTRIBUTE))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                // Checking page content.
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.title"))))
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.explanation"))))
                // It must say that the page doesn't exist.
                .andExpect(content().string(containsString(getMessage(messages, "user.assets.wrongPageNumber"))));
    }

}
