package com.ak.cardstore.cipher.symmetric;

import android.util.Log;

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

import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.util.StringUtil.base64StringToByteArray;
import static com.ak.cardstore.util.StringUtil.toBase64String;
import static com.ak.cardstore.util.StringUtil.toUTF8ByteArray;
import static com.ak.cardstore.util.StringUtil.toUTF8String;

/**
 * A cipher class to handle the encryption and decryption of the data using the {@link javax.crypto.SecretKey}.
 *
 * @author Abhishek
 */

@AllArgsConstructor
public class SymmetricKeyCipher {

    private static final String LOG_TAG = SymmetricKeyCipher.class.getSimpleName();

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

        final byte[] cipherText = this.cipherOperator.doCipherOperation(cipher, toUTF8ByteArray(dataToEncrypt), ENCRYPTION_ERROR);
        return ImmutablePair.of(toBase64String(cipherText), toBase64String(cipher.getIV()));
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
                Optional.of(base64StringToByteArray(initialVector)));

        final byte[] plainText = this.cipherOperator.doCipherOperation(cipher, base64StringToByteArray(dataToDecrypt), DECRYPTION_ERROR);
        return toUTF8String(plainText);
    }

    private Key retrieveSymmetricKey(final String password) {
        final Key symmetricKey;
        try {
            symmetricKey = this.symmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password);
        } catch (final UnrecoverableKeyException e) {
            Log.e(LOG_TAG, KEY_RETRIEVAL_ERROR, e);
            throw new CipherOperationException(KEY_RETRIEVAL_ERROR, e);
        }

        return symmetricKey;
    }
}
