package com.ak.cardstore.validation;

import com.ak.cardstore.exception.UserValidationException;

import java.util.regex.Pattern;

import lombok.NonNull;

/**
 * A utility class to validate the {@link com.ak.cardstore.pojo.User} password
 *
 * @author Abhishek
 */

public class PasswordValidator {

    private static final int PASSWORD_MINIMUM_LENGTH = 8;
    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    /**
     * Checks the password for following conditions
     * 1. Minimum length
     * 2. At least 1 capital letter
     * 3. At least 1 small letter
     * 4. At least 1 digit
     * 5. At least 1 special symbol
     *
     * @param password pasword to validate
     */
    public void validatePassword(@NonNull final String password) {
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
