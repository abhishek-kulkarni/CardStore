package com.ak.cardstore.pojo;

import com.ak.cardstore.Make;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Abhishek
 */

public class CardUnitTest {

    @Test
    public void testCard() {
        final Card card = Make.aCard();
        Assertions.assertNotNull(card);
    }
}
