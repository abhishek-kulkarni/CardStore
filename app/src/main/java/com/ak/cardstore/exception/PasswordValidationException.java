package com.ak.cardstore.exception;

/**
 * This exception is thrown when password validation fails
 *
 * @author Abhishek
 */

public class PasswordValidationException extends RuntimeException {

    public PasswordValidationException(final String message) {
        super(message);
    }
}
