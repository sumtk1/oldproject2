package com.gloiot.hygooilstation.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by lvjiayu on 2016/10/5.
 */

public class NumIncreaseTextView extends TextView {

    //动画持续时间
    private int duration;
    //显示的数字
    private float number;

    public NumIncreaseTextView(Context context) {
        super(context, null);
    }

    public NumIncreaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public NumIncreaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showNumberWithAnimation(float number) {
        //修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, number);
        objectAnimator.setDuration(duration);
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public void showNumberWithAnimation() {
        //修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, number);
        objectAnimator.setDuration(duration);
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public float getNumber() {
        return number;
    }

    //设置数字
    public void setNumber(float number) {
        this.number = number;
        setText(String.format("%1$04.2f", number));
    }

    //设置持续时间
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
