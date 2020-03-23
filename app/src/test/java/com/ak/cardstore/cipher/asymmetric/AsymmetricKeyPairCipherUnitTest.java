package com.ak.cardstore.cipher.asymmetric;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Optional;

import javax.crypto.Cipher;

import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.KEY_ALGORITHM;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class AsymmetricKeyPairCipherUnitTest {

    private static final String ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);
    private static final String ASYMMETRIC_KEY_PAIR_ALIAS = "com.ak.cardstore.askp";

    @Mock
    private AsymmetricKeyPairRetriever mockAsymmetricKeyPairRetriever;

    @Mock
    private CipherRetriever mockCipherRetriever;

    @Mock
    private CipherOperator mockCipherOperator;

    @InjectMocks
    private AsymmetricKeyPairCipher asymmetricKeyPairCipher;

    @Test
    public void testEncrypt() {
        final String dataToEncrypt = Make.aString();
        final String expectedCipherText = Make.aString();

        final Key mockPublicKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockAsymmetricKeyPairRetriever.retrievePublicKey(ASYMMETRIC_KEY_PAIR_ALIAS)).thenReturn(mockPublicKey);
        when(this.mockCipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, mockPublicKey, Optional.empty()))
                .thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, dataToEncrypt, "Error encrypting data!"))
                .thenReturn(expectedCipherText.getBytes(StandardCharsets.UTF_8));

        final String encryptedData = this.asymmetricKeyPairCipher.encrypt(dataToEncrypt);
        Assert.assertEquals(expectedCipherText, encryptedData);
    }

    @Test
    public void testDecrypt() {
        final String dataToDecrypt = Make.aString();
        final String expectedPlainText = Make.aString();

        final Key mockPrivateKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockAsymmetricKeyPairRetriever.retrievePrivateKey(ASYMMETRIC_KEY_PAIR_ALIAS)).thenReturn(mockPrivateKey);
        when(this.mockCipherRetriever.retrieve(ASYMMETRIC_KEY_PAIR_CIPHER_TRANSFORMATION, Cipher.DECRYPT_MODE, mockPrivateKey, Optional.empty()))
                .thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, dataToDecrypt, "Error decrypting data!"))
                .thenReturn(expectedPlainText.getBytes(StandardCharsets.UTF_8));

        final String encryptedData = this.asymmetricKeyPairCipher.decrypt(dataToDecrypt);
        Assert.assertEquals(expectedPlainText, encryptedData);
    }
}
