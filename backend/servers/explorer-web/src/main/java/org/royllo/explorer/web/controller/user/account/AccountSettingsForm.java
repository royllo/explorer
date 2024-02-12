package org.royllo.explorer.web.controller.user.account;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Account settings form.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AccountSettingsForm {

    /** Full name maximum size. */
    private static final int FULL_NAME_MAXIMUM_SIZE = 40;

    /** Biography maximum size. */
    private static final int BIOGRAPHY_MAXIMUM_SIZE = 255;

    /** Website maximum size. */
    private static final int WEBSITE_MAXIMUM_SIZE = 50;

    /** Full name. */
    @Size(max = FULL_NAME_MAXIMUM_SIZE, message = "{validation.user.fullName.size.too_long}")
    String fullName;

    /** Biography. */
    @Size(max = BIOGRAPHY_MAXIMUM_SIZE, message = "{validation.user.biography.size.too_long}")
    String biography;

    /** Website. */
    @Pattern(regexp = "^(?:(https?://)?(www\\.)?[-a-zA-Z0-9%_]+(\\.[a-zA-Z]{2,})/?.*)?$", message = "{validation.user.website.invalid}")
    @Size(max = WEBSITE_MAXIMUM_SIZE, message = "{validation.user.website.size.too_long}")
    String website;

}
