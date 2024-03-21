package org.royllo.explorer.core.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for a page size for services (between 1 and 100).
 */
public class PageSizeValidator implements ConstraintValidator<PageSize, Integer> {

    /** Minimum page size. */
    private static final int MINIMUM_PAGE_SIZE = 1;

    /** Maximum page size. */
    private static final int MAXIMUM_PAGE_SIZE = 100;

    @Override
    public final boolean isValid(final Integer value, final ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value >= MINIMUM_PAGE_SIZE && value <= MAXIMUM_PAGE_SIZE;
    }

}
