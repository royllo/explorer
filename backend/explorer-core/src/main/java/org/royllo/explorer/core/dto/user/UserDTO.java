package org.royllo.explorer.core.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "User ID is required")
    String userId;

    /** Username. */
    @NotBlank(message = "Username is mandatory")
    String username;

    /** User role. TODO Allow several roles for one user with ElementCollection */
    @NotNull(message = "Role is mandatory")
    UserRole role;

}
