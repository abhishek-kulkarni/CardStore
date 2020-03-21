package com.ak.cardstore.exception;

/**
 * This exception is thrown when IO fails for {@link com.ak.cardstore.dao.SharedPreferencesDataAccessor}
 *
 * @author Abhishek
 */

public class SharedPreferencesIOException extends RuntimeException {

    public SharedPreferencesIOException(final String message) {
        super(message);
    }
}
