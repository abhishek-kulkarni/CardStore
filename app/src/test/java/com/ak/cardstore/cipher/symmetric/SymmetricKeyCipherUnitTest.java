package com.ak.cardstore.cipher.symmetric;

import android.util.Log;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.exception.CipherOperationException;
import com.ak.cardstore.util.StringUtil;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.UnrecoverableKeyException;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;

import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator.KEY_ALGORITHM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Cipher.class, Log.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class SymmetricKeyCipherUnitTest {

    private static final String SYMMETRIC_KEY_CIPHER_TRANSFORMATION = String.format("%s/%s/%s", KEY_ALGORITHM, BLOCK_MODE, ENCRYPTION_PADDING);
    private static final String SYMMETRIC_KEY_ALIAS = "com.ak.cardstore.sk";

    @Mock
    private SymmetricKeyRetriever mockSymmetricKeyRetriever;

    @Mock
    private CipherRetriever mockCipherRetriever;

    @Mock
    private CipherOperator mockCipherOperator;

    @InjectMocks
    private SymmetricKeyCipher symmetricKeyCipher;

    @Before
    public void setupLog() {
        mockStatic(Log.class);
        when(Log.e(anyString(), anyString(), any(Throwable.class))).thenReturn(0);
    }

    @Test
    public void testEncrypt_ThrowsUnrecoverableKeyException() throws UnrecoverableKeyException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenThrow(UnrecoverableKeyException.class);

        final CipherOperationException cipherOperationException = assertThrows(CipherOperationException.class,
                () -> this.symmetricKeyCipher.encrypt(dataToEncrypt, password));
        assertEquals("Error retrieving key!", cipherOperationException.getMessage());
        assertTrue(cipherOperationException.getCause() instanceof UnrecoverableKeyException);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
    }

    @Test
    public void testEncrypt() throws UnrecoverableKeyException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();
        final byte[] cipherTextBytes = Make.aByteArray();
        final String expectedCipherText = Base64.getEncoder().encodeToString(cipherTextBytes);
        final byte[] initialVectorByes = Make.aByteArray();
        final String expectedInitialVector = Base64.getEncoder().encodeToString(initialVectorByes);

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(SYMMETRIC_KEY_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty()))
                .thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, StringUtil.toUTF8ByteArray(dataToEncrypt), "Error encrypting data!"))
                .thenReturn(cipherTextBytes);
        when(mockCipher.getIV()).thenReturn(initialVectorByes);

        final ImmutablePair<String, String> encryptedDataIvPair = this.symmetricKeyCipher.encrypt(dataToEncrypt, password);
        assertEquals(expectedCipherText, encryptedDataIvPair.getLeft());
        assertEquals(expectedInitialVector, encryptedDataIvPair.getRight());

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(SYMMETRIC_KEY_CIPHER_TRANSFORMATION, Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty());
        verify(this.mockCipherOperator).doCipherOperation(mockCipher, StringUtil.toUTF8ByteArray(dataToEncrypt), "Error encrypting data!");
        verify(mockCipher).getIV();
    }

    @Test
    public void testDecrypt() throws UnrecoverableKeyException {
        final String dataToDecrypt = Make.aBase64String();
        final String password = Make.aString();
        final String expectedPlainText = Make.aString();
        final String initialVector = Make.aBase64String();

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(anyString(), anyInt(), any(Key.class), any())).thenReturn(mockCipher);
        when(this.mockCipherOperator.doCipherOperation(mockCipher, StringUtil.base64StringToByteArray(dataToDecrypt), "Error decrypting data!"))
                .thenReturn(expectedPlainText.getBytes(StandardCharsets.UTF_8));

        final String decryptedData = this.symmetricKeyCipher.decrypt(dataToDecrypt, password, initialVector);
        assertEquals(expectedPlainText, decryptedData);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(anyString(), anyInt(), any(Key.class), any());
        verify(this.mockCipherOperator).doCipherOperation(mockCipher, StringUtil.base64StringToByteArray(dataToDecrypt), "Error decrypting data!");
    }
}
