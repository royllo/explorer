package org.royllo.explorer.core.service.user;

import jakarta.validation.Valid;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.springframework.data.domain.Page;

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
     * Gets assets by user id.
     *
     * @param userId   creator id
     * @param page     page number
     * @param pageSize page size
     * @return assets owned by user
     */
    Page<AssetDTO> getAssetsByUserId(String userId, int page, int pageSize);

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
