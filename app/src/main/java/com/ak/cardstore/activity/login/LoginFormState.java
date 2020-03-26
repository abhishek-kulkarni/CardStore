package com.ak.cardstore.activity.login;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Data validation state of the login form.
 */

@Getter(AccessLevel.PACKAGE)
class LoginFormState {

    private final Optional<String> passwordError;
    private final boolean isDataValid;

    LoginFormState(@NonNull final String passwordError) {
        this.passwordError = Optional.of(passwordError);
        this.isDataValid = false;
    }

    LoginFormState(final boolean isDataValid) {
        this.passwordError = Optional.empty();
        this.isDataValid = isDataValid;
    }
}
