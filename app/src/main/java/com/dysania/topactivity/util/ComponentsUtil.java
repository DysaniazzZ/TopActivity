package com.dysania.topactivity.util;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import com.dysania.topactivity.R;
import java.util.List;

/**
 * Created by DysaniazzZ on 14/09/2017.
 */

public class ComponentsUtil {

    public static String getTopActivityDetail(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        ComponentName topActivity = runningTaskInfos.get(0).topActivity;
        return context.getString(R.string.top_activity_details, topActivity.getPackageName(), topActivity.getClassName());
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
