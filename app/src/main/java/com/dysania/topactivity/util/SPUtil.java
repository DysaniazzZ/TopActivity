package com.dysania.topactivity.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class SPUtil {

    private static final String IS_SHOW_WINDOW = "is_show_window";

    public static boolean isShowWindow(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_SHOW_WINDOW, false);
    }

    public static void setShowWindow(Context context, boolean isShow) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(IS_SHOW_WINDOW, isShow).apply();
    }
}
