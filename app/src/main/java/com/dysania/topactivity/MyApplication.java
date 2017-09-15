package com.dysania.topactivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.dysania.topactivity.service.TrackerAccessibilityService;
import com.dysania.topactivity.service.TrackerService;
import com.dysania.topactivity.util.ComponentsUtil;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;

/**
 * Created by DysaniazzZ on 15/09/2017.
 */
public class MyApplication extends Application {

    private Context mContext;
    public static boolean sIsBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initActivityLifecycleCallbacks();
    }

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                sIsBackground = false;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //应用切到后台
                sIsBackground = true;
                if (getResources().getBoolean(R.bool.use_tracker_service) && SPUtil.isTrackerWindowShown(mContext) && ComponentsUtil
                        .isServiceRunning(mContext, TrackerService.class)) {
                    NotificationUtil.showNotification(mContext, false);
                    return;
                }

                if (getResources().getBoolean(R.bool.use_tracker_accessibility_service) && SPUtil.isTrackerWindowShown(mContext)
                        && TrackerAccessibilityService.getInstance() != null) {
                    NotificationUtil.showNotification(mContext, false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
}
