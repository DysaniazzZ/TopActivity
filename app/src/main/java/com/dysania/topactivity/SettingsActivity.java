package com.dysania.topactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        if(isShowWindow) {
            startService(mTrackerServiceIntent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SPUtil.setShowWindow(mContext, isChecked);
        if (isChecked) {
            startService(mTrackerServiceIntent);
        } else {
            stopService(mTrackerServiceIntent);
        }
    }
}
