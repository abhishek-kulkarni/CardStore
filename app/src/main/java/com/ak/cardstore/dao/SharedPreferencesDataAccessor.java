package com.ak.cardstore.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.ak.cardstore.context.AppContextProvider;
import com.ak.cardstore.exception.SharedPreferencesIOException;
import com.ak.cardstore.util.LoggerUtil;

import java.util.Optional;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * A data accessor to read from and write to the shared preferences.
 *
 * @author Abhishek
 */

@Log4j2
public class SharedPreferencesDataAccessor {

    private static final String SAVE_ERROR = "Error saving shared preferences file %s";

    /**
     * Saves the preferencesKey and preferencesValue to the shared preference file identified by given preferencesFileName.
     *
     * @param preferencesFileName shared preferences file name
     * @param preferencesKey      key to save
     * @param preferencesValue    value to save
     */
    public void save(@NonNull final String preferencesFileName, @NonNull final String preferencesKey, @NonNull final String preferencesValue) {
        final Context appContext = AppContextProvider.getAppContext();

        final SharedPreferences sharedPreferences = appContext.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);

        final boolean isCommitSuccessful = sharedPreferences.edit()
                .putString(preferencesKey, preferencesValue)
                .commit();
        if (!isCommitSuccessful) {
            final String errorMessage = LoggerUtil.logError(log, Optional.empty(), SAVE_ERROR, preferencesFileName);
            throw new SharedPreferencesIOException(errorMessage);
        } else {
            log.info("Successfully saved shared preferences file {}", preferencesFileName);
        }
    }

    /**
     * Reads and returns the preferencesValue for a given preferencesKey from the shared preferences file identified by given preferencesFileName.
     *
     * @param preferencesFileName shared preferences file name
     * @param preferencesKey      key for which the value is returned
     * @return preferencesValue for a given preferencesKey
     */
    public String get(@NonNull final String preferencesFileName, @NonNull final String preferencesKey) {
        final Context appContext = AppContextProvider.getAppContext();

        final SharedPreferences sharedPreferences = appContext.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);

        final String preferencesValue = sharedPreferences.getString(preferencesKey, null);
        return preferencesValue;
    }
}
