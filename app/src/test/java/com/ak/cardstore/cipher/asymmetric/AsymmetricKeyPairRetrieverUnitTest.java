package com.ak.cardstore.cipher.asymmetric;

import com.ak.cardstore.Make;
import com.ak.cardstore.TestKeyStore;
import com.ak.cardstore.cipher.KeyStoreRetriever;
import com.ak.cardstore.exception.AsymmetricKeyPairRetrievalException;
import com.ak.cardstore.exception.PrivateKeyRetrievalException;
import com.ak.cardstore.exception.PublicKeyRetrievalException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AsymmetricKeyPairRetriever.class, KeyStore.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class AsymmetricKeyPairRetrieverUnitTest {

    @Mock
    private KeyStoreRetriever mockKeyStoreRetriever;

    @Mock
    private AsymmetricKeyPairGenerator mockAsymmetricKeyPairGenerator;

    @InjectMocks
    private AsymmetricKeyPairRetriever asymmetricKeyPairRetriever;

    @Test
    public void testRetrievePrivateKey_WithKeystoreContainsAlias()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();

        final PrivateKey mockPrivateKey = mock(PrivateKey.class);

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, null)).thenReturn(mockPrivateKey);

        final Key privateKey = this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias);
        assertSame(mockPrivateKey, privateKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, null);
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePrivateKey_ThrowsKeyStoreException() {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);

        final PrivateKeyRetrievalException privateKeyRetrievalException = assertThrows(PrivateKeyRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias));
        assertEquals("Key store AndroidKeyStore not initialized", privateKeyRetrievalException.getMessage());
        assertTrue(privateKeyRetrievalException.getCause() instanceof KeyStoreException);

        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verifyNoInteractions(mockKeyStoreSpi);
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePrivateKey_ThrowsNoSuchAlgorithmException()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, null)).thenThrow(NoSuchAlgorithmException.class);

        final PrivateKeyRetrievalException privateKeyRetrievalException = assertThrows(PrivateKeyRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias));
        assertEquals("Error retrieving private key with alias " + keyAlias, privateKeyRetrievalException.getMessage());
        assertTrue(privateKeyRetrievalException.getCause() instanceof NoSuchAlgorithmException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, null);
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePrivateKey_ThrowsUnrecoverableKeyException()
            throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetKey(keyAlias, null)).thenThrow(UnrecoverableKeyException.class);

        final PrivateKeyRetrievalException privateKeyRetrievalException = assertThrows(PrivateKeyRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias));
        assertEquals("Error retrieving private key with alias " + keyAlias, privateKeyRetrievalException.getMessage());
        assertTrue(privateKeyRetrievalException.getCause() instanceof UnrecoverableKeyException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetKey(keyAlias, null);
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePrivateKey_WithKeystoreContainsAliasFalse()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();

        final PrivateKey mockPrivateKey = mock(PrivateKey.class);
        final KeyPair mockKeyPair = new KeyPair(mock(PublicKey.class), mockPrivateKey);
        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockAsymmetricKeyPairGenerator.generate("AndroidKeyStore", keyAlias)).thenReturn(mockKeyPair);

        final Key privateKey = this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias);
        assertSame(mockPrivateKey, privateKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi, never()).engineGetKey(keyAlias, null);
        verify(this.mockAsymmetricKeyPairGenerator).generate("AndroidKeyStore", keyAlias);
    }

    @Test
    public void testRetrievePrivateKey_WithKeystoreContainsAliasFalse_ThrowsNoSuchProviderException()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, UnrecoverableKeyException {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        doNothing().when(mockKeyStoreSpi).engineLoad(null);

        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockAsymmetricKeyPairGenerator.generate("AndroidKeyStore", keyAlias)).thenThrow(NoSuchProviderException.class);

        final AsymmetricKeyPairRetrievalException asymmetricKeyPairRetrievalException = assertThrows(AsymmetricKeyPairRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePrivateKey(keyAlias));
        assertEquals("Error generating a new key pair using provider AndroidKeyStore", asymmetricKeyPairRetrievalException.getMessage());
        assertTrue(asymmetricKeyPairRetrievalException.getCause() instanceof NoSuchProviderException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi, times(0)).engineGetKey(keyAlias, null);
        verify(this.mockAsymmetricKeyPairGenerator).generate("AndroidKeyStore", keyAlias);
    }

    @Test
    public void testRetrievePublicKey_WithKeystoreContainsAlias() throws CertificateException, NoSuchAlgorithmException, IOException {
        final String keyAlias = Make.aString();

        final Certificate mockCertificate = mock(Certificate.class);
        final PublicKey mockPublicKey = mock(PublicKey.class);

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(true);
        when(mockKeyStoreSpi.engineGetCertificate(keyAlias)).thenReturn(mockCertificate);
        when(mockCertificate.getPublicKey()).thenReturn(mockPublicKey);

        final Key publicKey = this.asymmetricKeyPairRetriever.retrievePublicKey(keyAlias);
        assertSame(mockPublicKey, publicKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(mockKeyStoreSpi).engineGetCertificate(keyAlias);
        verify(mockCertificate).getPublicKey();
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePublicKey_WithKeystoreException() {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);

        final PublicKeyRetrievalException publicKeyRetrievalException = assertThrows(PublicKeyRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePublicKey(keyAlias));
        assertEquals("Key store AndroidKeyStore not initialized", publicKeyRetrievalException.getMessage());
        assertTrue(publicKeyRetrievalException.getCause() instanceof KeyStoreException);

        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verifyNoInteractions(this.mockAsymmetricKeyPairGenerator);
    }

    @Test
    public void testRetrievePublicKey_WithKeystoreContainsAliasFalse()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        final String keyAlias = Make.aString();

        final PublicKey mockPublicKey = mock(PublicKey.class);
        final KeyPair mockKeyPair = new KeyPair(mockPublicKey, mock(PrivateKey.class));

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockAsymmetricKeyPairGenerator.generate("AndroidKeyStore", keyAlias)).thenReturn(mockKeyPair);

        final Key publicKey = this.asymmetricKeyPairRetriever.retrievePublicKey(keyAlias);
        assertSame(mockPublicKey, publicKey);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(this.mockAsymmetricKeyPairGenerator).generate("AndroidKeyStore", keyAlias);
    }

    @Test
    public void testRetrievePublicKey_WithKeystoreContainsAliasFalse_ThrowsNoSuchProviderException()
            throws CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        final String keyAlias = Make.aString();

        final KeyStoreSpi mockKeyStoreSpi = mock(KeyStoreSpi.class);
        final KeyStore testKeyStore = new TestKeyStore(mockKeyStoreSpi, null, null);
        testKeyStore.load(null);

        when(this.mockKeyStoreRetriever.retrieve("AndroidKeyStore")).thenReturn(testKeyStore);
        when(mockKeyStoreSpi.engineContainsAlias(keyAlias)).thenReturn(false);
        when(this.mockAsymmetricKeyPairGenerator.generate("AndroidKeyStore", keyAlias)).thenThrow(NoSuchProviderException.class);

        final AsymmetricKeyPairRetrievalException asymmetricKeyPairRetrievalException = assertThrows(AsymmetricKeyPairRetrievalException.class,
                () -> this.asymmetricKeyPairRetriever.retrievePublicKey(keyAlias));
        assertEquals("Error generating a new key pair using provider AndroidKeyStore", asymmetricKeyPairRetrievalException.getMessage());
        assertTrue(asymmetricKeyPairRetrievalException.getCause() instanceof NoSuchProviderException);

        verify(mockKeyStoreSpi).engineLoad(null);
        verify(this.mockKeyStoreRetriever).retrieve("AndroidKeyStore");
        verify(mockKeyStoreSpi).engineContainsAlias(keyAlias);
        verify(this.mockAsymmetricKeyPairGenerator).generate("AndroidKeyStore", keyAlias);
    }
}
