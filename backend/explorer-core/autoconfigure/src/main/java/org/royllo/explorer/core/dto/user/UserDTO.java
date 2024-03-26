package org.royllo.explorer.core.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;
import org.royllo.explorer.core.util.enums.UserRole;
import org.royllo.explorer.core.util.validator.Username;

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
    public static final int FULL_NAME_MAXIMUM_SIZE = 40;

    /** Profile picture file name maximum size. */
    public static final int PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE = 255;

    /** Biography maximum size. */
    public static final int BIOGRAPHY_MAXIMUM_SIZE = 255;

    /** Website maximum size. */
    public static final int WEBSITE_MAXIMUM_SIZE = 50;

    /** Username maximum size. */
    public static final int USERNAME_MAXIMUM_SIZE = 20;

    /** Username preview size - The size of the preview on both ends. */
    public static final int USERNAME_PREVIEW_SIZE = 3;

    /** Unique identifier. */
    Long id;

    /** User UUID. */
    @NotNull(message = "{validation.user.userId.required}")
    String userId;

    /** User role. */
    @NotNull(message = "{validation.user.role.required}")
    UserRole role;

    /** Username. */
    @Setter
    @NonFinal
    @Username
    String username;

    /** Profile picture file name. */
    @Setter
    @NonFinal
    @Size(max = PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE, message = "{validation.user.profilePictureFilename.length.too_long}")
    String profilePictureFileName;

    /** Full name. */
    @Setter
    @NonFinal
    @Size(max = FULL_NAME_MAXIMUM_SIZE, message = "{validation.user.fullName.length.too_long}")
    String fullName;

    /** Biography. */
    @Setter
    @NonFinal
    @Size(max = BIOGRAPHY_MAXIMUM_SIZE, message = "{validation.user.biography.length.too_long}")
    String biography;

    /** Website. */
    @Setter
    @NonFinal
    @Size(max = WEBSITE_MAXIMUM_SIZE, message = "{validation.user.website.length.too_long}")
    String website;

    /**
     * Returns the shortened username.
     *
     * @return the shortened username
     */
    public String getShortenedUsername() {
        // If username is too long, make a short version.
        if (StringUtils.length(username) >= USERNAME_MAXIMUM_SIZE) {
            return username.substring(0, USERNAME_PREVIEW_SIZE)
                    + "..."
                    + username.substring(username.length() - USERNAME_PREVIEW_SIZE);
        }
        return username;
    }

    /**
     * Returns the current settings.
     *
     * @return the current settings
     */
    public UserDTOSettings getCurrentSettings() {
        return UserDTOSettings.builder()
                .username(username)
                .profilePictureFileName(profilePictureFileName)
                .fullName(fullName)
                .biography(biography)
                .website(website)
                .build();
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
