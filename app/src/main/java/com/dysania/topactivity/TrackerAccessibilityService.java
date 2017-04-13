package com.dysania.topactivity;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

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
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && SPUtil.isShowWindow(this)) {
            TrackerWindow.show(this, getString(R.string.top_activity_detail, event.getPackageName(), event.getClassName()));
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sTrackerAccessibilityService = this;
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sTrackerAccessibilityService = null;
        TrackerWindow.dismiss();
        return super.onUnbind(intent);
    }
}
