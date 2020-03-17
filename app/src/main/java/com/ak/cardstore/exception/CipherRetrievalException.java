package com.ak.cardstore.exception;

/**
 * This exception is thrown when cipher retrieval fails
 *
 * @author Abhishek
 */

public class CipherRetrievalException extends RuntimeException {

    public CipherRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
