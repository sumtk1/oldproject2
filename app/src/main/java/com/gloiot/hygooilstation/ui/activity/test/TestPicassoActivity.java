package com.gloiot.hygooilstation.ui.activity.test;

import android.view.View;
import android.widget.ImageView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.PictureUtlis;

import butterknife.Bind;
import butterknife.OnClick;

public class TestPicassoActivity extends BaseActivity {

    @Bind(R.id.iv_image)
    ImageView mIvImage;
    private String url1 = "", url2 = "";

    @Override
    public int initResource() {
        return R.layout.activity_test_picasso;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        url1 = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/qqwlw/hygoilstationPics/release/20180122022534738800681.jpg";
        url2 = "http://oss.zhenxuanzhuangyuan.com/tj-hygo-shop/hygoilstationPics/release/201801220208041852530178.jpg";
    }

    @OnClick({R.id.tv_1, R.id.tv_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                PictureUtlis.loadImageViewHolder(mContext, url1, R.mipmap.jiazaitu, mIvImage);
                break;
            case R.id.tv_2:
                PictureUtlis.loadImageViewHolder(mContext, url2, R.mipmap.jiazaitu, mIvImage);
                break;
        }
    }
}
