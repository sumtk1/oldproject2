package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.app.Activity;
import android.content.Intent;
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

public class OilstationCityActivity extends BaseActivity {

    private ListView lv_listview;
    private TextView tv_listview_no;
    private CommonAdapter mOilstationAreaAdapter;
    private List<String[]> areaList = new ArrayList<>();
    public static Activity stationCityActivity;
    private String mProvince;

    @Override
    public int initResource() {
        return R.layout.activity_listview_mgtop20;
    }

    @Override
    public void initComponent() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        tv_listview_no = (TextView) findViewById(R.id.tv_listview_no);
    }

    @Override
    public void initData() {
        stationCityActivity = this;
        Intent intent = getIntent();
        String provinceName = intent.getStringExtra("省份");
        mProvince = intent.getStringExtra("省");
        CommonUtlis.setTitleBar(this, true, "油站区域", "");
        requestHandleArrayList.add(requestAction.getShi(this, provinceName));
    }

    private void processData() {
        mOilstationAreaAdapter = new CommonAdapter<String[]>(OilstationCityActivity.this, R.layout.item_oilstation_area, areaList) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_oilstation_area, strings[0]);
            }
        };
        lv_listview.setAdapter(mOilstationAreaAdapter);
        lv_listview.setDividerHeight(0);
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OilstationCityActivity.this, OilstationDistrictActivity.class);
                intent.putExtra("市名", areaList.get(position)[0]);
                intent.putExtra("市", areaList.get(position)[0]);
                intent.putExtra("省", mProvince);
                startActivity(intent);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETSHI:

//                Log.e("shi",response.toString());

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("市列表");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        String a[] = new String[1];
                        a[0] = jsonArray.get(i) + "";
                        areaList.add(a);

//                        String[] a = new String[2];
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        a[0] = jsonObject.getString("id");
//                        a[1] = jsonObject.getString("名称");
//                        areaList.add(a);
                    }
                    processData();
                } else {
                    tv_listview_no.setText("无数据");
                }
                break;
        }
    }
}
