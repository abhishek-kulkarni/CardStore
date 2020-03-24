package com.ak.cardstore.validation;

import com.ak.cardstore.Make;
import com.ak.cardstore.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author Abhishek
 */

public class StringUtilUnitTest {

    @Test
    public void testBase64StringConversions() {
        final String inputString = Make.aString();
        final byte[] inputByteArray = inputString.getBytes(StandardCharsets.UTF_8);

        final String base64String = StringUtil.toBase64String(inputByteArray);
        final byte[] outputByteArray = StringUtil.base64StringToByteArray(base64String);
        Assertions.assertEquals(inputString, StringUtils.toEncodedString(outputByteArray, StandardCharsets.UTF_8));
    }

    @Test
    public void testUTF8StringConversions() {
        final String inputString = Make.aString();

        final byte[] outputByteArray = StringUtil.toUTF8ByteArray(inputString);
        final String outputString = StringUtil.toUTF8String(outputByteArray);
        Assertions.assertEquals(inputString, outputString);
    }
}
