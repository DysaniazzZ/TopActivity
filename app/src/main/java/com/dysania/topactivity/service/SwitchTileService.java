package com.dysania.topactivity.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.dysania.topactivity.activity.SettingsActivity;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;
import com.dysania.topactivity.util.TrackerWindowUtil;

/**
 * Created by DysaniazzZ on 15/09/2017.
 */
@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.N)
public class SwitchTileService extends TileService {

    private boolean mIsTileActive = false;

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        SPUtil.setQSTileAdded(this, true);
        NotificationUtil.cancelNotification(this);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        SPUtil.setQSTileAdded(this, false);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState(isTrackerAvailable() && SPUtil.isTrackerWindowShown(this));
    }

    @Override
    public void onClick() {
        super.onClick();
        if (mIsTileActive) {
            TrackerWindowUtil.dismiss();
            SPUtil.setTrackerWindowShown(this, false);
        } else {
            if (isTrackerAvailable()) {
                TrackerWindowUtil.show(this, null);
                SPUtil.setTrackerWindowShown(this, true);
            } else {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(SettingsActivity.EXTRA_FROM_QS_TILE, true);
                startActivityAndCollapse(intent);
            }
        }
        updateTileState(!mIsTileActive);
    }

    private void updateTileState(boolean isTileActive) {
        Tile tile = getQsTile();
        int newState = isTileActive ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
        tile.setState(newState);
        tile.updateTile();

        mIsTileActive = isTileActive;
    }

    private boolean isTrackerAvailable() {
        return TrackerAccessibilityService.getInstance() != null && Settings.canDrawOverlays(this);
    }
}
