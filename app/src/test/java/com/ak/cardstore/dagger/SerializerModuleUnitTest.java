package com.ak.cardstore.dagger;

import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.pojo.Wallet;
import com.ak.cardstore.serialization.Serializer;

import org.junit.jupiter.api.Test;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */

public class SerializerModuleUnitTest {

    private static final TestComponent SERIALIZER_MODULE_TEST_COMPONENT = DaggerSerializerModuleUnitTest_TestComponent.create();

    @Test
    public void testProvideUserSerializer() {
        final Serializer<User> userSerializer = SERIALIZER_MODULE_TEST_COMPONENT.provideUserSerializer();
        final Serializer<User> userSerializerOther = SERIALIZER_MODULE_TEST_COMPONENT.provideUserSerializer();

        assertNotNull(userSerializer);
        assertNotNull(userSerializerOther);
        assertEquals(userSerializer, userSerializerOther);
    }

    @Test
    public void testProvideWalletSerializer() {
        final Serializer<Wallet> walletSerializer = SERIALIZER_MODULE_TEST_COMPONENT.provideWalletSerializer();
        final Serializer<Wallet> walletSerializerOther = SERIALIZER_MODULE_TEST_COMPONENT.provideWalletSerializer();

        assertNotNull(walletSerializer);
        assertNotNull(walletSerializerOther);
        assertEquals(walletSerializer, walletSerializerOther);
    }

    @Test
    public void testProvideEncryptedConfigurationSerializer() {
        final Serializer<EncryptedConfiguration> encryptedConfigurationSerializer = SERIALIZER_MODULE_TEST_COMPONENT.provideEncryptedConfigurationSerializer();
        final Serializer<EncryptedConfiguration> encryptedConfigurationSerializerOther = SERIALIZER_MODULE_TEST_COMPONENT.provideEncryptedConfigurationSerializer();

        assertNotNull(encryptedConfigurationSerializer);
        assertNotNull(encryptedConfigurationSerializerOther);
        assertEquals(encryptedConfigurationSerializer, encryptedConfigurationSerializerOther);
    }

    @Singleton
    @Component(modules = SerializerModule.class)
    public interface TestComponent {

        @Named("userSerializer")
        Serializer<User> provideUserSerializer();

        @Named("walletSerializer")
        Serializer<Wallet> provideWalletSerializer();

        @Named("encryptedConfigurationSerializer")
        Serializer<EncryptedConfiguration> provideEncryptedConfigurationSerializer();
    }
}
