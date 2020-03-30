package com.ak.cardstore.dagger;

import com.ak.cardstore.configuration.AppConfigurationManager;
import com.ak.cardstore.configuration.UserConfigurationManager;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */
public class ConfigurationModuleUnitTest {

    private static final TestComponent CONFIGURATION_MODULE_TEST_COMPONENT = DaggerConfigurationModuleUnitTest_TestComponent.create();

    @Test
    public void testProvideUserConfigurationManager() {
        final UserConfigurationManager userConfigurationManager = CONFIGURATION_MODULE_TEST_COMPONENT.provideUserConfigurationManager();
        final UserConfigurationManager userConfigurationManagerOther = CONFIGURATION_MODULE_TEST_COMPONENT.provideUserConfigurationManager();

        assertNotNull(userConfigurationManager);
        assertNotNull(userConfigurationManagerOther);
        assertEquals(userConfigurationManager, userConfigurationManagerOther);
    }

    @Test
    public void testProvideAppConfigurationManager() {
        final AppConfigurationManager appConfigurationManager = CONFIGURATION_MODULE_TEST_COMPONENT.provideAppConfigurationManager();
        final AppConfigurationManager appConfigurationManagerOther = CONFIGURATION_MODULE_TEST_COMPONENT.provideAppConfigurationManager();

        assertNotNull(appConfigurationManager);
        assertNotNull(appConfigurationManagerOther);
        assertEquals(appConfigurationManager, appConfigurationManagerOther);
    }

    @Singleton
    @Component(modules = ConfigurationModule.class)
    public interface TestComponent {
        UserConfigurationManager provideUserConfigurationManager();

        AppConfigurationManager provideAppConfigurationManager();
    }
}
