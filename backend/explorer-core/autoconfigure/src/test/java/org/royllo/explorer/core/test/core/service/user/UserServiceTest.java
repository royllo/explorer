package org.royllo.explorer.core.test.core.service.user;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.dto.user.UserDTOSettings;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.dto.user.UserDTO.BIOGRAPHY_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.FULL_NAME_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.WEBSITE_MAXIMUM_SIZE;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.UserRole.ADMINISTRATOR;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

@SpringBootTest
@DirtiesContext
@DisplayName("UserService tests")
public class UserServiceTest extends TestWithMockServers {

    /** Test user. */
    public static final String STRAUMAT_USER_ID = "22222222-2222-2222-2222-222222222222";
    public static final String STRAUMAT_USERNAME = "straumat";

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("addUser()")
    public void addUser() {
        // =============================================================================================================
        // Error tests.

        // Creating a user with null username.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> userService.addUser(null))
                .satisfies(violations -> {
                    assertEquals(1, violations.getConstraintViolations().size());
                    assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("username")));
                });

        // Creating a user with an empty username.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> userService.addUser(""))
                .satisfies(violations -> {
                    assertEquals(1, violations.getConstraintViolations().size());
                    assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("username")));
                });

        // Creating an existing user.
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.addUser(STRAUMAT_USERNAME))
                .withMessage("Username 'straumat' already registered");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.addUser(ADMINISTRATOR_USER_USERNAME))
                .withMessage("Username 'administrator' already registered");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.addUser(STRAUMAT_USERNAME))
                .withMessage("Username 'straumat' already registered");

        // =============================================================================================================
        // Normal behavior.

        // Creating a new user.
        assertThat(userService.addUser("newUser2"))
                .isNotNull()
                .satisfies(user -> {
                    assertNotNull(user.getId());
                    assertEquals("newuser2", user.getUsername());
                    assertEquals(USER, user.getRole());
                });
    }

    @Test
    @DisplayName("updateUser()")
    public void updateUser() {
        // We retrieve the existing user.
        UserDTO straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID)
                .orElseThrow(() -> new AssertionError("straumat not found"));
        UserDTOSettings currentSettings = straumatUser.getCurrentSettings();

        // =============================================================================================================
        // Constraint tests.

        // We now try to update a non-existing user.
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.updateUser(null, currentSettings))
                .withMessage("User not found with userId: null");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.updateUser("NON_EXISTING_USERNAME", currentSettings))
                .withMessage("User not found with userId: NON_EXISTING_USERNAME");

        // We try to update 'straumat' username with an existing username.
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.updateUser(STRAUMAT_USER_ID,
                        UserDTOSettings.builder()
                                .username(ADMINISTRATOR_USER_USERNAME)  // <- Existing username.
                                .profilePictureFileName(currentSettings.profilePictureFileName())
                                .fullName(currentSettings.fullName())
                                .biography(currentSettings.biography())
                                .website(currentSettings.website())
                                .build()))
                .withMessage("New username 'administrator' already registered");

        // Checking fields constraints.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> userService.updateUser(STRAUMAT_USER_ID,
                        UserDTOSettings.builder()
                                .username(RandomStringUtils.randomAlphanumeric(41))
                                .profilePictureFileName(RandomStringUtils.randomAlphanumeric(PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE + 1))
                                .fullName(RandomStringUtils.randomAlphanumeric(FULL_NAME_MAXIMUM_SIZE + 1))
                                .biography(RandomStringUtils.randomAlphanumeric(BIOGRAPHY_MAXIMUM_SIZE + 1))
                                .website(RandomStringUtils.randomAlphanumeric(WEBSITE_MAXIMUM_SIZE + 1))
                                .build())
                )
                .satisfies(violations -> {
                    assertEquals(5, violations.getConstraintViolations().size());
                    assertTrue(isPropertyInConstraintViolations(violations, "username"));
                    assertTrue(isPropertyInConstraintViolations(violations, "profilePictureFileName"));
                    assertTrue(isPropertyInConstraintViolations(violations, "fullName"));
                    assertTrue(isPropertyInConstraintViolations(violations, "biography"));
                    assertTrue(isPropertyInConstraintViolations(violations, "website"));
                });

        // =============================================================================================================
        // Normal behavior.

        // We check the existing user.
        assertEquals(2, straumatUser.getId().longValue());
        assertEquals(STRAUMAT_USERNAME, straumatUser.getUsername());
        assertEquals("22222222-2222-2222-2222-222222222222.jpeg", straumatUser.getProfilePictureFileName());
        assertEquals("Stéphane Traumat", straumatUser.getFullName());
        assertEquals("I am a developer", straumatUser.getBiography());
        assertEquals("https://github.com/straumat", straumatUser.getWebsite());
        assertEquals(ADMINISTRATOR, straumatUser.getRole());

        // We update the user settings.
        userService.updateUser(STRAUMAT_USER_ID, UserDTOSettings.builder()
                .username("straumat2")
                .profilePictureFileName("33333333-3333-3333-3333-333333333333.jpeg")
                .fullName("Stéphane Traumat (Updated)")
                .biography("I am a developer (Updated)")
                .website("https://github.com/straumat2")
                .build());

        // We check what has been updated.
        straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID)
                .orElseThrow(() -> new AssertionError("straumat not found"));
        // Not supposed to be updated.
        assertEquals(2, straumatUser.getId().longValue());
        assertEquals(STRAUMAT_USER_ID, straumatUser.getUserId());
        assertEquals(ADMINISTRATOR, straumatUser.getRole());
        // Should be updated.
        assertEquals("straumat2", straumatUser.getUsername());
        assertEquals("33333333-3333-3333-3333-333333333333.jpeg", straumatUser.getProfilePictureFileName());
        assertEquals("Stéphane Traumat (Updated)", straumatUser.getFullName());
        assertEquals("I am a developer (Updated)", straumatUser.getBiography());
        assertEquals("https://github.com/straumat2", straumatUser.getWebsite());

        // We go back to normal settings.
        userService.updateUser(STRAUMAT_USER_ID, currentSettings);
        straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID)
                .orElseThrow(() -> new AssertionError("straumat not found"));
        assertEquals(2, straumatUser.getId().longValue());
        assertEquals(STRAUMAT_USERNAME, straumatUser.getUsername());
        assertEquals("22222222-2222-2222-2222-222222222222.jpeg", straumatUser.getProfilePictureFileName());
        assertEquals("Stéphane Traumat", straumatUser.getFullName());
        assertEquals("I am a developer", straumatUser.getBiography());
        assertEquals("https://github.com/straumat", straumatUser.getWebsite());
        assertEquals(ADMINISTRATOR, straumatUser.getRole());
    }

    @Test
    @DisplayName("getUserByUsername()")
    public void getUserByUsername() {
        // Non-existing user.
        assertFalse(userService.getUserByUsername("NON_EXISTING_USERNAME").isPresent());

        // Existing user.
        assertThat(userService.getUserByUsername(STRAUMAT_USERNAME))
                .isNotNull()
                .hasValueSatisfying(user -> {
                    assertEquals(2, user.getId().longValue());
                    assertEquals(STRAUMAT_USERNAME, user.getUsername());
                    assertEquals("Stéphane Traumat", user.getFullName());
                    assertEquals("I am a developer", user.getBiography());
                    assertEquals("https://github.com/straumat", user.getWebsite());
                    assertEquals(ADMINISTRATOR, user.getRole());
                });

        // Existing user (Uppercase).
        assertThat(userService.getUserByUsername("STRAUMAT"))
                .isNotNull()
                .hasValueSatisfying(user -> {
                    assertEquals(2, user.getId().longValue());
                    assertEquals(STRAUMAT_USERNAME, user.getUsername());
                    assertEquals("Stéphane Traumat", user.getFullName());
                    assertEquals("I am a developer", user.getBiography());
                    assertEquals("https://github.com/straumat", user.getWebsite());
                    assertEquals(ADMINISTRATOR, user.getRole());
                });
    }

    @Test
    @DisplayName("getUserByUserId()")
    public void getUserByUserId() {
        // Non-existing user.
        assertFalse(userService.getUserByUserId("NON_EXISTING_USERNAME").isPresent());

        // Existing user.
        assertThat(userService.getUserByUserId(STRAUMAT_USER_ID))
                .isNotNull()
                .hasValueSatisfying(user -> {
                    assertEquals(2, user.getId().longValue());
                    assertEquals(STRAUMAT_USERNAME, user.getUsername());
                    assertEquals("Stéphane Traumat", user.getFullName());
                    assertEquals("I am a developer", user.getBiography());
                    assertEquals("https://github.com/straumat", user.getWebsite());
                    assertEquals(ADMINISTRATOR, user.getRole());
                });
    }

}
