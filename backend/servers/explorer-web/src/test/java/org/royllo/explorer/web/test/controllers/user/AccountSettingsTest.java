package org.royllo.explorer.web.test.controllers.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_USERNAME;
import static org.royllo.explorer.web.util.constants.AccountSettingsPageConstants.ACCOUNT_SETTINGS_PAGE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Account settings controller tests")
@AutoConfigureMockMvc
public class AccountSettingsTest extends BaseWebTest {

    @Autowired
    MessageSource messages;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Account settings form")
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    void accountSettingsForm() throws Exception {

        mockMvc.perform(get("/account/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Checking page content
                .andExpect(content().string(containsString(getMessage(messages, "user.settings.title"))))
                .andExpect(content().string(containsString("/22222222-2222-2222-2222-222222222222.jpeg")))
                .andExpect(content().string(containsString("Traumat")))
                .andExpect(content().string(containsString("developer")))
                .andExpect(content().string(containsString("github.com/straumat")))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.fullName.length.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.biography.length.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.length.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "user.settings.information.success")))));

    }

    @Test
    @DisplayName("Account settings saved")
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    void accountSettingsSaved() throws Exception {

        // =============================================================================================================
        // When there are error on fields.

        mockMvc.perform(post("/account/settings")
                        .param("username", RandomStringUtils.randomAlphanumeric(31))
                        .param("fullName", RandomStringUtils.randomAlphanumeric(41))
                        .param("biography", RandomStringUtils.randomAlphanumeric(256))
                        .param("website", RandomStringUtils.randomAlphanumeric(51))
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "username"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.username.invalid"))))
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "fullName"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.fullName.length.too_long"))))
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "biography"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.biography.length.too_long"))))
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "website"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.website.invalid"))))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.website.length.too_long"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "user.settings.information.success")))));

        // We try to use an existing username.
        mockMvc.perform(post("/account/settings")
                        .param("username", ADMINISTRATOR_USER_USERNAME)
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(FORM_ATTRIBUTE, "username"))
                .andExpect(content().string(containsString(getMessage(messages, "validation.user.username.exists"))));

        // =============================================================================================================
        // When it works.

        // We check the existing values.
        Optional<UserDTO> existingUser = userService.getUserByUsername(STRAUMAT_USER_USERNAME);
        assertTrue(existingUser.isPresent());
        assertEquals(STRAUMAT_USER_USERNAME, existingUser.get().getUsername());
        assertEquals("22222222-2222-2222-2222-222222222222.jpeg", existingUser.get().getProfilePictureFileName());
        assertEquals("Stéphane Traumat", existingUser.get().getFullName());
        assertEquals("I am a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());

        MockMultipartFile newProfilePicture = new MockMultipartFile("user-file",
                new ClassPathResource("test-profile-picture.jpg").getContentAsByteArray());

        // We update the values.
        mockMvc.perform(multipart("/account/settings")
                        .file("profilePicture", newProfilePicture.getBytes())
                        .param("username", "pDupont")
                        .param("fullName", "Paul Dupont")
                        .param("biography", "I'm an architect")
                        .param("website", "https://www.architect.com")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTINGS_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.username.exists")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.username.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.fullName.length.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.biography.length.too_long")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.invalid")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "validation.user.website.length.too_long")))))
                .andExpect(content().string(containsString(getMessage(messages, "user.settings.information.success"))));

        // We check the updated values.
        Optional<UserDTO> userUpdated = userService.getUserByUserId(STRAUMAT_USER_USER_ID);
        assertTrue(userUpdated.isPresent());
        assertEquals("22222222-2222-2222-2222-222222222222.jpg", userUpdated.get().getProfilePictureFileName());
        assertEquals("pdupont", userUpdated.get().getUsername());
        assertEquals("Paul Dupont", userUpdated.get().getFullName());
        assertEquals("I'm an architect", userUpdated.get().getBiography());
        assertEquals("https://www.architect.com", userUpdated.get().getWebsite());

        // We go back to normal values
        MockMultipartFile oldProfilePicture = new MockMultipartFile("user-file",
                new ClassPathResource("test-profile-picture.jpg").getContentAsByteArray());
        mockMvc.perform(multipart("/account/settings")
                        .file("profilePicture", oldProfilePicture.getBytes())
                        .param("username", STRAUMAT_USER_USERNAME)
                        .param("fullName", STRAUMAT_USER_FULL_NAME)
                        .param("biography", STRAUMAT_USER_BIOGRAPHY)
                        .param("website", STRAUMAT_USER_WEBSITE)
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }

}
