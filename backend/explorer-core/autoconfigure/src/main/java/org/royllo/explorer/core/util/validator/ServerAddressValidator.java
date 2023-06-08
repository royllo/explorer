package org.royllo.explorer.core.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Server address validator.
 */
public class ServerAddressValidator implements ConstraintValidator<ServerAddress, String> {

    /**
     * Server address pattern used to validate.
     */
    private static final Pattern SERVER_ADDRESS_PATTERN = Pattern.compile(
            "^((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)):(\\d{1,5})$"
    );

    @Override
    public final boolean isValid(final String serverAddress,
                                 final ConstraintValidatorContext constraintValidatorContext) {
        if (serverAddress == null || serverAddress.isEmpty()) {
            return false; // Empty server address is considered invalid
        }
        return SERVER_ADDRESS_PATTERN.matcher(serverAddress).matches();
    }

}
