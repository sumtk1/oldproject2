package com.gloiot.hygooilstation.ui.activity.trade.aboutpay;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.trade.GatheringActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;

/**
 * 选择收款方式（扫码/生成二维码）
 * Created by Dlt on 2017/5/25 10:40
 */
public class SelectGatheringStyleActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_scan_qr_code, ll_generate_qr_code;

    @Override
    public int initResource() {
        return R.layout.activity_select_gathering_style;
    }

    @Override
    public void initComponent() {
        ll_scan_qr_code = (LinearLayout) findViewById(R.id.ll_scan_qr_code);
        ll_generate_qr_code = (LinearLayout) findViewById(R.id.ll_generate_qr_code);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "收款", "");
        ll_scan_qr_code.setOnClickListener(this);
        ll_generate_qr_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_scan_qr_code://扫码
                startActivity(new Intent(SelectGatheringStyleActivity.this, SetPayMoneyActivity.class));
                break;
            case R.id.ll_generate_qr_code://生成二维码
                startActivity(new Intent(SelectGatheringStyleActivity.this, GatheringActivity.class));
                break;
        }
    }


}
