package com.ak.cardstore.cipher.asymmetric;

import com.ak.cardstore.cipher.KeyStoreRetriever;
import com.ak.cardstore.exception.AsymmetricKeyPairRetrievalException;
import com.ak.cardstore.exception.PrivateKeyRetrievalException;
import com.ak.cardstore.exception.PublicKeyRetrievalException;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to retrieve the asymmetric {@link PrivateKey} and {@link PublicKey} pair from the key store.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class AsymmetricKeyPairRetriever {

    private static final String KEY_STORE_TYPE = "AndroidKeyStore";

    private static final String KEY_STORE_NOT_INITIALIZED_ERROR = "Key store %s not initialized";
    private static final String PRIVATE_KEY_RETRIEVAL_ERROR = "Error retrieving private key with alias %s";
    private static final String KEY_PAIR_GENERATION_PROVIDER_ERROR = "Error generating a new key pair using provider %s";

    private final KeyStoreRetriever keyStoreRetriever;
    private final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator;

    /**
     * Retrieves and returns the asymmetric {@link PrivateKey} for the decryption.
     * <p>
     * Generates a new key if one is not already present.
     *
     * @param keyAlias the alias for the private key
     * @return asymmetric {@link PrivateKey} for the decryption
     */
    public Key retrievePrivateKey(@NonNull final String keyAlias) {
        final KeyStore androidKeyStore = this.keyStoreRetriever.retrieve(KEY_STORE_TYPE);

        final Optional<Key> optionalPrivateKey = this.retrievePrivateKey(androidKeyStore, keyAlias);
        if (optionalPrivateKey.isPresent()) {
            return optionalPrivateKey.get();
        }

        final KeyPair keyPair = this.generateAsymmetricKeyPair(keyAlias);
        return keyPair.getPrivate();
    }

    /**
     * Retrieves and returns the asymmetric {@link PublicKey} for the encryption.
     * <p>
     * Generates a new key if one is not already present.
     *
     * @param keyAlias the alias for the public key
     * @return asymmetric {@link PublicKey} for the encryption
     */
    public Key retrievePublicKey(final String keyAlias) {
        final KeyStore androidKeyStore = this.keyStoreRetriever.retrieve(KEY_STORE_TYPE);

        final Optional<Key> optionalPublicKey = this.retrievePublicKey(androidKeyStore, keyAlias);
        if (optionalPublicKey.isPresent()) {
            return optionalPublicKey.get();
        }

        final KeyPair keyPair = this.generateAsymmetricKeyPair(keyAlias);
        return keyPair.getPublic();
    }

    private Optional<Key> retrievePublicKey(final KeyStore androidKeyStore, final String keyAlias) {
        PublicKey publicKey = null;
        try {
            final boolean keyStoreContainsAlias = androidKeyStore.containsAlias(keyAlias);
            if (keyStoreContainsAlias) {
                final Certificate certificate = androidKeyStore.getCertificate(keyAlias);
                publicKey = certificate.getPublicKey();
            }
        } catch (final KeyStoreException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_STORE_NOT_INITIALIZED_ERROR, KEY_STORE_TYPE);
            throw new PublicKeyRetrievalException(errorMessage, e);
        }

        return Optional.ofNullable(publicKey);
    }

    private Optional<Key> retrievePrivateKey(final KeyStore androidKeyStore, final String keyAlias) {
        Key privateKey = null;
        try {
            final boolean keyStoreContainsAlias = androidKeyStore.containsAlias(keyAlias);
            if (keyStoreContainsAlias) {
                privateKey = androidKeyStore.getKey(keyAlias, null);
            }
        } catch (final KeyStoreException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_STORE_NOT_INITIALIZED_ERROR, KEY_STORE_TYPE);
            throw new PrivateKeyRetrievalException(errorMessage, e);
        } catch (final NoSuchAlgorithmException | UnrecoverableKeyException e) {
            final String errorMessage = logError(log, Optional.of(e), PRIVATE_KEY_RETRIEVAL_ERROR, keyAlias);
            throw new PrivateKeyRetrievalException(errorMessage, e);
        }

        return Optional.ofNullable(privateKey);
    }

    private KeyPair generateAsymmetricKeyPair(final String keyAlias) {
        final KeyPair keyPair;
        try {
            keyPair = this.asymmetricKeyPairGenerator.generate(KEY_STORE_TYPE, keyAlias);
        } catch (final NoSuchProviderException e) {
            final String errorMessage = logError(log, Optional.of(e), KEY_PAIR_GENERATION_PROVIDER_ERROR, KEY_STORE_TYPE);
            throw new AsymmetricKeyPairRetrievalException(errorMessage, e);
        }

        return keyPair;
    }
}
