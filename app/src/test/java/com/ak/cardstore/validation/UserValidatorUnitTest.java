package com.ak.cardstore.validation;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.UserValidationException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Abhishek
 */

public class UserValidatorUnitTest {

    @Test
    public void testUser_WithPasswordLengthLessThan8() {
        final UserValidator userValidator = new UserValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> userValidator.validatePassword(Make.aString().substring(0, 5)));
        assertEquals("Password must be at least 8 characters in length!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutUpperCaseLetter() {
        final UserValidator userValidator = new UserValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> userValidator.validatePassword(Make.aString().toLowerCase()));
        assertEquals("Password must contain at least one upper case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutLowerCaseLetter() {
        final UserValidator userValidator = new UserValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> userValidator.validatePassword(Make.aString().toUpperCase()));
        assertEquals("Password must contain at least one lower case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutDigit() {
        final UserValidator userValidator = new UserValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> userValidator.validatePassword("Password!"));
        assertEquals("Password must contain at least one digit!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutSpecialCharacter() {
        final UserValidator userValidator = new UserValidator();

        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> userValidator.validatePassword("Password123"));
        assertEquals("Password must contain at least one special character!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithValidPassword() {
        final UserValidator userValidator = new UserValidator();

        userValidator.validatePassword("Password123!");
    }
}
