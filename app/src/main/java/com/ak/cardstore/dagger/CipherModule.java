package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.cipher.KeyStoreRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link CipherRetriever}, {@link CipherOperator} and {@link KeyStoreRetriever}.
 *
 * @author Abhishek
 */

@Module
public class CipherModule {

    /**
     * Provides {@link CipherRetriever}
     *
     * @return {@link CipherRetriever}
     */
    @Provides
    @Singleton
    public CipherRetriever provideCipherRetriever() {
        return new CipherRetriever();
    }

    /**
     * Provides {@link CipherOperator}
     *
     * @return {@link CipherOperator}
     */
    @Provides
    @Singleton
    public CipherOperator provideCipherOperator() {
        return new CipherOperator();
    }

    /**
     * Provides {@link KeyStoreRetriever}
     *
     * @return {@link KeyStoreRetriever}
     */
    @Provides
    @Singleton
    public KeyStoreRetriever provideKeyStoreRetriever() {
        return new KeyStoreRetriever();
    }
}
