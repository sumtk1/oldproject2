package com.gloiot.hygooilstation.ui.activity.message;

import android.content.Intent;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 交易消息--明细
 */
public class TradingDetailActivity extends BaseActivity {

    private TextView tv_detail_income, tv_detail_type, tv_detail_time, tv_detail_oddNum, tv_detail_explain;

    @Override
    public int initResource() {
        return R.layout.activity_trading_detail;
    }

    @Override
    public void initComponent() {
        tv_detail_income = (TextView) findViewById(R.id.tv_detail_income);
        tv_detail_type = (TextView) findViewById(R.id.tv_detail_type);
        tv_detail_time = (TextView) findViewById(R.id.tv_detail_time);
        tv_detail_oddNum = (TextView) findViewById(R.id.tv_detail_oddNum);
        tv_detail_explain = (TextView) findViewById(R.id.tv_detail_explain);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "交易明细", "");

        Intent intent = getIntent();
        String goodsOrderNum = intent.getStringExtra("goodsOrderNum");
        requestHandleArrayList.add(requestAction.getTradingDetail(this, goodsOrderNum));

//        Log.e("交易单号-",goodsOrderNum+"--");
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_TRADINGDETAIL:
//                Log.e("交易详情--", response.toString() + "--");
//                tv_detail_income.setText(response.getString("金额"));

                if (response.getString("金额").substring(0, 1).equals("-")) {
                    tv_detail_income.setText(response.getString("金额"));
                    tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.blue_478AF5));
                } else {
                    tv_detail_income.setText("+" + response.getString("金额"));
                    tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.orange_FF690C));
                }

                tv_detail_type.setText(response.getString("类型"));
                tv_detail_time.setText(response.getString("时间"));
                tv_detail_oddNum.setText(response.getString("交易单号"));
                tv_detail_explain.setText(response.getString("备注"));
                break;
        }
    }
}
