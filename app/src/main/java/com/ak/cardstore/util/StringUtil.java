package com.ak.cardstore.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class to handle conversion between {@link String} and byte[].
 *
 * @author Abhishek
 */

public class StringUtil {

    /**
     * Returns the UTF_8 byte[] for the given stringToConvert
     *
     * @param utf8String string to convert
     * @return UTF_8 byte[] for the given stringToConvert
     */
    public static byte[] toUTF8ByteArray(final String utf8String) {
        return utf8String.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Returns the byte[] for the given base64String
     *
     * @param base64String Base64 encoded string to convert
     * @return byte[] for the given base64String
     */
    public static byte[] base64StringToByteArray(final String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * Returns the Base64 encoded {@link String} for the given byteArrayToConvert
     *
     * @param byteArray byte array to convert
     * @return Base64 encoded {@link String} for the given byteArrayToConvert
     */
    public static String toBase64String(final byte[] byteArray) {
        return Base64.getEncoder().encodeToString((byteArray));
    }

    /**
     * Returns the UTF_8 string for the given byteArray
     *
     * @param byteArray byte array to convert
     * @return UTF_8 string for the given byteArray
     */
    public static String toUTF8String(final byte[] byteArray) {
        return StringUtils.toEncodedString(byteArray, StandardCharsets.UTF_8);
    }
}
