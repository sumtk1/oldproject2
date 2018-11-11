package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullableListView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gloiot.hygooilstation.R.id.tv_listview_no;

/**
 * @author dlt
 *         钱包--明细列表
 */
public class WalletListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mListView;
    private TextView mTvNoData;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mWalletlistAdapter;
    private int page = 0;
    private String stationId;

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
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        CommonUtlis.setTitleBar(this, true, "明细", "");
        pullToRefreshLayout.setOnRefreshListener(this);
        request(0, 0, 1, 0);
//        requestHandleArrayList.add(requestAction.getWalletDetail(this, stationId));
    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.getWalletDetail(this, pullToRefreshLayout, requestType, page, requestTag, showLoad, stationId));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            mWalletlistAdapter.notifyDataSetChanged();
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
//        mWalletlistAdapter = new CommonAdapter<String[]>(WalletListActivity.this, R.layout.item_wallet_detail, list) {
        mWalletlistAdapter = new CommonAdapter<String[]>(WalletListActivity.this, R.layout.item_wallet_detail_new, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
//                holder.setText(R.id.tv_walletlist_name, strings[2]);
//                holder.setText(R.id.tv_walletlist_time, strings[1]);
//                if (strings[0].substring(0, 1).equals("-")) {
////                    holder.setText(R.id.tv_walletlist_money, strings[0]);
//
//                    String moneyString = strings[0];
//                    double moneyDouble = Double.parseDouble(moneyString);
//                    String moneyResult = String.format("%.2f", moneyDouble);
//                    holder.setText(R.id.tv_walletlist_money, moneyResult);
//
//                    holder.setTextColorRes(R.id.tv_walletlist_money, R.color.black_555);
//                } else {
//                    String moneyString = strings[0];
//                    double moneyDouble = Double.parseDouble(moneyString);
//                    String moneyResult = String.format("%.2f", moneyDouble);
//                    holder.setText(R.id.tv_walletlist_money, "+" + moneyResult);
//
//                    holder.setTextColorRes(R.id.tv_walletlist_money, R.color.orange_FF690C);
//                }
//
//                if (strings[2].equals("提现")) {
//                    holder.setText(R.id.tv_walletlist_state, "(" + strings[3] + ")");
//                } else {
//                    holder.setText(R.id.tv_walletlist_state, "");
//                }


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
//                    if (strings[5].equals("提现")) {//判断类别
//                        holder.setVisible(R.id.rl_youhui, false);
//                        holder.setVisible(R.id.rl_shishou, false);
//                    } else if (strings[5].equals("加油")) {
//                        holder.setVisible(R.id.rl_youhui, true);
//                        holder.setVisible(R.id.rl_shishou, true);
//                        holder.setText(R.id.tv_statisticsDetail_youhui, strings[3]);
//                        holder.setText(R.id.tv_statisticsDetail_shishou, "￥" + strings[4]);
//                    } else if (strings[5].equals("撤单")) {
//                        holder.setVisible(R.id.rl_youhui, true);
//                        holder.setVisible(R.id.rl_shishou, true);
//                        holder.setText(R.id.tv_statisticsDetail_youhui, strings[3]);
//                        holder.setText(R.id.tv_statisticsDetail_shishou, "￥" + strings[4]);
//                    } else if (strings[5].equals("活动加油")) {
//                        holder.setVisible(R.id.rl_youhui, true);
//                        holder.setVisible(R.id.rl_shishou, true);
//                        holder.setText(R.id.tv_statisticsDetail_youhui, strings[3]);
//                        holder.setText(R.id.tv_statisticsDetail_shishou, "￥" + strings[4]);
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

                holder.setText(R.id.tv_time, strings[1]);
                try {
                    if (strings[0].substring(0, 1).equals("-")) {
                        if (strings[2].equals("处理中")) {
                            holder.setTextColorRes(R.id.tv_money, R.color.gray_b4b4b4);
                        } else if (strings[2].equals("处理失败")) {
                            holder.setTextColorRes(R.id.tv_money, R.color.gray_b4b4b4);
                        } else {
                            holder.setTextColorRes(R.id.tv_money, R.color.blue_478AF5);
                        }
                        holder.setText(R.id.tv_money, strings[0]);
                    } else {
                        holder.setTextColorRes(R.id.tv_money, R.color.orange_FF690C);
                        holder.setText(R.id.tv_money, "+" + strings[0]);
                    }

                    if (strings[5].equals("提现")) {
                        holder.setText(R.id.tv_type, "提现到银行卡");
                    } else if (strings[5].equals("提现到支付宝")) {
                        holder.setText(R.id.tv_type, "提现到支付宝");
                    } else if (strings[5].equals("加油") || strings[5].equals("活动加油")) {
                        holder.setText(R.id.tv_type, "用户加油");
                    } else if (strings[5].equals("撤单")) {
                        holder.setText(R.id.tv_type, "用户撤单");
                    } else {
                        holder.setText(R.id.tv_type, strings[5]);
                    }

                    if (strings[2].equals("处理中")) {
                        holder.setText(R.id.tv_state, "处理中");
                        holder.setTextColor(R.id.tv_state, Color.parseColor("#999999"));
                        holder.setBackgroundRes(R.id.tv_state, R.drawable.bg_shape_biankuang_gray_999_10dp);
                    } else if (strings[2].equals("处理失败")) {
                        holder.setText(R.id.tv_state, "提取失败");
                        holder.setTextColor(R.id.tv_state, Color.parseColor("#ff7676"));
                        holder.setBackgroundRes(R.id.tv_state, R.drawable.bg_shape_biankuang_red_ff7676_10dp);
                    } else {
                        holder.setText(R.id.tv_state, "");
                        holder.setTextColor(R.id.tv_state, Color.parseColor("#ffffff"));
                        holder.setBackgroundRes(R.id.tv_state, R.drawable.bg_btn_white);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        mListView.setAdapter(mWalletlistAdapter);
        mListView.setDivider(new ColorDrawable(Color.parseColor("#f3f3f3")));
        mListView.setDividerHeight(2);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WalletListActivity.this, WalletDetailActivity.class);
                intent.putExtra("id", list.get(position)[6]);
                startActivity(intent);
            }
        });

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case RequestAction.TAG_WALLETLIST:
//                int num = Integer.parseInt(response.getString("条数"));
//                if (num != 0) {
//                    JSONArray jsonArray = response.getJSONArray("列表");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String[] a = new String[4];
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        a[0] = jsonObject.getString("金额");
//                        a[1] = jsonObject.getString("时间");
//                        a[2] = jsonObject.getString("姓名");
//                        a[3] = jsonObject.getString("交易单号");
//                        list.add(a);
//                    }
//                    processData();
//                } else {
//                    tv_listview_no.setText("无数据");
//                }
//                break;

