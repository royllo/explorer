package org.royllo.explorer.core.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator for a page number for services (between 1 and 100).
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = PageNumberValidator.class)
public @interface PageNumber {

    String message() default "{validation.page.firstPage}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
