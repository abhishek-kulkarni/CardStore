package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairCipher;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.configuration.AppConfigurationManager;
import com.ak.cardstore.configuration.UserConfigurationManager;
import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.dao.SharedPreferencesDataAccessor;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link UserConfigurationManager} and {@link AppConfigurationManager}.
 *
 * @author Abhishek
 */

@Module(includes = {SerializerModule.class,
        AsymmetricCipherModule.class,
        SymmetricCipherModule.class,
        DaoModule.class})
public class ConfigurationModule {

    /**
     * Provides {@link UserConfigurationManager}
     *
     * @return {@link UserConfigurationManager}
     */
    @Provides
    @Singleton
    public UserConfigurationManager provideUserConfigurationManager(
            @Named("userSerializer") final Serializer<User> userSerializer,
            final AsymmetricKeyPairCipher asymmetricKeyPairCipher,
            final SharedPreferencesDataAccessor sharedPreferencesDataAccessor) {
        return new UserConfigurationManager(userSerializer, asymmetricKeyPairCipher, sharedPreferencesDataAccessor);
    }

    /**
     * Provides {@link AppConfigurationManager}.
     *
     * @return {@link AppConfigurationManager}
     */
    @Provides
    @Singleton
    public AppConfigurationManager provideAppConfigurationManager(
            @Named("walletSerializer") final Serializer<Wallet> walletSerializer,
            final SymmetricKeyCipher symmetricKeyCipher,
            @Named("encryptedConfigurationSerializer") final Serializer<EncryptedConfiguration> encryptedConfigurationSerializer,
            final FileBasedDataAccessor fileBasedDataAccessor) {
        return new AppConfigurationManager(walletSerializer, symmetricKeyCipher, encryptedConfigurationSerializer, fileBasedDataAccessor);
    }
}
