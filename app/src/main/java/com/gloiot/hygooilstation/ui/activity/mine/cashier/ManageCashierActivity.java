package com.gloiot.hygooilstation.ui.activity.mine.cashier;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收银员管理
 */
public class ManageCashierActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_manageCashier_listview;
    private LinearLayout ll_manageCashier_noCashier;
    private TextView tv_toptitle_right;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mCashierListAdapter;
    private String stationId;

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        requestHandleArrayList.add(requestAction.getCashierList(this, stationId));
    }

    @Override
    public int initResource() {
        return R.layout.activity_manage_cashier;
    }

    @Override
    public void initComponent() {
        lv_manageCashier_listview = (ListView) findViewById(R.id.lv_manageCashier_listview);
        ll_manageCashier_noCashier = (LinearLayout) findViewById(R.id.ll_manageCashier_noCashier);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "收银员管理", "添加收银员");
        tv_toptitle_right.setOnClickListener(this);
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_toptitle_right:
                startActivity(new Intent(this, AddCashierActivity.class));
                break;
            case R.id.ll_manageCashier_noCashier:
                startActivity(new Intent(this, AddCashierActivity.class));
                break;
        }
    }

    private void processData() {
        mCashierListAdapter = new CommonAdapter<String[]>(ManageCashierActivity.this, R.layout.item_cashier_list, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_cashier_name, strings[1]);
            }
        };
        lv_manageCashier_listview.setAdapter(mCashierListAdapter);
        lv_manageCashier_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageCashierActivity.this, DeleteCashierActivity.class);
                intent.putExtra("name", list.get(position)[1]);
                intent.putExtra("account", list.get(position)[0]);
                intent.putExtra("phoneNum", list.get(position)[2]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_CASHIERLIST:
//                Log.e("收银员列表数据", response.toString());
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[4];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("账号");
                        a[1] = jsonObject.getString("真实名");
                        a[2] = jsonObject.getString("手机号码");
                        a[3] = jsonObject.getString("查询密码");
                        list.add(a);
                    }
                    ll_manageCashier_noCashier.setVisibility(View.GONE);
                    processData();
                } else {
                    ll_manageCashier_noCashier.setVisibility(View.VISIBLE);
                    ll_manageCashier_noCashier.setOnClickListener(this);
                }
                break;
        }
    }

}
