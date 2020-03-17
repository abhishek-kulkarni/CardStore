package com.ak.cardstore.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utility class to handle conversion between {@link String} and byte[].
 *
 * @author Abhishek
 */

public class StringUtil {

    private static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    /**
     * Returns the UTF_8 byte[] for the given stringToConvert
     *
     * @param stringToConvert string to convert
     * @return UTF_8 byte[] for the given stringToConvert
     */
    public static byte[] toByteArray(final String stringToConvert) {
        return stringToConvert.getBytes(CHARSET_UTF_8);
    }

    /**
     * Returns the UTF_8 {@link String} for the given byteArrayToConvert
     *
     * @param byteArrayToConvert byte array to convert
     * @return UTF_8 {@link String} for the given byteArrayToConvert
     */
    public static String getString(final byte[] byteArrayToConvert) {
        return StringUtils.toEncodedString(byteArrayToConvert, CHARSET_UTF_8);
    }
}
