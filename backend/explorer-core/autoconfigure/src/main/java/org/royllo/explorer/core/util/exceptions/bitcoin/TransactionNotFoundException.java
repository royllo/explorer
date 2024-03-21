package org.royllo.explorer.core.util.exceptions.bitcoin;

/**
 * Exception thrown when a Bitcoin transaction is not found.
 */
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Transaction not found exception.
     *
     * @param message exception message
     */
    public TransactionNotFoundException(final String message) {
        super(message);
    }
}

