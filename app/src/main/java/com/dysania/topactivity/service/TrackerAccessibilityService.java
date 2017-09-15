package com.dysania.topactivity.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import com.dysania.topactivity.R;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;
import com.dysania.topactivity.util.TrackerWindowUtil;

/**
 * Created by DysaniazzZ on 13/04/2017.
 */
public class TrackerAccessibilityService extends AccessibilityService {

    private static TrackerAccessibilityService sTrackerAccessibilityService;

    public static TrackerAccessibilityService getInstance() {
        return sTrackerAccessibilityService;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && SPUtil.isTrackerWindowShown(this)) {
            TrackerWindowUtil.show(this, getString(R.string.top_activity_details, event.getPackageName(), event.getClassName()));
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sTrackerAccessibilityService = this;
        if (SPUtil.isTrackerWindowShown(this)) {
            NotificationUtil.showNotification(this, false);
        }
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sTrackerAccessibilityService = null;
        TrackerWindowUtil.dismiss();
        NotificationUtil.cancelNotification(this);
        return super.onUnbind(intent);
    }
}
