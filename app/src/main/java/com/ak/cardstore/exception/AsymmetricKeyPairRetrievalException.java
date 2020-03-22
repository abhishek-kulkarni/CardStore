package com.ak.cardstore.exception;

/**
 * This exception is thrown when asymmetric key pair retrieval fails
 *
 * @author Abhishek
 */

public class AsymmetricKeyPairRetrievalException extends RuntimeException {

    public AsymmetricKeyPairRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
