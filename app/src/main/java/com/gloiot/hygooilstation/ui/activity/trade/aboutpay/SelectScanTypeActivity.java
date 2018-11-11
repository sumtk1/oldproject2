package com.gloiot.hygooilstation.ui.activity.trade.aboutpay;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 选择扫码支付方式（支付宝/微信）
 * Created by Dlt on 2017/5/25 15:03
 */
public class SelectScanTypeActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_HYGO = 111;
    public static final int REQUEST_CODE_WECHATE = 112;
    private RelativeLayout rl_type_hygo, rl_type_wechat;

    @Override
    public int initResource() {
        return R.layout.activity_select_scan_type;
    }

    @Override
    public void initComponent() {
        rl_type_hygo = (RelativeLayout) findViewById(R.id.rl_type_hygo);
        rl_type_wechat = (RelativeLayout) findViewById(R.id.rl_type_wechat);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "扫码收款", "");
        rl_type_hygo.setOnClickListener(this);
        rl_type_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_type_hygo:

                checkPermission(new BaseActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new BaseActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
                                Intent intent = new Intent(SelectScanTypeActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, REQUEST_CODE_HYGO);
                            }
                        }, R.string.perm_VIBRATE, Manifest.permission.VIBRATE);

                    }
                }, R.string.perm_camera, Manifest.permission.CAMERA);

                break;
            case R.id.rl_type_wechat:
                checkPermission(new BaseActivity.CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new BaseActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
                                Intent intent = new Intent(SelectScanTypeActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, REQUEST_CODE_WECHATE);
                            }
                        }, R.string.perm_VIBRATE, Manifest.permission.VIBRATE);

                    }
                }, R.string.perm_camera, Manifest.permission.CAMERA);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE_HYGO) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    MToast.showToast(mContext, "解析环游购二维码成功--" + result);

                    startActivity(new Intent(SelectScanTypeActivity.this,PayStateActivity.class));

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    MToast.showToast(mContext, "解析二维码失败");
                }
            }
        } else if (requestCode == REQUEST_CODE_WECHATE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    MToast.showToast(mContext, "解析微信二维码成功--" + result);

                    startActivity(new Intent(SelectScanTypeActivity.this,PayStateActivity.class));
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    MToast.showToast(mContext, "解析二维码失败");
                }
            }
        }
    }

}
