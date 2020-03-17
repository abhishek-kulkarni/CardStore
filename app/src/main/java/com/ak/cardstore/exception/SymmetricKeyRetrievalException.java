package com.ak.cardstore.exception;

/**
 * This exception is thrown when secret key retrieval fails
 *
 * @author Abhishek
 */

public class SymmetricKeyRetrievalException extends RuntimeException {

    public SymmetricKeyRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
