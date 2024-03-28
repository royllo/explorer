package org.royllo.explorer.web.test.controllers.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.dto.asset.AssetDTO.ASSET_ID_ALIAS_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.asset.AssetDTO.ASSET_ID_ALIAS_MINIMUM_SIZE;
import static org.royllo.explorer.core.dto.asset.AssetDTO.README_MAXIMUM_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_NAME_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSETS_PAGE;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_ASSET_FORM_PAGE;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("User assets controller tests")
@AutoConfigureMockMvc
public class UserAssetsTest extends BaseWebTest {

    @Autowired
    AssetRepository assetRepository;

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
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
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

    @Test
    @DisplayName("User asset form")
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    void userAssetForm() throws Exception {

        // =============================================================================================================
        // Errors.

        // Trying to edit an asset that doesn't exist.
        mockMvc.perform(get("/account/asset/123/edit"))
                .andExpect(status().isNotFound());

        // Trying to edit an asset we don't own.
        mockMvc.perform(get("/account/asset/" + ROYLLO_COIN_ASSET_ID + "/edit"))
                .andExpect(status().isUnauthorized());

        // =============================================================================================================
        // We want to edit tricky royllo coin (we own it).
        mockMvc.perform(get("/account/asset/" + TRICKY_ROYLLO_COIN_ASSET_ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSET_FORM_PAGE))
                .andExpect(model().attributeExists(ASSET_ID_ATTRIBUTE))
                .andExpect(model().attributeExists(ASSET_NAME_ATTRIBUTE))
                // Form content.
                .andExpect(content().string(containsString("value=\"" + TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS + "\"")))
                .andExpect(content().string(containsString(">This is a tricky coin!<")));

    }


    @Test
    @DisplayName("User assets edition saved")
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    void userAssetSave() throws Exception {

        // =============================================================================================================
        // Errors.

        // Trying without setting the asset.
        mockMvc.perform(post("/account/asset/save"))
                .andExpect(status().isNotFound());

        // Trying to edit an asset that doesn't exist.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, "NONEXISTENT_ASSET_ID")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound());

        // Trying to edit an asset we don't own.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, ROYLLO_COIN_ASSET_ID)
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized());

        // Asset id alias too small.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("assetIdAlias", RandomStringUtils.randomAlphabetic(ASSET_ID_ALIAS_MINIMUM_SIZE - 1))
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSET_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(containsString(getMessage(messages, "validation.asset.assetIdAlias.size"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.readme.size")))));

        // Asset id alias too long.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("assetIdAlias", RandomStringUtils.randomAlphabetic(ASSET_ID_ALIAS_MAXIMUM_SIZE + 1))
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSET_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(containsString(getMessage(messages, "validation.asset.assetIdAlias.size"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.readme.size")))));

        // Asset id alias with invalid characters.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("assetIdAlias", "aaaa aaaa")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSET_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.size")))))
                .andExpect(content().string(containsString(getMessage(messages, "validation.asset.assetIdAlias.invalid"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.readme.size")))));

        // Readme too long.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("readme", RandomStringUtils.randomAlphabetic(README_MAXIMUM_SIZE + 1))
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_ASSET_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.size")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.invalid")))))
                .andExpect(content().string(containsString(getMessage(messages, "validation.asset.readme.size"))));

        // =============================================================================================================
        // Valid update.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("assetIdAlias", "trickyRoylloCoinAlias")
                        .param("readme", "**This is a tricky coin new readme !**")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(containsString("redirect:/account/assets?assetUpdated=" + TRICKY_ROYLLO_COIN_ASSET_ID)))
                // Of course no errors
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.size")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.assetIdAlias.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.asset.readme.size")))));

        // Checking in database that the value of the asset has been updated.
        Optional<Asset> trickyRoylloCoin = assetRepository.findByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID);
        assertTrue(trickyRoylloCoin.isPresent());
        assertEquals("trickyRoylloCoinAlias", trickyRoylloCoin.get().getAssetIdAlias());
        assertEquals("**This is a tricky coin new readme !**", trickyRoylloCoin.get().getReadme());

        // Setting back the original values.
        mockMvc.perform(post("/account/asset/save")
                        .param(ASSET_ID_ATTRIBUTE, TRICKY_ROYLLO_COIN_ASSET_ID)
                        .param("assetIdAlias", TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS)
                        .param("readme", "This is a tricky coin!")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
    }

}
