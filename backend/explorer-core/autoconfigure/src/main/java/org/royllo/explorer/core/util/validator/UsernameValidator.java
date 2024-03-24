package org.royllo.explorer.core.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Validator for username.
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

    /** Regular expression pattern for username. */
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_]{3,20}$"
    );

    @Override
    public final boolean isValid(final String username, final ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(username)) {
            return false; // Empty username is considered invalid.
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

}
