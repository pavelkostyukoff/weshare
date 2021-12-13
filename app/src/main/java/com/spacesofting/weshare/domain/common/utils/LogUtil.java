package com.spacesofting.weshare.domain.common.utils;

import android.util.Log;

import com.spacesofting.weshare.BuildConfig;

public abstract class LogUtil {



    public static void Log(String msg) {

        String full_msg = getClassName(3) + ", " + getMethodName(3) + "(): " + msg;
        if (BuildConfig.DBG)
            Log.d(BuildConfig.TAG, full_msg);

      //  if (!BuildConfig.DBG)
         //   Crashlytics.log(full_msg);

    }

    public static void Log() {

        String full_msg = getClassName(3) + ", " + getMethodName(3) + "()";
        if (BuildConfig.DBG)
            Log.d(BuildConfig.TAG, full_msg);

        if (!BuildConfig.DBG){

        }
         //   Crashlytics.log(full_msg);
    }

    public static void Loge(String msg, Exception e) {

        String full_msg = getClassName(3) + ", " + getMethodName(3) + "(): " + msg;
        Log.e(BuildConfig.TAG, full_msg);

        if (!BuildConfig.DBG){}
         //   Crashlytics.logException(e);
    }

    public static void Loge(Exception e) {

        Log.e(BuildConfig.TAG, getClassName(3) + ", " + getMethodName(3)
                + "()");

        if (!BuildConfig.DBG){

        }
           // Crashlytics.logException(e);
    }

    public static void Loge(String msg) {

        String full_msg = getClassName(3) + ", " + getMethodName(3) + "(): " + msg;
        Log.e(BuildConfig.TAG, full_msg);

        if (!BuildConfig.DBG){}
           // Crashlytics.log(full_msg);
    }

    public static void Logi(String msg) {

        String full_msg = getClassName(3) + ", " + getMethodName(3) + "(): " + msg;
        Log.i(BuildConfig.TAG, full_msg);

        if (!BuildConfig.DBG){}
       //     Crashlytics.log(full_msg);
    }


    private static String getMethodName(final int depth) {

        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[1 + depth].getMethodName();
    }

    private static String getClassName(final int depth) {

        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[1 + depth].getClassName();
    }
}
