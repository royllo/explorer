package org.royllo.explorer.core.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Validator for a transaction output.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TransactionOutputValidator.class)
@Documented
@SuppressWarnings({"checkstyle:WhitespaceAround"})
public @interface TransactionOutput {

    /**
     * Message.
     *
     * @return message
     */
    String message();

    /**
     * Group of rates.
     *
     * @return rates
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};

}
