package com.ak.cardstore.cipher;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.CipherRetrievalException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class CipherRetrieverUnitTest {

    @Test
    public void testRetrieve_WithNoSuchAlgorithmException() throws NoSuchPaddingException, NoSuchAlgorithmException {
        mockStatic(Cipher.class);

        final String transformation = "AES/CBC/PKCS7Padding";
        final Key mockKey = mock(Key.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenThrow(NoSuchAlgorithmException.class);

        final CipherRetrievalException cipherRetrievalException = assertThrows(CipherRetrievalException.class,
                () -> cipherRetriever.retrieve(Cipher.ENCRYPT_MODE, mockKey, Optional.empty()));
        assertEquals("Failed to retrieve cipher for transformation " + transformation, cipherRetrievalException.getMessage());
        assertTrue(cipherRetrievalException.getCause() instanceof NoSuchAlgorithmException);
    }

    @Test
    public void testRetrieve_WithNoSuchPaddingException() throws NoSuchPaddingException, NoSuchAlgorithmException {
        mockStatic(Cipher.class);

        final String transformation = "AES/CBC/PKCS7Padding";
        final Key mockKey = mock(Key.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenThrow(NoSuchPaddingException.class);

        final CipherRetrievalException cipherRetrievalException = assertThrows(CipherRetrievalException.class,
                () -> cipherRetriever.retrieve(Cipher.ENCRYPT_MODE, mockKey, Optional.empty()));
        assertEquals("Failed to retrieve cipher for transformation " + transformation, cipherRetrievalException.getMessage());
        assertTrue(cipherRetrievalException.getCause() instanceof NoSuchPaddingException);
    }

    @Test
    public void testRetrieve_WithInvalidKeyException() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        mockStatic(Cipher.class);

        final int opMode = Cipher.ENCRYPT_MODE;
        final String transformation = "AES/CBC/PKCS7Padding";
        final Key mockKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenReturn(mockCipher);
        doThrow(new InvalidKeyException()).when(mockCipher).init(opMode, mockKey);

        final CipherRetrievalException cipherRetrievalException = assertThrows(CipherRetrievalException.class,
                () -> cipherRetriever.retrieve(opMode, mockKey, Optional.empty()));
        assertEquals("Failed to retrieve cipher due to invalid key", cipherRetrievalException.getMessage());
        assertTrue(cipherRetrievalException.getCause() instanceof InvalidKeyException);
    }

    @Test
    public void testRetrieve_WithInvalidAlgorithmParameterException()
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        mockStatic(Cipher.class);

        final int opMode = Cipher.DECRYPT_MODE;
        final String transformation = "AES/CBC/PKCS7Padding";
        final String initialVector = Make.aString();
        final Key mockKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenReturn(mockCipher);
        doThrow(new InvalidAlgorithmParameterException()).when(mockCipher).init(anyInt(), any(Key.class), any(IvParameterSpec.class));

        final CipherRetrievalException cipherRetrievalException = assertThrows(CipherRetrievalException.class,
                () -> cipherRetriever.retrieve(opMode, mockKey, Optional.of(initialVector.getBytes(StandardCharsets.UTF_8))));
        assertEquals("Failed to retrieve cipher due to invalid initial vector", cipherRetrievalException.getMessage());
        assertTrue(cipherRetrievalException.getCause() instanceof InvalidAlgorithmParameterException);
    }

    @Test
    public void testRetrieve_WithEncryptMode() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        mockStatic(Cipher.class);

        final int opMode = Cipher.ENCRYPT_MODE;
        final String transformation = "AES/CBC/PKCS7Padding";
        final Key mockKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenReturn(mockCipher);
        doNothing().when(mockCipher).init(opMode, mockKey);

        final Cipher cipher = cipherRetriever.retrieve(opMode, mockKey, Optional.empty());
        assertSame(mockCipher, cipher);

        verify(mockCipher).init(opMode, mockKey);
    }

    @Test
    public void testRetrieve_WithDecryptMode()
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        mockStatic(Cipher.class);

        final int opMode = Cipher.DECRYPT_MODE;
        final String transformation = "AES/CBC/PKCS7Padding";
        final String initialVector = Make.aString();
        final Key mockKey = mock(Key.class);
        final Cipher mockCipher = mock(Cipher.class);

        final CipherRetriever cipherRetriever = new CipherRetriever();

        when(Cipher.getInstance(transformation)).thenReturn(mockCipher);
        doNothing().when(mockCipher).init(anyInt(), any(Key.class), any(IvParameterSpec.class));

        final Cipher cipher = cipherRetriever.retrieve(opMode, mockKey, Optional.of(initialVector.getBytes(StandardCharsets.UTF_8)));
        assertSame(mockCipher, cipher);

        verify(mockCipher).init(anyInt(), any(Key.class), any(IvParameterSpec.class));
    }
}
