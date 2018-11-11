package com.gloiot.hygooilstation.ui.activity.trade;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.NoDoubleClickUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SettlementOverallActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_settlementoverall_jiaoyi, tv_settlementoverall_daozhang, tv_settlementoverall_personName,
            tv_settlementoverall_totalNums, tv_settlementoverall_lastTime, tv_settlementoverall_thisTime,
            tv_settlementoverall_comfirm, tv_settlementoverall_cancle;
    private String lastSettlementTime, thisTime, jiaoyi, daozhang, personName, totalNum;

    @Override

    public int initResource() {
        return R.layout.activity_settlement_overall;
    }

    @Override
    public void initComponent() {
        tv_settlementoverall_jiaoyi = (TextView) findViewById(R.id.tv_settlementoverall_jiaoyi);
        tv_settlementoverall_daozhang = (TextView) findViewById(R.id.tv_settlementoverall_daozhang);
        tv_settlementoverall_personName = (TextView) findViewById(R.id.tv_settlementoverall_personName);
        tv_settlementoverall_totalNums = (TextView) findViewById(R.id.tv_settlementoverall_totalNums);
        tv_settlementoverall_lastTime = (TextView) findViewById(R.id.tv_settlementoverall_lastTime);
        tv_settlementoverall_thisTime = (TextView) findViewById(R.id.tv_settlementoverall_thisTime);
        tv_settlementoverall_comfirm = (TextView) findViewById(R.id.tv_settlementoverall_comfirm);
        tv_settlementoverall_cancle = (TextView) findViewById(R.id.tv_settlementoverall_cancle);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "结算", "");

        Intent intent = getIntent();
        lastSettlementTime = intent.getStringExtra("上次结算时间");
        requestHandleArrayList.add(requestAction.settlementConfirm(SettlementOverallActivity.this, lastSettlementTime));
        tv_settlementoverall_comfirm.setOnClickListener(this);
        tv_settlementoverall_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
        switch (v.getId()) {
            case R.id.tv_settlementoverall_comfirm:
                requestHandleArrayList.add(requestAction.gotoSettle(this, lastSettlementTime, thisTime, jiaoyi, daozhang, personName, totalNum));
                break;
            case R.id.tv_settlementoverall_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SETTLEMENTCONFIRM:

//                Log.e("确认结算", response.toString());

                thisTime = response.getString("本次结算时间");
                jiaoyi = response.getString("实收金额");
                daozhang = response.getString("到账金额");
                personName = response.getString("结算人");
                totalNum = response.getString("累计条数");

                tv_settlementoverall_jiaoyi.setText("￥" + jiaoyi);
                tv_settlementoverall_daozhang.setText("￥" + daozhang);
                tv_settlementoverall_personName.setText(personName);
                tv_settlementoverall_thisTime.setText(thisTime);
                tv_settlementoverall_lastTime.setText(response.getString("上次结算时间"));
                tv_settlementoverall_totalNums.setText(totalNum + "笔");
                break;
            case RequestAction.TAG_GOTOSETTLE:
                MToast.showToast(mContext, "结算成功");
                finish();
                break;
            default:
                break;
        }
    }
}
