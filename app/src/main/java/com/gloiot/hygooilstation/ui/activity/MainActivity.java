package com.gloiot.hygooilstation.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.socket.SocketServer;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.bean.Message;
import com.gloiot.hygooilstation.server.db.DBManager;
import com.gloiot.hygooilstation.server.db.DB_Message;
import com.gloiot.hygooilstation.server.network.OnDataListener;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.adapter.MainFragmentAdapter;
import com.gloiot.hygooilstation.ui.widget.fragmentnavigator.BottomNavigatorView;
import com.gloiot.hygooilstation.ui.widget.fragmentnavigator.FragmentNavigator;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.NetBroadcastReceiver;
import com.gloiot.hygooilstation.utils.NetEvent;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.gloiot.hygooilstation.utils.bluetoothprint.BluetoothUtil;
import com.gloiot.hygooilstation.utils.bluetoothprint.ESCUtil;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AutoLayoutActivity implements OnDataListener, BottomNavigatorView.OnBottomNavigatorViewItemClickListener, NetEvent {

    private static final int DEFAULT_POSITION = 0;
    private FragmentNavigator mNavigator;
    private BottomNavigatorView bottomNavigatorView;

    public static Activity mainActivity;

    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;

    //    private WebSocket webSocket;
    public ArrayList<Message> list = new ArrayList<>();
    public DB_Message db_message;
    protected String mData = "";
    private String account, randomCode;

    public boolean isSetAdapter = false;

    public LocalBroadcastManager localBroadcastManager;

    protected Context mContext;
    protected RequestAction requestAction;
    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();

    private String deviceBrand;
    private String deviceModel;

    private SimpleDateFormat formatter;

    public static NotificationCompat.Builder mBuilder;
    public static NotificationManager mNotifyMgr;


    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferencesUtils.getBoolean(this, ConstantUtlis.SP_FROMNOTIFICATION, false)) {
            setCurrentTab(1);
            SharedPreferencesUtils.setBoolean(this, ConstantUtlis.SP_FROMNOTIFICATION, false);
        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mainActivity = this;
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        editor = preferences.edit();


        if (check_login())
            IMDBManager.getInstance(mContext).getHygoHelperInstance(preferences.getString(ConstantUtlis.SP_USEACCOUNT, ""), "HygoOilstationIm", 1);
//        if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USEACCOUNT, "")) &&
//                !TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""))) {
//            //app重开后，将持久化存储的账号和随机码传入
//            SocketListener.getInstance().staredData(preferences.getString(ConstantUtlis.SP_USEACCOUNT, ""),
//                    preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
//        }
//        // 连接
//        SocketServer.socketOnConnect();

        socketReConnection();

        requestAction = new RequestAction();

        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new MainFragmentAdapter(), R.id.container);
//        mNavigator.setDefaultPosition(DEFAULT_POSITION); //0324修改，不在这里设默认位置，而在initData()中，根据从哪个页面进入的设置选中哪个位置。
        mNavigator.onCreate(savedInstanceState);

        bottomNavigatorView = (BottomNavigatorView) findViewById(R.id.bottomNavigatorView);
        if (bottomNavigatorView != null) {
            bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);
        }

        setCurrentTab(mNavigator.getCurrentPosition());

//        App.getInstance().addActivity(this);//如果有这句，如果Activity被回收，则进入所有其他Activity均报错，因数据输入输出的问题
        initData();

        account = SharedPreferencesUtils.getString(this, ConstantUtlis.SP_USEACCOUNT, "");
        randomCode = SharedPreferencesUtils.getString(this, ConstantUtlis.SP_RANDOMCODE, "");

