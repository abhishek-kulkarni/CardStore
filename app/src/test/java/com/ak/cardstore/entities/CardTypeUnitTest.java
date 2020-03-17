package com.ak.cardstore.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * @author Abhishek
 */

public class CardTypeUnitTest {

    @ParameterizedTest
    @MethodSource("testArgumentsProvider")
    public void testCardProcessor(final CardType cardType, final String expectedDisplayName) {
        Assertions.assertEquals(expectedDisplayName, cardType.getDisplayName());
    }

    private static Stream<Arguments> testArgumentsProvider() {
        return Stream.of(
                Arguments.arguments(CardType.CREDIT, "Credit"),
                Arguments.arguments(CardType.DEBIT, "Debit")
        );
    }
}
