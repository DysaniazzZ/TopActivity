package com.dysania.topactivity.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.dysania.topactivity.R;
import com.dysania.topactivity.service.TrackerAccessibilityService;
import com.dysania.topactivity.service.TrackerService;
import com.dysania.topactivity.util.ComponentsUtil;
import com.dysania.topactivity.util.NotificationUtil;
import com.dysania.topactivity.util.SPUtil;
import com.dysania.topactivity.util.TrackerWindowUtil;

public class SettingsActivity extends AppCompatActivity implements OnCheckedChangeListener {

    private Context mContext;
    private Switch mSwShowDetails;
    private Intent mTrackerServiceIntent;

    public static final String EXTRA_FROM_QS_TILE = "extra_from_qs_tile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;

        mSwShowDetails = (Switch) findViewById(R.id.sw_show_details);
        mSwShowDetails.setOnCheckedChangeListener(this);

        mTrackerServiceIntent = new Intent(mContext, TrackerService.class);

        if (getIntent().getBooleanExtra(EXTRA_FROM_QS_TILE, false)) {
            mSwShowDetails.setChecked(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(EXTRA_FROM_QS_TILE, false)) {
            mSwShowDetails.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSwitchStatus();
        NotificationUtil.cancelNotification(mContext);
    }

    private void refreshSwitchStatus() {
        mSwShowDetails.setChecked(SPUtil.isTrackerWindowShown(mContext));

        if (getResources().getBoolean(R.bool.use_tracker_service) && !ComponentsUtil.isServiceRunning(mContext, TrackerService.class)) {
            mSwShowDetails.setChecked(false);
            return;
        }

        if (getResources().getBoolean(R.bool.use_tracker_accessibility_service) && TrackerAccessibilityService.getInstance() == null) {
            mSwShowDetails.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (getResources().getBoolean(R.bool.use_tracker_service)) {
            //TrackerService 可用时
            if (isChecked) {
                startService(mTrackerServiceIntent);
            } else {
                stopService(mTrackerServiceIntent);
            }
            SPUtil.setTrackerWindowShown(mContext, isChecked);
            return;
        } else if (getResources().getBoolean(R.bool.use_tracker_accessibility_service)) {
            //TrackerAccessibilityService 可用时
            if (!isChecked) {
                SPUtil.setTrackerWindowShown(mContext, isChecked);
                TrackerWindowUtil.dismiss();
                return;
            }

            //Android 5.0（包括）以后使用辅助服务获取前台活动
            if (VERSION.SDK_INT < VERSION_CODES.M) {
                //Android 6.0（不包括）之前只需要判断辅助服务是否开启
                whetherToShowStartAccessibilityServiceDialog();
            } else {
                //Android 6.0（包括）之后需要先判断悬浮窗权限是否授予
                if (!Settings.canDrawOverlays(mContext)) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(getString(R.string.enable_floating_window_msg))
                            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSwShowDetails.setChecked(false);
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mSwShowDetails.setChecked(false);
                                }
                            })
                            .show();
                } else {
                    whetherToShowStartAccessibilityServiceDialog();
                }
            }
        }
    }

    private void whetherToShowStartAccessibilityServiceDialog() {
        if (TrackerAccessibilityService.getInstance() == null) {
            new AlertDialog.Builder(mContext)
                    .setMessage(getString(R.string.enable_accessibility_msg))
                    .setPositiveButton(android.R.string.yes, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtil.setTrackerWindowShown(mContext, true);
                            Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSwShowDetails.setChecked(false);
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mSwShowDetails.setChecked(false);
                        }
                    })
                    .show();
        } else {
            SPUtil.setTrackerWindowShown(mContext, true);
            TrackerWindowUtil.show(mContext, getString(R.string.top_activity_details, getPackageName(), getClass().getName()));
        }
    }
}
