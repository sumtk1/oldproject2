package com.gloiot.hygooilstation.ui.activity.trade.aboutpay;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MdStyleProgress;
import com.gloiot.hygooilstation.utils.CommonUtlis;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 扫码支付状态
 * Created by Dlt on 2017/5/25 16:43
 */
public class PayStateActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_toptitle_title)
    TextView tvToptitleTitle;
    @Bind(R.id.progress)
    MdStyleProgress progress;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.tv_pay_state)
    TextView tvPayState;
    @Bind(R.id.btn_success)
    Button btnSuccess;
    @Bind(R.id.btn_failed)
    Button btnFailed;

    @Override
    public int initResource() {
        return R.layout.activity_pay_state;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "支付结果", "");
        tvPayMoney.setText("￥200.00");
        tvPayMoney.setTextColor(getResources().getColor(R.color.gray_b4));
        tvPayState.setText("等待对方微信支付中");
    }

    @OnClick({R.id.btn_success, R.id.btn_failed})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_success:
                if (progress.getStatus() != MdStyleProgress.Status.LoadSuccess) {
                    progress.setStatus(MdStyleProgress.Status.LoadSuccess);
                    progress.startAnima();
                    tvPayMoney.setText("￥200.00");
                    tvPayMoney.setTextColor(getResources().getColor(R.color.orange_F57F3E));
                    tvPayState.setText("对方微信支付完成");
                }
                break;
            case R.id.btn_failed:
                if (progress.getStatus() != MdStyleProgress.Status.LoadFail) {
                    progress.setStatus(MdStyleProgress.Status.LoadFail);
                    progress.failAnima();
                }
                break;
        }
    }
}
