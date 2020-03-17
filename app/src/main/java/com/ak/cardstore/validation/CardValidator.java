package com.ak.cardstore.validation;

import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.exception.CardValidationException;
import com.ak.cardstore.pojo.Card;

import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;

import lombok.NonNull;

/**
 * A class to validate the {@link Card} data
 *
 * @author Abhishek
 */

public class CardValidator {

    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int CVV_LENGTH_3 = 3;
    private static final int CVV_LENGTH_4 = 4;
    private static final int PIN_LENGTH_LOWER_BOUND = 4;
    private static final int PIN_LENGTH_UPPER_BOUND = 6;

    /**
     * Validates the card number to be a 16 digit number
     *
     * @param cardNumber card number
     */
    public void validateCardNumber(@NonNull final String cardNumber) {
        if (cardNumber.length() != CARD_NUMBER_LENGTH) {
            throw new CardValidationException(String.format("Card number %s must be of %s digits!", cardNumber, CARD_NUMBER_LENGTH));
        }

        if (!StringUtils.isNumeric(cardNumber)) {
            throw new CardValidationException(String.format("Card number %s must not contain non-numberic characters!", cardNumber));
        }
    }

    /**
     * Validates the name of card for not being an empty string.
     *
     * @param nameOnCard name on the card
     */
    public void validateNameOnCard(@NonNull final String nameOnCard) {
        if (nameOnCard.isEmpty()) {
            throw new CardValidationException("Name on card cannot be empty!");
        }
    }

    /**
     * Validates expiry date for the month and year to be current or in the future.
     *
     * @param cardExpiryDate card expiry date
     */
    public void validateExpiryDate(@NonNull final MonthYear cardExpiryDate) {
        final ZonedDateTime currentDate = ZonedDateTime.now();

        if (cardExpiryDate.isBefore(currentDate)) {
            throw new CardValidationException("Card expiry date cannot be in the past!");
        }
    }

    /**
     * Validates the CVV to be a 3 digit number
     *
     * @param cvv card CVV
     */
    public void validateCVV(@NonNull final String cvv) {
        final int cvvLength = cvv.length();
        if (cvvLength != CVV_LENGTH_3 && cvvLength != CVV_LENGTH_4) {
            throw new CardValidationException(String.format("CVV %s must be of %s or %s digits!", cvv, CVV_LENGTH_3, CVV_LENGTH_4));
        }

        if (!StringUtils.isNumeric(cvv)) {
            throw new CardValidationException(String.format("CVV %s must not contain non-numberic characters!", cvv));
        }
    }

    /**
     * Validates the pin to be a 4 to 6 digit number
     *
     * @param pin card pin
     */
    public void validatePin(final String pin) {
        if (null == pin) {
            return;
        }

        final int pinLength = pin.length();
        if (pinLength < PIN_LENGTH_LOWER_BOUND || pinLength > PIN_LENGTH_UPPER_BOUND) {
            throw new CardValidationException(String.format("Pin %s must be between %s and %s digits!", pin, PIN_LENGTH_LOWER_BOUND,
                    PIN_LENGTH_UPPER_BOUND));
        }

        if (!StringUtils.isNumeric(pin)) {
            throw new CardValidationException(String.format("Pin %s must not contain non-numberic characters!", pin));
        }
    }
}
