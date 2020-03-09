package com.ak.cardstore;

import com.ak.cardstore.entities.CardProcessor;
import com.ak.cardstore.entities.CardType;
import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.pojo.Card;
import com.ak.cardstore.pojo.Wallet;
import com.google.common.collect.ImmutableSet;

import java.time.Month;
import java.time.Year;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class Make {

    /**
     * Returns a random {@link String}
     *
     * @return a random {@link String}
     */
    public static String aString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns a pseudorandom {@code int} value between the specified origin (inclusive) and the specified bound (exclusive).
     *
     * @param origin the least value returned
     * @param bound  the upper bound (exclusive)
     * @return a pseudorandom {@code int} value between the origin (inclusive) and the bound (exclusive)
     */
    public static int anInt(final int origin, final int bound) {
        return ThreadLocalRandom.current()
                .nextInt(origin, bound);
    }

    /**
     * Returns a pseudorandom {@link Year}
     *
     * @return a pseudorandom {@link Year}
     */
    public static Year aYear() {
        final int randomYear = anInt(Year.MIN_VALUE, Year.MAX_VALUE);
        return Year.of(randomYear);
    }

    /**
     * Returns a test {@link Wallet} with dummy data
     *
     * @return a test {@link Wallet} with dummy data
     */
    public static Wallet aWallet() {
        final Set<Card> cards = ImmutableSet.of(Card.builder()
                .cardProcessor(getRandomElement(CardProcessor.values()))
                .cardType(getRandomElement(CardType.values()))
                .cvv(aCVV())
                .expiryDate(MonthYear.builder()
                        .month(getRandomElement(Month.values()))
                        .year(aYear())
                        .build())
                .nameOnCard(aString())
                .number(aValidCardNumber())
                .pin(aPin())
                .build());

        final Wallet testWallet = Wallet.builder()
                .cards(cards)
                .build();

        return testWallet;
    }

    /**
     * Returns a valid card number with 16 digits
     *
     * @return a valid card number
     */
    public static String aValidCardNumber() {
        // Any 16 digit string
        return "1234567887654321";
    }

    /**
     * Returns a card number with invalid number of digits
     *
     * @return a card number with invalid number of digits
     */
    public static String anInvalidCardNumber() {
        // Any 16 digit string
        return "123456787654321";
    }

    /**
     * Returns a 3 digit CVV
     *
     * @return a 3 digit CVV
     */
    public static String aCVV() {
        // Any 3 digit string
        return String.valueOf(anInt(100, 1000));
    }

    /**
     * Returns a 4 to 6 digit pin
     *
     * @return a 4 to 6 digit pin
     */
    public static String aPin() {
        // Any 4 to 6 digit string
        return String.valueOf(anInt(1000, 1000000));
    }

    private static <ARRAY_ELEMENT_TYPE> ARRAY_ELEMENT_TYPE getRandomElement(final ARRAY_ELEMENT_TYPE[] array) {
        final int numOfElements = array.length;
        final int randomElementIndex = anInt(0, numOfElements);

        return array[randomElementIndex];
    }
}
