package org.royllo.explorer.core.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.royllo.explorer.core.util.validator.Username;

import static org.royllo.explorer.core.dto.user.UserDTO.BIOGRAPHY_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.FULL_NAME_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE;
import static org.royllo.explorer.core.dto.user.UserDTO.WEBSITE_MAXIMUM_SIZE;

/**
 * User settings.
 *
 * @param username               username
 * @param profilePictureFileName profile picture file name
 * @param fullName               full name
 * @param biography              biography
 * @param website                website
 */
@Builder
public record UserDTOSettings(
        @Username
        String username,
        @Size(max = PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE, message = "{validation.user.profilePictureFilename.length.too_long}")
        String profilePictureFileName,
        @Size(max = FULL_NAME_MAXIMUM_SIZE, message = "{validation.user.fullName.length.too_long}")
        String fullName,
        @Size(max = BIOGRAPHY_MAXIMUM_SIZE, message = "{validation.user.biography.length.too_long}")
        String biography,
        @Size(max = WEBSITE_MAXIMUM_SIZE, message = "{validation.user.website.length.too_long}")
        String website) {

    /**
     * Returns a cleaned username corresponding to standards.
     *
     * @return the cleaned username
     */
    public String getCleanedUsername() {
        if (username != null) {
            return username.toLowerCase().trim();
        } else {
            return null;
        }
    }

}
