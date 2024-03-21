package org.royllo.explorer.core.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator for a page size for services (minimum is 1).
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = PageSizeValidator.class)
public @interface PageSize {

    String message() default "{validation.page.pageSize}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
