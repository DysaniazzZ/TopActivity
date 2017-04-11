package com.dysania.topactivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class TrackerWindow {

    private static WindowManager sWindowManager;
    private static LayoutParams sLayoutParams;
    private static View sView;

    public static void init(Context context) {
        sWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        sLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_TOAST,
                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        sLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        sView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.view_tracker_window, null);
    }

    public static void show(Context context, String content) {
        if (sWindowManager == null) {
            init(context);
        }
        try {
            TextView textView = (TextView) sView.findViewById(R.id.tv_tracker_content);
            textView.setText(content);
            sWindowManager.addView(sView, sLayoutParams);
        } catch (Exception e) {
        }
    }

    public static void dismiss() {
        try {
            sWindowManager.removeView(sView);
        } catch (Exception e) {
        }
    }
}
