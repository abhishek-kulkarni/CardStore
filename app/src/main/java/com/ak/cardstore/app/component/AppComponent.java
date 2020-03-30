package com.ak.cardstore.app.component;

import com.ak.cardstore.activity.login.LoginActivity;
import com.ak.cardstore.activity.register.RegisterActivity;
import com.ak.cardstore.dagger.ConfigurationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component class to generate dependencies needed by the application.
 *
 * @author Abhishek
 */

@Singleton
@Component(modules = ConfigurationModule.class)
public interface AppComponent {

    /**
     * Inject dependencies of the {@link LoginActivity}
     *
     * @param loginActivity {@link LoginActivity}
     */
    void inject(final LoginActivity loginActivity);

    /**
     * Inject dependencies of the {@link RegisterActivity}
     *
     * @param registerActivity {@link RegisterActivity}
     */
    void inject(final RegisterActivity registerActivity);
}
