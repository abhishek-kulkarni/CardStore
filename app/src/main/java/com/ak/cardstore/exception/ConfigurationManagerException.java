package com.ak.cardstore.exception;

/**
 * This exception is thrown when configuration manager fails
 *
 * @author Abhishek
 */

public class ConfigurationManagerException extends RuntimeException {

    public ConfigurationManagerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
