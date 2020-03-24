package com.ak.cardstore.configuration;

import android.os.Build;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.exception.ConfigurationManagerException;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Abhishek
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1, Build.VERSION_CODES.P})
public class AppConfigurationManagerUnitTest {

    private static final String CONFIGURATION_FILE_NAME = "com.ak.cardstore.wallet.cdb";

    private Serializer<Wallet> mockWalletSerializer;
    private SymmetricKeyCipher mockSymmetricKeyCipher;
    private Serializer<EncryptedConfiguration> mockEncryptedConfigurationSerializer;
    private FileBasedDataAccessor mockFileBasedDataAccessor;
    private AppConfigurationManager appConfigurationManager;

    @Before
    public void setup() {
        this.mockWalletSerializer = mock(Serializer.class);
        this.mockSymmetricKeyCipher = mock(SymmetricKeyCipher.class);
        this.mockEncryptedConfigurationSerializer = mock(Serializer.class);
        this.mockFileBasedDataAccessor = mock(FileBasedDataAccessor.class);

        this.appConfigurationManager = new AppConfigurationManager(this.mockWalletSerializer, this.mockSymmetricKeyCipher,
                this.mockEncryptedConfigurationSerializer, this.mockFileBasedDataAccessor);
    }

    @Test
    public void testSave() throws IOException {
        final Wallet wallet = Make.aWallet();
        final String password = Make.aString();

        final String serializedWallet = Make.aString();
        final String serializedEncryptedWallet = Make.aString();
        final String initialVector = Make.aString();
        final EncryptedConfiguration encryptedConfiguration = EncryptedConfiguration.builder()
                .serializedAndEncryptedWallet(serializedEncryptedWallet)
                .initialVector(initialVector)
                .build();
        final String serializedEncryptedConfiguration = Make.aString();

        when(this.mockWalletSerializer.serialize(wallet)).thenReturn(serializedWallet);
        when(this.mockSymmetricKeyCipher.encrypt(serializedWallet, password)).thenReturn(ImmutablePair.of(serializedEncryptedWallet, initialVector));
        when(this.mockEncryptedConfigurationSerializer.serialize(encryptedConfiguration)).thenReturn(serializedEncryptedConfiguration);
        doNothing().when(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);

        this.appConfigurationManager.save(wallet, password);

        verify(this.mockWalletSerializer).serialize(wallet);
        verify(this.mockSymmetricKeyCipher).encrypt(serializedWallet, password);
        verify(this.mockEncryptedConfigurationSerializer).serialize(encryptedConfiguration);
        verify(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);
    }

    @Test
    public void testSave_WithIOException() throws IOException {
        final Wallet wallet = Make.aWallet();
        final String password = Make.aString();

        final String serializedWallet = Make.aString();
        final String serializedEncryptedWallet = Make.aString();
        final String initialVector = Make.aString();
        final EncryptedConfiguration encryptedConfiguration = EncryptedConfiguration.builder()
                .serializedAndEncryptedWallet(serializedEncryptedWallet)
                .initialVector(initialVector)
                .build();
        final String serializedEncryptedConfiguration = Make.aString();

        when(this.mockWalletSerializer.serialize(wallet)).thenReturn(serializedWallet);
        when(this.mockSymmetricKeyCipher.encrypt(serializedWallet, password)).thenReturn(ImmutablePair.of(serializedEncryptedWallet, initialVector));
        when(this.mockEncryptedConfigurationSerializer.serialize(encryptedConfiguration)).thenReturn(serializedEncryptedConfiguration);
        doThrow(IOException.class).when(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);

        final ConfigurationManagerException configurationManagerException = assertThrows(ConfigurationManagerException.class,
                () -> this.appConfigurationManager.save(wallet, password));
        assertEquals("Error saving the configuration file!", configurationManagerException.getMessage());
        assertTrue(configurationManagerException.getCause() instanceof IOException);

        verify(this.mockWalletSerializer).serialize(wallet);
        verify(this.mockSymmetricKeyCipher).encrypt(serializedWallet, password);
        verify(this.mockEncryptedConfigurationSerializer).serialize(encryptedConfiguration);
        verify(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);
    }

    @Test
    public void testLoad() throws IOException {
        final Wallet expctedWallet = Make.aWallet();
        final String password = Make.aString();

        final String serializedWallet = Make.aString();
        final String serializedEncryptedWallet = Make.aString();
        final String initialVector = Make.aString();
        final EncryptedConfiguration encryptedConfiguration = EncryptedConfiguration.builder()
                .serializedAndEncryptedWallet(serializedEncryptedWallet)
                .initialVector(initialVector)
                .build();
        final String serializedEncryptedConfiguration = Make.aString();

        when(this.mockFileBasedDataAccessor.getContents(CONFIGURATION_FILE_NAME)).thenReturn(serializedEncryptedConfiguration);
        when(this.mockEncryptedConfigurationSerializer.deserialize(serializedEncryptedConfiguration)).thenReturn(encryptedConfiguration);
        when(this.mockSymmetricKeyCipher.decrypt(encryptedConfiguration.getSerializedAndEncryptedWallet(), password,
                encryptedConfiguration.getInitialVector())).thenReturn(serializedWallet);
        when(this.mockWalletSerializer.deserialize(serializedWallet)).thenReturn(expctedWallet);


        final Wallet wallet = this.appConfigurationManager.load(password);
        assertSame(expctedWallet, wallet);

        verify(this.mockFileBasedDataAccessor).getContents(CONFIGURATION_FILE_NAME);
        verify(this.mockEncryptedConfigurationSerializer).deserialize(serializedEncryptedConfiguration);
        verify(this.mockSymmetricKeyCipher).decrypt(encryptedConfiguration.getSerializedAndEncryptedWallet(), password,
                encryptedConfiguration.getInitialVector());
        verify(this.mockWalletSerializer).deserialize(serializedWallet);
    }

    @Test
    public void testLoad_WithIOException() throws IOException {
        final String password = Make.aString();

        when(this.mockFileBasedDataAccessor.getContents(CONFIGURATION_FILE_NAME)).thenThrow(IOException.class);

        final ConfigurationManagerException configurationManagerException = assertThrows(ConfigurationManagerException.class,
                () -> this.appConfigurationManager.load(password));
        assertEquals("Error reading the configuration file!", configurationManagerException.getMessage());
        assertTrue(configurationManagerException.getCause() instanceof IOException);

        verify(this.mockFileBasedDataAccessor).getContents(CONFIGURATION_FILE_NAME);
        verifyNoInteractions(this.mockEncryptedConfigurationSerializer);
        verifyNoInteractions(this.mockSymmetricKeyCipher);
        verifyNoInteractions(this.mockWalletSerializer);
    }
}
