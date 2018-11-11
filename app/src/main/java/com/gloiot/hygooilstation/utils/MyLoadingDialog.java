package com.gloiot.hygooilstation.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.gloiot.hygooilstation.R;

/**
 * Created by dlt on 2016/9/9.
 */
public class MyLoadingDialog extends Dialog implements DialogInterface {

    public static final int SlideTop = 0;
    public static final int SlideTopDismiss = 1;

    public static final int BtnCancel = 0;
    public static final int BtnNormal = 1;

    public volatile static MyLoadingDialog instance;
    private Context context;
    private int wHeight, width; // 屏幕高度宽度
    private View mDialogView;
    private RelativeLayout in_loading;

    public MyLoadingDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public MyLoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    public static MyLoadingDialog getInstance(Context context) {
//        if (instance == null) {
//            synchronized (MyDialogBuilder.class) {
//                if (instance == null) {
        instance = new MyLoadingDialog(context, R.style.dialog_untran);
//                }
//            }
//        }
        return instance;
    }

    public void init() {
        wHeight = getScreenHeight();
        width = getScreenWidth();
        mDialogView = View.inflate(context, R.layout.include_loading, null);
        in_loading = (RelativeLayout) mDialogView.findViewById(R.id.in_loading);
        setContentView(mDialogView);
    }

    /**
     * 设置最大高度
     *
     * @return
     */
    public MyLoadingDialog setMaxHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = view.getHeight();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (cHeight >= wHeight * 1 / 3) {
                    lp.height = wHeight * 1 / 3;
                }
                view.setLayoutParams(lp);
            }
        });
        return this;
    }

    /**
     * 显示Dialog
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * Dialog消失无动画
     */
    public void dismissNoAnimator() {
        instance = null;
        superDismiss();
    }

    /**
     * 继承父类消失Dialog
     */
    public void superDismiss() {
        super.dismiss();
    }

    // 获得屏幕高度
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    // 获得屏幕宽度
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
