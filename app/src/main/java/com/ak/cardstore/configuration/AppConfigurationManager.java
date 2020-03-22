package com.ak.cardstore.configuration;

import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.exception.ConfigurationManagerException;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * A class to manage the application configuration.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class AppConfigurationManager {

    private static final String CONFIGURATION_FILE_NAME = "wallet.cdb";

    private static final String CONFIGURATION_SAVE_ERROR = "Error saving the configuration file!";
    private static final String CONFIGURATION_READ_ERROR = "Error reading the configuration file!";

    private final Serializer<Wallet> walletSerializer;
    private final SymmetricKeyCipher symmetricKeyCipher;
    private final Serializer<EncryptedConfiguration> encryptedConfigurationSerializer;
    private final FileBasedDataAccessor fileBasedDataAccessor;

    /**
     * Saves the application configuration by executing the following steps
     * 1. Serialize the wallet
     * 2. Encrypt the wallet
     * 3. Serialize the encrypted wallet and initial vector
     * 4. Save the serialized encrypted wallet
     *
     * @param wallet wallet to save
     */
    public void save(final Wallet wallet, final String password) {
        final String serializedWallet = this.walletSerializer.serialize(wallet);

        final Pair<String, String> serializedEncryptedWalletAndIvPair = this.symmetricKeyCipher.encrypt(serializedWallet, password);
        final EncryptedConfiguration encryptedConfiguration = EncryptedConfiguration.builder()
                .serializedAndEncryptedWallet(serializedEncryptedWalletAndIvPair.getKey())
                .initialVector(serializedEncryptedWalletAndIvPair.getValue())
                .build();

        final String serializedEncryptedConfiguration = this.encryptedConfigurationSerializer.serialize(encryptedConfiguration);

        try {
            this.fileBasedDataAccessor.save(CONFIGURATION_FILE_NAME, serializedEncryptedConfiguration);
        } catch (final IOException e) {
            log.error(CONFIGURATION_SAVE_ERROR, e);
            throw new ConfigurationManagerException(CONFIGURATION_SAVE_ERROR, e);
        }
    }

    public Wallet load(final String password) {
        final String serializedEncryptedConfiguration;
        try {
            serializedEncryptedConfiguration = this.fileBasedDataAccessor.getContents(CONFIGURATION_FILE_NAME);
        } catch (final IOException e) {
            log.error(CONFIGURATION_READ_ERROR, e);
            throw new ConfigurationManagerException(CONFIGURATION_READ_ERROR, e);
        }

        final EncryptedConfiguration encryptedConfiguration = this.encryptedConfigurationSerializer.deserialize(serializedEncryptedConfiguration);

        final String serializedWallet = this.symmetricKeyCipher.decrypt(encryptedConfiguration.getSerializedAndEncryptedWallet(),
                password, encryptedConfiguration.getInitialVector());

        final Wallet wallet = this.walletSerializer.deserialize(serializedWallet);
        return wallet;
    }
}
