package com.ak.cardstore.pojo;

import com.ak.cardstore.validation.UserValidator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A POJO to represent an app user.
 *
 * @author Abhishek
 */

@EqualsAndHashCode
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
    public static final class Builder {

        private static final UserValidator USER_VALIDATOR = new UserValidator();

        @NonNull
        private String password;

        /**
         * Updates the user password and returns the builder for chaining.
         *
         * @param password user password
         * @return builder
         */
        public Builder password(final String password) {
            USER_VALIDATOR.validatePassword(password);
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
    }
}
