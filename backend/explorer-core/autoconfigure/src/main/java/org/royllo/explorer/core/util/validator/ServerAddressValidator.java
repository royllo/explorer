package org.royllo.explorer.core.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Server address validator.
 */
public class ServerAddressValidator implements ConstraintValidator<ServerAddress, String> {

    /** Regular expression pattern for hostname with port. */
    private static final Pattern HOSTNAME_OR_IP_PATTERN = Pattern.compile(
            "^(http(s)?://)?((([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})|((\\d{1,3}\\.){3}\\d{1,3}))(\\:\\d{1,5})?$"
    );

    @Override
    public final boolean isValid(final String serverAddress,
                                 final ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(serverAddress)) {
            return false; // Empty server address is considered invalid.
        }
        return HOSTNAME_OR_IP_PATTERN.matcher(serverAddress).matches();
    }

}
