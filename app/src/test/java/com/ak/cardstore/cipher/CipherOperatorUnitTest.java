package com.ak.cardstore.cipher;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.CipherOperationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.charset.StandardCharsets;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class CipherOperatorUnitTest {

    @Test
    public void testDoCipherOperation() throws BadPaddingException, IllegalBlockSizeException {
        final Cipher mockCipher = mock(Cipher.class);

        final String dataToOperate = Make.aString();
        final String operationErrorMessage = Make.aString();
        final byte[] expectedOutputText = Make.aString().getBytes(StandardCharsets.UTF_8);

        when(mockCipher.doFinal(dataToOperate.getBytes(StandardCharsets.UTF_8))).thenReturn(expectedOutputText);

        final CipherOperator cipherOperator = new CipherOperator();
        final byte[] outputText = cipherOperator.doCipherOperation(mockCipher, dataToOperate, operationErrorMessage);
        assertEquals(expectedOutputText, outputText);
    }

    @Test
    public void testDoCipherOperation_ThrowsBadPaddingException() throws BadPaddingException, IllegalBlockSizeException {
        final Cipher mockCipher = mock(Cipher.class);

        final String dataToOperate = Make.aString();
        final String operationErrorMessage = Make.aString();
        final byte[] expectedOutputText = Make.aString().getBytes(StandardCharsets.UTF_8);

        when(mockCipher.doFinal(dataToOperate.getBytes(StandardCharsets.UTF_8))).thenThrow(BadPaddingException.class);

        final CipherOperator cipherOperator = new CipherOperator();

        final CipherOperationException cipherOperationException = Assert.assertThrows(CipherOperationException.class,
                () -> cipherOperator.doCipherOperation(mockCipher, dataToOperate, operationErrorMessage));
        assertEquals(operationErrorMessage, cipherOperationException.getMessage());
        assertTrue(cipherOperationException.getCause() instanceof BadPaddingException);
    }

    @Test
    public void testDoCipherOperation_ThrowsIllegalBlockSizeException() throws BadPaddingException, IllegalBlockSizeException {
        final Cipher mockCipher = mock(Cipher.class);

        final String dataToOperate = Make.aString();
        final String operationErrorMessage = Make.aString();
        final byte[] expectedOutputText = Make.aString().getBytes(StandardCharsets.UTF_8);

        when(mockCipher.doFinal(dataToOperate.getBytes(StandardCharsets.UTF_8))).thenThrow(IllegalBlockSizeException.class);

        final CipherOperator cipherOperator = new CipherOperator();

        final CipherOperationException cipherOperationException = Assert.assertThrows(CipherOperationException.class,
                () -> cipherOperator.doCipherOperation(mockCipher, dataToOperate, operationErrorMessage));
        assertEquals(operationErrorMessage, cipherOperationException.getMessage());
        assertTrue(cipherOperationException.getCause() instanceof IllegalBlockSizeException);
    }
}
