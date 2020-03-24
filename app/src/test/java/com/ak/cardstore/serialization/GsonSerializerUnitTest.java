package com.ak.cardstore.serialization;

import com.ak.cardstore.Make;
import com.ak.cardstore.pojo.EncryptedConfiguration;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.pojo.Wallet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Abhishek
 */

public class GsonSerializerUnitTest {

    @Test
    public void testSerialize_WithNullType() {
        Assertions.assertThrows(NullPointerException.class, () -> new GsonSerializer<>(null));
    }

    @Test
    public void testSerialize_WithNull() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final String serializedWallet = walletSerializer.serialize(null);
        assertNull(serializedWallet);
    }

    @Test
    public void testDeserialize_WithNull() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final Wallet wallet = walletSerializer.deserialize(null);
        assertNull(wallet);
    }

    @Test
    public void testSerializeAndDeserialize() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final Wallet testWallet = Wallet.builder()
                .build();

        final Wallet wallet = walletSerializer.deserialize(walletSerializer.serialize(testWallet));
        assertEquals(testWallet, wallet);
    }

    @Test
    public void testSerializeAndDeserialize_WithUser() {
        final Serializer<User> userSerializer = new GsonSerializer<>(User.class);

        final User testUser = Make.aValidUser();

        final User user = userSerializer.deserialize(userSerializer.serialize(testUser));
        assertEquals(testUser, user);
    }

    @Test
    public void testSerializeAndDeserialize_WithEncryptedConfiguration() {
        final Serializer<EncryptedConfiguration> encryptedConfigurationSerializer = new GsonSerializer<>(EncryptedConfiguration.class);

        final EncryptedConfiguration testEncryptedConfiguration = Make.anEncryptedConfiguration();

        final EncryptedConfiguration encryptedConfiguration = encryptedConfigurationSerializer.deserialize(encryptedConfigurationSerializer.serialize(testEncryptedConfiguration));
        assertEquals(testEncryptedConfiguration, encryptedConfiguration);
    }
}
