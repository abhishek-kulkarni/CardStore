package com.ak.cardstore.app;

import android.content.Context;
import android.os.Build;

import com.ak.cardstore.app.component.AppComponent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author Abhishek
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1, Build.VERSION_CODES.P})
public class AppUnitTest {

    @Test
    public void testGetAppContext() {
        final Context context = App.getAppContext();
        Assert.assertNotNull(context);
    }

    @Test
    public void testGetAppComponent() {
        final AppComponent appComponent = App.getAppComponent();
        Assert.assertNotNull(appComponent);
    }
}
