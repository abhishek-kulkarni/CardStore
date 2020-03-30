package com.ak.cardstore.dao;

import android.content.Context;
import android.util.Log;

import com.ak.cardstore.Make;
import com.ak.cardstore.app.App;
import com.ak.cardstore.util.StringUtil;

import org.junit.Assert;
import org.junit.Before;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileBasedDataAccessor.class, App.class, Log.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class FileBasedDataAccessorUnitTest {

    @Before
    public void setupLog() {
        mockStatic(Log.class);
        when(Log.e(anyString(), anyString(), any(Throwable.class))).thenReturn(0);
    }

    @Test
    public void testSave() throws Exception {
        final String fileName = Make.aString();
        final String dataToSave = Make.aString();

        final Context mockAppContext = mock(Context.class);
        final FileOutputStream mockFileOutputStream = mock(FileOutputStream.class);

        mockStatic(App.class);
        when(App.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.openFileOutput(fileName, Context.MODE_PRIVATE)).thenReturn(mockFileOutputStream);
        doNothing().when(mockFileOutputStream).write(StringUtil.toUTF8ByteArray(dataToSave));

        final FileBasedDataAccessor fileBasedDataAccessor = new FileBasedDataAccessor();
        fileBasedDataAccessor.save(fileName, dataToSave);

        verifyStatic(App.class);
        App.getAppContext();
        verify(mockAppContext).openFileOutput(fileName, Context.MODE_PRIVATE);
        verify(mockFileOutputStream).write(StringUtil.toUTF8ByteArray(dataToSave));
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

        mockStatic(App.class);
        when(App.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.openFileInput(fileName)).thenReturn(mockFileInputStream);
        whenNew(InputStreamReader.class).withArguments(mockFileInputStream, StandardCharsets.UTF_8).thenReturn(testInputStreamReader);
        when(mockBufferedReader.readLine()).thenReturn(null);

        final FileBasedDataAccessor fileBasedDataAccessor = new FileBasedDataAccessor();
        final String fileContents = fileBasedDataAccessor.getContents(fileName);
        Assert.assertEquals(expectedFileContents, fileContents);

        verifyStatic(App.class);
        App.getAppContext();
        verify(mockAppContext).openFileInput(fileName);
        verifyNew(InputStreamReader.class).withArguments(mockFileInputStream, StandardCharsets.UTF_8);
    }
}
