package com.ak.cardstore.context;

import android.content.Context;
import android.os.Build;

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
public class AppContextProviderUnitTest {

    @Test
    public void testGetAppContext() {
        final Context context = AppContextProvider.getAppContext();
        Assert.assertNotNull(context);
    }
}
