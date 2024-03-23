package org.royllo.explorer.core.service.user;

import jakarta.validation.Valid;
import org.royllo.explorer.core.dto.user.UserDTO;

import java.util.Optional;

/**
 * User service.
 */
public interface UserService {

    /**
     * Add a user.
     *
     * @param username user name
     * @return user created
     */
    UserDTO addUser(String username);

    /**
     * Update user.
     * TODO UserId should be used instead of username
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
     * TODO Is this method useful? I don't think so!
     *
     * @param username exact username to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUsername(String username);

}
