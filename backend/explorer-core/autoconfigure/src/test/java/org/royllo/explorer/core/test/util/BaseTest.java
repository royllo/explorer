package org.royllo.explorer.core.test.util;

import jakarta.validation.ConstraintViolationException;
import org.royllo.explorer.core.util.base.Base;

/**
 * Base test class.
 */
public class BaseTest extends Base {

    /**
     * Check if the property path is in the constraint violations.
     *
     * @param e            ConstraintViolationException
     * @param propertyPath Property path
     * @return true if found
     */
    protected boolean isPropertyInConstraintViolations(ConstraintViolationException e, String propertyPath) {
        return e.getConstraintViolations()
                .stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().contains(propertyPath));

    }
}
