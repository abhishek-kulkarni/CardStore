package com.ak.cardstore.pojo;

import com.ak.cardstore.exception.UserValidationException;

import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * A POJO to represent an app user.
 *
 * @author Abhishek
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @NonNull
    private final String password;

    /**
     * Returns the user {@link Builder}
     *
     * @return user {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for {@link User}
     */
    public static class Builder {

        private static final int PASSWORD_MINIMUM_LENGTH = 8;
        private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
        private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
        private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
        private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

        @NonNull
        private String password;

        /**
         * Updates the user password and returns the builder for chaining.
         *
         * @param password user password
         * @return builder
         */
        public Builder password(final String password) {
            this.validatePassword(password);
            this.password = password;
            return this;
        }

        /**
         * Builds and returns the {@link User}
         *
         * @return {@link User}
         */
        public User build() {
            return new User(this.password);
        }

        /*
         * Check for following conditions
         * 1. Minimum length
         * 2. At least 1 capital letter
         * 3. At least 1 small letter
         * 4. At least 1 digit
         * 5. At least 1 special symbol
         */
        private void validatePassword(final String password) {
            if (password.length() < PASSWORD_MINIMUM_LENGTH) {
                throw new UserValidationException("Password must be at least 8 characters in length!");
            }

            if (!UPPER_CASE_PATTERN.matcher(password).find()) {
                throw new UserValidationException("Password must contain at least one upper case letter!");
            }

            if (!LOWER_CASE_PATTERN.matcher(password).find()) {
                throw new UserValidationException("Password must contain at least one lower case letter!");
            }

            if (!DIGIT_PATTERN.matcher(password).find()) {
                throw new UserValidationException("Password must contain at least one digit!");
            }

            if (!SPECIAL_CHARACTER_PATTERN.matcher(password).find()) {
                throw new UserValidationException("Password must contain at least one special character!");
            }
        }
    }
}