//            case RequestAction.TAG_WALLETDETAIL:
//
//                Log.e("钱包明细", response.toString());
//
//                int num = Integer.parseInt(response.getString("条数"));
//                if (num != 0) {
//                    JSONArray jsonArray = response.getJSONArray("列表");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String[] a = new String[7];
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        a[0] = jsonObject.getString("油站金额");
//                        a[1] = jsonObject.getString("录入时间");
//                        a[2] = jsonObject.getString("状态");
//                        a[3] = jsonObject.getString("优惠金额");
//                        a[4] = jsonObject.getString("实收金额");
//                        a[5] = jsonObject.getString("类别");
//                        a[6] = jsonObject.getString("id");
//
//                        list.add(a);
//                    }
//                    processData();
//                } else {
//                    tv_listview_no.setText("无数据");
//                }
//                break;

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
        L.e("钱包列表明细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[7];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("油站金额");
                a[1] = jsonObject.getString("录入时间");
                a[2] = jsonObject.getString("状态");
                a[3] = jsonObject.getString("优惠金额");
                a[4] = jsonObject.getString("实收金额");
                a[5] = jsonObject.getString("类别");
                a[6] = jsonObject.getString("id");

                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);

            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                mWalletlistAdapter.notifyDataSetChanged();
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


}
