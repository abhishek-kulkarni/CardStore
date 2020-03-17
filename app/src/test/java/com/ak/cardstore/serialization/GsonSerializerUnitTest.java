package com.ak.cardstore.serialization;

import com.ak.cardstore.pojo.Wallet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Abhishek
 */

public class GsonSerializerUnitTest {

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
}
