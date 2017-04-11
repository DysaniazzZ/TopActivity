package com.dysania.topactivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class TrackerService extends Service {

    private Timer mTimer;
    private String mTopActivityDetail;
    private ActivityManager mActivityManager;
    private NotificationManager mNotificationManager;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TrackerTask(), 0, 1000);  //每隔1秒就执行一次
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class TrackerTask extends TimerTask {

        @Override
        public void run() {
            //Note: Android 5.0 后这个方法将不可用
            List<RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(1);
            ComponentName topActivity = runningTaskInfos.get(0).topActivity;
            String topActivityDetail = getString(R.string.top_activity_detail, topActivity.getPackageName(), topActivity.getClassName());
            if (!topActivityDetail.equals(mTopActivityDetail)) {
                mTopActivityDetail = topActivityDetail;
                if (SPUtil.isShowWindow(TrackerService.this)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TrackerWindow.show(TrackerService.this, mTopActivityDetail);
                        }
                    });
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        TrackerWindow.dismiss();
    }
}
