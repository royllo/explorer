package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserDTOTest tests")
public class UserDTOTest {

    @Test
    @DisplayName("getShortenedUsername()")
    public void getShortenedUsername() {
        // Null username.
        UserDTO user = UserDTO.builder()
                .build();
        assertNull(user.getShortenedUsername());

        // Normal username.
        user = UserDTO.builder()
                .username("straumat")
                .build();
        assertEquals("straumat", user.getShortenedUsername());

        // Long username.
        user = UserDTO.builder()
                .username("abcedefghijklmnopqrstuvwxyz")
                .build();
        assertEquals("abc...xyz", user.getShortenedUsername());
    }

    @Test
    @DisplayName("hasSettingsSet()")
    public void hasSettingsSet() {
        // No settings set.
        UserDTO user = UserDTO.builder()
                .build();
        assertFalse(user.hasSettingsSet());

        // Full name set.
        user.setFullName("Stéphane traumat");
        assertTrue(user.hasSettingsSet());
        // Biography set.
        user.setBiography("I am a software engineer.");
        assertTrue(user.hasSettingsSet());
        // Website set.
        user.setWebsite("https://www.royllo.org");
        assertTrue(user.hasSettingsSet());
    }

}
