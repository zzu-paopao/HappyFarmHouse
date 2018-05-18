package com.zzu.luanchuan.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class MyActivityManagerUtils {



    private static MyActivityManagerUtils sInstance = new MyActivityManagerUtils();
    private WeakReference<Activity> sCurrentActivityWeakRef;


    private MyActivityManagerUtils() {

    }

    public static MyActivityManagerUtils getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

}
