package com.ak.cardstore;

import com.ak.cardstore.entities.CardProcessor;
import com.ak.cardstore.entities.CardType;
import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.pojo.Card;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.pojo.Wallet;
import com.google.common.collect.ImmutableSet;

import java.time.Month;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class Make {

    /**
     * Returns a pseudorandom {@link String}
     *
     * @return a pseudorandom {@link String}
     */
    public static String aString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns a pseudorandom {@code boolean} value.
     *
     * @return a pseudorandom {@code boolean} value
     */
    public static boolean aBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
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
     * Returns a pseudorandom {@link Month}
     *
     * @return a pseudorandom {@link Month}
     */
    public static Month aMonth() {
        return getRandomElement(Month.values());
    }

    /**
     * Returns a test {@link Wallet} with dummy data
     *
     * @return a test {@link Wallet} with dummy data
     */
    public static Wallet aWallet() {
        return Wallet.builder()
                .cards(ImmutableSet.of(aCard()))
                .build();
    }

    /**
     * Return a test {@link Card}.
     *
     * @return a test {@link Card}.
     */
    public static Card aCard() {
        return Card.builder()
                .cardProcessor(getRandomElement(CardProcessor.values()))
                .cardType(getRandomElement(CardType.values()))
                .cardIssuer(Make.aString())
                .friendlyName(Make.aString())
                .cvv(aValidCVV())
                .expiryDate(aValidExpiryDate())
                .nameOnCard(aString())
                .number(aValidCardNumber())
                .pin(aValidPin())
                .build();
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
    public static String aCardNumberWithInvalidLength() {
        if (aBoolean()) {
            // Any 17+ digit string
            return "12345678987654321";
        }

        // Any string with 15 or less digits
        return "123456787654321";
    }

    /**
     * Returns a card number with non-numeric characters
     *
     * @return a card number with non-numeric characters
     */
    public static String aNonNumericCardNumber() {
        // Any non 16 digit string
        return "12345678a7654321";
    }

    /**
     * Returns a 3 digit CVV
     *
     * @return a 3 digit CVV
     */
    public static String aValidCVV() {
        // Any 3 or 4 digit number
        return String.valueOf(anInt(100, 10000));
    }

    /**
     * Returns a CVV with non-3 digit number
     *
     * @return a CVV with non-3 digit number
     */
    public static String aCVVWithInvalidCVVLength() {
        // Any non 3 digit number
        if (aBoolean()) {
            return String.valueOf(anInt(0, 100));
        }

        return String.valueOf(anInt(10000, 100000));
    }

    /**
     * Returns a CVV with non-numeric characters
     *
     * @return a CVV with non-numeric characters
     */
    public static String aNonNumericCVV() {
        if (aBoolean()) {
            // Any non-numeric 3 digit string
            return "1a2";
        }

        // Any non-numeric 4 digit string
        return "123a";
    }

    /**
     * Returns a 4 to 6 digit pin
     *
     * @return a 4 to 6 digit pin
     */
    public static String aValidPin() {
        // Any 4 to 6 digit string
        return String.valueOf(anInt(1000, 1000000));
    }

    /**
     * Returns a pin with length less than 4 or greater than 6
     *
     * @return a pin with length less than 4 or greater than 6
     */
    public static String aPinWithInvalidLength() {
        if (aBoolean()) {
            // Any string with 3 or less digits
            return String.valueOf(anInt(0, 1000));
        }

        // Any string with 7 or more digits
        return String.valueOf(anInt(1000000, 10000000));
    }

    /**
     * Returns a pin with non-numeric characters
     *
     * @return a pin with non-numeric characters
     */
    public static String aNonNumericPin() {
        if (aBoolean()) {
            // Any 4 digit string with non-numeric characters
            return "123a";
        }

        if (aBoolean()) {
            // Any 5 digit string with non-numeric characters
            return "1234a";
        }

        // Any 6 digit string with non-numeric characters
        return "12345a";
    }

    /**
     * Returns an expiry date with current or future {@link MonthYear}.
     *
     * @return an expiry date with current or future {@link MonthYear}.
     */
    public static MonthYear aValidExpiryDate() {
        final ZonedDateTime zonedDateTime;
        if (aBoolean()) {
            zonedDateTime = ZonedDateTime.now().plusDays(590);
        } else {
            zonedDateTime = ZonedDateTime.now();
        }

        return MonthYear.of(zonedDateTime.getMonth(), zonedDateTime.getYear());
    }

    /**
     * Returns an expiry date in the past.
     *
     * @return an expiry date in the past
     */
    public static MonthYear anExpiryDateInThePast() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.now().minusMonths(1);
        return MonthYear.of(zonedDateTime.getMonth(), zonedDateTime.getYear());
    }

    /**
     * Returns a user with a valid pseudorandom password.
     *
     * @return a user with a valid pseudorandom password
     */
    public static User aValidUser() {
        return User.builder()
                .password("Password123!")
                .build();
    }

    private static <ARRAY_ELEMENT_TYPE> ARRAY_ELEMENT_TYPE getRandomElement(final ARRAY_ELEMENT_TYPE[] array) {
        final int numOfElements = array.length;
        final int randomElementIndex = anInt(0, numOfElements);

        return array[randomElementIndex];
    }
}
