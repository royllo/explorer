package org.royllo.explorer.api.service.user;

import org.royllo.explorer.api.dto.user.UserDTO;

import java.util.Optional;

/**
 * User service.
 */
public interface UserService {

    /**
     * Get anonymous user.
     *
     * @return anonymous user
     */
    UserDTO getAnonymousUser();

    /**
     * Get user by username.
     *
     * @param username exact username to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUsername(String username);

}
