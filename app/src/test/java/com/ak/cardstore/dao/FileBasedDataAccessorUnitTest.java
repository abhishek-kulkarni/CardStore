package com.ak.cardstore.dao;

import android.content.Context;

import com.ak.cardstore.Make;
import com.ak.cardstore.context.AppContextProvider;
import com.ak.cardstore.util.StringUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileBasedDataAccessor.class, AppContextProvider.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class FileBasedDataAccessorUnitTest {

    @Test
    public void testSave() throws Exception {
        final String fileName = Make.aString();
        final String dataToSave = Make.aString();

        final Context mockAppContext = mock(Context.class);
        final FileOutputStream mockFileOutputStream = mock(FileOutputStream.class);

        mockStatic(AppContextProvider.class);
        when(AppContextProvider.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.openFileOutput(fileName, Context.MODE_PRIVATE)).thenReturn(mockFileOutputStream);
        doNothing().when(mockFileOutputStream).write(StringUtil.toByteArray(dataToSave));

        final FileBasedDataAccessor fileBasedDataAccessor = new FileBasedDataAccessor();
        fileBasedDataAccessor.save(fileName, dataToSave);

        verifyStatic(AppContextProvider.class);
        AppContextProvider.getAppContext();
        verify(mockAppContext).openFileOutput(fileName, Context.MODE_PRIVATE);
        verify(mockFileOutputStream).write(StringUtil.toByteArray(dataToSave));
    }

    @Test
    public void testGetContents() throws Exception {
        final String fileName = Make.aString();
        final String expectedFileContents = Make.aString()
                .concat(System.lineSeparator())
                .concat(Make.aString());

        final InputStreamReader testInputStreamReader = new InputStreamReader(
                new ByteArrayInputStream(expectedFileContents.getBytes(StandardCharsets.UTF_8)));

        final Context mockAppContext = mock(Context.class);
        final FileInputStream mockFileInputStream = mock(FileInputStream.class);
        final BufferedReader mockBufferedReader = mock(BufferedReader.class);

        mockStatic(AppContextProvider.class);
        when(AppContextProvider.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.openFileInput(fileName)).thenReturn(mockFileInputStream);
        whenNew(InputStreamReader.class).withArguments(mockFileInputStream, StandardCharsets.UTF_8).thenReturn(testInputStreamReader);
        when(mockBufferedReader.readLine()).thenReturn(null);

        final FileBasedDataAccessor fileBasedDataAccessor = new FileBasedDataAccessor();
        final String fileContents = fileBasedDataAccessor.getContents(fileName);
        Assert.assertEquals(expectedFileContents, fileContents);

        verifyStatic(AppContextProvider.class);
        AppContextProvider.getAppContext();
        verify(mockAppContext).openFileInput(fileName);
        verifyNew(InputStreamReader.class).withArguments(mockFileInputStream, StandardCharsets.UTF_8);
    }
}
