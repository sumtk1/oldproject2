package com.gloiot.hygooilstation.ui.activity.trade;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.ListViewForScrollView;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullableScrollView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
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
 * 结算
 */
public class SettlementActivity extends BaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableScrollView pullablescrollview;
    private ListViewForScrollView mListView;
    private TextView mTvNoData;
    private TextView tv_settlement_gosettle, tv_settlement_lasttime;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mSettlementListAdapter;
    private String lastSettlementTime;
    private int page = 0;

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        request(0, 0, 1, 0);
    }

    @Override
    public int initResource() {
        return R.layout.activity_settlement;
    }

    @Override
    public void initComponent() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefreshlayout);
        pullablescrollview = (PullableScrollView) findViewById(R.id.pullablescrollview);
        mListView = (ListViewForScrollView) findViewById(R.id.lv_listview);
        tv_settlement_gosettle = (TextView) findViewById(R.id.tv_settlement_gosettle);
        tv_settlement_lasttime = (TextView) findViewById(R.id.tv_settlement_lasttime);
        mTvNoData = (TextView) findViewById(tv_listview_no);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "结算", "");
        pullToRefreshLayout.setOnRefreshListener(this);
        tv_settlement_gosettle.setOnClickListener(this);
        pullablescrollview.smoothScrollTo(0, 0);//默认listview会滑倒顶部位置，这里设置为ScrollView的开始位置
    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.getSettlementList(this, pullToRefreshLayout, requestType, page, requestTag, showLoad));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            mSettlementListAdapter.notifyDataSetChanged();
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
        mSettlementListAdapter = new CommonAdapter<String[]>(SettlementActivity.this, R.layout.item_settlement, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_settlement_name, strings[0]);
                holder.setText(R.id.tv_settlement_oddNum, strings[3]);
                holder.setText(R.id.tv_settlement_starttime, strings[4]);
                holder.setText(R.id.tv_settlement_endtime, strings[5]);
                holder.setText(R.id.tv_jiaoyi, strings[1]);
                holder.setText(R.id.tv_daozhang, strings[2]);
            }
        };
        mListView.setAdapter(mSettlementListAdapter);

//        lv_settlement_listview.setSelection(list.size() - 1);//默认滚动到最后一个item

//        CommonUtlis.setListViewHeightBasedOnChildren(mListView);

        //设置分隔线
        mListView.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
        mListView.setDividerHeight(16);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SettlementActivity.this, SettlementDetailActivity.class);
                intent.putExtra("name", list.get(position)[0]);
                intent.putExtra("oddNum", list.get(position)[3]);
                intent.putExtra("start", list.get(position)[4]);
                intent.putExtra("end", list.get(position)[5]);
                intent.putExtra("jiaoyi", list.get(position)[1]);
                intent.putExtra("daozhang", list.get(position)[2]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case RequestAction.TAG_SETTLEMENTLIST:
////                Log.e("结算列表", response.toString());
//                lastSettlementTime = response.getString("上次结算时间");
//                tv_settlement_lasttime.setText(lastSettlementTime);
//                int num = Integer.parseInt(response.getString("条数"));
//                if (num != 0) {
//                    JSONArray jsonArray = response.getJSONArray("列表");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String[] a = new String[6];
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        a[0] = jsonObject.getString("真实名");
//                        a[1] = jsonObject.getString("交易金额");
//                        a[2] = jsonObject.getString("到账金额");
//                        a[3] = jsonObject.getString("结算单号");
//                        a[4] = jsonObject.getString("开始时间");
//                        a[5] = jsonObject.getString("结束时间");
//                        list.add(a);
//                    }
//                    tv_listview_no.setText("");//防止刚进来无数据，点结算之后无数据三个字不消失。
//                    processData();
//                } else {
//                    tv_listview_no.setText("无数据");
//                }
//                break;
//            case RequestAction.TAG_GOTOSETTLE:
//                list.clear();
//                requestHandleArrayList.add(requestAction.getSettlementList(this));
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


            default:
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
        Log.e("结算单列表", response.toString());
        lastSettlementTime = response.getString("上次结算时间");
        tv_settlement_lasttime.setText(lastSettlementTime);
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[6];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("真实名");
                a[1] = jsonObject.getString("交易金额");
                a[2] = jsonObject.getString("到账金额");
                a[3] = jsonObject.getString("结算单号");
                a[4] = jsonObject.getString("开始时间");
                a[5] = jsonObject.getString("结束时间");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);
            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                mSettlementListAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_settlement_gosettle:
//                requestHandleArrayList.add(requestAction.gotoSettle(this, lastSettlementTime));

                Intent intent = new Intent(SettlementActivity.this, SettlementOverallActivity.class);
                intent.putExtra("上次结算时间", lastSettlementTime);
                startActivity(intent);

                break;
            default:
                break;
        }

    }
}
