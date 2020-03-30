package com.ak.cardstore.dagger;

import com.ak.cardstore.dao.FileBasedDataAccessor;
import com.ak.cardstore.dao.SharedPreferencesDataAccessor;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */

public class DaoModuleUnitTest {

    private static final TestComponent DAO_MODULE_TEST_COMPONENT = DaggerDaoModuleUnitTest_TestComponent.create();

    @Test
    public void testProvideSharedPreferencesDataAccessor() {
        final SharedPreferencesDataAccessor sharedPreferencesDataAccessor = DAO_MODULE_TEST_COMPONENT.provideSharedPreferencesDataAccessor();
        final SharedPreferencesDataAccessor sharedPreferencesDataAccessorOther = DAO_MODULE_TEST_COMPONENT.provideSharedPreferencesDataAccessor();

        assertNotNull(sharedPreferencesDataAccessor);
        assertNotNull(sharedPreferencesDataAccessorOther);
        assertEquals(sharedPreferencesDataAccessor, sharedPreferencesDataAccessorOther);
    }

    @Test
    public void testProvideFileBasedDataAccessor() {
        final FileBasedDataAccessor fileBasedDataAccessor = DAO_MODULE_TEST_COMPONENT.provideFileBasedDataAccessor();
        final FileBasedDataAccessor fileBasedDataAccessorOther = DAO_MODULE_TEST_COMPONENT.provideFileBasedDataAccessor();

        assertNotNull(fileBasedDataAccessor);
        assertNotNull(fileBasedDataAccessorOther);
        assertEquals(fileBasedDataAccessor, fileBasedDataAccessorOther);
    }

    @Singleton
    @Component(modules = DaoModule.class)
    public interface TestComponent {
        SharedPreferencesDataAccessor provideSharedPreferencesDataAccessor();

        FileBasedDataAccessor provideFileBasedDataAccessor();
    }
}
