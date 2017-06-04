package com.momu.tale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.momu.tale.activity.PinLockActivity;
import com.momu.tale.preference.AppPreference;
import com.momu.tale.utility.LogHelper;

import java.util.List;

/**
 * Created by knulps on 2017. 6. 4..
 */

public class MyApplication extends Application implements ActivityLifecycleCallbacks {
    private static final String TAG = "TaleApplication";

    public static String stateOfLifeCycle = "";
    public static boolean wasInBackground = false;

    private String currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        LogHelper.e(TAG, "application oncreate");

//        if(!AppPreference.loadScreenPinNumber(getApplicationContext()).equals("")) {
//            LogHelper.e(TAG, "잠금화면 시작!");
//            Intent lockIntent = new Intent(getApplicationContext(), PinLockActivity.class);
//            lockIntent.putExtra("isLockMode", true);
//            startActivity(lockIntent);
//        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        LogHelper.e(TAG, "onActivityCreated:" + activity.getLocalClassName());
        wasInBackground = false;
        stateOfLifeCycle = "Create";
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogHelper.e(TAG, "onActivityDestroyed:" + activity.getLocalClassName());
        wasInBackground = false;
        stateOfLifeCycle = "Destroy";
    }

    @Override
    public void onTrimMemory(int level) {
//        LogHelper.e(TAG, "onTrimMemory:");
        if (stateOfLifeCycle.equals("Stop")) {

            wasInBackground = true;
            LogHelper.e(TAG, "wasinBackground true");
        }
        super.onTrimMemory(level);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogHelper.e(TAG, "onActivityPaused:" + activity.getLocalClassName());
        stateOfLifeCycle = "Pause";
    }
    @Override
    public void onActivityResumed(Activity activity) {
        LogHelper.e(TAG, "onActivityResumed:" + activity.getLocalClassName());
        stateOfLifeCycle = "Resume";
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity,
                                            Bundle outState) {
        LogHelper.e(TAG, "onActivitySaveInstanceState:" + activity.getLocalClassName());
    }
    @Override
    public void onActivityStarted(Activity activity) {
        LogHelper.e(TAG, "wasinBackground : " + wasInBackground  + ", localclassname : " + activity.getLocalClassName() +", currentActivity : " + currentActivity);

        if(!AppPreference.loadScreenPinNumber(activity).equals("") && wasInBackground && !activity.getLocalClassName().equals("activity.PinLockActivity") && !(currentActivity != null && currentActivity.equals("activity.PinLockActivity"))) {
            LogHelper.e(TAG, "잠금화면 시작!");
            Intent lockIntent = new Intent(activity, PinLockActivity.class);
            lockIntent.putExtra("isLockMode", true);
            activity.startActivity(lockIntent);
        }

        currentActivity = activity.getLocalClassName();
        LogHelper.e(TAG, "onActivityStarted:" + activity.getLocalClassName() + ", " + wasInBackground);
        stateOfLifeCycle = "Start";

        wasInBackground = false;
    }

    public void onActivityStopped(Activity activity) {
        LogHelper.e(TAG, "onActivityStopped:" + activity.getLocalClassName());
        stateOfLifeCycle = "Stop";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
