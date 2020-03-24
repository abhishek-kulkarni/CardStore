package com.ak.cardstore.cipher;

import android.util.Log;

import com.ak.cardstore.Make;
import com.ak.cardstore.TestKeyStore;
import com.ak.cardstore.exception.KeyStoreRetrievalException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({KeyStoreRetriever.class, Log.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class KeyStoreRetrieverUnitTest {

    @Before
    public void setupLog() {
        mockStatic(Log.class);
        when(Log.e(anyString(), anyString(), any(Throwable.class))).thenReturn(0);
    }

    @Test
    public void testRetrieve_WithInvalidType() {
        final KeyStoreRetriever keyStoreRetriever = new KeyStoreRetriever();
        final String keyStoreType = Make.aString();

        final KeyStoreRetrievalException keyStoreRetrievalException = assertThrows(KeyStoreRetrievalException.class,
                () -> keyStoreRetriever.retrieve(keyStoreType));
        assertEquals("Invalid key store " + keyStoreType, keyStoreRetrievalException.getMessage());
        assertTrue(keyStoreRetrievalException.getCause() instanceof KeyStoreException);
    }

    @Test
    public void testRetrieve() {
        final KeyStoreRetriever keyStoreRetriever = new KeyStoreRetriever();
        final String keyStoreType = KeyStore.getDefaultType();

        final KeyStore keyStore = keyStoreRetriever.retrieve(keyStoreType);
        assertNotNull(keyStore);
        assertEquals(keyStoreType, keyStore.getType());
    }

    @Test
    public void testRetrieve_WithCertificateException() throws Exception {
        mockStatic(KeyStore.class);

        final KeyStoreRetriever keyStoreRetriever = new KeyStoreRetriever();
        final String keyStoreType = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(KeyStore.getInstance(keyStoreType)).thenReturn(testKeyStore);
        doThrow(new CertificateException()).when(mockKeyStoreSpi).engineLoad(null);

        final KeyStoreRetrievalException keyStoreRetrievalException = assertThrows(KeyStoreRetrievalException.class,
                () -> keyStoreRetriever.retrieve(keyStoreType));
        assertEquals("Error loading key store " + keyStoreType, keyStoreRetrievalException.getMessage());
        assertTrue(keyStoreRetrievalException.getCause() instanceof CertificateException);

        verifyStatic(KeyStore.class);
        KeyStore.getInstance(keyStoreType);
        verify(mockKeyStoreSpi).engineLoad(null);
    }

    @Test
    public void testRetrieve_WithIOException() throws Exception {
        mockStatic(KeyStore.class);

        final KeyStoreRetriever keyStoreRetriever = new KeyStoreRetriever();
        final String keyStoreType = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(KeyStore.getInstance(keyStoreType)).thenReturn(testKeyStore);
        doThrow(new IOException()).when(mockKeyStoreSpi).engineLoad(null);

        final KeyStoreRetrievalException keyStoreRetrievalException = assertThrows(KeyStoreRetrievalException.class,
                () -> keyStoreRetriever.retrieve(keyStoreType));
        assertEquals("Error loading key store " + keyStoreType, keyStoreRetrievalException.getMessage());
        assertTrue(keyStoreRetrievalException.getCause() instanceof IOException);

        verifyStatic(KeyStore.class);
        KeyStore.getInstance(keyStoreType);
        verify(mockKeyStoreSpi).engineLoad(null);
    }

    @Test
    public void testRetrieve_WithNoSuchAlgorithmException() throws Exception {
        mockStatic(KeyStore.class);

        final KeyStoreRetriever keyStoreRetriever = new KeyStoreRetriever();
        final String keyStoreType = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(KeyStore.getInstance(keyStoreType)).thenReturn(testKeyStore);
        doThrow(new NoSuchAlgorithmException()).when(mockKeyStoreSpi).engineLoad(null);

        final KeyStoreRetrievalException keyStoreRetrievalException = assertThrows(KeyStoreRetrievalException.class,
                () -> keyStoreRetriever.retrieve(keyStoreType));
        assertEquals("Error loading key store " + keyStoreType, keyStoreRetrievalException.getMessage());
        assertTrue(keyStoreRetrievalException.getCause() instanceof NoSuchAlgorithmException);

        verifyStatic(KeyStore.class);
        KeyStore.getInstance(keyStoreType);
        verify(mockKeyStoreSpi).engineLoad(null);
    }
}
