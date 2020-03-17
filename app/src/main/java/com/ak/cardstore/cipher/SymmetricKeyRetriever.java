package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.SymmetricKeyRetrievalException;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to retrieve the {@link javax.crypto.SecretKey} from the key store.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class SymmetricKeyRetriever {

    private static final String KEY_STORE_TYPE = "AndroidKeyStore";

    private static final String INVALID_KEY_STORE_ERROR = "Invalid key store %s";
    private static final String KEY_STORE_LOAD_ERROR = "Error loading key store %s";
    private static final String KEY_STORE_NOT_INITIALIZED_ERROR = "Key store %s not initialized";
    private static final String KEY_RETRIEVAL_ERROR = "Error retrieving key with alias %s";
    private static final String KEY_GENERATION_PROVIDER_ERROR = "Error generating a new key using provider %s";

    private final SymmetricKeyGenerator symmetricKeyGenerator;

    /**
     * Retrieves and returns the Symmetric {@link Key} for the encryption/decryption.
     * <p>
     * Generates a new key if one if not already present.
     *
     * @param keyAlias the alias name
     * @param password the password for recovering the key
     * @return Symmetric {@link Key} for the encryption/decryption
     * @throws UnrecoverableKeyException if the key cannot be recovered (e.g., the given password is wrong).
     */
    public Key retrieve(@NonNull final String keyAlias, final String password)
            throws UnrecoverableKeyException {
        final KeyStore androidKeyStore = this.loadAndroidKeyStore();

        final Key symmetricKey;
        try {
            final boolean keyStoreContainsAlias = androidKeyStore.containsAlias(keyAlias);
            if (keyStoreContainsAlias) {
                symmetricKey = androidKeyStore.getKey(keyAlias, password.toCharArray());
            } else {
                symmetricKey = this.symmetricKeyGenerator.generate(KEY_STORE_TYPE, keyAlias, password);
                androidKeyStore.setKeyEntry(keyAlias, symmetricKey, password.toCharArray(), null);
            }
        } catch (final KeyStoreException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_STORE_NOT_INITIALIZED_ERROR, KEY_STORE_TYPE);
            throw new SymmetricKeyRetrievalException(errorMessage, e);
        } catch (final NoSuchAlgorithmException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_RETRIEVAL_ERROR, keyAlias);
            throw new SymmetricKeyRetrievalException(errorMessage, e);
        } catch (final NoSuchProviderException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_GENERATION_PROVIDER_ERROR, KEY_STORE_TYPE);
            throw new SymmetricKeyRetrievalException(errorMessage, e);
        }

        return symmetricKey;
    }

    private KeyStore loadAndroidKeyStore() {
        final KeyStore androidKeyStore;
        try {
            androidKeyStore = KeyStore.getInstance(KEY_STORE_TYPE);
        } catch (final KeyStoreException e) {
            final String errorMessage = logError(log, Optional.of(e), INVALID_KEY_STORE_ERROR, KEY_STORE_TYPE);
            throw new SymmetricKeyRetrievalException(errorMessage, e);
        }

        try {
            androidKeyStore.load(null);
        } catch (final CertificateException | IOException | NoSuchAlgorithmException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_STORE_LOAD_ERROR, KEY_STORE_TYPE);
            throw new SymmetricKeyRetrievalException(errorMessage, e);
        }

        return androidKeyStore;
    }
}
