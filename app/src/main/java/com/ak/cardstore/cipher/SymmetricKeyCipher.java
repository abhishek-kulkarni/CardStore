package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.CipherRetrievalException;
import com.ak.cardstore.exception.DataEncryptionException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.cipher.SymmetricKeyGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.util.LoggerUtil.logError;
import static com.ak.cardstore.util.StringUtil.getString;
import static com.ak.cardstore.util.StringUtil.toByteArray;

/**
 * A cipher class to handle the encryption and decryption of the data.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class SymmetricKeyCipher {

    private static final String SYMMETRIC_KEY_ALIAS = "JaiGajanan";
    private static final String CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);

    private static final String CIPHER_TRANSFORMATION_ERROR = "Failed to retrieve cipher for transformation %s";
    private static final String KEY_RETRIEVAL_ERROR = "Error retrieving key";
    private static final String ENCRYPTION_ERROR = "Error encrypting data!";
    private static final String DECRYPTION_ERROR = "Error decrypting data!";
    private static final String INVALID_INITIAL_VECTOR_ERROR = "Failed to retrieve cipher due to invalid initial vector";

    private final SymmetricKeyRetriever symmetricKeyRetriever;

    /**
     * Encrypts and returns the data passed.
     *
     * @param dataToEncrypt data to encrypt
     * @param password      password to use fot encryption
     * @return Pair of <encrypted data, initial vector>
     */
    public ImmutablePair<String, String> encrypt(@NonNull final String dataToEncrypt, @NonNull final String password) {
        final Key symmetricKey = this.retrieveSymmetricKey(password);
        final Cipher cipher = this.loadCipher(Cipher.ENCRYPT_MODE, symmetricKey, Optional.empty());

        final byte[] cipherText;
        try {
            cipherText = cipher.doFinal(toByteArray(dataToEncrypt));
        } catch (final BadPaddingException | IllegalBlockSizeException e) {
            log.error(ENCRYPTION_ERROR, e);
            throw new DataEncryptionException(ENCRYPTION_ERROR, e);
        }

        return ImmutablePair.of(getString(cipherText), getString(cipher.getIV()));
    }

    /**
     * Decrypts the passed data and returns it
     *
     * @param dataToDecrypt data to decrypt
     * @param password      password to use for decryption
     * @param initialVector initial vector to use for decryption
     * @return decrypted data
     */
    public String decrypt(@NonNull final String dataToDecrypt, @NonNull final String password, @NonNull final String initialVector) {
        final Key symmetricKey = this.retrieveSymmetricKey(password);
        final Cipher cipher = this.loadCipher(Cipher.DECRYPT_MODE, symmetricKey, Optional.of(toByteArray(initialVector)));

        final byte[] plainText;
        try {
            plainText = cipher.doFinal(toByteArray(dataToDecrypt));
        } catch (final BadPaddingException | IllegalBlockSizeException e) {
            log.error(DECRYPTION_ERROR, e);
            throw new DataEncryptionException(DECRYPTION_ERROR, e);
        }

        return getString(plainText);
    }

    private Key retrieveSymmetricKey(final String password) {
        final Key symmetricKey;
        try {
            symmetricKey = this.symmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password);
        } catch (final UnrecoverableKeyException e) {
            log.error(KEY_RETRIEVAL_ERROR, e);
            throw new DataEncryptionException(KEY_RETRIEVAL_ERROR, e);
        }

        return symmetricKey;
    }

    private Cipher loadCipher(final int opMode, final Key symmetricKey, final Optional<byte[]> initialVector) {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
            final String errorMessage = logError(log, Optional.of(e), CIPHER_TRANSFORMATION_ERROR, CIPHER_TRANSFORMATION);
            throw new CipherRetrievalException(errorMessage, e);
        }

        try {
            if (initialVector.isPresent() && opMode == Cipher.DECRYPT_MODE) {
                cipher.init(opMode, symmetricKey, new IvParameterSpec(initialVector.get()));
            } else {
                cipher.init(opMode, symmetricKey);
            }
        } catch (final InvalidKeyException e) {
            final String errorMessage = logError(log, Optional.of(e), CIPHER_TRANSFORMATION_ERROR, CIPHER_TRANSFORMATION);
            throw new CipherRetrievalException(errorMessage, e);
        } catch (final InvalidAlgorithmParameterException e) {
            log.error(INVALID_INITIAL_VECTOR_ERROR, e);
            throw new CipherRetrievalException(INVALID_INITIAL_VECTOR_ERROR, e);
        }

        return cipher;
    }
}
