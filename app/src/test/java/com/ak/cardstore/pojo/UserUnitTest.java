package com.ak.cardstore.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Abhishek
 */

public class UserUnitTest {

    @Test
    public void testUser() {
        final User user = User.builder()
                .password("Password123!")
                .build();
        Assertions.assertNotNull(user);
    }
}
