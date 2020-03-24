package com.ak.cardstore.dao;

import android.content.Context;
import android.util.Log;

import com.ak.cardstore.context.AppContextProvider;
import com.ak.cardstore.util.StringUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * File based data accessor.
 *
 * @author Abhishek
 */

public class FileBasedDataAccessor {

    private static final String LOG_TAG = FileBasedDataAccessor.class.getSimpleName();

    /**
     * Saves the data in a file to the storage
     *
     * @param fileName   name of the file
     * @param dataToSave data to be saved to the file
     */
    public void save(final String fileName, final String dataToSave) throws IOException {
        final Context appContext = AppContextProvider.getAppContext();

        try (final FileOutputStream fileOutputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(StringUtil.toUTF8ByteArray(dataToSave));
            Log.i(LOG_TAG, "Successfully saved the file " + fileName);
        }
    }

    /**
     * Reads the file specified by fileName and returns the file contents.
     *
     * @param fileName file name to read
     * @return file contents
     */
    public String getContents(final String fileName) throws IOException {
        final Context appContext = AppContextProvider.getAppContext();

        final StringBuilder stringBuilder = new StringBuilder();

        try (
                final FileInputStream fileInputStream = appContext.openFileInput(fileName);
                final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();

                if (null != line) {
                    stringBuilder.append(System.lineSeparator());
                }
            }
        }

        Log.i(LOG_TAG, "Successfully read the file " + fileName + ". Returning the file contents.");
        return stringBuilder.toString();
    }
}
