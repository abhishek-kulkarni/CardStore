package com.ak.cardstore.dagger;

import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.dao.SharedPreferencesDataAccessor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger {@link Module} to provide {@link SharedPreferencesDataAccessor} and {@link FileBasedDataAccessor}.
 *
 * @author Abhishek
 */

@Module
public class DaoModule {

    /**
     * Provides {@link SharedPreferencesDataAccessor}
     *
     * @return {@link SharedPreferencesDataAccessor}
     */
    @Provides
    @Singleton
    public SharedPreferencesDataAccessor provideSharedPreferencesDataAccessor() {
        return new SharedPreferencesDataAccessor();
    }

    /**
     * Provides {@link FileBasedDataAccessor}
     *
     * @return {@link FileBasedDataAccessor}
     */
    @Provides
    @Singleton
    public FileBasedDataAccessor provideFileBasedDataAccessor() {
        return new FileBasedDataAccessor();
    }
}
