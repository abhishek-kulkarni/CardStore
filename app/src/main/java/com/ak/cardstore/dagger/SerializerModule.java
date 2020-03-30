package com.ak.cardstore.dagger;

import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.GsonSerializer;
import com.ak.cardstore.serialization.Serializer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link Serializer<User>}, {@link Serializer<Wallet>} and {@link Serializer<EncryptedConfiguration>}.
 *
 * @author Abhishek
 */

@Module
public class SerializerModule {

    /**
     * Provides {@link Serializer<User>}
     *
     * @return {@link Serializer<User>}
     */
    @Provides
    @Singleton
    @Named("userSerializer")
    public Serializer<User> provideUserSerializer() {
        return new GsonSerializer<>(User.class);
    }

    /**
     * Provides {@link Serializer<Wallet>}
     *
     * @return {@link Serializer<Wallet>}
     */
    @Provides
    @Singleton
    @Named("walletSerializer")
    public Serializer<Wallet> provideWalletSerializer() {
        return new GsonSerializer<>(Wallet.class);
    }

    /**
     * Provides {@link Serializer<EncryptedConfiguration>}
     *
     * @return {@link Serializer<EncryptedConfiguration>}
     */
    @Provides
    @Singleton
    @Named("encryptedConfigurationSerializer")
    public Serializer<EncryptedConfiguration> provideEncryptedConfiguration() {
        return new GsonSerializer<>(EncryptedConfiguration.class);
    }
}
