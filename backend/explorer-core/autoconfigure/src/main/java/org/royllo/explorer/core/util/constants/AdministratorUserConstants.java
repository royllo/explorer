package org.royllo.explorer.core.util.constants;

import lombok.experimental.UtilityClass;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.user.UserDTO;

import static org.royllo.explorer.core.util.enums.UserRole.ADMINISTRATOR;

/**
 * Administrator user constants.
 */
@UtilityClass
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "unused"})
public class AdministratorUserConstants {

    /** Administrator user ID in database. */
    public static final long ADMINISTRATOR_ID = 0L;

    /** Administrator user USER_ID in database. */
    public static final String ADMINISTRATOR_USER_ID = "00000000-0000-0000-0000-000000000000";

    /** Administrator user username. */
    public static final String ADMINISTRATOR_USER_USERNAME = "administrator";

    /** Administrator user. */
    public static final User ADMINISTRATOR_USER = User.builder()
            .id(ADMINISTRATOR_ID)
            .userId(ADMINISTRATOR_USER_ID)
            .username(ADMINISTRATOR_USER_USERNAME)
            .role(ADMINISTRATOR)
            .build();

    /** Administrator user DTO. */
    public static final UserDTO ADMINISTRATOR_USER_DTO = UserDTO.builder()
            .id(ADMINISTRATOR_ID)
            .userId(ADMINISTRATOR_USER_ID)
            .username(ADMINISTRATOR_USER_USERNAME)
            .role(ADMINISTRATOR)
            .build();

}
