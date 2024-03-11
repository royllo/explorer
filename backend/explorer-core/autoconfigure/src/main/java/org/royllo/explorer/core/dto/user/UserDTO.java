package org.royllo.explorer.core.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
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

    /** Full name maximum size. */
    private static final int FULL_NAME_MAXIMUM_SIZE = 40;

    /** Profile picture file name maximum size. */
    private static final int PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE = 255;

    /** Biography maximum size. */
    private static final int BIOGRAPHY_MAXIMUM_SIZE = 255;

    /** Website maximum size. */
    private static final int WEBSITE_MAXIMUM_SIZE = 50;

    /** Username maximum size - If longer thant this, the username must be shortened. */
    public static final int USERNAME_MAXIMUM_SIZE = 20;

    /** Username preview size - The size of the preview on both ends. */
    public static final int USERNAME_PREVIEW_SIZE = 3;

    /** Unique identifier. */
    Long id;

    /** User UUID. */
    @NotNull(message = "{validation.user.userId.required}")
    String userId;

    /** Username. */
    @Setter
    @NonFinal
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "{validation.user.username.invalid}")
    String username;

    /** User role. */
    @NotNull(message = "{validation.user.role.required}")
    UserRole role;

    /** Profile picture file name. */
    @Setter
    @NonFinal
    @Size(max = PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE, message = "{validation.user.profilePictureFilename.size.too_long}")
    String profilePictureFileName;

    /** Full name. */
    @Setter
    @NonFinal
    @Size(max = FULL_NAME_MAXIMUM_SIZE, message = "{validation.user.fullName.size.too_long}")
    String fullName;

    /** Biography. */
    @Setter
    @NonFinal
    @Size(max = BIOGRAPHY_MAXIMUM_SIZE, message = "{validation.user.biography.size.too_long}")
    String biography;

    /** Website. */
    @Setter
    @NonFinal
    @Size(max = WEBSITE_MAXIMUM_SIZE, message = "{validation.user.website.size.too_long}")
    String website;

    /**
     * Returns the shortened username.
     *
     * @return the shortened username
     */
    public String getShortenedUsername() {
        // If username is too long, cut it.
        if (username != null && username.length() >= USERNAME_MAXIMUM_SIZE) {
            return username.substring(0, USERNAME_PREVIEW_SIZE)
                    + "..."
                    + username.substring(username.length() - USERNAME_PREVIEW_SIZE);
        }
        return username;
    }

    /**
     * Returns true if the user has settings set.
     *
     * @return true if the user has settings set
     */
    public boolean hasSettingsSet() {
        return fullName != null || biography != null || website != null;
    }

}
