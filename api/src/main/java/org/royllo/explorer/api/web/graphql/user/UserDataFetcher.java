package org.royllo.explorer.api.web.graphql.user;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.dto.user.UserDTO;
import org.royllo.explorer.api.service.user.UserService;

/**
 * User data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

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
        return userService.getUserByUsername(username).orElseThrow(DgsEntityNotFoundException::new);
    }

}
