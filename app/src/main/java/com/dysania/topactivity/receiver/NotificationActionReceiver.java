package com.dysania.topactivity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dysania.topactivity.R;
import com.dysania.topactivity.util.ComponentsUtil;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;
import com.dysania.topactivity.util.TrackerWindowUtil;

/**
 * Created by DysaniazzZ on 13/09/2017.
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra(NotificationUtil.EXTRA_NOTIFICATION_ACTION, 0);
        switch (action) {
            case NotificationUtil.ACTION_PAUSE:
                TrackerWindowUtil.dismiss();
                SPUtil.setTrackerWindowShown(context, false);
                NotificationUtil.showNotification(context, true);
                break;
            case NotificationUtil.ACTION_RESUME:
                if (context.getResources().getBoolean(R.bool.use_tracker_service)) {
                    String topActivityDetail = ComponentsUtil.getTopActivityDetail(context);
                    TrackerWindowUtil.show(context, topActivityDetail);
                } else if (context.getResources().getBoolean(R.bool.use_tracker_accessibility_service)) {
                    TrackerWindowUtil.show(context, null);
                }
                SPUtil.setTrackerWindowShown(context, true);
                NotificationUtil.showNotification(context, false);
                break;
            case NotificationUtil.ACTION_STOP:
                TrackerWindowUtil.dismiss();
                SPUtil.setTrackerWindowShown(context, false);
                NotificationUtil.cancelNotification(context);
                break;
        }
    }
}
