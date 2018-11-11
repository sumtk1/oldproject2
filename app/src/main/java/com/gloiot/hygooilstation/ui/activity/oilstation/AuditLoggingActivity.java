package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.text.TextUtils;
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
 * 审核记录
 */
public class AuditLoggingActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mListView;
    private TextView mTvNoData;
    private String stationId;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mAuditloggingAdapter;
    private int page = 0;

    @Override
    public int initResource() {
        return R.layout.layout_common_mingxi_mt0;
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
        stationId = intent.getStringExtra("stationId");
        CommonUtlis.setTitleBar(this, true, "审核记录", "");
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
        requestHandleArrayList.add(requestAction.auditlogging(this, pullToRefreshLayout, requestType, page, requestTag, showLoad, stationId));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            mAuditloggingAdapter.notifyDataSetChanged();
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

        mAuditloggingAdapter = new CommonAdapter<String[]>(AuditLoggingActivity.this, R.layout.item_oils_auditlogging, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_auditlogging_time, strings[8]);
                if (strings[1].equals("审核通过")) {
                    holder.setBackgroundRes(R.id.tv_auditlogging_state, R.color.blue_478AF5);//不要用setBackgroundColor,会出错
                    holder.setText(R.id.tv_auditlogging_state, "已通过");
                    holder.setTextColorRes(R.id.tv_auditlogging_type, R.color.blue_478AF5);
                } else if (strings[1].equals("审核未通过")) {
                    holder.setBackgroundRes(R.id.tv_auditlogging_state, R.color.orange_FF690C);
                    holder.setText(R.id.tv_auditlogging_state, "未通过");
                    holder.setTextColorRes(R.id.tv_auditlogging_type, R.color.orange_FF690C);
                } else if (strings[1].equals("未审核")) {
                    holder.setBackgroundRes(R.id.tv_auditlogging_state, R.color.orange_FEA523);
                    holder.setText(R.id.tv_auditlogging_state, "审核中");
                    holder.setTextColorRes(R.id.tv_auditlogging_type, R.color.orange_FEA523);
                }
                holder.setText(R.id.tv_auditlogging_type, strings[4]);
                holder.setText(R.id.tv_auditlogging_market, "￥" + strings[5] + "/L");
                holder.setText(R.id.tv_auditlogging_rangli, "￥" + strings[6] + "/L");

                try {
                    if (!TextUtils.isEmpty(strings[7])) {
                        float youhui = Float.parseFloat(strings[7]);//这里可能会报错，若是ratio为""
                        float ratio = 100 - youhui;
                        holder.setText(R.id.tv_auditlogging_ratio, ratio + "%");
                    } else {
                        holder.setText(R.id.tv_auditlogging_ratio, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        mListView.setAdapter(mAuditloggingAdapter);
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
        L.e("油品审核记录", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[9];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("id");
                a[1] = jsonObject.getString("状态");
                a[2] = jsonObject.getString("油站id");
                a[3] = jsonObject.getString("油站名称");
                a[4] = jsonObject.getString("油品型号");
                a[5] = jsonObject.getString("市场价");
                a[6] = jsonObject.getString("优惠金额");
                a[7] = jsonObject.getString("优惠比例");
                a[8] = jsonObject.getString("录入时间");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            mTvNoData.setVisibility(View.GONE);

            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                mAuditloggingAdapter.notifyDataSetChanged();
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
