package com.ak.cardstore.cipher;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.DataEncryptionException;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class SymmetricKeyCipherUnitTest {

    private static final String SYMMETRIC_KEY_ALIAS = "JaiGajanan";

    @Mock
    private SymmetricKeyRetriever mockSymmetricKeyRetriever;

    @Mock
    private CipherRetriever mockCipherRetriever;

    @InjectMocks
    private SymmetricKeyCipher symmetricKeyCipher;

    @Test
    public void testEncrypt_ThrowsUnrecoverableKeyException() throws UnrecoverableKeyException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenThrow(UnrecoverableKeyException.class);

        final DataEncryptionException dataEncryptionException = assertThrows(DataEncryptionException.class,
                () -> this.symmetricKeyCipher.encrypt(dataToEncrypt, password));
        assertEquals("Error retrieving key!", dataEncryptionException.getMessage());
        assertTrue(dataEncryptionException.getCause() instanceof UnrecoverableKeyException);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
    }

    @Test
    public void testEncrypt_ThrowsBadPaddingException() throws UnrecoverableKeyException, BadPaddingException, IllegalBlockSizeException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty())).thenReturn(mockCipher);
        when(mockCipher.doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8))).thenThrow(BadPaddingException.class);

        final DataEncryptionException dataEncryptionException = assertThrows(DataEncryptionException.class,
                () -> this.symmetricKeyCipher.encrypt(dataToEncrypt, password));
        assertEquals("Error encrypting data!", dataEncryptionException.getMessage());
        assertTrue(dataEncryptionException.getCause() instanceof BadPaddingException);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty());
        verify(mockCipher).doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncrypt_ThrowsIllegalBlockSizeException() throws UnrecoverableKeyException, BadPaddingException, IllegalBlockSizeException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty())).thenReturn(mockCipher);
        when(mockCipher.doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8))).thenThrow(IllegalBlockSizeException.class);

        final DataEncryptionException dataEncryptionException = assertThrows(DataEncryptionException.class,
                () -> this.symmetricKeyCipher.encrypt(dataToEncrypt, password));
        assertEquals("Error encrypting data!", dataEncryptionException.getMessage());
        assertTrue(dataEncryptionException.getCause() instanceof IllegalBlockSizeException);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty());
        verify(mockCipher).doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncrypt() throws UnrecoverableKeyException, BadPaddingException, IllegalBlockSizeException {
        final String dataToEncrypt = Make.aString();
        final String password = Make.aString();
        final String expectedCipherText = Make.aString();
        final String expectedInitialVector = Make.aString();

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty())).thenReturn(mockCipher);
        when(mockCipher.doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8))).thenReturn(expectedCipherText.getBytes(StandardCharsets.UTF_8));
        when(mockCipher.getIV()).thenReturn(expectedInitialVector.getBytes(StandardCharsets.UTF_8));

        final ImmutablePair<String, String> encryptedDataIvPair = this.symmetricKeyCipher.encrypt(dataToEncrypt, password);
        assertEquals(expectedCipherText, encryptedDataIvPair.getLeft());
        assertEquals(expectedInitialVector, encryptedDataIvPair.getRight());

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(Cipher.ENCRYPT_MODE, mockSymmetricKey, Optional.empty());
        verify(mockCipher).doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8));
        verify(mockCipher).getIV();
    }

    @Test
    public void testDecrypt() throws UnrecoverableKeyException, BadPaddingException, IllegalBlockSizeException {
        final String dataToDecrypt = Make.aString();
        final String password = Make.aString();
        final String expectedPlainText = Make.aString();
        final String initialVector = Make.aString();

        final Key mockSymmetricKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        when(this.mockSymmetricKeyRetriever.retrieve(SYMMETRIC_KEY_ALIAS, password)).thenReturn(mockSymmetricKey);
        when(this.mockCipherRetriever.retrieve(anyInt(), any(Key.class), any(Optional.class))).thenReturn(mockCipher);
        when(mockCipher.doFinal(dataToDecrypt.getBytes(StandardCharsets.UTF_8))).thenReturn(expectedPlainText.getBytes(StandardCharsets.UTF_8));

        final String decryptedData = this.symmetricKeyCipher.decrypt(dataToDecrypt, password, initialVector);
        assertEquals(expectedPlainText, decryptedData);

        verify(this.mockSymmetricKeyRetriever).retrieve(SYMMETRIC_KEY_ALIAS, password);
        verify(this.mockCipherRetriever).retrieve(anyInt(), any(Key.class), any(Optional.class));
        verify(mockCipher).doFinal(dataToDecrypt.getBytes(StandardCharsets.UTF_8));
    }
}
