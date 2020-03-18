package com.ak.cardstore.cipher;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.SymmetricKeyGenerationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static com.ak.cardstore.cipher.SymmetricKeyGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.DEVICE_UNLOCK_REQUIRED;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.INVALIDATE_ON_BIOMETRIC_ENROLLMENT;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.USER_AUTHENTICATION_REQUIRED;
import static com.ak.cardstore.cipher.SymmetricKeyGenerator.USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({SymmetricKeyGenerator.class, KeyGenerator.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class SymmetricKeyGeneratorUnitTest {

    private static final String KEY_ALIAS = Make.aString();

    @Mock
    private KeyGenParameterSpec.Builder mockKeyGenParameterSpecBuilder;

    @Mock
    private KeyGenParameterSpec mockKeyGenParameterSpec;

    @Before
    public void setup() throws Exception {
        whenNew(KeyGenParameterSpec.Builder.class)
                .withArguments(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setBlockModes(BLOCK_MODE))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setEncryptionPaddings(ENCRYPTION_PADDING))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setUserAuthenticationValidityDurationSeconds(USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setUserAuthenticationRequired(USER_AUTHENTICATION_REQUIRED))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setInvalidatedByBiometricEnrollment(INVALIDATE_ON_BIOMETRIC_ENROLLMENT))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.setUnlockedDeviceRequired(DEVICE_UNLOCK_REQUIRED))
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        when(this.mockKeyGenParameterSpecBuilder.build())
                .thenReturn(this.mockKeyGenParameterSpec);
    }

    @Test
    public void testGenerate() throws Exception {
        final SymmetricKeyGenerator symmetricKeyGenerator = new SymmetricKeyGenerator();

        final String provider = Make.aString();
        final String password = Make.aString();

        final KeyGenerator mockKeyGenerator = mock(KeyGenerator.class);
        final SecretKey mockSymmetricKey = mock(SecretKey.class);

        mockStatic(KeyGenerator.class);
        when(KeyGenerator.getInstance(KEY_ALGORITHM, provider)).thenReturn(mockKeyGenerator);
        doNothing().when(mockKeyGenerator).init(any(KeyGenParameterSpec.class), any(SecureRandom.class));
        when(mockKeyGenerator.generateKey()).thenReturn(mockSymmetricKey);

        final SecretKey secretKey = symmetricKeyGenerator.generate(provider, KEY_ALIAS, password);
        Assert.assertSame(mockSymmetricKey, secretKey);
    }

    @Test
    public void testGenerate_GetInstanceThrowsNoSuchAlgorithmException() throws Exception {
        final SymmetricKeyGenerator symmetricKeyGenerator = new SymmetricKeyGenerator();

        final String provider = Make.aString();
        final String password = Make.aString();

        mockStatic(KeyGenerator.class);
        when(KeyGenerator.getInstance(KEY_ALGORITHM, provider)).thenThrow(NoSuchAlgorithmException.class);

        final SymmetricKeyGenerationException symmetricKeyGenerationException = Assert.assertThrows(SymmetricKeyGenerationException.class,
                () -> symmetricKeyGenerator.generate(provider, KEY_ALIAS, password));
        assertEquals("Symmetric key generation failed for algorithm AES", symmetricKeyGenerationException.getMessage());
        assertTrue(symmetricKeyGenerationException.getCause() instanceof NoSuchAlgorithmException);
    }

    @Test
    public void testGenerate_InitThrowsInvalidAlgorithmParameterException() throws Exception {
        final SymmetricKeyGenerator symmetricKeyGenerator = new SymmetricKeyGenerator();

        final String provider = Make.aString();
        final String password = Make.aString();

        final KeyGenerator mockKeyGenerator = mock(KeyGenerator.class);

        mockStatic(KeyGenerator.class);
        when(KeyGenerator.getInstance(KEY_ALGORITHM, provider)).thenReturn(mockKeyGenerator);
        doThrow(new InvalidAlgorithmParameterException()).when(mockKeyGenerator).init(any(KeyGenParameterSpec.class), any(SecureRandom.class));

        final SymmetricKeyGenerationException symmetricKeyGenerationException = Assert.assertThrows(SymmetricKeyGenerationException.class,
                () -> symmetricKeyGenerator.generate(provider, KEY_ALIAS, password));
        assertTrue(symmetricKeyGenerationException.getMessage().startsWith("Invalid KeyGenParameterSpec "));
        assertTrue(symmetricKeyGenerationException.getCause() instanceof InvalidAlgorithmParameterException);
    }
}
