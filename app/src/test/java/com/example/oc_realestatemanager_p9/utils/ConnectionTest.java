package com.example.oc_realestatemanager_p9.utils;

import static junit.framework.TestCase.assertTrue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;

import com.example.oc_realestatemanager_p9.ui.activity.MainActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)

 public class ConnectionTest {

    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private ActivityController<MainActivity> activity;

    @Before
    public void setUp() throws Exception {
        ConnectivityManager connectivityManager = getConnectivityManager();
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.getActiveNetworkInfo());
        activity = Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void testConnectionEnable() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        assertTrue(Utils.isInternetAvailable(activity.get()));
    }

    @Test
    public void testConnectionDisable(){
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        assertTrue(Utils.isInternetAvailable(activity.get()));
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}

