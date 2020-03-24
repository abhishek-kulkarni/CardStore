package com.ak.cardstore.util;

import android.os.Build;

import com.ak.cardstore.Make;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @author Abhishek
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1, Build.VERSION_CODES.P})
public class LoggerUtilUnitTest {

    @Test
    public void testLogError_WithCause() {
        final String tag = Make.aString();
        final Throwable cause = new RuntimeException();
        final String errorMessageFormat = "Test format %s";
        final int errorMessageArg = 1;
        final String expectedErrorMessage = "Test format 1";

        final String errorMessage = LoggerUtil.logError(tag, Optional.of(cause), errorMessageFormat, errorMessageArg);
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Test
    public void testLogError_WithoutCause() {
        final String tag = Make.aString();
        final String errorMessageFormat = "Test format %s";
        final int errorMessageArg = 1;
        final String expectedErrorMessage = "Test format 1";

        final String errorMessage = LoggerUtil.logError(tag, Optional.empty(), errorMessageFormat, errorMessageArg);
        assertEquals(expectedErrorMessage, errorMessage);
    }
}
