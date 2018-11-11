package com.gloiot.hygooilstation.ui.activity.trade;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.MySelectPopupWindow;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullableListView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.bluetoothprint.BluetoothUtil;
import com.gloiot.hygooilstation.utils.bluetoothprint.ESCUtil;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易--结算--结算明细
 */
public class SettlementDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {

    private TextView tv_personName, tv_oddNum, tv_settlementdetail_starttime, tv_settlementdetail_endtime, tv_jiaoyi, tv_daozhang, tv_totalNum;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mListView;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mSettlementDetailAdapter;
    private ImageView iv_toptitle_more_img;
    private MySelectPopupWindow selectWindow;
    private int selectPosition = -1;
    private List<String[]> printerlist = new ArrayList<>();//打印机列表数据
    private List<String[]> stationPrinterList = new ArrayList<>();//油站打印机列表数据
    private List<String[]> qiantaiPrinterList = new ArrayList<>();//前台打印机列表数据
    private MyDialogBuilder myDialogBuilder;
    private boolean isFirstRequestForPrinter = true;//是否第一次请求打印机接口
    private String settlementOddNum, name, start, end, totalNums, jiaoyi, daozhang;
    private int page = 0;

    private String deviceBrand;
    private String deviceModel;
    private String imei;

    @Override
    public int initResource() {
        return R.layout.activity_settlement_detail;
    }

    @Override
    public void initComponent() {
        tv_personName = (TextView) findViewById(R.id.tv_personName);
        tv_oddNum = (TextView) findViewById(R.id.tv_oddNum);
        tv_settlementdetail_starttime = (TextView) findViewById(R.id.tv_settlementdetail_starttime);
        tv_settlementdetail_endtime = (TextView) findViewById(R.id.tv_settlementdetail_endtime);
        tv_jiaoyi = (TextView) findViewById(R.id.tv_jiaoyi);
        tv_daozhang = (TextView) findViewById(R.id.tv_daozhang);
        tv_totalNum = (TextView) findViewById(R.id.tv_totalNum);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefreshlayout);
        mListView = (PullableListView) findViewById(R.id.pullablelistview);
        iv_toptitle_more_img = (ImageView) findViewById(R.id.iv_toptitle_more_img);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "结算明细", "");
        iv_toptitle_more_img.setImageResource(R.mipmap.dayin);
        iv_toptitle_more_img.setOnClickListener(this);
        Intent intent = getIntent();
        settlementOddNum = intent.getStringExtra("oddNum");
//        money = intent.getStringExtra("money");
        name = intent.getStringExtra("name");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        jiaoyi = intent.getStringExtra("jiaoyi");
        daozhang = intent.getStringExtra("daozhang");

//        tv_settlementdetail_money.setText("￥" + money);
//        tv_settlementdetail_name.setText(name);
//        tv_settlementdetail_oddNum.setText(settlementOddNum);

        tv_personName.setText(name);
        tv_oddNum.setText(settlementOddNum);
        tv_jiaoyi.setText(jiaoyi);
        tv_daozhang.setText(daozhang);
        tv_settlementdetail_starttime.setText(start);
        tv_settlementdetail_endtime.setText(end);

        pullToRefreshLayout.setOnRefreshListener(this);
        request(0, 0, 1, 0);

        deviceBrand = Build.BRAND;
//        deviceModel = Build.MODEL;

//        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        imei = telephonyManager.getDeviceId();//需要有读取手机状态的权限，否则三星直接crash

