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

    /** Username maximum size - If longer thant this, the username must be shortened. */
    public static final int USERNAME_MAXIMUM_SIZE = 20;

    /** Username preview size - The size of the preview on both ends. */
    public static final int USERNAME_PREVIEW_SIZE = 3;

    /** Unique identifier. */
    Long id;

    /** User UUID. */
    @NotNull(message = "User ID is required")
    String userId;

    /** Username. */
    @NotBlank(message = "Username is mandatory")
    String username;

    /** User role. */
    @NotNull(message = "Role is mandatory")
    UserRole role;

    /**
     * Returns the shortened username.
     *
     * @return the shortened username
     */
    public String getShortenedUsername() {
        // If username is null, return null (it should never append).
        if (username == null) {
            return null;
        }
        // If username proof is too small for substring, return raw proof.
        if (username.length() <= USERNAME_MAXIMUM_SIZE) {
            return username;
        }
        return username.substring(0, USERNAME_PREVIEW_SIZE) + "..." + username.substring(username.length() - USERNAME_PREVIEW_SIZE);
    }

}
