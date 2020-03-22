package com.ak.cardstore.cipher.asymmetric;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.ak.cardstore.exception.AsymmetricKeyPairGenerationException;
import com.google.common.annotations.VisibleForTesting;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Optional;

import javax.security.auth.x500.X500Principal;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import static com.ak.cardstore.util.LoggerUtil.logError;

/**
 * A class to generate the Asymmetric {@link KeyPair} for the cipher operation.
 *
 * @author Abhishek
 */

@Log4j2
public class AsymmetricKeyPairGenerator {

    public static final String KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA;
    public static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB;
    public static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1;

    public static final String CERTIFICATE_NAME_FORMAT = "%s CA Certificate";

    @VisibleForTesting
    static final int USER_AUTHENTICATION_VALIDITY_DURATION_SECONDS = 30;
    @VisibleForTesting
    static final boolean USER_AUTHENTICATION_REQUIRED = true;
    @VisibleForTesting
    static final boolean INVALIDATE_ON_BIOMETRIC_ENROLLMENT = true;
    @VisibleForTesting
    static final boolean DEVICE_UNLOCK_REQUIRED = true;

    private static final String NO_SUCH_ALGORITHM_ERROR = "Asymmetric key generation failed for algorithm %s";
    private static final String INVALID_KEY_GEN_PARAMETER_SPEC_ERROR = "Invalid KeyGenParameterSpec %s";

    /**
     * Generates and returns a new asymmetric {@link KeyPair} for the encryption/decryption.
     *
     * @param provider Key generator provider
     * @param keyAlias the alias name
     * @return Asymmetric {@link KeyPair} for the encryption/decryption
     * @throws NoSuchProviderException if the specified provider is not registered in the security provider list
     */
    public KeyPair generate(@NonNull final String provider, @NonNull final String keyAlias) throws NoSuchProviderException {
        final KeyGenParameterSpec keyPairGenParameterSpec = this.buildKeyGenParameterSpec(keyAlias);

        final KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        } catch (final NoSuchAlgorithmException e) {
            final String errorMessage = logError(log, Optional.of(e), NO_SUCH_ALGORITHM_ERROR, KEY_ALGORITHM);
            throw new AsymmetricKeyPairGenerationException(errorMessage, e);
        }

        try {
            keyPairGenerator.initialize(keyPairGenParameterSpec, new SecureRandom());
        } catch (final InvalidAlgorithmParameterException e) {
            final String errorMessage = logError(log, Optional.of(e), INVALID_KEY_GEN_PARAMETER_SPEC_ERROR, keyPairGenParameterSpec);
            throw new AsymmetricKeyPairGenerationException(errorMessage, e);
        }

        return keyPairGenerator.generateKeyPair();
    }

    private KeyGenParameterSpec buildKeyGenParameterSpec(final String keyAlias) {
        final String certificateName = String.format(CERTIFICATE_NAME_FORMAT, keyAlias);

        final KeyGenParameterSpec.Builder keyGenParameterSpecBuilder = new KeyGenParameterSpec
                .Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setCertificateSubject(new X500Principal(certificateName))
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
