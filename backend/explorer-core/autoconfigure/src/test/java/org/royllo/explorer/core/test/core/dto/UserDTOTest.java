package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.user.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("UserDTOTest tests")
public class UserDTOTest {

    @Test
    @DisplayName("Null username")
    public void nullUsername() {
        UserDTO user = UserDTO.builder()
                .build();
        assertNull(user.getShortenedUsername());
    }

    @Test
    @DisplayName("Normal username")
    public void normalUsername() {
        UserDTO user = UserDTO.builder()
                .username("straumat")
                .build();
        assertEquals("straumat", user.getShortenedUsername());
    }

    @Test
    @DisplayName("Long username")
    public void longUsername() {
        UserDTO user = UserDTO.builder()
                .username("abcedefghijklmnopqrstuvwxyz")
                .build();
        assertEquals("abc...xyz", user.getShortenedUsername());
    }

}
