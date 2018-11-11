package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MySelectPopupWindow;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddOilgunActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_addOilgun_num;
    private TextView tv_addOilgun_select, tv_addOilgun_select_qiantai;
    private RelativeLayout rl_addOilgun_confirm, rl_addOilgun_select, rl_addOilgun_select_qiantai;
    private String stationId, oilsId, oilsType;
    private MySelectPopupWindow selectWindow, selectWindow1;
    private int selectPosition = -1, selectPosition1 = -1;
    private List<String[]> printerlist = new ArrayList<>();//打印机列表数据

    private List<String[]> stationPrinterList = new ArrayList<>();//油站打印机列表数据
    private List<String[]> qiantaiPrinterList = new ArrayList<>();//前台打印机列表数据

    private String deviceBrand;
    private String deviceModel;

    @Override
    public int initResource() {
        return R.layout.activity_add_oilgun;
    }

    @Override
    public void initComponent() {
        et_addOilgun_num = (EditText) findViewById(R.id.et_addOilgun_num);
        tv_addOilgun_select = (TextView) findViewById(R.id.tv_addOilgun_select);
        tv_addOilgun_select_qiantai = (TextView) findViewById(R.id.tv_addOilgun_select_qiantai);
        rl_addOilgun_select = (RelativeLayout) findViewById(R.id.rl_addOilgun_select);
        rl_addOilgun_select_qiantai = (RelativeLayout) findViewById(R.id.rl_addOilgun_select_qiantai);
        rl_addOilgun_confirm = (RelativeLayout) findViewById(R.id.rl_addOilgun_confirm);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "添加新油枪", "");
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        Intent intent = getIntent();
        oilsId = intent.getStringExtra("oilsId");
        oilsType = intent.getStringExtra("oilsType");
        rl_addOilgun_confirm.setOnClickListener(this);
        rl_addOilgun_select.setOnClickListener(this);
        rl_addOilgun_select_qiantai.setOnClickListener(this);
        requestHandleArrayList.add(requestAction.getPrinterData(this));

        deviceBrand = Build.BRAND;
        deviceModel = Build.MODEL;

        Log.e("获取设备信息-", "brand:" + deviceBrand + ",model:" + deviceModel);

        //SUNMI的brand为SUNMI

        if(deviceBrand.equals("SUNMI")){



        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_addOilgun_select://选择油枪打印机
                if (!stationPrinterList.isEmpty()) {
                    showSelectWindow();
                    selectPosition = -1;//每次调用后重置
                } else {
                    MyPromptDialogUtils.showPrompt(mContext, "您还没有可添加的油枪打印机");
                }
                break;
            case R.id.rl_addOilgun_select_qiantai://选择前台打印机
                if (!qiantaiPrinterList.isEmpty()) {
                    showSelectWindow1();
                    selectPosition1 = -1;//每次调用后重置
                } else {
                    MyPromptDialogUtils.showPrompt(mContext, "您还没有可添加的前台打印机");
                }
                break;
            case R.id.rl_addOilgun_confirm:
                String oilgunNum = et_addOilgun_num.getText().toString();
                if (TextUtils.isEmpty(oilgunNum)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入油枪编号");
                } else {
                    String printerNum = tv_addOilgun_select.getText().toString();
                    String printerNum1 = tv_addOilgun_select_qiantai.getText().toString();
                    requestHandleArrayList.add(requestAction.addOilsguns(this, stationId, oilsId, oilsType, oilgunNum, printerNum, printerNum1));
                }

                break;
            default:
                break;
        }
    }


    //弹出选择框--选择油站打印机
    private void showSelectWindow() {
        selectWindow = new MySelectPopupWindow(mContext, itemsOnClick, stationPrinterList);
        selectWindow.showAtLocation(findViewById(R.id.activity_add_oilgun), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ListView listView = selectWindow.setListViewSingle(mContext);
        final CommonAdapter commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_popupwindow_singletext, stationPrinterList) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_popsingletext_text, strings[0]);
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
            }
        });
        selectWindow.setMaxHeight(listView);
        selectWindow.setTitle("选择打印机");
    }

    //为弹出窗口实现监听类--选择油站打印机
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectWindow.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.ll_popupWindow_cancel:
                    selectPosition = -1;
                    break;
                // 确认
                case R.id.tv_popupWindow_confirm:
                    if (selectPosition < 0) {
                        return;
                    }
                    String printerNum = stationPrinterList.get(selectPosition)[0].toString();
                    tv_addOilgun_select.setText(printerNum);
                    selectPosition = -1;
                    break;
                default:
                    break;
            }
        }
    };


    //弹出选择框--选择前台打印机
    private void showSelectWindow1() {
        selectWindow1 = new MySelectPopupWindow(mContext, itemsOnClick1, qiantaiPrinterList);
        selectWindow1.showAtLocation(findViewById(R.id.activity_add_oilgun), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ListView listView = selectWindow1.setListViewSingle(mContext);
        final CommonAdapter commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_popupwindow_singletext, qiantaiPrinterList) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_popsingletext_text, strings[0]);
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition1 = position;
            }
        });
        selectWindow1.setMaxHeight(listView);
        selectWindow1.setTitle("选择打印机");
    }

    //为弹出窗口实现监听类--选择前台打印机
    private View.OnClickListener itemsOnClick1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectWindow1.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.ll_popupWindow_cancel:
                    selectPosition1 = -1;
                    break;
                // 确认
                case R.id.tv_popupWindow_confirm:
                    if (selectPosition1 < 0) {
                        return;
                    }
                    String printerNum = qiantaiPrinterList.get(selectPosition1)[0].toString();
                    tv_addOilgun_select_qiantai.setText(printerNum);
                    selectPosition1 = -1;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETPRINTERDATA:
//                Log.e("打印机", response.toString());
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[3];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("设备终端号");
                        a[1] = jsonObject.getString("id");
                        a[2] = jsonObject.getString("类别");
                        printerlist.add(a);
                    }

                    for (int i = 0; i < printerlist.size(); i++) {
                        if (printerlist.get(i)[2].equals("前台")) {
                            String[] qiantai = new String[1];
                            qiantai[0] = printerlist.get(i)[0];
                            qiantaiPrinterList.add(qiantai);
                        } else {
                            String[] youzhan = new String[1];
                            youzhan[0] = printerlist.get(i)[0];
                            stationPrinterList.add(youzhan);
                        }
                    }

                } else {
//                    MToast.showToast(mContext, "您还没有添加打印机");
                }

                break;
            case RequestAction.TAG_ADDOILSGUN:

                MToast.showToast(mContext, "添加油枪成功");
                finish();

                break;
        }
    }

}
