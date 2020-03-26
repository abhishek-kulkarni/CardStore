package com.ak.cardstore.configuration;

import android.util.Log;

import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.exception.ConfigurationManagerException;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

import lombok.AllArgsConstructor;

/**
 * A class to manage the application configuration.
 *
 * @author Abhishek
 */

@AllArgsConstructor
public class AppConfigurationManager {

    private static final String LOG_TAG = AppConfigurationManager.class.getSimpleName();

    private static final String CONFIGURATION_FILE_NAME = "com.ak.cardstore.wallet.cdb";

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
     * @param wallet   wallet to save
     * @param password password to encrypt the configuration with
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
            Log.e(LOG_TAG, CONFIGURATION_SAVE_ERROR, e);
            throw new ConfigurationManagerException(CONFIGURATION_SAVE_ERROR, e);
        }
    }

    /**
     * Loads the application configuration by executing the following steps
     * 1. Read the serialized encrypted wallet
     * 2. Deserialize the serialized encrypted wallet
     * 3. decrypt the encrypted wallet
     * 4. deserialize the serialized wallet
     *
     * @param password password to decrypt the configuration with
     * @return Wallet
     */
    public Wallet load(final String password) {
        final String serializedEncryptedConfiguration;
        try {
            serializedEncryptedConfiguration = this.fileBasedDataAccessor.getContents(CONFIGURATION_FILE_NAME);
        } catch (final IOException e) {
            Log.e(LOG_TAG, CONFIGURATION_READ_ERROR, e);
            throw new ConfigurationManagerException(CONFIGURATION_READ_ERROR, e);
        }

        final EncryptedConfiguration encryptedConfiguration = this.encryptedConfigurationSerializer.deserialize(serializedEncryptedConfiguration);

        final String serializedWallet = this.symmetricKeyCipher.decrypt(encryptedConfiguration.getSerializedAndEncryptedWallet(),
                password, encryptedConfiguration.getInitialVector());

        final Wallet wallet = this.walletSerializer.deserialize(serializedWallet);
        return wallet;
    }
}
