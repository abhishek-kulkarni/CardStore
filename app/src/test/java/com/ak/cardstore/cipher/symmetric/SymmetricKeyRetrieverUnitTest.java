package com.ak.cardstore.cipher.symmetric;

import android.os.Build;

import com.ak.cardstore.Make;
import com.ak.cardstore.TestKeyStore;
import com.ak.cardstore.cipher.KeyStoreRetriever;
import com.ak.cardstore.exception.SymmetricKeyRetrievalException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Abhishek
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1, Build.VERSION_CODES.P})
public class SymmetricKeyRetrieverUnitTest {

    private KeyStoreRetriever mockKeyStoreRetriever;
    private SymmetricKeyGenerator mockSymmetricKeyGenerator;
    private SymmetricKeyRetriever symmetricKeyRetriever;

    @Before
    public void setup() {
        this.mockKeyStoreRetriever = mock(KeyStoreRetriever.class);
        this.mockSymmetricKeyGenerator = mock(SymmetricKeyGenerator.class);

        this.symmetricKeyRetriever = new SymmetricKeyRetriever(this.mockKeyStoreRetriever, this.mockSymmetricKeyGenerator);
    }

    @Test
    public void testRetrieve_WithKeystoreContainsAlias()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final Key mockSymmetricKey = mock(Key.class);

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, password.toCharArray())).thenReturn(mockSymmetricKey);

        final Key symmetricKey = this.symmetricKeyRetriever.retrieve(keyAlias, password);
        assertSame(mockSymmetricKey, symmetricKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, password.toCharArray());
        verifyNoInteractions(this.mockSymmetricKeyGenerator);
    }

    @Test
    public void testRetrieve_ThrowsKeyStoreException() {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);

        final SymmetricKeyRetrievalException symmetricKeyRetrievalException = assertThrows(SymmetricKeyRetrievalException.class,
                () -> this.symmetricKeyRetriever.retrieve(keyAlias, password));
        assertEquals("Key store AndroidKeyStore not initialized", symmetricKeyRetrievalException.getMessage());
        assertTrue(symmetricKeyRetrievalException.getCause() instanceof KeyStoreException);

        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verifyNoInteractions(mockKeyStoreSpi);
        verifyNoInteractions(this.mockSymmetricKeyGenerator);
    }

    @Test
    public void testRetrieve_ThrowsNoSuchAlgorithmException()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, password.toCharArray())).thenThrow(NoSuchAlgorithmException.class);

        final SymmetricKeyRetrievalException symmetricKeyRetrievalException = assertThrows(SymmetricKeyRetrievalException.class,
                () -> this.symmetricKeyRetriever.retrieve(keyAlias, password));
        assertEquals("Error retrieving key with alias " + keyAlias, symmetricKeyRetrievalException.getMessage());
        assertTrue(symmetricKeyRetrievalException.getCause() instanceof NoSuchAlgorithmException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, password.toCharArray());
        verifyNoInteractions(this.mockSymmetricKeyGenerator);
    }

    @Test
    public void testRetrieve_ThrowsUnrecoverableKeyExceptions()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, password.toCharArray())).thenThrow(UnrecoverableKeyException.class);

        assertThrows(UnrecoverableKeyException.class, () -> this.symmetricKeyRetriever.retrieve(keyAlias, password));

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, password.toCharArray());
        verifyNoInteractions(this.mockSymmetricKeyGenerator);
    }

    @Test
    public void testRetrieve_WithKeystoreContainsAliasFalse()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, KeyStoreException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final SecretKey mockSymmetricKey = mock(SecretKey.class);

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockSymmetricKeyGenerator.generate("AndroidKeyStore", keyAlias, password)).thenReturn(mockSymmetricKey);
        doNothing().when(mockKeyStoreSpi).engineSetKeyEntry(keyAlias, mockSymmetricKey, password.toCharArray(), null);

        final Key symmetricKey = this.symmetricKeyRetriever.retrieve(keyAlias, password);
        assertSame(mockSymmetricKey, symmetricKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi, never()).engineGetKey(keyAlias, password.toCharArray());
        verify(this.mockSymmetricKeyGenerator).generate("AndroidKeyStore", keyAlias, password);
        verify(mockKeyStoreSpi).engineSetKeyEntry(keyAlias, mockSymmetricKey, password.toCharArray(), null);
    }

    @Test
    public void testRetrieve_WithKeystoreContainsAliasFalse_ThrowsNoSuchProviderException()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, KeyStoreException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();
        final String password = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockSymmetricKeyGenerator.generate("AndroidKeyStore", keyAlias, password)).thenThrow(NoSuchProviderException.class);

        final SymmetricKeyRetrievalException symmetricKeyRetrievalException = assertThrows(SymmetricKeyRetrievalException.class,
                () -> this.symmetricKeyRetriever.retrieve(keyAlias, password));
        assertEquals("Error generating a new key using provider AndroidKeyStore", symmetricKeyRetrievalException.getMessage());
        assertTrue(symmetricKeyRetrievalException.getCause() instanceof NoSuchProviderException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi, times(0)).engineGetKey(keyAlias, password.toCharArray());
        verify(this.mockSymmetricKeyGenerator).generate("AndroidKeyStore", keyAlias, password);
        verify(mockKeyStoreSpi, never()).engineSetKeyEntry(anyString(), any(Key.class), any(), any());
    }
}
