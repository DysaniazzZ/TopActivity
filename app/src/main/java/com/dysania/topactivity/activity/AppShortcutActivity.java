package com.dysania.topactivity.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.dysania.topactivity.service.TrackerAccessibilityService;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;

/**
 * Created by DysaniazzZ on 14/09/2017.
 */
@TargetApi(VERSION_CODES.M)
public class AppShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(TrackerAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            SPUtil.setTrackerWindowShown(this, true);
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
            return;
        }

        boolean isShown = SPUtil.isTrackerWindowShown(this);
        Intent intent = new Intent(NotificationUtil.ACTION_NOTIFICATION_CHANGED);
        if(isShown) {
            intent.putExtra(NotificationUtil.EXTRA_NOTIFICATION_ACTION, NotificationUtil.ACTION_PAUSE);
        } else {
            intent.putExtra(NotificationUtil.EXTRA_NOTIFICATION_ACTION, NotificationUtil.ACTION_RESUME);
        }
        sendBroadcast(intent);
        finish();
    }
}
