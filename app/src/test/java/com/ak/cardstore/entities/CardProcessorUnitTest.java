package com.ak.cardstore.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * @author Abhishek
 */

public class CardProcessorUnitTest {

    @ParameterizedTest
    @MethodSource("testArgumentsProvider")
    public void testCardProcessor(final CardProcessor cardProcessor, final String expectedDisplayName) {
        Assertions.assertEquals(expectedDisplayName, cardProcessor.getDisplayName());
    }

    private static Stream<Arguments> testArgumentsProvider() {
        return Stream.of(
                Arguments.arguments(CardProcessor.AmEx, "American Express"),
                Arguments.arguments(CardProcessor.Visa, "Visa"),
                Arguments.arguments(CardProcessor.MasterCard, "Master Card"),
                Arguments.arguments(CardProcessor.Discover, "Discover")
        );
    }
}
