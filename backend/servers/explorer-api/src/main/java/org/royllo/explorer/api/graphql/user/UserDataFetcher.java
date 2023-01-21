package org.royllo.explorer.api.graphql.user;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.service.user.UserService;

import java.util.Optional;

/**
 * User data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher extends BaseDataFetcher {

    /** User service. */
    private final UserService userService;

    /**
     * Get user by username.
     *
     * @param username username
     * @return user if found or null
     */
    @DgsQuery
    public final UserDTO userByUsername(final @InputArgument String username) {
        final Optional<UserDTO> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            logger.info("userByUsername - User with username {} not found", username);
            throw new DgsEntityNotFoundException();
        } else {
            logger.info("userByUsername - User with username '{}' found: {}", username, user.get());
            return user.get();
        }
    }

}
