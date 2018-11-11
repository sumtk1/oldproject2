package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyOilgunActivity extends BaseActivity {

    private List<String[]> list = new ArrayList<>();
    private GridView gd_myOilstype;

    @Override
    public int initResource() {
        return R.layout.activity_my_oilgun;
    }

    @Override
    public void initComponent() {
        gd_myOilstype = (GridView) findViewById(R.id.gd_myOilstype);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "我的油枪", "");

        requestHandleArrayList.add(requestAction.getStationOilstype(this));
    }

    private void processData() {

        gd_myOilstype.setAdapter(new MyGVAdapter(list));

        gd_myOilstype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MyOilgunActivity.this, MyEachoilsGunActivity.class);//使用listview侧滑
                Intent intent = new Intent(MyOilgunActivity.this, MyEachOilsgun1Activity.class);//使用RecyclerView侧滑
                intent.putExtra("oilsId", list.get(position)[0]);
                intent.putExtra("oilsType", list.get(position)[1]);
                startActivity(intent);
            }
        });


    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_STATIONOILSTYPE:

//                Log.e("我的油品型号", response.toString());

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[2];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("油品型号");
                        list.add(a);
                    }
                    processData();
                } else {

                }

                break;
        }
    }


    /**
     * gridview适配器
     */
    class MyGVAdapter extends BaseAdapter {

        private List<String[]> list = new ArrayList<>();

        public MyGVAdapter(List<String[]> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(MyOilgunActivity.this, R.layout.item_myoilstype, null);
            TextView tv_oilstype_image = (TextView) view.findViewById(R.id.tv_oilstype_image);
            TextView tv_oilstype_text = (TextView) view.findViewById(R.id.tv_oilstype_text);

            tv_oilstype_text.setText(list.get(position)[1]);
            tv_oilstype_image.setText(list.get(position)[1]);
            if (list.get(position)[1].equals("0#")) {
//                tv_oilstype_image.setBackgroundColor(getResources().getColor(R.color.orange_F29305));
                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_f29305);
            } else if (list.get(position)[1].equals("92#")) {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_6ed12b);

            } else if (list.get(position)[1].equals("93#")) {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_20dc91);

            } else if (list.get(position)[1].equals("95#")) {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_d824cf);

            } else if (list.get(position)[1].equals("97#")) {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_176bea);

            } else if (list.get(position)[1].equals("98#")) {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_992fdf);

            } else {

                tv_oilstype_image.setBackgroundResource(R.drawable.shape_circle_63_176bea);

            }
            return view;
        }
    }
}
