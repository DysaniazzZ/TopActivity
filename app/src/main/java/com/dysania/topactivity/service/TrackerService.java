package com.dysania.topactivity.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.dysania.topactivity.util.ComponentsUtil;
import com.dysania.topactivity.util.SPUtil;
import com.dysania.topactivity.util.TrackerWindowUtil;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class TrackerService extends Service {

    private Timer mTimer;
    private String mTopActivityDetail;
    private Handler mHandler = new Handler();

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
            String topActivityDetail = ComponentsUtil.getTopActivityDetail(getApplicationContext());
            if (!topActivityDetail.equals(mTopActivityDetail)) {
                mTopActivityDetail = topActivityDetail;
                if (SPUtil.isTrackerWindowShown(TrackerService.this)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TrackerWindowUtil.show(TrackerService.this, mTopActivityDetail);
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
        TrackerWindowUtil.dismiss();
    }
}
