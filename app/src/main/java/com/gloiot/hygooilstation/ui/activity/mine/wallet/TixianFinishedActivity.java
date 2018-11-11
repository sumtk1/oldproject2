package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;

import java.text.DecimalFormat;

/**
 * 提现申请已提交（提现完成）
 */
public class TixianFinishedActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_finishtixian_bankcard, tv_finishtixian_tixian, tv_finishtixian_poundage,
            tv_wait_message, tv_account_type, tv_finishtixian_over;

    @Override
    public int initResource() {
        return R.layout.activity_tixian_detail;
    }

    @Override
    public void initComponent() {
        tv_finishtixian_bankcard = (TextView) findViewById(R.id.tv_finishtixian_bankcard);
        tv_finishtixian_tixian = (TextView) findViewById(R.id.tv_finishtixian_tixian);
        tv_wait_message = (TextView) findViewById(R.id.tv_wait_message);
        tv_account_type = (TextView) findViewById(R.id.tv_account_type);
        tv_finishtixian_poundage = (TextView) findViewById(R.id.tv_finishtixian_poundage);//手续费
        tv_finishtixian_over = (TextView) findViewById(R.id.tv_finishtixian_over);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, false, "提现", "");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Intent intent = getIntent();
        String extractType = intent.getStringExtra("extractType");
        tv_finishtixian_bankcard.setText(intent.getStringExtra("bankName"));
        if (extractType.equals("bankcard")) {
            tv_wait_message.setText("请等待银行处理（1-2天）");
            tv_account_type.setText("银行卡");
        } else if (extractType.equals("alipay")) {
            tv_wait_message.setText("请等待支付宝处理（1-2天）");
            tv_account_type.setText("支付宝");
        }

        String money = intent.getStringExtra("money");

        if (!TextUtils.isEmpty(money)) {
            try {
                Double doubleMoney = Double.parseDouble(money);
                tv_finishtixian_tixian.setText("￥" + decimalFormat.format(doubleMoney));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            tv_finishtixian_tixian.setText("￥" + decimalFormat.format(money));
        }

        tv_finishtixian_poundage.setText("￥" + intent.getStringExtra("shouxufei"));
        tv_finishtixian_over.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_finishtixian_over:
                WalletActivity.walletActivity.finish();
                TixianActivity.tiXianActivity.finish();
                startActivity(new Intent(this, WalletActivity.class));
                finish();
                break;
        }

    }

}
