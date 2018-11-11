package com.gloiot.hygooilstation.ui.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.MainActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_login_account, et_login_password;
    private TextView tv_login, tv_login_forgetPwd;
    private String mAccount, mPassword;

    @Override
    public int initResource() {
        return R.layout.activity_login;
    }

    @Override
    public void initComponent() {
        et_login_account = (EditText) findViewById(R.id.et_login_account);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login_forgetPwd = (TextView) findViewById(R.id.tv_login_forgetPwd);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, false, "登录", "");
        if (SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_ISLOGINPWDRESET, "").equals("true")) {
            MToast.showToast(mContext, "您的密码已重置，请重新登录");
            SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_ISLOGINPWDRESET, "false");
        }
        tv_login.setOnClickListener(this);
        tv_login_forgetPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_login://登录
                if (login_verification()) {
                    mAccount = et_login_account.getText().toString().trim();
                    mPassword = MD5Util.Md5(et_login_password.getText().toString()).trim();
                    requestHandleArrayList.add(requestAction.userLogin(this, mAccount, mPassword));
                }
                break;
            case R.id.tv_login_forgetPwd://忘记密码
                startActivity(new Intent(this, ForgetPwdActivity.class));


//                PhotoPicker.builder()
//                        .setPhotoCount(5 )
//                        .setShowCamera(true)
//                        .setShowGif(false)
//                        .setPreviewEnabled(true)
//                        .start(this, PhotoPicker.REQUEST_CODE);

                //-------------------------------------测试图片选择框架
//                checkPermission(new CheckPermListener() {
//                    @Override
//                    public void superPermission() {
//
//                        ChoosePhoto mChoosePhoto = new ChoosePhoto(mContext) {
//                            @Override
//                            protected void setPortraitonSuccess(String myPicUrl) {
//                            }
//
//                            @Override
//                            protected void setPortraitonSuccess(String myPicUrl, boolean a) {
//                            }
//
//                            @Override
//                            protected void setPortraitFailure() {
////                MToast.showToast(OilstationPicsActivity.this, "抱歉该图片格式不支持");
//                            }
//
//                            @Override
//                            protected void setPicsSuccess(final List<String> picsUrl, boolean over) {//这个成功是从本地上传到阿里云成功
//
//                                for (int i = 0; i < picsUrl.size(); i++) {
//                                    Log.e("地址=", picsUrl.get(i));
//                                }
//
//                            }
//                        };
//                        mChoosePhoto.setMuti(1);
//                        mChoosePhoto.setPics2();
//
//
//                    }
//                }, R.string.camera_updatepics, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //-------------------------------------测试图片选择框架


                //---------------------测试加油订单SUNMI打印格式
//                String jiaoyidanhao = "123456789123456789";
//                String stationNum = "1000175";
//                String oilStation = "国贸油站";
//                String payType = "积分";
//                String oilgun = "1";
//                String oilType = "97#";
//                String jiaoyiTime = "2017-06-05 12:12:12";
//                String discount = "1";
//                String jiaoyiMoney = "66";
//                String outOfPocket = "66";
//                String remark = "测试SUNMI打印格式";
//
//                Bitmap clubLogo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
//                Bitmap image = CodeUtils.createImage("success", 250, 250, clubLogo);
//
//                //SUNMI的brand为SUNMI,这里其实可以不用判断了，因为这个请求只有SUNMI设备会调用。
////                if (deviceBrand.equals("SUNMI")) {
//                if (true) {
//
//                    // 1: Get BluetoothAdapter
//                    BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
//                    if (btAdapter == null) {
////                        Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
//                        Toast.makeText(mContext, "请开启蓝牙!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    // 2: Get Sunmi's InnerPrinter BluetoothDevice
//                    BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
//                    if (device == null) {
////                        Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!", Toast.LENGTH_LONG).show();
//                        Toast.makeText(mContext, "请确认蓝牙连接内置打印机!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    // 3: Generate a order data
//
//                    byte[] data = ESCUtil.generateRefuelOrderformNew1(jiaoyidanhao, stationNum, oilStation, payType, oilgun, oilType, jiaoyiTime, discount,
//                            jiaoyiMoney, outOfPocket, remark,image);
//
//                    // 4: Using InnerPrinter print data
//                    BluetoothSocket socket = null;
//                    try {
//                        socket = BluetoothUtil.getSocket(device);
//                        BluetoothUtil.sendData(data, socket);
//                    } catch (IOException e) {
//                        if (socket != null) {
//                            try {
//                                socket.close();
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    }
//
//                }
                //---------------------测试加油订单SUNMI打印格式


                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_LOGIN:
                L.e("登录", response.toString());
                String loginState = response.getString("登录状态");
                String randomCode = response.getString("随机码");
                String authenticationState = response.getString("认证状态");
                String userAccount = response.getString("账号");
                String accountType = response.getString("类别");
                String pwdState = response.getString("密码状态");//支付密码的状态：已设置/未设置。

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("账号", userAccount);
                hashMap.put("随机码", randomCode);
                hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                CommonUtlis.saveMap(ConstantUtlis.SP_INFO_JSON, hashMap);

//                Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//                for (Map.Entry<String, Object> e : entry) {
//                    Log.e("SP_INFO_JSON", e.getKey() + "-" + e.getValue());
//                }

                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_RANDOMCODE, randomCode);//将随机码存储起来
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_AUTHENTIFICATIONSTATE, authenticationState);//认证状态
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_LOGINSTATE, loginState);//登录状态
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_USEACCOUNT, userAccount);//用户账号
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_ACCOUNTTYPE, accountType);//账号类别
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_PAYPWDSTATE, pwdState);//支付密码状态

//                Log.e("登录返回数据--", response.toString());

//                if (authenticationState.equals("未上传资料")) {
//                    startActivity(new Intent(this, QualificationInfoActivity.class));
//                } else if (pwdState.equals("未设置")) {
//                    startActivity(new Intent(this, SetPaypwdActivity.class));
//                    return;
//                } else if (authenticationState.equals("未审核") || authenticationState.equals("审核未通过")) {
//                    startActivity(new Intent(this, FinishAuthenticationActivity.class));
//                } else if (authenticationState.equals("审核通过")) {
//                    Intent intent = new Intent(this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

//                imDB(userAccount);
//                imSocket();
//
//                IMDBManager.getInstance(mContext).getHygoHelperInstance(preferences.getString(ConstantUtlis.SP_USEACCOUNT, ""), "HygoIm", 1);
//                if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USEACCOUNT, "")) &&
//                        !TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""))) {
//                    //app重开后，将持久化存储的账号和随机码传入
//                    SocketListener.getInstance().staredData(preferences.getString(ConstantUtlis.SP_USEACCOUNT, ""),
//                            preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
//
//                }
//                // 连接
//                SocketServer.socketOnConnect();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMLOGINTOMAIN, true);//从登录页进入主页
                finish();

                imSocket(userAccount, randomCode);
                break;
        }
    }

    private void imDB(String account) {
        IMDBManager.getInstance(mContext).getHygoHelperInstance(account, "HygoOilstationIm", 1);
    }

    // socket认证
    private void imSocket(String account, String random) {
        if (!TextUtils.isEmpty(account)) {
            SocketListener.getInstance().connectionRenZheng(account, random);
        }
    }


    //页面数据验证
    private boolean login_verification() {
        if (TextUtils.isEmpty(et_login_account.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "账号不能为空，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_login_password.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "密码不能为空，请重新输入");
            return false;
        } else if (et_login_password.getText().length() < 6) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的密码有误");
            return false;
        } else {
            return true;
        }
    }


}
