package com.ak.cardstore.cipher.symmetric;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.exception.CipherOperationException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.security.Key;
import java.security.UnrecoverableKeyException;
import java.util.Optional;

import javax.crypto.Cipher;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.util.StringUtil.getString;
import static com.ak.cardstore.util.StringUtil.toByteArray;

/**
 * A cipher class to handle the encryption and decryption of the data using the {@link javax.crypto.SecretKey}.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class SymmetricKeyCipher {

    private static final String SYMMETRIC_KEY_CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);
    private static final String SYMMETRIC_KEY_ALIAS = "com.ak.cardstore.sk";

    private static final String KEY_RETRIEVAL_ERROR = "Error retrieving key!";
    private static final String ENCRYPTION_ERROR = "Error encrypting data!";
    private static final String DECRYPTION_ERROR = "Error decrypting data!";

    private final SymmetricKeyRetriever symmetricKeyRetriever;
    private final CipherRetriever cipherRetriever;
    private final CipherOperator cipherOperator;

    /**
     * Encrypts the passed data using {@link javax.crypto.SecretKey} and returns the pair of <encrypted data, initial vector>
     *
     * @param dataToEncrypt data to encrypt
     * @param password      password to use fot encryption
     * @return Pair of <encrypted data, initial vector>
     */
    public ImmutablePair<String, String> encrypt(@NonNull final String dataToEncrypt, @NonNull final String password) {
        final Key symmetricKey = this.retrieveSymmetricKey(password);
        final Cipher cipher = this.cipherRetriever.retrieve(SYMMETRIC_KEY_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, symmetricKey,
                Optional.empty());

        final byte[] cipherText = this.cipherOperator.doCipherOperation(cipher, dataToEncrypt, ENCRYPTION_ERROR);
        return ImmutablePair.of(getString(cipherText), getString(cipher.getIV()));
    }

    /**
     * Decrypts the passed data using {@link javax.crypto.SecretKey} and returns the decrypted data
     *
     * @param dataToDecrypt data to decrypt
     * @param password      password to use for decryption
     * @param initialVector initial vector to use for decryption
     * @return decrypted data
     */
    public String decrypt(@NonNull final String dataToDecrypt, @NonNull final String password, @NonNull final String initialVector) {
        final Key symmetricKey = this.retrieveSymmetricKey(password);
        final Cipher cipher = this.cipherRetriever.retrieve(SYMMETRIC_KEY_CIPHER_TRANSFORMATION, Cipher.DECRYPT_MODE, symmetricKey,
                Optional.of(toByteArray(initialVector)));

        final byte[] plainText = this.cipherOperator.doCipherOperation(cipher, dataToDecrypt, DECRYPTION_ERROR);
        return getString(plainText);
    }

    private Key retrieveSymmetricKey(final String password) {
        final Key symmetricKey;
        try {
            symmetricKey = this.symmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password);
        } catch (final UnrecoverableKeyException e) {
            log.error(KEY_RETRIEVAL_ERROR, e);
            throw new CipherOperationException(KEY_RETRIEVAL_ERROR, e);
        }

        return symmetricKey;
    }
}
