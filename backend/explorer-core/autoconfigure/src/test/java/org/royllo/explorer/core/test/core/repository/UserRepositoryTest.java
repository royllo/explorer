package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;

@SpringBootTest
@DisplayName("UserRepository tests")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find anonymous user")
    public void findAnonymousUser() {
        // TODO Review this test
        // Finding the user by its id.
        final Optional<User> anonymousUserById = userRepository.findById(ANONYMOUS_ID);
        assertTrue(anonymousUserById.isPresent());
        assertEquals(ANONYMOUS_ID, anonymousUserById.get().getId().longValue());
        assertEquals(ANONYMOUS_USER_ID, anonymousUserById.get().getUserId());
        assertEquals("anonymous", anonymousUserById.get().getUsername());

        // Finding the user by its uid.
        final Optional<User> anonymousUserByUserId = userRepository.findByUserId(ANONYMOUS_USER_ID);
        assertTrue(anonymousUserByUserId.isPresent());
        assertEquals(ANONYMOUS_ID, anonymousUserByUserId.get().getId().longValue());
        assertEquals(ANONYMOUS_USER_ID, anonymousUserByUserId.get().getUserId());
        assertEquals("anonymous", anonymousUserByUserId.get().getUsername());

        // Finding the user by its username.
        final Optional<User> anonymousUserByUsername = userRepository.findByUsernameIgnoreCase(ANONYMOUS_USER_USERNAME);
        assertTrue(anonymousUserByUsername.isPresent());
        assertEquals(ANONYMOUS_ID, anonymousUserByUsername.get().getId().longValue());
        assertEquals(ANONYMOUS_USER_ID, anonymousUserByUsername.get().getUserId());
        assertEquals("anonymous", anonymousUserByUsername.get().getUsername());
    }

    @Test
    @DisplayName("Find non existing user")
    public void findNonExistingUser() {
        // TODO Review this test
        assertFalse(userRepository.findById(99999999999L).isPresent());
        assertFalse(userRepository.findByUsernameIgnoreCase("NON_EXISTING_USER").isPresent());
    }

    @Test
    @DisplayName("Find existing user")
    public void findExistingUser() {
        // TODO Review this test
        final Optional<User> existingUser = userRepository.findByUsernameIgnoreCase("straumat");
        assertTrue(existingUser.isPresent());
        assertEquals(2, existingUser.get().getId().longValue());
        assertEquals("straumat", existingUser.get().getUsername());
    }

}
