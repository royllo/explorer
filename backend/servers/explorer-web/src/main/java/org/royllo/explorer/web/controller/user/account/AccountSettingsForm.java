package org.royllo.explorer.web.controller.user.account;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


/**
 * Account settings form.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AccountSettingsForm {

    /** Profile picture file name maximum size. */
    private static final int PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE = 255;

    /** Full name maximum size. */
    private static final int FULL_NAME_MAXIMUM_SIZE = 40;

    /** Biography maximum size. */
    private static final int BIOGRAPHY_MAXIMUM_SIZE = 255;

    /** Website maximum size. */
    private static final int WEBSITE_MAXIMUM_SIZE = 50;

    /** Username. */
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "{validation.user.username.invalid}")
    String username;

    /** Profile picture. */
    MultipartFile profilePicture;

    /** Profile picture filename. */
    @Size(max = PROFILE_PICTURE_FILE_NAME_MAXIMUM_SIZE, message = "{validation.user.profilePictureFilename.length.too_long}")
    String profilePictureFilename;

    /** Full name. */
    @Size(max = FULL_NAME_MAXIMUM_SIZE, message = "{validation.user.fullName.length.too_long}")
    String fullName;

    /** Biography. */
    @Size(max = BIOGRAPHY_MAXIMUM_SIZE, message = "{validation.user.biography.length.too_long}")
    String biography;

    /** Website. */
    @Pattern(regexp = "^(?:(https?://)?(www\\.)?[-a-zA-Z0-9%_]+(\\.[a-zA-Z]{2,})/?.*)?$", message = "{validation.user.website.invalid}")
    @Size(max = WEBSITE_MAXIMUM_SIZE, message = "{validation.user.website.length.too_long}")
    String website;

}
