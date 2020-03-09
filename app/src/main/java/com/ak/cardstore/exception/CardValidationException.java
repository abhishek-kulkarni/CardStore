package com.ak.cardstore.exception;

/**
 * This exception is thrown when card data validation fails
 *
 * @author Abhishek
 */

public class CardValidationException extends RuntimeException {

    public CardValidationException(final String message) {
        super(message);
    }
}
