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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_USERNAME;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
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

        // Creating a user with empty name or null.
        ConstraintViolationException violations = assertThrows(ConstraintViolationException.class, () -> userService.addUser(null));
        assertEquals(1, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("username")));

        violations = assertThrows(ConstraintViolationException.class, () -> userService.addUser(""));
        assertEquals(1, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("username")));

        // Creating an existing user.
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> userService.addUser(ANONYMOUS_USER_USERNAME));
        assertEquals("Username 'anonymous' already registered", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> userService.addUser(ADMINISTRATOR_USER_USERNAME));
        assertEquals("Username 'administrator' already registered", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> userService.addUser(STRAUMAT_USERNAME));
        assertEquals("Username 'straumat' already registered", e.getMessage());

        // =============================================================================================================
        // Normal behavior.

        // Creating a new user.
        final UserDTO newUser = userService.addUser("newUser2");
        assertNotNull(newUser);
        assertNotNull(newUser.getId());
        assertEquals("newuser2", newUser.getUsername());
        assertEquals(USER, newUser.getRole());
    }

    @Test
    @DisplayName("updateUser()")
    public void updateUser() {
        // We retrieve the existing user.
        UserDTO straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID).orElse(null);
        assertNotNull(straumatUser);
        UserDTOSettings currentSettings = straumatUser.getCurrentSettings();

        // =============================================================================================================
        // Errors.

        // We now try to update a non-existing user.
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser("NON_EXISTING_USERNAME", currentSettings));
        assertEquals("User not found with userId: NON_EXISTING_USERNAME", e.getMessage());

        // We try to update the user with an existing username.
        e = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(STRAUMAT_USER_ID,
                        UserDTOSettings.builder()
                                .username(ADMINISTRATOR_USER_USERNAME)  // <- Existing username.
                                .profilePictureFileName(currentSettings.profilePictureFileName())
                                .fullName(currentSettings.fullName())
                                .biography(currentSettings.biography())
                                .website(currentSettings.website())
                                .build()));
        assertEquals("New username 'administrator' already registered", e.getMessage());

        // Checking fields constraints.
        ConstraintViolationException violations = assertThrows(ConstraintViolationException.class, () -> {
            userService.updateUser(STRAUMAT_USER_ID,
                    UserDTOSettings.builder()
                            .username(RandomStringUtils.randomAlphanumeric(41))
                            .profilePictureFileName(RandomStringUtils.randomAlphanumeric(301))
                            .fullName(RandomStringUtils.randomAlphanumeric(41))
                            .biography(RandomStringUtils.randomAlphanumeric(256))
                            .website(RandomStringUtils.randomAlphanumeric(51))
                            .build());
        });
        assertEquals(5, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("username")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("profilePictureFileName")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("fullName")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("biography")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getMessage().contains("website")));

        // =============================================================================================================
        // We make change to settings - Normal behavior.

        // We check the existing user.
        assertEquals(2, straumatUser.getId().longValue());
        assertEquals(STRAUMAT_USERNAME, straumatUser.getUsername());
        assertEquals("22222222-2222-2222-2222-222222222222.jpeg", straumatUser.getProfilePictureFileName());
        assertEquals("Stéphane Traumat", straumatUser.getFullName());
        assertEquals("I am a developer", straumatUser.getBiography());
        assertEquals("https://github.com/straumat", straumatUser.getWebsite());
        assertEquals(ADMINISTRATOR, straumatUser.getRole());

        // We update the user settings.
        UserDTOSettings newSettings = UserDTOSettings.builder()
                .username("straumat2")
                .profilePictureFileName("33333333-3333-3333-3333-333333333333.jpeg")
                .fullName("Stéphane Traumat (Updated)")
                .biography("I am a developer (Updated)")
                .website("https://github.com/straumat2")
                .build();
        userService.updateUser(STRAUMAT_USER_ID, newSettings);

        // We check what has been updated.
        straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID).orElse(null);
        assertNotNull(straumatUser);
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

        // We go back to normal.
        userService.updateUser(STRAUMAT_USER_ID, currentSettings);
        straumatUser = userService.getUserByUserId(STRAUMAT_USER_ID).orElse(null);
        assertNotNull(straumatUser);
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
        final Optional<UserDTO> nonExistingUser = userService.getUserByUsername("NON_EXISTING_USERNAME");
        assertFalse(nonExistingUser.isPresent());

        // Existing user.
        final Optional<UserDTO> existingUser = userService.getUserByUsername(STRAUMAT_USERNAME);
        assertTrue(existingUser.isPresent());
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals(STRAUMAT_USERNAME, existingUser.get().getUsername());
        assertEquals("Stéphane Traumat", existingUser.get().getFullName());
        assertEquals("I am a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());

        // Existing user (Uppercase).
        final Optional<UserDTO> existingUserUppercase = userService.getUserByUsername("STRAUMAT");
        assertTrue(existingUserUppercase.isPresent());
        assertEquals(2, existingUserUppercase.get().getId().longValue());
        assertEquals(STRAUMAT_USERNAME, existingUserUppercase.get().getUsername());
        assertEquals("Stéphane Traumat", existingUserUppercase.get().getFullName());
        assertEquals("I am a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());
        assertEquals(ADMINISTRATOR, existingUserUppercase.get().getRole());
    }

    @Test
    @DisplayName("getUserByUserId()")
    public void getUserByUserId() {
        // Non-existing user.
        final Optional<UserDTO> nonExistingUser = userService.getUserByUserId("NON_EXISTING_USERNAME");
        assertFalse(nonExistingUser.isPresent());

        // Existing user.
        final Optional<UserDTO> existingUser = userService.getUserByUserId(STRAUMAT_USER_ID);
        assertTrue(existingUser.isPresent());
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals(STRAUMAT_USERNAME, existingUser.get().getUsername());
        assertEquals("Stéphane Traumat", existingUser.get().getFullName());
        assertEquals("I am a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());
    }

}
