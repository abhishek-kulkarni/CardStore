package com.ak.cardstore.exception;

/**
 * This exception is thrown when private key retrieval fails
 *
 * @author Abhishek
 */

public class PrivateKeyRetrievalException extends RuntimeException {

    public PrivateKeyRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
