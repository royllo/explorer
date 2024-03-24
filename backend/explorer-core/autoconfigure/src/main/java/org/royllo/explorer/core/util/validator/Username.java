package org.royllo.explorer.core.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator for username.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
@SuppressWarnings({"checkstyle:WhitespaceAround"})
public @interface Username {

    String message() default "{validation.user.username.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
