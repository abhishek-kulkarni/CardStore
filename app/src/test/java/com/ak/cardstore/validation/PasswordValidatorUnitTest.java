package com.ak.cardstore.validation;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.UserValidationException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Abhishek
 */

public class PasswordValidatorUnitTest {

    @Test
    public void testUser_WithPasswordLengthLessThan8() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> passwordValidator.validatePassword(Make.aString().substring(0, 5)));
        assertEquals("Password must be at least 8 characters in length!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutUpperCaseLetter() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> passwordValidator.validatePassword(Make.aString().toLowerCase()));
        assertEquals("Password must contain at least one upper case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutLowerCaseLetter() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> passwordValidator.validatePassword(Make.aString().toUpperCase()));
        assertEquals("Password must contain at least one lower case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutDigit() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> passwordValidator.validatePassword("Password!"));
        assertEquals("Password must contain at least one digit!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutSpecialCharacter() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> passwordValidator.validatePassword("Password123"));
        assertEquals("Password must contain at least one special character!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithValidPassword() {
        final PasswordValidator passwordValidator = new PasswordValidator();

        passwordValidator.validatePassword("Password123!");
    }
}
