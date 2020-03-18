package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.DataEncryptionException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.security.Key;
import java.security.UnrecoverableKeyException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

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

    private static final String KEY_RETRIEVAL_ERROR = "Error retrieving key!";
    private static final String ENCRYPTION_ERROR = "Error encrypting data!";
    private static final String DECRYPTION_ERROR = "Error decrypting data!";

    private final SymmetricKeyRetriever symmetricKeyRetriever;
    private final CipherRetriever cipherRetriever;

    /**
     * Encrypts and returns the data passed.
     *
     * @param dataToEncrypt data to encrypt
     * @param password      password to use fot encryption
     * @return Pair of <encrypted data, initial vector>
     */
    public ImmutablePair<String, String> encrypt(@NonNull final String dataToEncrypt, @NonNull final String password) {
        final Key symmetricKey = this.retrieveSymmetricKey(password);
        final Cipher cipher = this.cipherRetriever.retrieve(Cipher.ENCRYPT_MODE, symmetricKey, Optional.empty());

        final byte[] cipherText = this.doCipherOperation(cipher, dataToEncrypt, ENCRYPTION_ERROR);
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
        final Cipher cipher = this.cipherRetriever.retrieve(Cipher.DECRYPT_MODE, symmetricKey, Optional.of(toByteArray(initialVector)));

        final byte[] plainText = this.doCipherOperation(cipher, dataToDecrypt, DECRYPTION_ERROR);
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

    private byte[] doCipherOperation(final Cipher cipher, final String dataToOperate, final String operationErrorMessage) {
        final byte[] outputText;
        try {
            outputText = cipher.doFinal(toByteArray(dataToOperate));
        } catch (final BadPaddingException | IllegalBlockSizeException e) {
            log.error(operationErrorMessage, e);
            throw new DataEncryptionException(operationErrorMessage, e);
        }

        return outputText;
    }
}
