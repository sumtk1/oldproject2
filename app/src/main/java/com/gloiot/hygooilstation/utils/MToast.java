package com.gloiot.hygooilstation.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by JinzLin on 2016/7/7.
 * Toast工具类
 */
public class MToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null; // toast隐藏后，将其置为null
        }
    };
    private static String toastContet = "";

    /**
     * 显示Toast
     *
     * @param context
     * @param content
     */
    public static void showToast(final Context context, final String content) {
        if (toastContet.equals(content)) { // 判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) { // 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) { // mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
