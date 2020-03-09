package com.ak.cardstore.serialization;

import com.ak.cardstore.pojo.Wallet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Abhishek
 */

public class GsonSerializerTest {

    @Test
    public void testSerialize_WithNull() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final String serializedWallet = walletSerializer.serialize(null);
        Assertions.assertNull(serializedWallet);
    }

    @Test
    public void testDeserialize_WithNull() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final Wallet wallet = walletSerializer.deserialize(null);
        Assertions.assertNull(wallet);
    }

    @Test
    public void testSerializeAndDeserialize() {
        final Serializer<Wallet> walletSerializer = new GsonSerializer<>(Wallet.class);

        final Wallet testWallet = Wallet.builder()
                .build();

        final Wallet wallet = walletSerializer.deserialize(null);
        Assertions.assertNull(wallet);
    }
}
