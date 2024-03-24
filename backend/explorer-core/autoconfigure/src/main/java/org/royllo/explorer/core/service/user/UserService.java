package org.royllo.explorer.core.service.user;

import jakarta.validation.Valid;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.dto.user.UserDTOSettings;
import org.royllo.explorer.core.util.validator.Username;

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
    UserDTO addUser(@Username String username);

    /**
     * Update user.
     *
     * @param userId       user id
     * @param userSettings user settings
     */
    void updateUser(String userId, @Valid UserDTOSettings userSettings);

    /**
     * Get user by its user id.
     *
     * @param userId user id to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUserId(String userId);

    /**
     * Get user by its username.
     *
     * @param username username to find
     * @return corresponding user
     */
    Optional<UserDTO> getUserByUsername(String username);

}
