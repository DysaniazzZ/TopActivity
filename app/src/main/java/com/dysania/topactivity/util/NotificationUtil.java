package com.dysania.topactivity.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import com.dysania.topactivity.R;
import com.dysania.topactivity.activity.SettingsActivity;

/**
 * Created by DysaniazzZ on 13/09/2017.
 */
public class NotificationUtil {

    private static final int NOTIFICATION_ID = 1;
    public static final String EXTRA_NOTIFICATION_ACTION = "extra_notification_action";
    public static final String ACTION_NOTIFICATION_CHANGED = "com.dysania.topactivity.ACTION_NOTIFICATION_CHANGED";

    public static final int ACTION_PAUSE = 0x01;
    public static final int ACTION_RESUME = 0x02;
    public static final int ACTION_STOP = 0x03;

    public static void showNotification(Context context, boolean isPaused) {
        PendingIntent activityIntent = PendingIntent.getActivity(context, 0, new Intent(context, SettingsActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.app_is_running, context.getString(R.string.app_name)))
                .setContentText(context.getString(R.string.action_to_settings))
                .setContentIntent(activityIntent)
                .setOngoing(!isPaused);

        if (isPaused) {
            builder.addAction(R.drawable.ic_action_resume, context.getString(R.string.action_to_resume),
                    getPendingIntent(context, ACTION_RESUME));
        } else {
            builder.addAction(R.drawable.ic_action_pause, context.getString(R.string.action_to_pause),
                    getPendingIntent(context, ACTION_PAUSE));
        }

        builder.addAction(R.drawable.ic_action_stop, context.getString(R.string.action_to_stop), getPendingIntent(context, ACTION_STOP));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private static PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(ACTION_NOTIFICATION_CHANGED);
        intent.putExtra(EXTRA_NOTIFICATION_ACTION, action);
        return PendingIntent.getBroadcast(context, action, intent, 0);
    }
}
