package com.dysania.topactivity;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by DysaniazzZ on 06/04/2017.
 */
public class UIUtil {

    private static Toast mToast = null;

    /**
     * 创建Toast并显示
     */
    public static void createToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 创建Toast并显示
     *
     * @param strId strings里的字符串id
     */
    public static void createToast(Context context, int strId) {
        if (context == null) {
            return;
        }
        createToast(context, context.getResources().getString(strId));
    }
}
