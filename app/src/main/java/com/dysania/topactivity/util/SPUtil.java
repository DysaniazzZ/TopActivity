package com.dysania.topactivity.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class SPUtil {

    private static final String IS_TRACKER_WINDOW_SHOWN = "is_tracker_window_shown";
    private static final String IS_QS_TILE_ADDED = "is_qs_tile_added";
    private static final String IS_NOTIFICATION_ENABLED = "is_notification_enabled";

    public static boolean isTrackerWindowShown(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_TRACKER_WINDOW_SHOWN, false);
    }

    public static void setTrackerWindowShown(Context context, boolean isShow) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(IS_TRACKER_WINDOW_SHOWN, isShow).apply();
    }

    public static boolean isQSTileAdded(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_QS_TILE_ADDED, false);
    }

    public static void setQSTileAdded(Context context, boolean isAdded) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(IS_QS_TILE_ADDED, isAdded).apply();
        setNotificationEnabled(context, !isAdded);
    }

    public static boolean isNotificationEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_NOTIFICATION_ENABLED, true);
    }

    public static void setNotificationEnabled(Context context, boolean isEnabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(IS_NOTIFICATION_ENABLED, isEnabled).apply();
    }
}
