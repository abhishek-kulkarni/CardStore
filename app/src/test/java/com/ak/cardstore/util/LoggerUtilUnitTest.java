package com.ak.cardstore.util;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * @author Abhishek
 */

@ExtendWith(MockitoExtension.class)
public class LoggerUtilUnitTest {

    @Mock
    private Logger mockLog;

    @Test
    public void testLogError_WithCause() {
        final Throwable cause = new RuntimeException();
        final String errorMessageFormat = "Test format %s";
        final int errorMessageArg = 1;
        final String expectedErrorMessage = "Test format 1";

        doNothing().when(this.mockLog).error(expectedErrorMessage, cause);

        final String errorMessage = LoggerUtil.logError(this.mockLog, Optional.of(cause), errorMessageFormat, errorMessageArg);
        assertEquals(expectedErrorMessage, errorMessage);

        verify(this.mockLog).error(expectedErrorMessage, cause);
    }

    @Test
    public void testLogError_WithoutCause() {
        final String errorMessageFormat = "Test format %s";
        final int errorMessageArg = 1;
        final String expectedErrorMessage = "Test format 1";

        doNothing().when(this.mockLog).error(expectedErrorMessage);

        final String errorMessage = LoggerUtil.logError(this.mockLog, Optional.empty(), errorMessageFormat, errorMessageArg);
        assertEquals(expectedErrorMessage, errorMessage);

        verify(this.mockLog).error(expectedErrorMessage);
    }
}
