package org.royllo.explorer.core.test.core.service.user;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.user.UserRepository;
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
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_ID;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_ID;
import static org.royllo.explorer.core.util.constants.AdministratorUserConstants.ADMINISTRATOR_USER_USERNAME;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.UserRole.ADMINISTRATOR;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

@SpringBootTest
@DirtiesContext
@DisplayName("UserService tests")
public class UserServiceTest extends TestWithMockServers {

    public static final String STRAUMAT_USERNAME = "straumat";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getAdministratorUser()")
    public void getAdministratorUser() {
        final UserDTO administratorUser = userRepository
                .findByUserId(ADMINISTRATOR_USER_ID)
                .map(USER_MAPPER::mapToUserDTO)
                .orElse(null);
        assertNotNull(administratorUser);
        assertEquals(ADMINISTRATOR_ID, administratorUser.getId().longValue());
        assertEquals(ADMINISTRATOR_USER_ID, administratorUser.getUserId());
        assertEquals(ADMINISTRATOR_USER_USERNAME, administratorUser.getUsername());
        assertEquals(ADMINISTRATOR_USER.getRole(), administratorUser.getRole());
    }

    @Test
    @DisplayName("getAnonymousUser()")
    public void getAnonymousUserTest() {
        final UserDTO anonymousUser = userRepository
                .findByUserId(ANONYMOUS_USER_ID)
                .map(USER_MAPPER::mapToUserDTO)
                .orElse(null);
        assertNotNull(anonymousUser);
        assertEquals(ANONYMOUS_ID, anonymousUser.getId().longValue());
        assertEquals(ANONYMOUS_USER_ID, anonymousUser.getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, anonymousUser.getUsername());
        assertEquals(ANONYMOUS_USER.getRole(), anonymousUser.getRole());
    }

    @Test
    @DisplayName("addUser()")
    public void createUserTest() {
        // Creating a user with empty name or null.
        AssertionError e = assertThrows(AssertionError.class, () -> userService.addUser(null));
        assertEquals("Username is required", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.addUser(""));
        assertEquals("Username is required", e.getMessage());

        // Creating an existing user.
        e = assertThrows(AssertionError.class, () -> userService.addUser(ANONYMOUS_USER_USERNAME));
        assertEquals("Username 'anonymous' already registered", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.addUser(ADMINISTRATOR_USER_USERNAME));
        assertEquals("Username 'administrator' already registered", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.addUser(STRAUMAT_USERNAME));
        assertEquals("Username 'straumat' already registered", e.getMessage());

        // Creating a new user.
        final UserDTO newUser = userService.addUser("newUser2");
        assertNotNull(newUser);
        assertNotNull(newUser.getId());
        assertEquals("newuser2", newUser.getUsername());
        assertEquals(USER, newUser.getRole());
    }

    @Test
    @DisplayName("updateUser()")
    public void updateUserTest() {
        // We retrieve an existing user.
        Optional<UserDTO> existingUser = userService.getUserByUsername(STRAUMAT_USERNAME);
        assertTrue(existingUser.isPresent());
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals(STRAUMAT_USERNAME, existingUser.get().getUsername());
        assertEquals("22222222-2222-2222-2222-222222222222.jpeg", existingUser.get().getProfilePictureFileName());
        assertEquals("Stéphane Traumat", existingUser.get().getFullName());
        assertEquals("I am a developer", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat", existingUser.get().getWebsite());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());

        // We update the user data.
        existingUser.get().setProfilePictureFileName("33333333-3333-3333-3333-333333333333.jpeg");
        existingUser.get().setFullName("Stéphane Traumat (Updated)");
        existingUser.get().setBiography("I am a developer (Updated)");
        existingUser.get().setWebsite("https://github.com/straumat2");
        userService.updateUser(STRAUMAT_USERNAME, existingUser.get());

        // We check what has been updated.
        existingUser = userService.getUserByUsername(STRAUMAT_USERNAME);
        assertTrue(existingUser.isPresent());
        // Not supposed to be updated.
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals(STRAUMAT_USERNAME, existingUser.get().getUsername());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());
        // Should be updated.
        assertEquals("33333333-3333-3333-3333-333333333333.jpeg", existingUser.get().getProfilePictureFileName());
        assertEquals("Stéphane Traumat (Updated)", existingUser.get().getFullName());
        assertEquals("I am a developer (Updated)", existingUser.get().getBiography());
        assertEquals("https://github.com/straumat2", existingUser.get().getWebsite());

        // We now try to update a non-existing user.
        Optional<UserDTO> finalExistingUser = existingUser;
        AssertionError e = assertThrows(AssertionError.class, () -> userService.updateUser("NON_EXISTING_USERNAME", finalExistingUser.get()));
        assertEquals("User not found with username: NON_EXISTING_USERNAME", e.getMessage());

        // Now checking the field constraints by updating to invalid values.
        existingUser.get().setFullName(RandomStringUtils.randomAlphanumeric(41));
        existingUser.get().setBiography(RandomStringUtils.randomAlphanumeric(256));
        existingUser.get().setWebsite(RandomStringUtils.randomAlphanumeric(51));
        Optional<UserDTO> finalExistingUserWithErrors = existingUser;
        ConstraintViolationException violations = assertThrows(ConstraintViolationException.class, () -> {
            userService.updateUser(STRAUMAT_USERNAME, finalExistingUserWithErrors.get());
        });
        assertEquals(3, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("fullName")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("biography")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getMessage().contains("website")));

