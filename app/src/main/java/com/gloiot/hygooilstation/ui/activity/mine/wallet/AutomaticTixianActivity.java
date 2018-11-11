package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutomaticTixianActivity extends BaseActivity {

    private ListView lv_listview;
    private TextView tv_listview_no;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mTixianTypeAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_listview_mgtop30;
    }

    @Override
    public void initComponent() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        tv_listview_no = (TextView) findViewById(R.id.tv_listview_no);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "提现", "");
        requestHandleArrayList.add(requestAction.getTixianType(this));
    }

    private void processData() {
        mTixianTypeAdapter = new CommonAdapter<String[]>(AutomaticTixianActivity.this, R.layout.item_tixian_automatic, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_tixianautomatic_type, strings[0]);
                holder.setText(R.id.tv_tixianautomatic_explain, strings[1]);
                if (strings[0].equals("日结")) {
                    holder.setBackgroundRes(R.id.rl_tixianautomatic, R.color.orange_FF690C);
                } else if (strings[0].equals("周结")) {
                    holder.setBackgroundRes(R.id.rl_tixianautomatic, R.color.orange_FEA523);
                } else if (strings[0].equals("月结")) {
                    holder.setBackgroundRes(R.id.rl_tixianautomatic, R.color.blue_478AF5);
                } else if (strings[0].equals("笔结")) {
                    holder.setBackgroundRes(R.id.rl_tixianautomatic, R.color.green_01C971);
                }

            }
        };
        lv_listview.setAdapter(mTixianTypeAdapter);

        //设置分隔线
        lv_listview.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
        lv_listview.setDividerHeight(30);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AutomaticTixianActivity.this, TixianActivity.class);
                intent.putExtra("typeflag", "automatic");
                startActivity(intent);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETTIXIANEXPLAIN:
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[2];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("标题");
                        a[1] = jsonObject.getString("内容");
                        list.add(a);
                    }
                    processData();
                } else {
                    tv_listview_no.setText("无数据");
                }
                break;
            default:
                break;
        }
    }
}
