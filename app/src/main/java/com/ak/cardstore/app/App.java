package com.ak.cardstore.app;

import android.app.Application;
import android.content.Context;

import com.ak.cardstore.app.component.AppComponent;
import com.ak.cardstore.app.component.DaggerAppComponent;

/**
 * A utility class to provide the application properties.
 *
 * @author Abhishek
 */

public class App extends Application {

    private static Context appContext;
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this.getApplicationContext();
        appComponent = DaggerAppComponent.create();
    }

    /**
     * Returns the application {@link Context}.
     *
     * @return application {@link Context}
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * Returns the {@link AppComponent}
     *
     * @return {@link AppComponent}
     */
    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
