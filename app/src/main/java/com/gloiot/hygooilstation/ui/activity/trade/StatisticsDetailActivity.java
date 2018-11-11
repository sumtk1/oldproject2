package com.gloiot.hygooilstation.ui.activity.trade;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullableListView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gloiot.hygooilstation.R.id.tv_listview_no;

/**
 * 交易--统计--月份统计详情
 */
public class StatisticsDetailActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mListView;
    private TextView mTvNoData;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mStatisticslistAdapter;
    private String year, month;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_common_mingxi;
    }

    @Override
    public void initComponent() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefreshlayout);
        mListView = (PullableListView) findViewById(R.id.pullablelistview);
        mTvNoData = (TextView) findViewById(tv_listview_no);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");

        CommonUtlis.setTitleBar(this, true, change(month) + "月统计", "");

//        Calendar c = Calendar.getInstance();//获取日历对象
//        String year = c.get(Calendar.YEAR) + "";//传入当前的年份

        pullToRefreshLayout.setOnRefreshListener(this);
        request(0, 0, 1, 0);
    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.statisticsDetail(this, pullToRefreshLayout, requestType, page, requestTag, showLoad, year, month));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            mStatisticslistAdapter.notifyDataSetChanged();
        }
        list.clear();
        request(1, 0, 2, -1);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (page > 0) {
            request(2, page + 1, 3, -1);
        } else {
            MToast.showToast(mContext, "已无数据加载");
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    private void setAdapter() {
        mStatisticslistAdapter = new CommonAdapter<String[]>(StatisticsDetailActivity.this, R.layout.item_trade_todayearning, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {


//                holder.setText(R.id.tv_statisticsDetail_time, strings[1]);
//
//                try {
//                    if (strings[0].substring(0, 1).equals("-")) {
//
//                        String moneyString = strings[0];
//                        double moneyDouble = Double.parseDouble(moneyString);
//                        String moneyResult = String.format("%.2f", moneyDouble);
//                        holder.setText(R.id.tv_statisticsDetail_money, moneyResult);
//
//                        holder.setTextColorRes(R.id.tv_statisticsDetail_money, R.color.black_555);
//
//                    } else {
//                        String moneyString = strings[0];
//                        double moneyDouble = Double.parseDouble(moneyString);
//                        String moneyResult = String.format("%.2f", moneyDouble);
//                        holder.setText(R.id.tv_statisticsDetail_money, "+" + moneyResult);
//
//                        holder.setTextColorRes(R.id.tv_statisticsDetail_money, R.color.orange_FF690C);
//
//                    }
//
//                    if (strings[5].equals("提现")) {
//                        holder.setVisible(R.id.rl_youhui, false);
//                        holder.setVisible(R.id.rl_shishou, false);
//                    } else {
//                        holder.setVisible(R.id.rl_youhui, true);
//                        holder.setVisible(R.id.rl_shishou, true);
//                        holder.setText(R.id.tv_statisticsDetail_youhui, strings[3]);
//                        holder.setText(R.id.tv_statisticsDetail_shishou, "￥" + strings[4]);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                holder.setText(R.id.tv_oddNum, "交易单号：" + strings[1]);
                holder.setText(R.id.tv_time, strings[3]);
                holder.setText(R.id.tv_type, strings[7]);
//                if (strings[8].equals("已删除")) {
//                    holder.setVisible(R.id.tv_text_oilprice, false);
//                    holder.setVisible(R.id.tv_oilsPrice, false);
//                } else {
//                    holder.setVisible(R.id.tv_text_oilprice, true);
//                    holder.setVisible(R.id.tv_oilsPrice, true);
//                    holder.setText(R.id.tv_oilsPrice, strings[8] + "/L");
//                }

                holder.setText(R.id.tv_oilsPrice, strings[8] + "/L");

                holder.setText(R.id.tv_jiaoyi, strings[5]);
                holder.setText(R.id.tv_daozhang, strings[2]);
            }
        };
        mListView.setAdapter(mStatisticslistAdapter);

        //设置分隔线
        mListView.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
        mListView.setDividerHeight(16);

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case 1:
                processResponseData(response, false);
                break;
            case 2:
                processResponseData(response, false);
                break;
            case 3:
                processResponseData(response, true);
                break;
        }
    }


    /**
     * 处理请求返回数据
     *
     * @param response
     * @param isLoadMore
     */
    private void processResponseData(JSONObject response, boolean isLoadMore) throws JSONException {
        L.e("按月份统计明细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[9];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("id");
                a[1] = jsonObject.getString("交易单号");
                a[2] = jsonObject.getString("到账金额");
                a[3] = jsonObject.getString("录入时间");
                a[4] = jsonObject.getString("状态");
                a[5] = jsonObject.getString("交易金额");
                a[6] = jsonObject.getString("类别");
                a[7] = jsonObject.getString("油品型号");
                a[8] = jsonObject.getString("市场价");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);

            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                mStatisticslistAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }

        } else {

            if (isLoadMore) {
                MToast.showToast(mContext, "已无数据加载");
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            } else {
                mListView.setVisibility(View.GONE);
                mTvNoData.setVisibility(View.VISIBLE);
                mTvNoData.setText("无数据");
            }
        }
    }

    public String change(String s) {
        switch (s) {
            case "1":
                return "一";
            case "2":
                return "二";
            case "3":
                return "三";
            case "4":
                return "四";
            case "5":
                return "五";
            case "6":
                return "六";
            case "7":
                return "七";
            case "8":
                return "八";
            case "9":
                return "九";
            case "10":
                return "十";
            case "11":
                return "十一";
            case "12":
                return "十二";
            default:
                return s;
        }
    }

}
