package com.ak.cardstore.exception;

/**
 * This exception is thrown when encryption of data fails
 *
 * @author Abhishek
 */

public class DataEncryptionException extends RuntimeException {

    public DataEncryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
