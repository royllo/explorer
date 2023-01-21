package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("UserRepository tests")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find anonymous user")
    public void findAnonymousUser() {
        // Finding the user by its uid.
        final Optional<User> anonymousUserByUid = userRepository.findById(UserConstants.ANONYMOUS_USER_ID);
        assertTrue(anonymousUserByUid.isPresent());
        assertEquals(0, anonymousUserByUid.get().getId().longValue());
        assertEquals("anonymous", anonymousUserByUid.get().getUsername());

        // Finding the user by its username.
        final Optional<User> anonymousUserByUsername = userRepository.findByUsername(UserConstants.ANONYMOUS_USER_USERNAME);
        assertTrue(anonymousUserByUsername.isPresent());
        assertEquals(0, anonymousUserByUsername.get().getId().longValue());
        assertEquals("anonymous", anonymousUserByUsername.get().getUsername());
    }

    @Test
    @DisplayName("Find non existing user")
    public void findNonExistingUser() {
        assertFalse(userRepository.findById(99999999999L).isPresent());
        assertFalse(userRepository.findByUsername("NON_EXISTING_USER").isPresent());
    }

    @Test
    @DisplayName("Find existing user")
    public void findExistingUser() {
        final Optional<User> existingUser = userRepository.findByUsername("straumat");
        assertTrue(existingUser.isPresent());
        assertEquals(1, existingUser.get().getId().longValue());
        assertEquals("straumat", existingUser.get().getUsername());
    }

}
