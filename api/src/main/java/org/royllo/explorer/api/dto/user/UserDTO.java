package org.royllo.explorer.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.api.util.enums.UserRole;

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

    /** Username. */
    String username;

    /** User role. */
    UserRole role;

}
