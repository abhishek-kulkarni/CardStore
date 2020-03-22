package com.ak.cardstore.exception;

/**
 * This exception is thrown when public key retrieval fails
 *
 * @author Abhishek
 */

public class PublicKeyRetrievalException extends RuntimeException {

    public PublicKeyRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
