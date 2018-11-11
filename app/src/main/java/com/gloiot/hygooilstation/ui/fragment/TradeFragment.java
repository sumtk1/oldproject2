package com.gloiot.hygooilstation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.trade.GatheringActivity;
import com.gloiot.hygooilstation.ui.activity.trade.SettlementActivity;
import com.gloiot.hygooilstation.ui.activity.trade.StatisticsActivity;
import com.gloiot.hygooilstation.ui.activity.trade.Todayearning1Activity;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 交易
 * Created by dlt on 2016/9/14.
 */

public class TradeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = TradeFragment.class.getSimpleName();

    private AutoRelativeLayout rl_trade_todayIncome, rl_trade_statistics, rl_trade_gathering, rl_trade_settlement;

    private String accountType;

    public static Fragment newInstance(int position) {
        TradeFragment fragment = new TradeFragment();

        return fragment;
    }

    public TradeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade, container, false);
        initComponent(view);
        initData();
        return view;
    }

    private void initComponent(View view) {
        rl_trade_todayIncome = (AutoRelativeLayout) view.findViewById(R.id.rl_trade_todayIncome);
        rl_trade_statistics = (AutoRelativeLayout) view.findViewById(R.id.rl_trade_statistics);
        rl_trade_gathering = (AutoRelativeLayout) view.findViewById(R.id.rl_trade_gathering);
        rl_trade_settlement = (AutoRelativeLayout) view.findViewById(R.id.rl_trade_settlement);
    }

    private void initData() {
        accountType = SharedPreferencesUtils.getString(getActivity(), ConstantUtlis.SP_ACCOUNTTYPE, "");
        if (accountType.equals("收银员")) {
            rl_trade_todayIncome.setOnClickListener(this);
            rl_trade_gathering.setOnClickListener(this);
            rl_trade_settlement.setOnClickListener(this);
            rl_trade_statistics.setOnClickListener(this);
        } else if (accountType.equals("站长")) {
            rl_trade_todayIncome.setOnClickListener(this);
            rl_trade_statistics.setOnClickListener(this);
            rl_trade_gathering.setOnClickListener(this);
            rl_trade_settlement.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_trade_todayIncome://今日收益
//                startActivity(new Intent(getActivity(), TodayearningActivity.class));//用listview做时会出现数据错乱问题
                startActivity(new Intent(getActivity(), Todayearning1Activity.class));//改用RecyclerView做
                break;
            case R.id.rl_trade_statistics://统计
                if (accountType.equals("收银员")) {
                    MToast.showToast(getActivity(), "您无此权限");
                } else if (accountType.equals("站长")) {
                    startActivity(new Intent(getActivity(), StatisticsActivity.class));
                }
                break;
            case R.id.rl_trade_gathering://收款(二维码)
                startActivity(new Intent(getActivity(), GatheringActivity.class));
//                startActivity(new Intent(getActivity(), SelectGatheringStyleActivity.class));
                break;
            case R.id.rl_trade_settlement://结算
                startActivity(new Intent(getActivity(), SettlementActivity.class));
                break;
            default:
                break;
        }

    }
}
