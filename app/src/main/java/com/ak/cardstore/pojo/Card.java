package com.ak.cardstore.pojo;

import com.ak.cardstore.entities.CardProcessor;
import com.ak.cardstore.entities.CardType;
import com.ak.cardstore.entities.MonthYear;
import com.ak.cardstore.validation.CardValidator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A POJO to represent a credit or debit card
 *
 * @author Abhishek K.
 */

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Card {

    /**
     * Type of card: Credit or Debit
     */
    @NonNull
    private final CardType cardType;

    /**
     * Card processor: Visa, MasterCard, AmEx or Discover
     */
    @NonNull
    private final CardProcessor cardProcessor;

    /**
     * Company or institute that issues the card. Example: Bank of America, Chase Bank
     */
    @NonNull
    private final String cardIssuer;

    /**
     * Optional friendly name of the card like Cash back card, travel card
     */
    private final String friendlyName;

    /**
     * Card number
     */
    @NonNull
    private final String number;

    /**
     * Name of the owner on the card
     */
    @NonNull
    private final String nameOnCard;

    /**
     * Card expiry date
     */
    @NonNull
    private final MonthYear expiryDate;

    /**
     * CVV number for the card
     */
    @NonNull
    private final String cvv;

    /**
     * Optional pin for the card
     */
    private final String pin;

    /**
     * Returns the card {@link Builder}
     *
     * @return card {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final CardValidator CARD_VALIDATOR = new CardValidator();

        @NonNull
        private CardType cardType;

        @NonNull
        private CardProcessor cardProcessor;

        @NonNull
        private String cardIssuer;

        private String friendlyName;

        @NonNull
        private String number;

        @NonNull
        private String nameOnCard;

        @NonNull
        private MonthYear expiryDate;

        @NonNull
        private String cvv;

        private String pin;

        public Builder cardType(final CardType cardType) {
            this.cardType = cardType;
            return this;
        }

        public Builder cardProcessor(final CardProcessor cardProcessor) {
            this.cardProcessor = cardProcessor;
            return this;
        }

        public Builder cardIssuer(final String cardIssuer) {
            this.cardIssuer = cardIssuer;
            return this;
        }

        public Builder friendlyName(final String friendlyName) {
            this.friendlyName = friendlyName;
            return this;
        }

        public Builder number(final String number) {
            CARD_VALIDATOR.validateCardNumber(number);
            this.number = number;
            return this;
        }

        public Builder nameOnCard(final String nameOnCard) {
            CARD_VALIDATOR.validateNameOnCard(nameOnCard);
            this.nameOnCard = nameOnCard;
            return this;
        }

        public Builder expiryDate(final MonthYear expiryDate) {
            CARD_VALIDATOR.validateExpiryDate(expiryDate);
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder cvv(final String cvv) {
            CARD_VALIDATOR.validateCVV(cvv);
            this.cvv = cvv;
            return this;
        }

        public Builder pin(final String pin) {
            CARD_VALIDATOR.validatePin(pin);
            this.pin = pin;
            return this;
        }

        /**
         * Builds and returns an instance of {@link Card}.
         *
         * @return an instance of {@link Card}
         */
        public Card build() {
            return new Card(this.cardType, this.cardProcessor, this.cardIssuer, this.friendlyName, this.number, this.nameOnCard,
                    this.expiryDate, this.cvv, this.pin);
        }
    }
}
