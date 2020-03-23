package com.ak.cardstore.pojo;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.UserValidationException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Abhishek
 */

public class UserUnitTest {

    @Test
    public void testUser_WithPasswordLengthLessThan8() {
        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> User.builder()
                        .password(Make.aString().substring(0, 5))
                        .build());
        assertEquals("Password must be at least 8 characters in length!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutUpperCaseLetter() {
        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> User.builder()
                        .password(Make.aString().toLowerCase())
                        .build());
        assertEquals("Password must contain at least one upper case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutLowerCaseLetter() {
        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> User.builder()
                        .password(Make.aString().toUpperCase())
                        .build());
        assertEquals("Password must contain at least one lower case letter!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutDigit() {
        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> User.builder()
                        .password("Password!")
                        .build());
        assertEquals("Password must contain at least one digit!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithPasswordWithoutSpecialCharacter() {
        final UserValidationException userValidationException = assertThrows(UserValidationException.class,
                () -> User.builder()
                        .password("Password123")
                        .build());
        assertEquals("Password must contain at least one special character!", userValidationException.getMessage());
    }

    @Test
    public void testUser_WithValidPassword() {
        final User user = User.builder()
                .password("Password123!")
                .build();
        assertNotNull(user);
    }
}
