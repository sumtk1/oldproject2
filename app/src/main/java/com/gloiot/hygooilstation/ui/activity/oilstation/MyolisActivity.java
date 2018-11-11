package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
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
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的油品
 */
public class MyolisActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_myoils_listview;
    private RelativeLayout rl_myoils_add;
    private TextView tv_toptitle_right, tv_listview_no;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mMyoilsAdapter;
    private MyDialogBuilder myDialogBuilder;

    private String stationId, stationName;
    private String accountType;

    private int deletePosition = -1;//记录删除的位置，初始化为-1

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        requestHandleArrayList.add(requestAction.getmyoils(this, stationId));
    }

    @Override
    public int initResource() {
        return R.layout.activity_myoils;
    }

    @Override
    public void initComponent() {
        lv_myoils_listview = (ListView) findViewById(R.id.lv_myoils_listview);
        rl_myoils_add = (RelativeLayout) findViewById(R.id.rl_myoils_add);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_listview_no = (TextView) findViewById(R.id.tv_listview_no);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        stationId = intent.getStringExtra("id");
        stationName = intent.getStringExtra("stationName");
        accountType = SharedPreferencesUtils.getString(this, ConstantUtlis.SP_ACCOUNTTYPE, "");
        if (accountType.equals("收银员")) {
            CommonUtlis.setTitleBar(this, true, "我的油品", "");
            rl_myoils_add.setVisibility(View.GONE);
        } else if (accountType.equals("站长")) {
            CommonUtlis.setTitleBar(this, true, "我的油品", "审核记录");
            rl_myoils_add.setVisibility(View.VISIBLE);
            rl_myoils_add.setOnClickListener(this);
            tv_toptitle_right.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_toptitle_right:
                Intent intent = new Intent(MyolisActivity.this, AuditLoggingActivity.class);
                intent.putExtra("stationId", stationId);
                startActivity(intent);
                break;
            case R.id.rl_myoils_add:
                Intent intent1 = new Intent(MyolisActivity.this, AddOilsActivity.class);
                intent1.putExtra("stationId", stationId);
                intent1.putExtra("stationName", stationName);
                startActivity(intent1);
                break;
        }

    }

    private void processData(Boolean isChanged) {

        if (isChanged) {
            mMyoilsAdapter.notifyDataSetChanged();
        } else {
            mMyoilsAdapter = new CommonAdapter<String[]>(MyolisActivity.this, R.layout.item_myoils, list) {
                @Override
                public void convert(final ViewHolder holder, String[] strings) {
                    holder.setText(R.id.tv_myoils_type, strings[3]);
                    holder.setText(R.id.tv_myoils_market, strings[4] + "/L");

                    double ranglijiaDouble = Double.parseDouble(strings[5]);
                    BigDecimal b1 = new BigDecimal(ranglijiaDouble);
                    Double ranglijia = b1.setScale(3, RoundingMode.HALF_UP).doubleValue();
                    holder.setText(R.id.tv_myoils_rangli, ranglijia + "/L");

                    float youhui = Float.parseFloat(strings[6]);
                    float ratio = 100 - youhui;
                    String zhi = ratio + "";
                    double rangDouble = Double.parseDouble(zhi);
                    BigDecimal b = new BigDecimal(rangDouble);
                    Double jiesuanbi = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    holder.setText(R.id.tv_myoils_ratio, jiesuanbi + "%");

                    final int p = holder.getmPosition();//选中的位置
                    Button btn_delete = holder.getView(R.id.btn_delete);
                    SwipeLayout s = (SwipeLayout) holder.getConvertView();
                    s.close(false, false);
                    s.getFrontView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (accountType.equals("站长")) {
                                Intent intent = new Intent(MyolisActivity.this, ModifyOilsActivity.class);
                                intent.putExtra("stationId", list.get(p)[1]);
                                intent.putExtra("type", list.get(p)[3]);
                                intent.putExtra("market", list.get(p)[4]);
                                intent.putExtra("youhui", list.get(p)[5]);
                                intent.putExtra("ratio", list.get(p)[6]);
                                startActivity(intent);
                            } else if (accountType.equals("收银员")) {

                            }
                        }
                    });
                    s.setSwipeListener(MySwipe.mSwipeListener);
                    btn_delete.setTag(p);
                    btn_delete.setOnClickListener(onActionClick);
                }
            };
//        mMyoilsAdapter.notifyDataSetChanged();
            lv_myoils_listview.setAdapter(mMyoilsAdapter);
            //设置分隔线的正确写法，先给颜色，再设高度
            lv_myoils_listview.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
            lv_myoils_listview.setDividerHeight(30);

        }
    }

    View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int p = (int) v.getTag();
            int id = v.getId();
            if (id == R.id.btn_delete) {   //删除油品
                MySwipe.closeAllLayout();
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);

//                Log.e("账号类型：",accountType+"--");

                if (accountType.equals("站长")) {
                    myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("删除油品则无法继续出售此油品，您确认要删除吗？")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    String oilsId = list.get(p)[0];
                                    String stationId = list.get(p)[1];
                                    String oilsType = list.get(p)[3];
                                    deletePosition = p;
                                    requestHandleArrayList.add(requestAction.deleteoils(MyolisActivity.this, stationId, oilsId, oilsType));
                                }
                            })
                            .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismiss();
                                }
                            })
                            .show();
                } else if (accountType.equals("收银员")) {
                    myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("收银员无此权限！")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                }
                            })
                            .show();
                }
            }
        }
    };

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MYOILS:
//                Log.e("我的油品", response.toString());
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {

                    lv_myoils_listview.setVisibility(View.VISIBLE);

                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[7];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("油站id");
                        a[2] = jsonObject.getString("油站名称");
                        a[3] = jsonObject.getString("油品型号");
                        a[4] = jsonObject.getString("市场价");
                        a[5] = jsonObject.getString("优惠金额");
                        a[6] = jsonObject.getString("优惠百分比");
                        list.add(a);
                    }
                    processData(false);
                } else {
//                    tv_listview_no.setText("无数据");
//                    lv_myoils_listview.setAdapter(mMyoilsAdapter);
                    lv_myoils_listview.setVisibility(View.GONE);
                }
                break;
            case RequestAction.TAG_DELETEOILS:
                MToast.showToast(mContext, "删除油品成功");
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    processData(true);
                }
                deletePosition = -1;
                break;
        }
    }
}
