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
    public static final long ANONYMOUS_ID = 0L;

    /** Anonymous user ID in database. */
    public static final String ANONYMOUS_USER_ID = "00000000-0000-0000-0000-000000000000";

    /** Anonymous user username. */
    public static final String ANONYMOUS_USER_USERNAME = "anonymous";

    /** Anonymous user. */
    public static final UserDTO ANONYMOUS_USER = UserDTO.builder()
            .id(ANONYMOUS_ID)
            .userId(ANONYMOUS_USER_ID)
            .username(ANONYMOUS_USER_USERNAME)
            .build();

}
