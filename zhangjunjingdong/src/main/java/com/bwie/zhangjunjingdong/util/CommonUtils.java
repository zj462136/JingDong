package com.bwie.zhangjunjingdong.util;

import android.content.SharedPreferences;

import com.bwie.zhangjunjingdong.application.DashApplication;

public class CommonUtils {

    public static final String TAG = "Dash";
    private static SharedPreferences sharedPreferences;


    public static void runOnUIThread(Runnable runable) {

        if (android.os.Process.myTid() == DashApplication.getMainThreadId()) {
            runable.run();
        } else {

            DashApplication.getAppHanler().post(runable);
        }
    }


    public static void saveString(String flag, String str) {
        if (sharedPreferences == null) {
            sharedPreferences = DashApplication.getAppContext().getSharedPreferences(TAG, DashApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(flag, str);
        edit.commit();
    }

    public static String getString(String flag) {
        if (sharedPreferences == null) {
            sharedPreferences = DashApplication.getAppContext().getSharedPreferences(TAG, DashApplication.getAppContext().MODE_PRIVATE);
        }
        return sharedPreferences.getString(flag, "");
    }

    public static boolean getBoolean(String tag) {
        if (sharedPreferences == null) {
            sharedPreferences = DashApplication.getAppContext().getSharedPreferences(TAG, DashApplication.getAppContext().MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(tag, false);
    }

    public static void putBoolean(String tag, boolean content) {
        if (sharedPreferences == null) {
            sharedPreferences = DashApplication.getAppContext().getSharedPreferences(TAG, DashApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(tag, content);
        edit.commit();
    }


    public static void clearSp(String tag) {
        if (sharedPreferences == null) {
            sharedPreferences = DashApplication.getAppContext().getSharedPreferences(TAG, DashApplication.getAppContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(tag);
        edit.commit();
    }
}
