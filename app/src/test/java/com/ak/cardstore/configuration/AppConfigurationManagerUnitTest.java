package com.ak.cardstore.configuration;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.SymmetricKeyCipher;
import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.exception.ConfigurationManagerException;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Abhishek
 */

@ExtendWith(MockitoExtension.class)
public class AppConfigurationManagerUnitTest {

    private static final String CONFIGURATION_FILE_NAME = "wallet.cdb";

    @Mock
    private Serializer<Wallet> mockWalletSerializer;

    @Mock
    private SymmetricKeyCipher mockSymmetricKeyCipher;

    @Mock
    private Serializer<EncryptedConfiguration> mockEncryptedConfigurationSerializer;

    @Mock
    private FileBasedDataAccessor mockFileBasedDataAccessor;

    @Test
    public void testSave() throws IOException {
        final AppConfigurationManager appConfigurationManager = new AppConfigurationManager(this.mockWalletSerializer, this.mockSymmetricKeyCipher,
                this.mockEncryptedConfigurationSerializer, this.mockFileBasedDataAccessor);
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

        appConfigurationManager.save(wallet, password);

        verify(this.mockWalletSerializer).serialize(wallet);
        verify(this.mockSymmetricKeyCipher).encrypt(serializedWallet, password);
        verify(this.mockEncryptedConfigurationSerializer).serialize(encryptedConfiguration);
        verify(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);
    }

    @Test
    public void testSave_WithIOException() throws IOException {
        final AppConfigurationManager appConfigurationManager = new AppConfigurationManager(this.mockWalletSerializer, this.mockSymmetricKeyCipher,
                this.mockEncryptedConfigurationSerializer, this.mockFileBasedDataAccessor);
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

        final ConfigurationManagerException configurationManagerException = Assertions.assertThrows(ConfigurationManagerException.class,
                () -> appConfigurationManager.save(wallet, password));
        Assertions.assertEquals("Error saving the configuration file!", configurationManagerException.getMessage());
        Assertions.assertTrue(configurationManagerException.getCause() instanceof IOException);

        verify(this.mockWalletSerializer).serialize(wallet);
        verify(this.mockSymmetricKeyCipher).encrypt(serializedWallet, password);
        verify(this.mockEncryptedConfigurationSerializer).serialize(encryptedConfiguration);
        verify(this.mockFileBasedDataAccessor).save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);
    }

    @Test
    public void testLoad() throws IOException {
        final AppConfigurationManager appConfigurationManager = new AppConfigurationManager(this.mockWalletSerializer, this.mockSymmetricKeyCipher,
                this.mockEncryptedConfigurationSerializer, this.mockFileBasedDataAccessor);
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


        final Wallet wallet = appConfigurationManager.load(password);
        Assertions.assertSame(expctedWallet, wallet);

        verify(this.mockFileBasedDataAccessor).getContents(CONFIGURATION_FILE_NAME);
        verify(this.mockEncryptedConfigurationSerializer).deserialize(serializedEncryptedConfiguration);
        verify(this.mockSymmetricKeyCipher).decrypt(encryptedConfiguration.getSerializedAndEncryptedWallet(), password,
                encryptedConfiguration.getInitialVector());
        verify(this.mockWalletSerializer).deserialize(serializedWallet);
    }

    @Test
    public void testLoad_WithIOException() throws IOException {
        final AppConfigurationManager appConfigurationManager = new AppConfigurationManager(this.mockWalletSerializer, this.mockSymmetricKeyCipher,
                this.mockEncryptedConfigurationSerializer, this.mockFileBasedDataAccessor);
        final String password = Make.aString();

        when(this.mockFileBasedDataAccessor.getContents(CONFIGURATION_FILE_NAME)).thenThrow(IOException.class);

        final ConfigurationManagerException configurationManagerException = Assertions.assertThrows(ConfigurationManagerException.class,
                () -> appConfigurationManager.load(password));
        Assertions.assertEquals("Error reading the configuration file!", configurationManagerException.getMessage());
        Assertions.assertTrue(configurationManagerException.getCause() instanceof IOException);

        verify(this.mockFileBasedDataAccessor).getContents(CONFIGURATION_FILE_NAME);
        verifyNoInteractions(this.mockEncryptedConfigurationSerializer);
        verifyNoInteractions(this.mockSymmetricKeyCipher);
        verifyNoInteractions(this.mockWalletSerializer);
    }
}
