package com.ak.cardstore.exception;

/**
 * This exception is thrown when user data validation fails
 *
 * @author Abhishek
 */

public class UserValidationException extends RuntimeException {

    public UserValidationException(final String message) {
        super(message);
    }
}
