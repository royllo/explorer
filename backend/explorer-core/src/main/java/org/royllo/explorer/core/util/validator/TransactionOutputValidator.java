package org.royllo.explorer.core.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static org.royllo.explorer.core.util.constants.BitcoinConstants.TXID_SIZE;

/**
 * Transaction output validator.
 */
public class TransactionOutputValidator implements ConstraintValidator<TransactionOutput, String> {

    @Override
    public final boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
        // First, we check if we have found a valid transaction format.
        // Format is txid:vout where ':' is mandatory, txid is 64 characters and vout is an int superior or equals to 0.
        return value != null                                                                    // Transaction output cannot be null.
                && value.contains(":")                                                          // Transaction output has a ':' to separate tx id and vout.
                && value.substring(0, value.indexOf(":")).length() == TXID_SIZE                      // Transaction id size is 64 characters.
                && value.substring(value.indexOf(":") + 1).length() > 0               // Transaction vout is mandatory.
                && Pattern.matches("[0-9]+[\\.]?[0-9]*", (value.substring(value.indexOf(":") + 1)))     // Transaction vout must be a number
                && Integer.parseInt(value.substring(value.indexOf(":") + 1)) >= 0;    // Transaction vout must be superior to zero.
    }

}
