package com.ak.cardstore.util;

import android.util.Log;

import java.util.Optional;

/**
 * Utility class to handle logging.
 *
 * @author Abhishek
 */

public class LoggerUtil {

    /**
     * Constructs, logs and returns the error message.
     *
     * @param tag                tag to identify the class
     * @param optionalCause      cause of the error
     * @param errorMessageFormat {@link String#format(String, Object...)} compatible error message format
     * @param errorMessageArgs   arguments for the error message
     * @return error message
     */
    public static String logError(final String tag, final Optional<Throwable> optionalCause, final String errorMessageFormat,
                                  final Object... errorMessageArgs) {
        final String errorMessage = String.format(errorMessageFormat, errorMessageArgs);

        if (optionalCause.isPresent()) {
            Log.e(tag, errorMessage, optionalCause.get());
        } else {
            Log.e(tag, errorMessage);
        }

        return errorMessage;
    }
}
