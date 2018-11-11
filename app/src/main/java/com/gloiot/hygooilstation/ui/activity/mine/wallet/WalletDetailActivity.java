package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的--钱包--钱包列表--明细
 */
public class WalletDetailActivity extends BaseActivity {

    private TextView tv_money_flag, tv_detail_income, tv_detail_type, tv_detail_state, tv_detail_time,
            tv_detail_jiaoyi_money, tv_detail_surplus, tv_detail_oddNum, tv_detail_explain;
    private RelativeLayout rl_detail_jiaoyi_money, rl_detail_oddNum;

    @Override
    public int initResource() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public void initComponent() {
        rl_detail_jiaoyi_money = (RelativeLayout) findViewById(R.id.rl_detail_jiaoyi_money);
        rl_detail_oddNum = (RelativeLayout) findViewById(R.id.rl_detail_oddNum);
        tv_money_flag = (TextView) findViewById(R.id.tv_money_flag);
        tv_detail_income = (TextView) findViewById(R.id.tv_detail_income);
        tv_detail_type = (TextView) findViewById(R.id.tv_detail_type);
        tv_detail_state = (TextView) findViewById(R.id.tv_detail_state);
        tv_detail_time = (TextView) findViewById(R.id.tv_detail_time);
        tv_detail_jiaoyi_money = (TextView) findViewById(R.id.tv_detail_jiaoyi_money);
        tv_detail_surplus = (TextView) findViewById(R.id.tv_detail_surplus);
        tv_detail_oddNum = (TextView) findViewById(R.id.tv_detail_oddNum);
        tv_detail_explain = (TextView) findViewById(R.id.tv_detail_explain);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        requestHandleArrayList.add(requestAction.getWalletListDetail(this, id));
        CommonUtlis.setTitleBar(this, true, "明细", "");
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case RequestAction.TAG_WALLETITEMDETAIL:
//                if (response.getString("金额").substring(0, 1).equals("-")) {
//                    tv_detail_income.setText(response.getString("金额"));
//                    tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.blue_478AF5));
//                    rl_detail_oddNum.setVisibility(View.GONE);
//                } else {
//                    tv_detail_income.setText("+" + response.getString("金额"));
//                    tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.orange_FF690C));
//                    rl_detail_oddNum.setVisibility(View.VISIBLE);
//                    tv_detail_oddNum.setText(response.getString("交易单号"));
//                }
//                tv_detail_time.setText(response.getString("时间"));
//                tv_detail_surplus.setText(response.getString("剩余金额"));
//                tv_detail_type.setText(response.getString("类型"));
//                tv_detail_explain.setText(response.getString("说明"));
//                break;
            case RequestAction.TAG_WALLETLISTMINGXI:
                L.e("明细详情--", response.toString() + "--");

//                try {
//                    if (response.getString("金额").substring(0, 1).equals("-")) {
//                        tv_detail_income.setText(response.getString("金额"));
//                        tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.blue_478AF5));
//                        rl_detail_oddNum.setVisibility(View.GONE);
//                    } else {
//                        tv_detail_income.setText("+" + response.getString("金额"));
//                        tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.orange_FF690C));
//                        rl_detail_oddNum.setVisibility(View.VISIBLE);
//                        tv_detail_oddNum.setText(response.getString("交易单号"));
//                    }
//                    tv_detail_time.setText(response.getString("时间"));
//
//                    String moneyString = response.getString("剩余金额");
//                    double moneyDouble = Double.parseDouble(moneyString);
//                    String moneyResult = String.format("%.2f", moneyDouble);
//                    tv_detail_surplus.setText(moneyResult);
//
//                    tv_detail_type.setText(response.getString("类型"));
//                    tv_detail_explain.setText(response.getString("说明"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                String money = response.getString("金额");
                String state = response.getString("订单状态");
                String type = response.getString("类型");

                try {
                    if (money.substring(0, 1).equals("-")) {
                        tv_money_flag.setText("出账金额");
                        tv_detail_income.setText(money);
                        if (state.equals("成功")) {
                            tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.blue_478AF5));
                        } else {
                            tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.gray_b4b4b4));
                        }
                    } else {
                        tv_money_flag.setText("入账金额");
                        tv_detail_income.setText("+" + money);
                        tv_detail_income.setTextColor(mContext.getResources().getColor(R.color.orange_FF690C));
                    }
                    if (type.equals("加油")) {
                        rl_detail_jiaoyi_money.setVisibility(View.VISIBLE);
                        tv_detail_jiaoyi_money.setText(response.getString("交易金额"));
                    } else {
                        rl_detail_jiaoyi_money.setVisibility(View.GONE);
                    }
                    tv_detail_state.setText(state);
                    tv_detail_time.setText(response.getString("时间"));
                    tv_detail_oddNum.setText(response.getString("交易单号"));
                    tv_detail_explain.setText(response.getString("说明"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
