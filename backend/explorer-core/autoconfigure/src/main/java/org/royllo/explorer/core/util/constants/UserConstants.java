package org.royllo.explorer.core.util.constants;

import lombok.experimental.UtilityClass;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.user.UserDTO;

/**
 * User constants.
 */
@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class UserConstants {

    /** Anonymous user ID in database. */
    public static final long ANONYMOUS_ID = 1L;

    /** Anonymous user USER_ID in database. */
    public static final String ANONYMOUS_USER_ID = "00000000-0000-0000-0000-000000000000";

    /** Anonymous user username. */
    public static final String ANONYMOUS_USER_USERNAME = "anonymous";

    /** Anonymous user. */
    public static final User ANONYMOUS_USER = User.builder()
            .id(ANONYMOUS_ID)
            .userId(ANONYMOUS_USER_ID)
            .username(ANONYMOUS_USER_USERNAME)
            .build();

    /** Anonymous user DTO. */
    public static final UserDTO ANONYMOUS_USER_DTO = UserDTO.builder()
            .id(ANONYMOUS_ID)
            .userId(ANONYMOUS_USER_ID)
            .username(ANONYMOUS_USER_USERNAME)
            .build();

}
