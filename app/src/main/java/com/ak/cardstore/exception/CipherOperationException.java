package com.ak.cardstore.exception;

/**
 * This exception is thrown when cipher operation (encryption/decryption of data) fails
 *
 * @author Abhishek
 */

public class CipherOperationException extends RuntimeException {

    public CipherOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
