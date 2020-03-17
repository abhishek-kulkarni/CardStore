package com.ak.cardstore.cipher;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.ak.cardstore.exception.SymmetricKeyGenerationException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Optional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to generate the Symmetric {@link Key} for the encryption.
 *
 * @author Abhishek
 */

@Log4j2
public class SymmetricKeyGenerator {

    public static final String KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    public static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    public static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;

    private static final int USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS = 30;
    private static final boolean USER_AUTHENTICATION_REQUIRED = true;
    private static final boolean INVALIDATE_ON_BIOMETRIC_ENROLLMENT = true;
    private static final boolean DEVICE_UNLOCK_REQUIRED = true;

    private static final String NO_SUCH_ALGORITHM_ERROR = "Symmetric key generation failed for algorithm %s";
    private static final String INVALID_KEY_GEN_PARAMETER_SPEC_ERROR = "Invalid KeyGenParameterSpec %s";

    /**
     * Generates and returns a new Symmetric {@link Key} for the encryption/decryption.
     *
     * @param keyAlias the alias name
     * @param password the password for recovering the key
     * @return Symmetric {@link Key} for the encryption/decryption
     * @throws NoSuchProviderException if the specified provider is not registered in the security provider list
     */
    public SecretKey generate(@NonNull final String provider, @NonNull final String keyAlias, final String password)
            throws NoSuchProviderException {
        final KeyGenParameterSpec keyGenParameterSpec = this.buildKeyGenParameterSpec(keyAlias);

        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, provider);
        } catch (final NoSuchAlgorithmException e) {
            final String errorMessage = logError(log, Optional.of(e), NO_SUCH_ALGORITHM_ERROR, KEY_ALGORITHM);
            throw new SymmetricKeyGenerationException(errorMessage, e);
        }

        try {
            keyGenerator.init(keyGenParameterSpec, new SecureRandom(password.getBytes(StandardCharsets.UTF_8)));
        } catch (final InvalidAlgorithmParameterException e) {
            final String errorMessage = logError(log, Optional.of(e), INVALID_KEY_GEN_PARAMETER_SPEC_ERROR, keyGenParameterSpec);
            throw new SymmetricKeyGenerationException(errorMessage, e);
        }

        return keyGenerator.generateKey();
    }

    private KeyGenParameterSpec buildKeyGenParameterSpec(final String keyAlias) {
        final KeyGenParameterSpec.Builder keyGenParameterSpecBuilder = new KeyGenParameterSpec
                .Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setUserAuthenticationValidityDurationSeconds(USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS)
                .setUserAuthenticationRequired(USER_AUTHENTICATION_REQUIRED)
                .setInvalidatedByBiometricEnrollment(INVALIDATE_ON_BIOMETRIC_ENROLLMENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            keyGenParameterSpecBuilder.setUnlockedDeviceRequired(DEVICE_UNLOCK_REQUIRED);
        }

        return keyGenParameterSpecBuilder.build();
    }
}
