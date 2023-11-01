package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.UserRole.ADMINISTRATOR;
import static org.royllo.explorer.core.util.enums.UserRole.USER;

@SpringBootTest
@DisplayName("UserService tests")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getAnonymousUser()")
    public void getAnonymousUserTest() {
        final UserDTO anonymousUser = userService.getAnonymousUser();
        assertNotNull(anonymousUser);
        assertEquals(ANONYMOUS_ID, anonymousUser.getId().longValue());
        assertEquals(ANONYMOUS_USER_USERNAME, anonymousUser.getUsername());
        assertEquals(USER, anonymousUser.getRole());
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
