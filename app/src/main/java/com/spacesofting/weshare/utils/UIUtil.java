package com.spacesofting.weshare.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;


public class UIUtil {

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean setTranslucentStatus(Activity activity) {
        return setTranslucent(activity, false, true);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean setTranslucent(Activity activity, boolean isNavigation, boolean isStatus) {
        if (!hasKitKat()) {
            return false;
        }
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = 0;
        if (isNavigation && !hasMenuKey) {
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            winParams.flags |= bits;
        }
        if (isStatus) {
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
        }
        if (bits != 0) {
            win.setAttributes(winParams);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Устанавливает язык приложения, затем необходимо перезапустить приложение
     * @param context контекст
     * @param language язык
     * @param country страна
     */
    public static void setLocale(Context context, String language, String country) {
        Locale locale = new Locale(language, country);

        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
    }
}
