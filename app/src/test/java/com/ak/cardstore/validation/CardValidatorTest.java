package com.ak.cardstore.validation;

import com.ak.cardstore.Make;
import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.exception.CardValidationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.ZonedDateTime;

/**
 * @author Abhishek
 */

public class CardValidatorTest {

    @Test
    public void testValidateCardNumber_WithNonNumericCardNumber() {
        final CardValidator cardValidator = new CardValidator();
        final String cardNumber = Make.aString();

        Assertions.assertThrows(CardValidationException.class,
                () -> cardValidator.validateCardNumber(cardNumber),
                "Card number " + cardNumber + " must not contain non-numberic characters!");
    }

    @Test
    public void testValidateCardNumber_WithInvalidCardNumberLength() {
        final CardValidator cardValidator = new CardValidator();
        final String cardNumber = Make.anInvalidCardNumber();

        Assertions.assertThrows(CardValidationException.class,
                () -> cardValidator.validateCardNumber(cardNumber),
                "Card number " + cardNumber + " must be of 16 digits!");
    }

    @Test
    public void testValidateCardNumber_WithValidCardNumber() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateCardNumber(Make.aValidCardNumber());
    }

    @Test
    public void testValidateNameOnCard_WithEmptyName() {
        final CardValidator cardValidator = new CardValidator();

        Assertions.assertThrows(CardValidationException.class,
                () -> cardValidator.validateNameOnCard(StringUtils.EMPTY),
                "Name on card cannot be empty!");
    }

    @Test
    public void testValidateNameOnCard_WithValidName() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateNameOnCard(Make.aString());
    }

    @Test
    public void testValidateExpiryDate_WithCurrentMonthAndYear() {
        final CardValidator cardValidator = new CardValidator();

        final ZonedDateTime currentDate = ZonedDateTime.now();

        cardValidator.validateExpiryDate(MonthYear.of(currentDate.getMonth(), currentDate.getYear()));
    }

    @Test
    public void testValidateExpiryDate_WithFutureMonthAndYear() {
        final CardValidator cardValidator = new CardValidator();

        final ZonedDateTime currentDate = ZonedDateTime.now().plusDays(590);

        cardValidator.validateExpiryDate(MonthYear.of(currentDate.getMonth(), currentDate.getYear()));
    }

    @Test
    public void testValidateExpiryDate_WithPastMonthAndYear() {
        final CardValidator cardValidator = new CardValidator();
        final ZonedDateTime currentDate = ZonedDateTime.now().minusMonths(1);

        Assertions.assertThrows(CardValidationException.class,
                () -> cardValidator.validateExpiryDate(MonthYear.of(Month.JANUARY, currentDate.getYear())),
                "Card expiry date cannot be in the past!");
    }
}