//        webSocket = WebSocket.getInstance(this, account, randomCode);
//        mcallback();

        db_message = new DB_Message(new DBManager(this));

        getMessageDB();


        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netBroadcastReceiver = new NetBroadcastReceiver();
        mContext.registerReceiver(netBroadcastReceiver, filter);

        //设置监听
        netBroadcastReceiver.setNetEvent(this);

        BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                badge.setBadgeNumber(IMDBManager.getInstance(mContext).GetAllReadNum());

                String data = intent.getStringExtra("data");
                L.e("MainActivity收到消息", "data=" + data);
                processReceivedMessage(data);

            }
        });


    }

    @Override
    public void onNetChange(int netMobile) {
        switch (netMobile) {
            case 1://wifi
            case 0://移动数据
            case -1://没有网络
                BroadcastManager.getInstance(this).sendBroadcast("com.gloiot.hygo.判断网络", netMobile + "");
                break;
        }
    }

    private void initData() {

        if (SharedPreferencesUtils.getBoolean(this, ConstantUtlis.SP_FROMNOTIFICATION, false)) {//从通知进来
            setCurrentTab(1);//那么这里会不会出现问题。布局重叠？
            SharedPreferencesUtils.setBoolean(this, ConstantUtlis.SP_FROMNOTIFICATION, false);
        } else {
            if (SharedPreferencesUtils.getBoolean(this, ConstantUtlis.SP_ISFROMLOGINTOMAIN, false)) {//从登录页或油站编辑页进入主页的话，默认选中第一个fragment，其他情况根据保存的状态选择。
                setCurrentTab(0);//测试发现，不能指定进入哪一个fragment，否则会出现fragment重叠。因为在onCreate()中先选中了第一个，所以会有两个首页。0324修改。
                SharedPreferencesUtils.setBoolean(this, ConstantUtlis.SP_ISFROMLOGINTOMAIN, false);
            } else if (SharedPreferencesUtils.getBoolean(this, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, false)) {
                setCurrentTab(0);
                SharedPreferencesUtils.setBoolean(this, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, false);
            } else {

            }
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);//获取实例

        deviceBrand = Build.BRAND;
        deviceModel = Build.MODEL;

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    //判断是否登录
    public boolean check_login() {
        if (preferences.getString(ConstantUtlis.SP_LOGINSTATE, "").equals("成功")) {
            return true;
        } else {
            return false;
        }
    }

    //------------------------网络请求回调
    @Override
    public void onStart(int requestTag, int showLoad) {

    }

    @Override
    public void onCancel(int requestTag, int showLoad) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
        setCurrentTab(position);
    }

    private void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }

    private void resetAllTabsAndShow(int position) {
        mNavigator.resetFragments(position, true);
        bottomNavigatorView.select(position);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_TRADINGDETAIL:
                L.e("SUNMI收到交易详情--MainActivity", response.toString() + "--");

                String jiaoyidanhao = response.getString("交易单号");
                String stationNum = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");//油站ID
                String oilStation = response.getString("油站名称");
                String payType = response.getString("支付方式");
                String oilgun = response.getString("油枪号");
                String oilType = response.getString("油品名称");
                String jiaoyiTime = response.getString("时间");
                String discount = response.getString("折扣比例");
                String jiaoyiMoney = response.getString("金额");
                String outOfPocket = response.getString("实付金额");
                String remark = response.getString("备注");

                //SUNMI的brand为SUNMI,这里其实可以不用判断了，因为这个请求只有SUNMI设备会调用。
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

//                    byte[] data = ESCUtil.generateRefuelOrderform(jiaoyidanhao, oilStation, payType, oilgun, oilType, jiaoyiTime, jiaoyiMoney);
                    byte[] data = ESCUtil.generateRefuelOrderformNew(jiaoyidanhao, stationNum, oilStation, payType, oilgun, oilType, jiaoyiTime, discount,
                            jiaoyiMoney, outOfPocket, remark);

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

                }

                break;
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else {
                MToast.showToast(mContext, response.getString("状态"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        MToast.showToast(mContext, "网络好像有点问题，请检查后重试");
    }
    //------------------------网络请求回调


    /**
     * 处理接收到的消息
     *
     * @param data
     */
    private void processReceivedMessage(String data) {
        try {
            String processData;
            if (data.contains("\\")) {
                processData = data.replace("\\", "").replace("\"{", "{").replace("}\"", "}");


            } else {
                processData = data;
            }


            L.e("MainActivity收到消息:", "processData---" + processData);

            JSONObject jsonObject = new JSONObject(processData);

            L.e("MainActivity收到消息:", "data---" + data + ",content==" + jsonObject.getJSONObject("content"));

            Message message = new Message();
            message.setMessageId(jsonObject.getString("msgid"));
            message.setSendId(jsonObject.getString("sendid"));
            message.setReceiveId(jsonObject.getString("receiveid"));
            message.setReceiveTime("系统当前时间");
            message.setSendTime(jsonObject.getString("time"));

//            L.e("content:", "content==" + jsonObject.getJSONObject("content"));

            message.setContent(jsonObject.getJSONObject("content").toString());
            message.setMessageType(jsonObject.getString("type"));

            // 判断数据库是否有重复msgid并添加进数据库（receiveid为账号）
            if (!db_message.ifMsgId(jsonObject.getString("receiveid"), jsonObject.getString("msgid"))) {

                list.add(message);
                ArrayList<Message> newList = new ArrayList<>();
                newList.add(message);
                addMessageDB(newList);

                JSONObject jsonContent = jsonObject.getJSONObject("content");
                // 消息内容
                String contentText = jsonContent.getString("text");
                // 判断消息类型
//                        String contentTitle = jsonObject.getString("type").equals("system") ? "系统消息" : "交易提醒";
                String contentTitle = "";
                if (jsonObject.getString("type").equals("system")) {
                    contentTitle = "系统消息";
                } else if (jsonObject.getString("type").equals("trading")) {
                    contentTitle = "交易提醒";

                    //接收到交易提醒消息，如果是SUNMI设备，再次请求完整数据，然后在成功的回调里使用蓝牙打印。
                    if (deviceBrand.equals("SUNMI")) {
                        Date curDate = new Date(System.currentTimeMillis());
                        String currentTime = formatter.format(curDate);//系统当前时间
                        String jiaoyiTime = jsonObject.getString("time");//交易发生的时间

                        try {
                            if (dateCompare(currentTime, jiaoyiTime)) {//交易时间在当前时间之前大于10分钟,不打印

                            } else {
                                final String jiaoyiNum = jsonContent.getString("jiaoyi");

                                //延迟两秒执行
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestHandleArrayList.add(requestAction.getTradingDetail(MainActivity.this, jiaoyiNum));
                                        L.e("MainActivitySUNMI设备发起请求", "jiaoyiNum=" + jiaoyiNum);
                                    }
                                }, 2000);
//                                requestHandleArrayList.add(requestAction.getTradingDetail(MainActivity.this, jiaoyiNum));

                                L.e("MainActivity收到交易消息，SUNMI设备", "jiaoyiNum=" + jiaoyiNum);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {
                    contentTitle = "提现消息";
                }

                // 通知栏消息提醒
                onNotification(contentTitle, contentText);
            }

//                    commonAdapter.notifyDataSetChanged();
//                    isMessageDataChanged = true;

            Intent intent = new Intent("com.gloiot.hygooilstation.MESSAGEDATA_CHANGE");
            localBroadcastManager.sendBroadcast(intent);//发送本地广播
            L.e("MainActivity发送广播", "发出广播时间：" + list.get(list.size() - 1).getSendTime());

        } catch (JSONException e) {
//            Log.e("消息提醒回调", "JSONException。。");
            e.printStackTrace();
        }
    }


    /**
     * 比较两个时间字符串
     */
    public boolean dateCompare(String s1, String s2) throws Exception {

        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        Date d2 = sdf.parse(s2);
        //比较
        if (Math.abs(((d1.getTime() - d2.getTime()) / (1 * 1 * 60 * 1000))) >= 10) {//d1比d2大10分钟
            return true;
        } else {
            return false;
        }

    }

    private void onNotification(String title, String text) {

        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)//当点击通知时，这条通知自动取消掉
                .setSmallIcon(R.mipmap.ic_tongzhi_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_tongzhi_big))
                .setContentTitle(title)
                .setContentText(text)
                .setSound(Uri.parse("android.resource://com.gloiot.hygooilstation/raw/msg"));//音频文件

//        Intent resultIntent = new Intent(this, this.getClass());

        Intent resultIntent = new Intent(this, this.getClass());
//        resultIntent.putExtra("lauchfrom", "notification");//启动MainActivity时进行一次判断，以确定进入哪个fragment。

        SharedPreferencesUtils.setBoolean(this, ConstantUtlis.SP_FROMNOTIFICATION, true);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置点击行为，和Notification相关联起来
        mBuilder.setContentIntent(resultPendingIntent);

        //设置ID为001？这里只是指定一个通知id
        int mNotificationId = 001;
        mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //要在setContentIntent之后完成
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


    public void getMessageDB() {
        ArrayList<Message> ListMsg = db_message.getMessageDB(account);
//        ArrayList<Message> ListMsg = db_message.getMessageDB("1000033");//测试
        for (Message message : ListMsg) {
            list.add(message);
        }
//        lv_message.setAdapter(commonAdapter);
        isSetAdapter = true;
    }

    private void addMessageDB(ArrayList<Message> list) {
        db_message.addMessageDB(list);
    }

    private void deleteMessageDB() {
        db_message.deleteMessageDB(account);
    }

    /**
     * 取消网络请求
     */
    public void cancelRequestHandle() {
        if (requestHandleArrayList.size() != 0) {
            for (int i = 0; i < requestHandleArrayList.size(); i++) {
                requestHandleArrayList.get(i).cancel(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("MainActivity onDestroy", "here---------000000");
        cancelRequestHandle();

        //反注册广播
        if (netBroadcastReceiver != null) {
            mContext.unregisterReceiver(netBroadcastReceiver);
        }

        if (mBuilder != null) {
            mNotifyMgr.cancel(001);
            L.e("MainActivity onDestroy", "tongzhi 不为空 ---");
        }

        L.e("MainActivity onDestroy", "tongzhi ---001");

//        App.getInstance().exit();//如果有这句，如果Activity被回收，则进入所有其他Activity均报错，因数据输入输出的问题
    }

//    @Override
//    public void finish() {
//        super.finish();
//        L.e("MainActivity finish","here---------000000");
//        if (mBuilder != null) {
//            mNotifyMgr.cancel(001);
//            L.e("MainActivity finish","tongzhi 不为空 ---");
//        }
//    }

    /**
     * app重开，socket重连
     */
    private void socketReConnection() {
        String account = preferences.getString(ConstantUtlis.SP_USEACCOUNT, "");
        String random = preferences.getString(ConstantUtlis.SP_RANDOMCODE, "");
        //app重开后，将持久化存储的账号和随机码传入
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(random)) {
            SocketListener.getInstance().staredData(account, random);
        }
        // 连接
        SocketServer.socketOnConnect();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