        // =============================================================================================================
        // Now changing the username we set back all the valid values.

        // Null username.
        e = assertThrows(AssertionError.class, () -> {
            Optional<UserDTO> localUser = userService.getUserByUsername(STRAUMAT_USERNAME);
            assertTrue(localUser.isPresent());
            localUser.get().setUsername(null);
            userService.updateUser(STRAUMAT_USERNAME, localUser.get());
            ;
        });
        assertEquals("New user name is required", e.getMessage());
        assertEquals("straumat", userService.getUserByUsername(STRAUMAT_USERNAME).get().getUsername());

        // Long username.
        violations = assertThrows(ConstraintViolationException.class, () -> {
            Optional<UserDTO> localUser = userService.getUserByUsername(STRAUMAT_USERNAME);
            assertTrue(localUser.isPresent());
            localUser.get().setUsername(RandomStringUtils.randomAlphanumeric(30));
            userService.updateUser(STRAUMAT_USERNAME, localUser.get());
        });
        assertTrue(violations.getConstraintViolations()
                .stream()
                .anyMatch(violation -> violation.getMessage().contains("validation.user.username.invalid")));
        assertEquals("straumat", userService.getUserByUsername(STRAUMAT_USERNAME).get().getUsername());

        // Username with space.
        violations = assertThrows(ConstraintViolationException.class, () -> {
            Optional<UserDTO> localUser = userService.getUserByUsername(STRAUMAT_USERNAME);
            assertTrue(localUser.isPresent());
            localUser.get().setUsername("straumat with space");
            userService.updateUser(STRAUMAT_USERNAME, localUser.get());
        });
        assertTrue(violations.getConstraintViolations()
                .stream()
                .anyMatch(violation -> violation.getMessage().contains("validation.user.username.invalid")));
        assertEquals("straumat", userService.getUserByUsername(STRAUMAT_USERNAME).get().getUsername());

        // Changing for an existing username.
        e = assertThrows(AssertionError.class, () -> {
            Optional<UserDTO> localUser = userService.getUserByUsername(STRAUMAT_USERNAME);
            assertTrue(localUser.isPresent());
            localUser.get().setUsername(ADMINISTRATOR_USER_USERNAME);
            userService.updateUser(STRAUMAT_USERNAME, localUser.get());
        });
        assertEquals("Username '" + ADMINISTRATOR_USER_USERNAME + "' already registered", e.getMessage());
        assertEquals("straumat", userService.getUserByUsername(STRAUMAT_USERNAME).get().getUsername());

        // Changing for a valid username.
        assertTrue(userService.getUserByUsername(STRAUMAT_USERNAME).isPresent());
        assertFalse(userService.getUserByUsername("MrRobot123").isPresent());
        Optional<UserDTO> localUser = userService.getUserByUsername(STRAUMAT_USERNAME);
        assertTrue(localUser.isPresent());
        localUser.get().setUsername("MrRobot123");
        userService.updateUser(STRAUMAT_USERNAME, localUser.get());
        assertFalse(userService.getUserByUsername("STRAUMAT_USERNAME").isPresent());
        assertTrue(userService.getUserByUsername("MrRobot123").isPresent());

        // We go back to normal.
        existingUser.get().setUsername(STRAUMAT_USERNAME);
        existingUser.get().setProfilePictureFileName("22222222-2222-2222-2222-222222222222.jpeg");
        existingUser.get().setFullName("Stéphane Traumat");
        existingUser.get().setBiography("I am a developer");
        existingUser.get().setWebsite("https://github.com/straumat");
        userService.updateUser("MrRobot123", existingUser.get());
    }

    @Test
    @DisplayName("getUserByUsername()")
    public void getUserByUsernameTest() {
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

}
