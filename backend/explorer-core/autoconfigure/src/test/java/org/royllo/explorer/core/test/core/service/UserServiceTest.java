package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
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
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getAdministratorUser()")
    public void getAdministratorUser() {
        final UserDTO administratorUser = userService.getAdministratorUser();
        assertNotNull(administratorUser);
        assertEquals(ADMINISTRATOR_ID, administratorUser.getId().longValue());
        assertEquals(ADMINISTRATOR_USER_ID, administratorUser.getUserId());
        assertEquals(ADMINISTRATOR_USER_USERNAME, administratorUser.getUsername());
        assertEquals(ADMINISTRATOR_USER.getRole(), administratorUser.getRole());
    }

    @Test
    @DisplayName("getAnonymousUser()")
    public void getAnonymousUserTest() {
        final UserDTO anonymousUser = userService.getAnonymousUser();
        assertNotNull(anonymousUser);
        assertEquals(ANONYMOUS_ID, anonymousUser.getId().longValue());
        assertEquals(ANONYMOUS_USER_ID, anonymousUser.getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, anonymousUser.getUsername());
        assertEquals(ANONYMOUS_USER.getRole(), anonymousUser.getRole());
    }

    @Test
    @DisplayName("createUser()")
    public void createUserTest() {
        // Creating a user with empty name or null.
        AssertionError e = assertThrows(AssertionError.class, () -> userService.createUser(null));
        assertEquals("Username is required", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.createUser(""));
        assertEquals("Username is required", e.getMessage());

        // Creating an existing user.
        e = assertThrows(AssertionError.class, () -> userService.createUser(ANONYMOUS_USER_USERNAME));
        assertEquals("Username 'anonymous' already registered", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.createUser(ADMINISTRATOR_USER_USERNAME));
        assertEquals("Username 'administrator' already registered", e.getMessage());
        e = assertThrows(AssertionError.class, () -> userService.createUser("straumat"));
        assertEquals("Username 'straumat' already registered", e.getMessage());

        // Creating a new user.
        final UserDTO newUser = userService.createUser("newUser");
        assertNotNull(newUser);
        assertNotNull(newUser.getId());
        assertEquals("newuser", newUser.getUsername());
        assertEquals(USER, newUser.getRole());
    }

    @Test
    @DisplayName("getUserByUsername()")
    public void getUserByUsernameTest() {
        // Non-existing user.
        final Optional<UserDTO> nonExistingUser = userService.getUserByUsername("NON_EXISTING_USERNAME");
        assertFalse(nonExistingUser.isPresent());

        // Existing user.
        final Optional<UserDTO> existingUser = userService.getUserByUsername("straumat");
        assertTrue(existingUser.isPresent());
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals("straumat", existingUser.get().getUsername());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());

        // Existing user (Uppercase).
        final Optional<UserDTO> existingUserUppercase = userService.getUserByUsername("STRAUMAT");
        assertTrue(existingUserUppercase.isPresent());
        assertEquals(2, existingUserUppercase.get().getId().longValue());
        assertEquals("straumat", existingUserUppercase.get().getUsername());
        assertEquals(ADMINISTRATOR, existingUserUppercase.get().getRole());
    }

}
