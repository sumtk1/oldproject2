package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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

public class OilstationDistrictActivity extends BaseActivity {

    private ListView lv_listview;
    private TextView tv_listview_no;
    private CommonAdapter mOilstationAreaAdapter;
    private List<String[]> areaList = new ArrayList<>();
    private String mProvince, mCity;

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
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("市名");
        mProvince = intent.getStringExtra("省");
        mCity = intent.getStringExtra("市");
        CommonUtlis.setTitleBar(this, true, "油站区域", "");
        requestHandleArrayList.add(requestAction.getQu(this, mProvince,cityName));
    }

    private void processData() {
        mOilstationAreaAdapter = new CommonAdapter<String[]>(OilstationDistrictActivity.this, R.layout.item_oilstation_area, areaList) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setVisible(R.id.iv_right, false);
                holder.setText(R.id.tv_oilstation_area, strings[0]);
            }
        };
        lv_listview.setAdapter(mOilstationAreaAdapter);
        lv_listview.setDividerHeight(0);
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONQU, areaList.get(position)[0]);
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHENG, mProvince);
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHI, mCity);//必须在最后一次整体存储，否则单独存储时在选择区之前返回会出现省市区的不匹配。
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, true);
                OilstationProvinceActivity.stationProvinceActivity.finish();
                OilstationCityActivity.stationCityActivity.finish();
                finish();
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETQU:

//                Log.e("qu",response.toString());

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("区列表");
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

//    @Override
//    public void onSuccess(int requestTag, JSONObject response) {
//        super.onSuccess(requestTag, response);
//        try {
//            if (response.getString("状态").equals("市id有误")) {
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONQU, mCity);//将区存为第二级数据
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHENG, mProvince);
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHI, mCity);//必须在最后一次整体存储，否则单独存储时在选择区之前返回会出现省市区的不匹配。
//                SharedPreferencesUtlis.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, true);
//                OilstationProvinceActivity.stationProvinceActivity.finish();
//                OilstationCityActivity.stationCityActivity.finish();
//                finish();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
