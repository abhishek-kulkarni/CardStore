package com.ak.cardstore.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.ak.cardstore.Make;
import com.ak.cardstore.context.AppContextProvider;
import com.ak.cardstore.exception.SharedPreferencesIOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Abhishek
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AppContextProvider.class})
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class SharedPreferencesDataAccessorUnitTest {

    @Test
    public void testSave_ReturnsTrue() {
        final String preferencesFileName = Make.aString();
        final String preferencesKey = Make.aString();
        final String preferencesValue = Make.aString();

        final Context mockAppContext = mock(Context.class);
        final SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        final SharedPreferences.Editor mockSharedPreferencesEditor = mock(SharedPreferences.Editor.class);

        mockStatic(AppContextProvider.class);
        when(AppContextProvider.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor);
        when(mockSharedPreferencesEditor.putString(preferencesKey, preferencesValue)).thenReturn(mockSharedPreferencesEditor);
        when(mockSharedPreferencesEditor.commit()).thenReturn(true);

        final SharedPreferencesDataAccessor sharedPreferencesDataAccessor = new SharedPreferencesDataAccessor();
        sharedPreferencesDataAccessor.save(preferencesFileName, preferencesKey, preferencesValue);

        verifyStatic(AppContextProvider.class);
        AppContextProvider.getAppContext();
        verify(mockAppContext).getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        verify(mockSharedPreferences).edit();
        verify(mockSharedPreferencesEditor).putString(preferencesKey, preferencesValue);
        verify(mockSharedPreferencesEditor).commit();
    }

    @Test
    public void testSave_ReturnsFalse() {
        final String preferencesFileName = Make.aString();
        final String preferencesKey = Make.aString();
        final String preferencesValue = Make.aString();

        final Context mockAppContext = mock(Context.class);
        final SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        final SharedPreferences.Editor mockSharedPreferencesEditor = mock(SharedPreferences.Editor.class);

        mockStatic(AppContextProvider.class);
        when(AppContextProvider.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor);
        when(mockSharedPreferencesEditor.putString(preferencesKey, preferencesValue)).thenReturn(mockSharedPreferencesEditor);
        when(mockSharedPreferencesEditor.commit()).thenReturn(false);

        final SharedPreferencesDataAccessor sharedPreferencesDataAccessor = new SharedPreferencesDataAccessor();
        final SharedPreferencesIOException sharedPreferencesIOException = Assert.assertThrows(SharedPreferencesIOException.class,
                () -> sharedPreferencesDataAccessor.save(preferencesFileName, preferencesKey, preferencesValue));
        Assert.assertEquals("Error saving shared preferences file " + preferencesFileName, sharedPreferencesIOException.getMessage());

        verifyStatic(AppContextProvider.class);
        AppContextProvider.getAppContext();
        verify(mockAppContext).getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        verify(mockSharedPreferences).edit();
        verify(mockSharedPreferencesEditor).putString(preferencesKey, preferencesValue);
        verify(mockSharedPreferencesEditor).commit();
    }

    @Test
    public void testGet() {
        final String preferencesFileName = Make.aString();
        final String preferencesKey = Make.aString();
        final String expectedPreferencesValue = Make.aString();

        final Context mockAppContext = mock(Context.class);
        final SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        final SharedPreferences.Editor mockSharedPreferencesEditor = mock(SharedPreferences.Editor.class);

        mockStatic(AppContextProvider.class);
        when(AppContextProvider.getAppContext()).thenReturn(mockAppContext);
        when(mockAppContext.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.getString(preferencesKey, null)).thenReturn(expectedPreferencesValue);

        final SharedPreferencesDataAccessor sharedPreferencesDataAccessor = new SharedPreferencesDataAccessor();
        final String preferencesValue = sharedPreferencesDataAccessor.get(preferencesFileName, preferencesKey);
        Assert.assertSame(expectedPreferencesValue, preferencesValue);

        verifyStatic(AppContextProvider.class);
        AppContextProvider.getAppContext();
        verify(mockAppContext).getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        verify(mockSharedPreferences).getString(preferencesKey, null);
    }
}
