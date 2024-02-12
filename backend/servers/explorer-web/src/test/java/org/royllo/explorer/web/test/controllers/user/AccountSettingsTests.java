package org.royllo.explorer.web.test.controllers.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.web.util.constants.AccountSettingsPageConstants.ACCOUNT_SETTINGS_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Account settings controller tests")
@AutoConfigureMockMvc
public class AccountSettingsTests extends BaseTest {

    @Autowired
    MessageSource messages;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Account settings form")
    @WithMockUser(username = "straumat")
    void accountSettingsForm() throws Exception {

        mockMvc.perform(get("/account/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Checking page content
                .andExpect(content().string(containsString(getMessage(messages, "account.settings.title"))))
                .andExpect(content().string(containsString("Traumat")))
                .andExpect(content().string(containsString("developer")))
                .andExpect(content().string(containsString("github.com/straumat")))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.fullName.size.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.biography.size.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.size.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "account.settings.information.success")))));

    }

    @Test
    @DisplayName("Account settings saved")
    @WithMockUser(username = "straumat")
    void accountSettingsSaved() throws Exception {

        // =============================================================================================================
        // When there are error on fields.

        mockMvc.perform(post("/account/settings")
                        .param("fullName", RandomStringUtils.randomAlphanumeric(41))
                        .param("biography", RandomStringUtils.randomAlphanumeric(256))
                        .param("website", RandomStringUtils.randomAlphanumeric(51))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "fullName"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.fullName.size.too_long"))))
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "biography"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.biography.size.too_long"))))
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "website"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.website.invalid"))))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.website.size.too_long"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "account.settings.information.success")))));

        // =============================================================================================================
        // When it works.

        // We check the existing values.
        Optional<UserDTO> existingUser = userService.getUserByUsername("straumat");
        assertTrue(existingUser.isPresent());
        assertEquals("Stéphane Traumat", existingUser.get().getFullName());
        assertEquals("I'm a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());

        // We update the values.
        mockMvc.perform(post("/account/settings")
                        .param("fullName", "Paul Dupont")
                        .param("biography", "I'm an architect")
                        .param("website", "https://www.architect.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.fullName.size.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.biography.size.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.size.too_long")))))
                .andExpect(content().string(containsString(getMessage(messages, "account.settings.information.success"))));

        // We check the updated values.
        Optional<UserDTO> userUpdated = userService.getUserByUsername("straumat");
        assertTrue(userUpdated.isPresent());
        assertEquals("Paul Dupont", userUpdated.get().getFullName());
        assertEquals("I'm an architect", userUpdated.get().getBiography());
        assertEquals("https://www.architect.com", userUpdated.get().getWebsite());

    }

}
