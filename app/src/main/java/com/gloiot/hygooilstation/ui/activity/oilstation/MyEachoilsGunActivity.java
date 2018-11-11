package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.swipe.MySwipe;
import com.gloiot.hygooilstation.ui.widget.swipe.SwipeLayout;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyEachoilsGunActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_oilsgun_top, rl_oilsgun_addOilgun;
    private TextView tv_oilsgun_oilstype, tv_nodata;
    private ListView lv_oilsgun_listview;
    private List<String[]> list = new ArrayList<>();
//    private CommonAdapter mOilgunsAdapter;
    private String stationId, oilsType, oilsId;
//    private MyDialogBuilder myDialogBuilder;
    private int deletePosition = -1;//记录删除的位置，初始化为-1

    private MyOilgunAdapter mAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        requestHandleArrayList.add(requestAction.getOilsguns(this, oilsId, stationId));
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_eachoils_gun;
    }

    @Override
    public void initComponent() {
        rl_oilsgun_top = (RelativeLayout) findViewById(R.id.rl_oilsgun_top);
        rl_oilsgun_addOilgun = (RelativeLayout) findViewById(R.id.rl_oilsgun_addOilgun);
        tv_oilsgun_oilstype = (TextView) findViewById(R.id.tv_oilsgun_oilstype);
        lv_oilsgun_listview = (ListView) findViewById(R.id.lv_oilsgun_listview);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "我的油枪", "");
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        Intent intent = getIntent();
        oilsType = intent.getStringExtra("oilsType");
        oilsId = intent.getStringExtra("oilsId");
        tv_oilsgun_oilstype.setText(oilsType);
        if (oilsType.equals("0#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.orange_F29305);
        } else if (oilsType.equals("92#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.green_6ED12B);
        } else if (oilsType.equals("93#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.green_20DC91);
        } else if (oilsType.equals("95#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.purple_D824CF);
        } else if (oilsType.equals("97#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.blue_176BEA);
        } else if (oilsType.equals("98#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.purple_992FDF);
        } else {
            rl_oilsgun_top.setBackgroundResource(R.color.blue_176BEA);
        }
        rl_oilsgun_addOilgun.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_oilsgun_addOilgun:
                Intent intent = new Intent(this, AddOilgunActivity.class);
                intent.putExtra("oilsId", oilsId);
                intent.putExtra("oilsType", oilsType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void processData(Boolean isChanged) {

        if (isChanged) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new MyOilgunAdapter(mContext, list, oilsType);
            lv_oilsgun_listview.setAdapter(mAdapter);
        }

    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MYOILSGUNS:

//                Log.e("当前油品对应的油枪", response.toString());

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[2];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("油枪号");
                        list.add(a);
                    }
                    tv_nodata.setText("");
                    processData(false);
                } else {
                    tv_nodata.setText("无数据");
                }

                break;

            case RequestAction.TAG_DELETEOILSGUN:

                MToast.showToast(mContext, "删除油枪成功");//然后需要刷新一遍数据,不需要再次调接口
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    processData(true);
                }
                deletePosition = -1;

                break;
            default:
                break;
        }
    }


    class MyOilgunAdapter extends BaseAdapter {

        private Context mContext;
        private List<String[]> list = new ArrayList<>();
        private String oilsType;
        private MyDialogBuilder myDialogBuilder;

        public MyOilgunAdapter(Context context, List<String[]> data, String oilsType) {
            this.mContext = context;
            this.list = data;
            this.oilsType = oilsType;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null != list ? list.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowview = inflater.inflate(R.layout.item_eachoils_gun, null);

            TextView tv_oilstype = (TextView) rowview.findViewById(R.id.tv_oilstype);
            TextView tv_oilgunNum = (TextView) rowview.findViewById(R.id.tv_oilgunNum);
            Button btn_delete = (Button) rowview.findViewById(R.id.btn_oilgun_delete);

            String[] strings = (String[]) getItem(position);
            tv_oilstype.setText(oilsType);
            tv_oilgunNum.setText(strings[1] + "号");


            if (oilsType.equals("0#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_f29305);

            } else if (oilsType.equals("92#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_6ed12b);
            } else if (oilsType.equals("93#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_20dc91);
            } else if (oilsType.equals("95#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_d824cf);
            } else if (oilsType.equals("97#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
            } else if (oilsType.equals("98#")) {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_992fdf);
            } else {

                tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
            }

            final int p = position;//选中的位置
            SwipeLayout s = (SwipeLayout) rowview;
            s.close(false, false);
            s.getFrontView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            s.setSwipeListener(MySwipe.mSwipeListener);
            btn_delete.setTag(p);
            btn_delete.setOnClickListener(onActionClick);


            return rowview;

        }

        View.OnClickListener onActionClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int p = (int) v.getTag();
                int id = v.getId();
                if (id == R.id.btn_oilgun_delete) {   //删除油枪
                    MySwipe.closeAllLayout();
                    myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("确定要删除这个油枪吗?")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    deletePosition = p;

                                    requestHandleArrayList.add(requestAction.deleteOilsguns(MyEachoilsGunActivity.this, list.get(p)[0]));

                                }
                            })
                            .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismiss();
                                }
                            })
                            .show();

                }
            }
        };


    }

}
