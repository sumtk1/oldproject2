package com.gloiot.hygooilstation.ui.widget.picsmagnify;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView.ScaleType;

import com.gloiot.hygooilstation.utils.PictureUtlis;

import java.util.ArrayList;

public class SpaceImageDetailActivity extends AppCompatActivity {

    private ArrayList<String> mDatas;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    SmoothImageView imageView = null;
    //    ImageView imageView;
    protected Context mContext;

   private int mScreenWidth;
   private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //（新增）获取屏幕宽高，即显示图片的imageview的宽高
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels - getStatusBarHeight(this);

        mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        //需要显示的图片在list中的位置
        mPosition = getIntent().getIntExtra("position", 0);
        //要缩放的imageview的原始坐标
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        //imageview的原始大小
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ScaleType.FIT_CENTER);
//        imageView = new ImageView(this);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        setContentView(imageView);//这里报错。。

//        ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);

//        PictureUtlis.loadImageViewHolder(mContext, mDatas.get(mPosition), R.mipmap.jiazaitu, imageView);
        //加载需要显示的图片
        PictureUtlis.loadImageViewHolderForOilPic(mContext, mDatas.get(mPosition), imageView, mScreenWidth, mScreenHeight);

//        Glide.with(mContext)
//                .load( mDatas.get(mPosition))
//                .centerCrop()
//                .thumbnail(0.1f)
//                .placeholder(R.mipmap.jiazaitu)
//                .error(R.mipmap.jiazaitu)
//                .into(imageView);

//		imageView.setImageResource(R.drawable.temp);
        // ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
        // 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
        // 0.5f);
        // scaleAnimation.setDuration(300);
        // scaleAnimation.setInterpolator(new AccelerateInterpolator());
        // imageView.startAnimation(scaleAnimation);

    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

}
