package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.cipher.KeyStoreRetriever;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairCipher;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link AsymmetricKeyPairCipher}, {@link AsymmetricKeyPairRetriever} and {@link AsymmetricKeyPairGenerator}.
 *
 * @author Abhishek
 */

@Module(includes = CipherModule.class)
public class AsymmetricCipherModule {

    /**
     * Provides {@link AsymmetricKeyPairCipher}
     *
     * @return {@link AsymmetricKeyPairCipher}
     */
    @Provides
    @Singleton
    public AsymmetricKeyPairCipher provideAsymmetricKeyPairCipher(
            final AsymmetricKeyPairRetriever asymmetricKeyPairRetriever,
            final CipherRetriever cipherRetriever,
            final CipherOperator cipherOperator) {
        return new AsymmetricKeyPairCipher(asymmetricKeyPairRetriever, cipherRetriever, cipherOperator);
    }

    /**
     * Provides {@link AsymmetricKeyPairRetriever}
     *
     * @return {@link AsymmetricKeyPairRetriever}
     */
    @Provides
    @Singleton
    public AsymmetricKeyPairRetriever provideAsymmetricKeyPairRetriever(
            final KeyStoreRetriever keyStoreRetriever,
            final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator) {
        return new AsymmetricKeyPairRetriever(keyStoreRetriever, asymmetricKeyPairGenerator);
    }

    /**
     * Provides {@link AsymmetricKeyPairGenerator}
     *
     * @return {@link AsymmetricKeyPairGenerator}
     */
    @Provides
    @Singleton
    public AsymmetricKeyPairGenerator provideAsymmetricKeyPairGenerator() {
        return new AsymmetricKeyPairGenerator();
    }
}
