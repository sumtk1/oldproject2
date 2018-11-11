package com.gloiot.hygooilstation.ui.activity.trade.aboutpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化扫描界面
 * Created by Dlt on 2017/5/25 16:07
 */
public class CaptureActivity extends BaseActivity {

    private CaptureFragment captureFragment;
    private ImageView iv_toptitle_more_img;
    private boolean isFlashlightOpen = false;//闪光灯是否打开

    @Override
    public int initResource() {
        return R.layout.activity_capture;
    }

    @Override
    public void initComponent() {
        iv_toptitle_more_img = (ImageView) findViewById(R.id.iv_toptitle_more_img);
        iv_toptitle_more_img.setImageResource(R.drawable.ic_shoudiantong);
    }

    @Override
    public void initData() {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        CommonUtlis.setTitleBar(this, true, "扫一扫", "");
        iv_toptitle_more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlashlightOpen) {
                    CodeUtils.isLightEnable(true);
                    isFlashlightOpen = true;
                    iv_toptitle_more_img.setImageResource(R.drawable.ic_shoudiantong_sel);
                } else {
                    CodeUtils.isLightEnable(false);
                    isFlashlightOpen = false;
                    iv_toptitle_more_img.setImageResource(R.drawable.ic_shoudiantong);
                }
            }
        });
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };

}
