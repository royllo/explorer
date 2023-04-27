package org.royllo.explorer.core.service.user;

import org.royllo.explorer.core.dto.user.UserDTO;

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
     * Get user by its user id.
     *
     * @param userId exact user id to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUserId(String userId);

    /**
     * Get user by username.
     *
     * @param username exact username to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUsername(String username);

}
