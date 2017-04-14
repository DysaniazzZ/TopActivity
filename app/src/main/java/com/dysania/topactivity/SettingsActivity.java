package com.dysania.topactivity;

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

public class SettingsActivity extends AppCompatActivity implements OnCheckedChangeListener {

    private Context mContext;
    private Switch mSwShowDetails;
    private Intent mTrackerServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mSwShowDetails = (Switch) findViewById(R.id.sw_show_details);
        mSwShowDetails.setOnCheckedChangeListener(this);
        mTrackerServiceIntent = new Intent(mContext, TrackerService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwShowDetails.setChecked(SPUtil.isShowWindow(mContext));
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && TrackerAccessibilityService.getInstance() == null) {
            mSwShowDetails.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            //Android 5.0（不包括）以前使用普通服务轮询获取
            SPUtil.setShowWindow(mContext, isChecked);
            if (isChecked) {
                startService(mTrackerServiceIntent);
            } else {
                stopService(mTrackerServiceIntent);
            }
            return;
        }

        if (!isChecked) {
            SPUtil.setShowWindow(mContext, isChecked);
            TrackerWindow.dismiss();
            return;
        }

        //Android 5.0（包括）以后使用辅助服务获取前台活动
        if (VERSION.SDK_INT < VERSION_CODES.N_MR1) {
            //Android 7.1（不包括）之前只需要判断辅助服务是否开启
            whetherToShowStartAccessibilityServiceDialog();
        } else {
            //Android 7.1（包括）之后需要先判断悬浮窗权限是否授予
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
                return;
            } else {
                whetherToShowStartAccessibilityServiceDialog();
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
                            SPUtil.setShowWindow(mContext, true);
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
            SPUtil.setShowWindow(mContext, true);
            TrackerWindow.show(mContext, getString(R.string.top_activity_details, getPackageName(), getClass().getName()));
        }
    }
}
