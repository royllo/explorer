package org.royllo.explorer.core.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.util.enums.UserRole;

import static lombok.AccessLevel.PRIVATE;

/**
 * Application user.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class UserDTO {

    /** Unique identifier. */
    Long id;

    /** User UUID. */
    String userId;

    /** Username. */
    String username;

    /** User role. */
    UserRole role;

}
