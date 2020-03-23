package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.CipherRetrievalException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to retrieve the {@link javax.crypto.Cipher}.
 *
 * @author Abhishek
 */

@Log4j2
public class CipherRetriever {

    private static final String CIPHER_TRANSFORMATION_ERROR = "Failed to retrieve cipher for transformation %s";
    private static final String INVALID_KEY_ERROR = "Failed to retrieve cipher due to invalid key";
    private static final String INVALID_INITIAL_VECTOR_ERROR = "Failed to retrieve cipher due to invalid initial vector";

    /**
     * Retrieves the {@link Cipher}.
     *
     * @param cipherTransformation  cipher transformation
     * @param opMode                operation mode of this cipher (one of the <code>ENCRYPT_MODE</code>, <code>DECRYPT_MODE</code>,
     *                              <code>WRAP_MODE</code> or <code>UNWRAP_MODE</code>)
     * @param key                   key to be used with cipher
     * @param optionalInitialVector optional initial vector
     * @return {@link Cipher}
     */
    public Cipher retrieve(final String cipherTransformation, final int opMode, final Key key, final Optional<byte[]> optionalInitialVector) {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(cipherTransformation);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
            final String errorMessage = logError(log, Optional.of(e), CIPHER_TRANSFORMATION_ERROR, cipherTransformation);
            throw new CipherRetrievalException(errorMessage, e);
        }

        try {
            if (optionalInitialVector.isPresent()) {
                cipher.init(opMode, key, new IvParameterSpec(optionalInitialVector.get()));
            } else {
                cipher.init(opMode, key);
            }
        } catch (final InvalidKeyException e) {
            log.error(INVALID_KEY_ERROR);
            throw new CipherRetrievalException(INVALID_KEY_ERROR, e);
        } catch (final InvalidAlgorithmParameterException e) {
            log.error(INVALID_INITIAL_VECTOR_ERROR, e);
            throw new CipherRetrievalException(INVALID_INITIAL_VECTOR_ERROR, e);
        }

        return cipher;
    }
}