//        Log.e("获取设备信息-", "brand:" + deviceBrand + ",model:" + deviceModel+",IMEI:"+imei);

    }

    /**
     * @param requestType 请求类型，初始请求=0，刷新=1，加载=2
     * @param page        页数
     * @param requestTag  请求tag,请求成功后用于区分是哪个请求
     * @param showLoad    是否显示请求加载框 0--显示， -1--不显示
     */
    private void request(int requestType, int page, int requestTag, int showLoad) {
        requestHandleArrayList.add(requestAction.getSettlementItemDetail(this, pullToRefreshLayout, requestType, page, requestTag, showLoad, settlementOddNum));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (!list.isEmpty()) {
            mSettlementDetailAdapter.notifyDataSetChanged();
        }
        list.clear();
        page = 0;
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

        mSettlementDetailAdapter = new CommonAdapter<String[]>(SettlementDetailActivity.this, R.layout.item_settlementdetail, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
//                holder.setText(tv_settlementdetail_money, "+" + strings[2]);
//                holder.setText(tv_settlementdetail_name, strings[0]);
//                holder.setText(R.id.tv_settlementdetail_type, strings[1]);
//                holder.setText(tv_settlementdetail_oddNum, strings[3]);
//                holder.setText(R.id.tv_settlementdetail_time, strings[4]);

                holder.setText(R.id.tv_oddNum, "交易单号" + strings[3]);
                holder.setText(R.id.tv_time, strings[4]);
                holder.setText(R.id.tv_type, strings[0]);
                holder.setText(R.id.tv_oilsPrice, strings[5] + "/L");
                holder.setText(R.id.tv_jiaoyi, strings[1]);
                holder.setText(R.id.tv_daozhang, strings[2]);

            }
        };
        mListView.setAdapter(mSettlementDetailAdapter);

        //设置分隔线
        mListView.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
        mListView.setDividerHeight(16);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toptitle_more_img:

                //SUNMI的brand为SUNMI
                if (deviceBrand.equals("SUNMI")) {

                    // 1: Get BluetoothAdapter
                    BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
                    if (btAdapter == null) {
//                        Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "请开启蓝牙!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // 2: Get Sunmi's InnerPrinter BluetoothDevice
                    BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
                    if (device == null) {
//                        Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!", Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "请确认蓝牙连接内置打印机!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // 3: Generate a order data
//                    byte[] data = ESCUtil.generateMockData();

                    byte[] data = ESCUtil.generateSettlementOrderform(settlementOddNum, name, totalNums, jiaoyi, daozhang, start, end);

                    // 4: Using InnerPrinter print data
                    BluetoothSocket socket = null;
                    try {
                        socket = BluetoothUtil.getSocket(device);
                        BluetoothUtil.sendData(data, socket);
                    } catch (IOException e) {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                } else {

                    if (!qiantaiPrinterList.isEmpty()) {
                        showSelectWindow();
                        selectPosition = -1;//每次调用后重置
                    } else {
                        if (isFirstRequestForPrinter) {
                            requestHandleArrayList.add(requestAction.getPrinterData(this));
                        } else {
                            showPrompt("您还没有可使用的前台打印机");
                        }
                    }

                }

                break;
            default:
                break;
        }
    }

    //弹出选择框--选择打印机（前台打印机）
    private void showSelectWindow() {
        selectWindow = new MySelectPopupWindow(mContext, itemsOnClick, qiantaiPrinterList);
        selectWindow.showAtLocation(findViewById(R.id.activity_settlement_detail), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ListView listView = selectWindow.setListViewSingle(mContext);
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
                selectPosition = position;
            }
        });
        selectWindow.setMaxHeight(listView);
        selectWindow.setTitle("选择打印机");
    }

    //为弹出窗口实现监听类--选择打印机
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
                    String printerNum = qiantaiPrinterList.get(selectPosition)[0].toString();

                    requestHandleArrayList.add(requestAction.settlementOfPrint(SettlementDetailActivity.this, jiaoyi, daozhang, name, totalNums, settlementOddNum, start, end, printerNum));

                    selectPosition = -1;
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
            case 1:
                processResponseData(response, false);
                break;
            case 2:
                processResponseData(response, false);
                break;
            case 3:
                processResponseData(response, true);
                break;
            case RequestAction.TAG_GETPRINTERDATA:
//                Log.e("打印机", response.toString());
                int num1 = Integer.parseInt(response.getString("条数"));
                if (num1 != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[3];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("设备终端号");
                        a[2] = jsonObject.getString("类别");
                        printerlist.add(a);
                    }

                    for (int i = 0; i < printerlist.size(); i++) {
                        if (printerlist.get(i)[2].equals("前台")) {
                            String[] qiantai = new String[1];
                            qiantai[0] = printerlist.get(i)[1];
                            qiantaiPrinterList.add(qiantai);
                        } else {
                            String[] youzhan = new String[1];
                            youzhan[0] = printerlist.get(i)[1];
                            stationPrinterList.add(youzhan);
                        }
                    }

                    if (!qiantaiPrinterList.isEmpty()) {
                        showSelectWindow();
                        selectPosition = -1;//每次调用后重置
                    } else {
                        showPrompt("您还没有可使用的前台打印机");
                    }

                } else {
//                    showPrompt("您还没有可添加的打印机");
                }
                isFirstRequestForPrinter = false;
                break;

            case RequestAction.TAG_SETTLEMENTOFPRINT:

//                Log.e("结算打印", response.toString() + "---");

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
        L.e("结算详细", response.toString());
        int num = Integer.parseInt(response.getString("条数"));
        totalNums = Integer.parseInt(response.getString("总条数")) + "";

        tv_totalNum.setText(totalNums + "笔");
        if (num != 0) {
            JSONArray jsonArray = response.getJSONArray("列表");
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] a = new String[6];
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                a[0] = jsonObject.getString("油品名称");
                a[1] = jsonObject.getString("交易金额");
                a[2] = jsonObject.getString("到账金额");
                a[3] = jsonObject.getString("交易单号");
                a[4] = jsonObject.getString("录入时间");
                a[5] = jsonObject.getString("市场价");
                list.add(a);
            }
            page = num == 10 ? Integer.parseInt(response.getString("页数")) : 0;
            mListView.setVisibility(View.VISIBLE);
            if (isLoadMore) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                mSettlementDetailAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }

        } else {

            if (isLoadMore) {
                MToast.showToast(mContext, "已无数据加载");
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            } else {
                mListView.setVisibility(View.GONE);
            }
        }
    }


    //弹出对话框根据输入内容给出提示信息
    private void showPrompt(String prompt) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder
                .withIcon(R.mipmap.iconfont_gantanhao)
                .withContene(prompt)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismissNoAnimator();
                    }
                }).show();
    }
}
