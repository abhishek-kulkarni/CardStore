package com.ak.cardstore.cipher.asymmetric;

import android.os.Build;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.util.StringUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;

import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.KEY_ALGORITHM;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Abhishek
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1, Build.VERSION_CODES.P})
public class AsymmetricKeyPairCipherUnitTest {

    private static final String ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);
    private static final String ASYMMETRIC_KEY_PAIR_ALIAS = "com.ak.cardstore.askp";

    private AsymmetricKeyPairRetriever mockAsymmetricKeyPairRetriever;
    private CipherRetriever mockCipherRetriever;
    private CipherOperator mockCipherOperator;
    private AsymmetricKeyPairCipher asymmetricKeyPairCipher;

    @Before
    public void setup() {
        this.mockAsymmetricKeyPairRetriever = mock(AsymmetricKeyPairRetriever.class);
        this.mockCipherRetriever = mock(CipherRetriever.class);
        this.mockCipherOperator = mock(CipherOperator.class);

        this.asymmetricKeyPairCipher = new AsymmetricKeyPairCipher(this.mockAsymmetricKeyPairRetriever, this.mockCipherRetriever,
                this.mockCipherOperator);
    }

    @Test
    public void testEncrypt() {
        final String dataToEncrypt = Make.aString();
        final byte[] cipherTextBytes = Make.aByteArray();
        final String expectedCipherText = Base64.getEncoder().encodeToString(cipherTextBytes);

        final Key mockPublicKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockAsymmetricKeyPairRetriever.retrievePublicKey(ASYMMETRIC_KEY_PAIR_ALIAS)).thenReturn(mockPublicKey);
        when(this.mockCipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, mockPublicKey, Optional.empty()))
                .thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, StringUtil.toUTF8ByteArray(dataToEncrypt), "Error encrypting data!"))
                .thenReturn(cipherTextBytes);

        final String encryptedData = this.asymmetricKeyPairCipher.encrypt(dataToEncrypt);
        Assert.assertEquals(expectedCipherText, encryptedData);
    }

    @Test
    public void testDecrypt() {
        final String dataToDecrypt = Make.aBase64String();
        final String expectedPlainText = Make.aString();

        final Key mockPrivateKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockAsymmetricKeyPairRetriever.retrievePrivateKey(ASYMMETRIC_KEY_PAIR_ALIAS)).thenReturn(mockPrivateKey);
        when(this.mockCipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.DECRYPT_MODE, mockPrivateKey, Optional.empty()))
                .thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, StringUtil.base64StringToByteArray(dataToDecrypt), "Error decrypting data!"))
                .thenReturn(expectedPlainText.getBytes(StandardCharsets.UTF_8));

        final String encryptedData = this.asymmetricKeyPairCipher.decrypt(dataToDecrypt);
        Assert.assertEquals(expectedPlainText, encryptedData);
    }
}
