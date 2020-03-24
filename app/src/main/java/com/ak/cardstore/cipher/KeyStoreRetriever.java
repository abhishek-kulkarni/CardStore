package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.KeyStoreRetrievalException;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Optional;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to retrieve the {@link java.security.KeyStore}.
 *
 * @author Abhishek
 */

public class KeyStoreRetriever {

    private static final String LOG_TAG = KeyStoreRetriever.class.getSimpleName();

    private static final String INVALID_KEY_STORE_ERROR = "Invalid key store %s";
    private static final String KEY_STORE_LOAD_ERROR = "Error loading key store %s";

    /**
     * Retrieves the {@link KeyStore}
     *
     * @param keyStoreType type of the key store to retrieve
     * @return {@link KeyStore}
     */
    public KeyStore retrieve(final String keyStoreType) {
        final KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (final KeyStoreException e) {
            final String errorMessage = logError(LOG_TAG, Optional.of(e), INVALID_KEY_STORE_ERROR, keyStoreType);
            throw new KeyStoreRetrievalException(errorMessage, e);
        }

        try {
            keyStore.load(null);
        } catch (final CertificateException | IOException | NoSuchAlgorithmException e) {
            final String errorMessage = logError(LOG_TAG, Optional.of(e), KEY_STORE_LOAD_ERROR, keyStoreType);
            throw new KeyStoreRetrievalException(errorMessage, e);
        }

        return keyStore;
    }
}
