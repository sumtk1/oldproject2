package com.gloiot.hygooilstation.ui.activity.trade.aboutpay;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置支付金额
 * Created by Dlt on 2017/5/25 11:54
 */
public class SetPayMoneyActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_station_name)
    TextView tvStationName;
    @Bind(R.id.et_oilgun_num)
    EditText etOilgunNum;
    @Bind(R.id.tv_oil_type)
    TextView tvOilType;
    @Bind(R.id.tv_oil_unitprice)
    TextView tvOilUnitprice;
    @Bind(R.id.et_input_money)
    EditText etInputMoney;
    @Bind(R.id.et_remark)
    EditText etRemark;
    @Bind(R.id.tv_next)
    TextView mTvNext;


    @Override
    public int initResource() {
        return R.layout.activity_set_pay_money;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "设置金额", "");

    }

    @OnClick(R.id.tv_next)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                startActivity(new Intent(SetPayMoneyActivity.this, SelectScanTypeActivity.class));
                break;
        }
    }

}
