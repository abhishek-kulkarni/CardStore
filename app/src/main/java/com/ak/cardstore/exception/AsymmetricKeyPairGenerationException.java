package com.ak.cardstore.exception;

/**
 * This exception is thrown when asymmetric key pair generation fails
 *
 * @author Abhishek
 */

public class AsymmetricKeyPairGenerationException extends RuntimeException {

    public AsymmetricKeyPairGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
