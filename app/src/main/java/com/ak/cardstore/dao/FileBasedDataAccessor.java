package com.ak.cardstore.dao;

import android.content.Context;

import com.ak.cardstore.context.AppContextProvider;
import com.ak.cardstore.util.StringUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lombok.extern.log4j.Log4j2;

/**
 * File based data accessor.
 *
 * @author Abhishek
 */

@Log4j2
public class FileBasedDataAccessor {

    /**
     * Saves the data in a file to the storage
     *
     * @param fileName   name of the file
     * @param dataToSave data to be saved to the file
     */
    public void save(final String fileName, final String dataToSave) throws IOException {
        final Context appContext = AppContextProvider.getAppContext();

        try (final FileOutputStream fileOutputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(StringUtil.toByteArray(dataToSave));
            log.info("Successfully saved the file {}", fileName);
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

        log.info("Successfully read the file {}. Returning the file contents.", fileName);
        return stringBuilder.toString();
    }
}
