package org.royllo.explorer.core.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Server address validator.
 */
public class ServerAddressValidator implements ConstraintValidator<ServerAddress, String> {

    /**
     * Regular expression pattern for hostname with port.
     */
    public static final Pattern HOSTNAME_PATTERN = Pattern.compile(
            "^([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(?::\\d+)?$"
    );

    /**
     * Regular expression pattern for IP address with port.
     */
    public static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}(?::\\d+)?$"
    );

    @Override
    public final boolean isValid(final String serverAddress,
                                 final ConstraintValidatorContext constraintValidatorContext) {
        if (serverAddress == null || serverAddress.isEmpty()) {
            return false; // Empty server address is considered invalid
        }
        return HOSTNAME_PATTERN.matcher(serverAddress).matches()
                || IP_ADDRESS_PATTERN.matcher(serverAddress).matches();
    }

}
