package com.ak.cardstore.activity.login.data;

import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */

// hide the private constructor to limit subclass types (Success, Error)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Result {

    @Nonnull
    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            return Result.Success.class.getSimpleName();
        } else if (this instanceof Result.Error) {
            final Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success extends Result {
    }

    // Error sub-class
    public final static class Error extends Result {

        private final Exception error;

        public Error(final Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
