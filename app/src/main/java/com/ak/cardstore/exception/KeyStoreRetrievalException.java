package com.ak.cardstore.exception;

/**
 * This exception is thrown when key store retrieval fails
 *
 * @author Abhishek
 */

public class KeyStoreRetrievalException extends RuntimeException {

    public KeyStoreRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
