package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.cipher.KeyStoreRetriever;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link SymmetricKeyCipher}, {@link SymmetricKeyRetriever} and {@link SymmetricKeyGenerator}.
 *
 * @author Abhishek
 */

@Module(includes = CipherModule.class)
public class SymmetricCipherModule {

    /**
     * Provides {@link SymmetricKeyCipher}
     *
     * @return {@link SymmetricKeyCipher}
     */
    @Provides
    @Singleton
    public SymmetricKeyCipher provideSymmetricKeyCipher(
            final SymmetricKeyRetriever symmetricKeyRetriever,
            final CipherRetriever cipherRetriever,
            final CipherOperator cipherOperator) {
        return new SymmetricKeyCipher(symmetricKeyRetriever, cipherRetriever, cipherOperator);
    }

    /**
     * Provides {@link SymmetricKeyRetriever}
     *
     * @return {@link SymmetricKeyRetriever}
     */
    @Provides
    @Singleton
    public SymmetricKeyRetriever provideSymmetricKeyRetriever(
            final KeyStoreRetriever keyStoreRetriever,
            final SymmetricKeyGenerator symmetricKeyGenerator) {
        return new SymmetricKeyRetriever(keyStoreRetriever, symmetricKeyGenerator);
    }

    /**
     * Provides {@link SymmetricKeyGenerator}
     *
     * @return {@link SymmetricKeyGenerator}
     */
    @Provides
    @Singleton
    public SymmetricKeyGenerator provideSymmetricKeyGenerator() {
        return new SymmetricKeyGenerator();
    }
}
