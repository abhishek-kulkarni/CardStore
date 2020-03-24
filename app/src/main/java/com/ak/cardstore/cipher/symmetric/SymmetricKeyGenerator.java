package com.ak.cardstore.cipher.symmetric;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.ak.cardstore.exception.SymmetricKeyGenerationException;
import com.google.common.annotations.VisibleForTesting;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Optional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.NonNull;

import static com.ak.cardstore.util.LoggerUtil.logError;
import static com.ak.cardstore.util.StringUtil.toByteArray;

/**
 * A class to generate the Symmetric {@link Key} for the cipher operation.
 *
 * @author Abhishek
 */

public class SymmetricKeyGenerator {

    private static final String LOG_TAG = SymmetricKeyGenerator.class.getSimpleName();

    public static final String KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    public static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    public static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;

    @VisibleForTesting
    static final int USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS = 30;
    @VisibleForTesting
    static final boolean USER_AUTHENTICATION_REQUIRED = true;
    @VisibleForTesting
    static final boolean INVALIDATE_ON_BIOMETRIC_ENROLLMENT = true;
    @VisibleForTesting
    static final boolean DEVICE_UNLOCK_REQUIRED = true;

    private static final String NO_SUCH_ALGORITHM_ERROR = "Symmetric key generation failed for algorithm %s";
    private static final String INVALID_KEY_GEN_PARAMETER_SPEC_ERROR = "Invalid KeyGenParameterSpec %s";

    /**
     * Generates and returns a new Symmetric {@link Key} for the encryption/decryption.
     *
     * @param provider Key generator provider
     * @param keyAlias the alias name
     * @param password the password for generating the key
     * @return Symmetric {@link Key} for the encryption/decryption
     * @throws NoSuchProviderException if the specified provider is not registered in the security provider list
     */
    public SecretKey generate(@NonNull final String provider, @NonNull final String keyAlias, @NonNull final String password)
            throws NoSuchProviderException {
        final KeyGenParameterSpec keyGenParameterSpec = this.buildKeyGenParameterSpec(keyAlias);

        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, provider);
        } catch (final NoSuchAlgorithmException e) {
            final String errorMessage = logError(LOG_TAG, Optional.of(e), NO_SUCH_ALGORITHM_ERROR, KEY_ALGORITHM);
            throw new SymmetricKeyGenerationException(errorMessage, e);
        }

        try {
            keyGenerator.init(keyGenParameterSpec, new SecureRandom(toByteArray(password)));
        } catch (final InvalidAlgorithmParameterException e) {
            final String errorMessage = logError(LOG_TAG, Optional.of(e), INVALID_KEY_GEN_PARAMETER_SPEC_ERROR, keyGenParameterSpec);
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
