package com.ak.cardstore.cipher;

import android.util.Log;

import com.ak.cardstore.exception.CipherOperationException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import static com.ak.cardstore.util.StringUtil.toByteArray;

/**
 * An operator class to do the cipher operation
 *
 * @author Abhishek
 */

public class CipherOperator {

    private static final String LOG_TAG = CipherOperator.class.getSimpleName();

    /**
     * Does the cipher operation and returns the output data.
     *
     * @param cipher                cipher
     * @param dataToOperate         data to operate on
     * @param operationErrorMessage error message if operation fails
     * @return cipher operation output
     */
    public byte[] doCipherOperation(final Cipher cipher, final String dataToOperate, final String operationErrorMessage) {
        final byte[] outputText;
        try {
            outputText = cipher.doFinal(toByteArray(dataToOperate));
        } catch (final BadPaddingException | IllegalBlockSizeException e) {
            Log.e(LOG_TAG, operationErrorMessage, e);
            throw new CipherOperationException(operationErrorMessage, e);
        }

        return outputText;
    }
}
