package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.core.util.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(UserRole.USER, anonymousUser.getRole());
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
        assertEquals(UserRole.ADMINISTRATOR, existingUser.get().getRole());
    }

}
