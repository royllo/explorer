package org.royllo.explorer.core.util.constants;

import org.royllo.explorer.core.dto.user.UserDTO;

/**
 * User constants.
 */
public final class UserConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private UserConstants() {
    }

    /** Anonymous user ID in database. */
    public static final Long ANONYMOUS_USER_ID = 0L;

    /** Anonymous user username. */
    public static final String ANONYMOUS_USER_USERNAME = "anonymous";

    /** Anonymous user. */
    public static final UserDTO ANONYMOUS_USER = UserDTO.builder()
            .id(ANONYMOUS_USER_ID)
            .username(ANONYMOUS_USER_USERNAME)
            .build();

}
