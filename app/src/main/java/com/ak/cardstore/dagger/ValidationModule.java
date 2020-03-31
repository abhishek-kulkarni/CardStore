package com.ak.cardstore.dagger;

import com.ak.cardstore.validation.PasswordValidator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link PasswordValidator}.
 *
 * @author Abhishek
 */

@Module
public class ValidationModule {

    /**
     * Provides {@link PasswordValidator}.
     *
     * @return {@link PasswordValidator}
     */
    @Provides
    @Singleton
    public PasswordValidator providePasswordValidator() {
        return new PasswordValidator();
    }
}
