package com.ak.cardstore.pojo;

import com.ak.cardstore.Make;
import com.google.common.collect.ImmutableSet;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Abhishek
 */

public class WalletUnitTest {

    @Test
    public void testWallet() {
        final Card card = Make.aCard();
        final Wallet wallet = Wallet.builder()
                .cards(ImmutableSet.of(card))
                .build();
        Assertions.assertNotNull(wallet);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(wallet.getCards()));
        Assertions.assertEquals(card, wallet.getCards().iterator().next());
    }
}
