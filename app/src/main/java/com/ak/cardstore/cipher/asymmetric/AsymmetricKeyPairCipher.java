package com.ak.cardstore.cipher.asymmetric;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;

import java.security.Key;
import java.util.Optional;

import javax.crypto.Cipher;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.util.StringUtil.getString;

/**
 * A cipher class to handle the encryption and decryption of the data using the asymmetric
 * {@link java.security.PrivateKey} and {@link java.security.PublicKey} pair.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class AsymmetricKeyPairCipher {

    private static final String ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);
    private static final String ASYMMETRIC_KEY_PAIR_ALIAS = "com.ak.cardstore.askp";

    private static final String ENCRYPTION_ERROR = "Error encrypting data!";
    private static final String DECRYPTION_ERROR = "Error decrypting data!";

    private final AsymmetricKeyPairRetriever asymmetricKeyPairRetriever;
    private final CipherRetriever cipherRetriever;
    private final CipherOperator cipherOperator;

    /**
     * Encrypts the passed data using {@link java.security.PublicKey} and returns the encrypted data.
     *
     * @param dataToEncrypt data to encrypt
     * @return encrypted data
     */
    public String encrypt(@NonNull final String dataToEncrypt) {
        final Key publicKey = this.asymmetricKeyPairRetriever.retrievePublicKey(ASYMMETRIC_KEY_PAIR_ALIAS);
        final Cipher cipher = this.cipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, publicKey,
                Optional.empty());

        final byte[] cipherText = this.cipherOperator.doCipherOperation(cipher, dataToEncrypt, ENCRYPTION_ERROR);
        return getString(cipherText);
    }

    /**
     * Decrypts the passed data using {@link java.security.PrivateKey} and returns the decrypted data
     *
     * @param dataToDecrypt data to decrypt
     * @return decrypted data
     */
    public String decrypt(@NonNull final String dataToDecrypt) {
        final Key privateKey = this.asymmetricKeyPairRetriever.retrievePrivateKey(ASYMMETRIC_KEY_PAIR_ALIAS);
        final Cipher cipher = this.cipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.DECRYPT_MODE, privateKey,
                Optional.empty());

        final byte[] plainText = this.cipherOperator.doCipherOperation(cipher, dataToDecrypt, DECRYPTION_ERROR);
        return getString(plainText);
    }
}
