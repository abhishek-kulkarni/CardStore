package com.ak.cardstore.validation;

import com.ak.cardstore.Make;
import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.exception.CardValidationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Abhishek
 */

public class CardValidatorTest {

    @Test
    public void testValidateCardNumber_WithNonNumericCardNumber() {
        final CardValidator cardValidator = new CardValidator();
        final String cardNumber = Make.aNonNumericCardNumber();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateCardNumber(cardNumber));
        assertEquals("Card number " + cardNumber + " must not contain non-numberic characters!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateCardNumber_WithInvalidCardNumberLength() {
        final CardValidator cardValidator = new CardValidator();
        final String cardNumber = Make.aCardNumberWithInvalidLength();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateCardNumber(cardNumber));
        assertEquals("Card number " + cardNumber + " must be of 16 digits!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateCardNumber_WithValidCardNumber() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateCardNumber(Make.aValidCardNumber());
    }

    @Test
    public void testValidateNameOnCard_WithEmptyName() {
        final CardValidator cardValidator = new CardValidator();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateNameOnCard(StringUtils.EMPTY));
        assertEquals("Name on card cannot be empty!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateNameOnCard_WithValidName() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateNameOnCard(Make.aString());
    }

    @Test
    public void testValidateExpiryDate_WithValidExpiryDate() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateExpiryDate(Make.aValidExpiryDate());
    }

    @Test
    public void testValidateExpiryDate_WithPastMonthAndYear() {
        final CardValidator cardValidator = new CardValidator();
        final MonthYear expiryDate = Make.anExpiryDateInThePast();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateExpiryDate(expiryDate));
        assertEquals("Card expiry date cannot be in the past!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateCVV_WithNonNumericCVV() {
        final CardValidator cardValidator = new CardValidator();
        final String cvv = Make.aNonNumericCVV();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateCVV(cvv));
        assertEquals("CVV " + cvv + " must not contain non-numberic characters!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateCVV_WithInvalidCVVLength() {
        final CardValidator cardValidator = new CardValidator();
        final String cvv = Make.aCVVWithInvalidCVVLength();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validateCVV(cvv));
        assertEquals("CVV " + cvv + " must be of 3 or 4 digits!", cardValidationException.getMessage());
    }

    @Test
    public void testValidateCVV_WithValidCVV() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validateCVV(Make.aValidCVV());
    }

    @Test
    public void testValidatePin_WithNullPin() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validatePin(null);
    }

    @Test
    public void testValidatePin_WithInvalidPinLength() {
        final CardValidator cardValidator = new CardValidator();
        final String pin = Make.aPinWithInvalidLength();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validatePin(pin));
        assertEquals("Pin " + pin + " must be between 4 and 6 digits!", cardValidationException.getMessage());
    }

    @Test
    public void testValidatePin_WithNonNumericPin() {
        final CardValidator cardValidator = new CardValidator();
        final String pin = Make.aNonNumericPin();

        final CardValidationException cardValidationException = assertThrows(CardValidationException.class,
                () -> cardValidator.validatePin(pin));
        assertEquals("Pin " + pin + " must not contain non-numberic characters!", cardValidationException.getMessage());
    }

    @Test
    public void testValidatePin_WithValidPin() {
        final CardValidator cardValidator = new CardValidator();

        cardValidator.validatePin(Make.aValidPin());
    }
}
