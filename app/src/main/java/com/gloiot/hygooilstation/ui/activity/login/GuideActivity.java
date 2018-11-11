package com.gloiot.hygooilstation.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private static final int[] mImageIds = new int[]{R.mipmap.yindao1, R.mipmap.yindao2};
    private ArrayList<ImageView> mImageViewList;
    private ViewPager vp_guide;
    private TextView tv_guide_letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置无标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_guide;
    }

    @Override
    public void initComponent() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        tv_guide_letsgo = (TextView) findViewById(R.id.tv_guide_letsgo);
    }

    @Override
    public void initData() {
        mImageViewList = new ArrayList<>();
        // 初始化引导页的2个页面
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setAdjustViewBounds(true);//保持原图的长宽比
            image.setBackgroundResource(mImageIds[i]);// 设置引导页背景
            mImageViewList.add(image);
        }
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageListener());

        tv_guide_letsgo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide_letsgo:
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISGUIDEPAGESHOWED, true);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    //  ViewPager数据适配器
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    // ViewPager的滑动监听
    class GuidePageListener implements OnPageChangeListener {
        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        // 某个页面被选中
        @Override
        public void onPageSelected(int position) {

            if (position == mImageIds.length - 1) {// 最后一个页面
                tv_guide_letsgo.setVisibility(View.VISIBLE);// 显示开始体验的按钮
            } else {
                tv_guide_letsgo.setVisibility(View.INVISIBLE);
            }

        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

}
