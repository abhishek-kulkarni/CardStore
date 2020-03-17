package com.ak.cardstore.util;

import org.apache.logging.log4j.Logger;

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
     * @param log                {@link Logger} for the logging class
     * @param optionalCause      cause of the error
     * @param errorMessageFormat {@link String#format(String, Object...)} compatible error message format
     * @param errorMessageArgs   arguments for the error message
     * @return error message
     */
    public static String logError(final Logger log, final Optional<Throwable> optionalCause, final String errorMessageFormat,
                                  final Object... errorMessageArgs) {
        final String errorMessage = String.format(errorMessageFormat, errorMessageArgs);

        if (optionalCause.isPresent()) {
            log.error(errorMessage, optionalCause.get());
        } else {
            log.error(errorMessage);
        }

        return errorMessage;
    }
}
