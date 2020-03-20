package com.ak.cardstore.context;

import android.app.Application;
import android.content.Context;

/**
 * A utility class to provide the application {@link android.content.Context}.
 *
 * @author Abhishek
 */

public class AppContextProvider extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this.getApplicationContext();
    }

    /**
     * Returns the application {@link Context}.
     *
     * @return application {@link Context}
     */
    public static Context getAppContext() {
        return appContext;
    }
}
