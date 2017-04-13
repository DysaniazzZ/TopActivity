package com.dysania.topactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity implements OnCheckedChangeListener {

    private Context mContext;
    private Switch mSwShowWindow;
    private Intent mTrackerServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mSwShowWindow = (Switch) findViewById(R.id.sw_show_window);
        mSwShowWindow.setOnCheckedChangeListener(this);
        mTrackerServiceIntent = new Intent(mContext, TrackerService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isShowWindow = SPUtil.isShowWindow(mContext);
        mSwShowWindow.setChecked(isShowWindow);
        //保证开关打开时服务是开启的
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            if (isShowWindow) {
                startService(mTrackerServiceIntent);
            }
        } else {
            if (TrackerAccessibilityService.getInstance() == null) {
                mSwShowWindow.setChecked(false);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SPUtil.setShowWindow(mContext, isChecked);
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            if (isChecked) {
                startService(mTrackerServiceIntent);
            } else {
                stopService(mTrackerServiceIntent);
            }
        } else {
            if (isChecked) {
                if (TrackerAccessibilityService.getInstance() == null) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(getString(R.string.enable_accessibility_msg))
                            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSwShowWindow.setChecked(false);
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mSwShowWindow.setChecked(false);
                                }
                            })
                            .show();
                } else {
                    TrackerWindow.show(mContext, getString(R.string.top_activity_detail, getPackageName(), getClass().getName()));
                }
            } else {
                TrackerWindow.dismiss();
            }
        }
    }
}
