package org.royllo.explorer.api.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.api.util.enums.UserRole.ADMINISTRATOR;
import static org.royllo.explorer.api.util.enums.UserRole.USER;

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
        assertEquals(0, anonymousUser.getId().longValue());
        assertEquals("anonymous", anonymousUser.getUsername());
        assertEquals(USER, anonymousUser.getRole());
    }

    @Test
    @DisplayName("getUserByUsername()")
    public void getUserByUsernameTest() {
        // Non-existing user.
        final Optional<UserDTO> nonExistingUser = userService.getUserByUsername("NON_EXISTING_USER");
        assertFalse(nonExistingUser.isPresent());

        // Existing user.
        final Optional<UserDTO> existingUser = userService.getUserByUsername("straumat");
        assertTrue(existingUser.isPresent());
        assertEquals(1, existingUser.get().getId().longValue());
        assertEquals("straumat", existingUser.get().getUsername());
        assertEquals(ADMINISTRATOR, existingUser.get().getRole());
    }

}
