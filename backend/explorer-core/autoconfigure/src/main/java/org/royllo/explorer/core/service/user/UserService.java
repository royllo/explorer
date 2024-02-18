package org.royllo.explorer.core.service.user;

import jakarta.validation.Valid;
import org.royllo.explorer.core.dto.user.UserDTO;

import java.util.Optional;

/**
 * User service.
 */
public interface UserService {

    /**
     * Get administrator user.
     *
     * @return administrator user
     */
    UserDTO getAdministratorUser();

    /**
     * Get anonymous user.
     *
     * @return anonymous user
     */
    UserDTO getAnonymousUser();

    /**
     * Create user.
     *
     * @param username user name
     * @return user created
     */
    UserDTO createUser(String username);

    /**
     * Update user.
     *
     * @param username user name
     * @param userData user data
     */
    void updateUser(String username, @Valid UserDTO userData);


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
