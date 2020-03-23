package com.ak.cardstore.cipher;

import com.ak.cardstore.exception.CipherOperationException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.StringUtil.toByteArray;

/**
 * An operator class to do the cipher operation
 *
 * @author Abhishek
 */

@Log4j2
public class CipherOperator {

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
            log.error(operationErrorMessage, e);
            throw new CipherOperationException(operationErrorMessage, e);
        }

        return outputText;
    }
}
