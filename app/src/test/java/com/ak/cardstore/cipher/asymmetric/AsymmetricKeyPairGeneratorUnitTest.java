package com.ak.cardstore.cipher.asymmetric;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.ak.cardstore.Make;
import com.ak.cardstore.exception.AsymmetricKeyPairGenerationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.security.auth.x500.X500Principal;

import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.BLOCK_MODE;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.DEVICE_UNLOCK_REQUIRED;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.ENCRYPTION_PADDING;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.INVALIDATE_ON_BIOMETRIC_ENROLLMENT;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.KEY_ALGORITHM;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.USER_AUTHENTICATION_REQUIRED;
import static com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator.USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AsymmetricKeyPairGenerator.class, X500Principal.class, KeyGenerator.class, Log.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*", "javax.security.*"})
public class AsymmetricKeyPairGeneratorUnitTest {

    private static final String KEY_ALIAS = Make.aString();

    @Mock
    private KeyGenParameterSpec.Builder mockKeyGenParameterSpecBuilder;

    @Mock
    private KeyGenParameterSpec mockKeyGenParameterSpec;

    @Mock
    private X500Principal mockX500Principal;

    @Before
    public void setup() throws Exception {
        whenNew(KeyGenParameterSpec.Builder.class)
                .withArguments(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .thenReturn(this.mockKeyGenParameterSpecBuilder);
        whenNew(X500Principal.class)
                .withArguments(String.format("CN=%s CA Certificate,O=CardStore,C=US", KEY_ALIAS))
                .thenReturn(this.mockX500Principal);

        when(this.mockKeyGenParameterSpecBuilder.setCertificateSubject(this.mockX500Principal))
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

        final Field sdkIntField = Build.VERSION.class.getField("SDK_INT");
        sdkIntField.setAccessible(true);

        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(sdkIntField, sdkIntField.getModifiers() & ~Modifier.FINAL);

        sdkIntField.set(null, android.os.Build.VERSION_CODES.P);

        mockStatic(Log.class);
        when(Log.e(anyString(), anyString(), any(Throwable.class))).thenReturn(0);
    }

    @Test
    public void testGenerate() throws Exception {
        final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator = new AsymmetricKeyPairGenerator();

        final String provider = Make.aString();

        final KeyPairGenerator mockKeyPairGenerator = mock(KeyPairGenerator.class);
        final KeyPair mockKeyPair = mock(KeyPair.class);

        mockStatic(KeyPairGenerator.class);
        when(KeyPairGenerator.getInstance(KEY_ALGORITHM, provider)).thenReturn(mockKeyPairGenerator);
        doNothing().when(mockKeyPairGenerator).initialize(any(KeyGenParameterSpec.class), any(SecureRandom.class));
        when(mockKeyPairGenerator.generateKeyPair()).thenReturn(mockKeyPair);

        final KeyPair keyPair = asymmetricKeyPairGenerator.generate(provider, KEY_ALIAS);
        Assert.assertSame(mockKeyPair, keyPair);

        verifyStatic(KeyPairGenerator.class);
        KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        verify(mockKeyPairGenerator).initialize(any(KeyGenParameterSpec.class), any(SecureRandom.class));
        verify(mockKeyPairGenerator).generateKeyPair();
    }

    @Test
    public void testGenerate_GetInstanceThrowsNoSuchAlgorithmException() throws Exception {
        final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator = new AsymmetricKeyPairGenerator();

        final String provider = Make.aString();

        mockStatic(KeyPairGenerator.class);
        when(KeyPairGenerator.getInstance(KEY_ALGORITHM, provider)).thenThrow(NoSuchAlgorithmException.class);

        final AsymmetricKeyPairGenerationException asymmetricKeyPairGenerationException = Assert.assertThrows(AsymmetricKeyPairGenerationException.class,
                () -> asymmetricKeyPairGenerator.generate(provider, KEY_ALIAS));
        assertEquals("Asymmetric key generation failed for algorithm RSA", asymmetricKeyPairGenerationException.getMessage());
        assertTrue(asymmetricKeyPairGenerationException.getCause() instanceof NoSuchAlgorithmException);

        verifyStatic(KeyPairGenerator.class);
        KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
    }

    @Test
    public void testGenerate_InitThrowsInvalidAlgorithmParameterException() throws Exception {
        final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator = new AsymmetricKeyPairGenerator();

        final String provider = Make.aString();

        final KeyPairGenerator mockKeyPairGenerator = mock(KeyPairGenerator.class);

        mockStatic(KeyPairGenerator.class);
        when(KeyPairGenerator.getInstance(KEY_ALGORITHM, provider)).thenReturn(mockKeyPairGenerator);
        doThrow(new InvalidAlgorithmParameterException()).when(mockKeyPairGenerator).initialize(any(KeyGenParameterSpec.class), any(SecureRandom.class));

        final AsymmetricKeyPairGenerationException asymmetricKeyPairGenerationException = Assert.assertThrows(AsymmetricKeyPairGenerationException.class,
                () -> asymmetricKeyPairGenerator.generate(provider, KEY_ALIAS));
        assertTrue(asymmetricKeyPairGenerationException.getMessage().startsWith("Invalid KeyGenParameterSpec "));
        assertTrue(asymmetricKeyPairGenerationException.getCause() instanceof InvalidAlgorithmParameterException);

        verifyStatic(KeyPairGenerator.class);
        KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        verify(mockKeyPairGenerator).initialize(any(KeyGenParameterSpec.class), any(SecureRandom.class));
    }
}
