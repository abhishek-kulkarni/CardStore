package com.ak.cardstore.exception;

/**
 * This exception is thrown when secret key generation fails
 *
 * @author Abhishek
 */

public class SymmetricKeyGenerationException extends RuntimeException {

    public SymmetricKeyGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
