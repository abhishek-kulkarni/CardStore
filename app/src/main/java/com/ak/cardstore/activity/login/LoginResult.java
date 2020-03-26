package com.ak.cardstore.activity.login;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Authentication result : success (user details) or error message.
 */

@Getter(AccessLevel.PACKAGE)
class LoginResult {

    private final boolean isSuccess;
    private final Optional<String> error;

    LoginResult(final String error) {
        this.isSuccess = false;
        this.error = Optional.of(error);
    }

    LoginResult(final boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = Optional.empty();
    }
}
