package com.gloiot.hygooilstation.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygooilstation.App;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.WebViewActivity;
import com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity;
import com.gloiot.hygooilstation.ui.activity.login.LoginActivity;
import com.gloiot.hygooilstation.ui.activity.mine.SuggestionFeedbackActivity;
import com.gloiot.hygooilstation.ui.activity.mine.alipay.MyAliPayActivity;
import com.gloiot.hygooilstation.ui.activity.mine.cashier.ManageCashierActivity;
import com.gloiot.hygooilstation.ui.activity.mine.mybankcard.MyBankCardActivity;
import com.gloiot.hygooilstation.ui.activity.mine.setting.ModifyLoginpwdActivity;
import com.gloiot.hygooilstation.ui.activity.mine.setting.SettingActivity;
import com.gloiot.hygooilstation.ui.activity.mine.wallet.WalletActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.gloiot.hygooilstation.utils.UrlUtlis;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 我的
 * Created by dlt on 2016/9/14.
 */

public class MyFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = MyFragment.class.getSimpleName();

    private AutoLinearLayout ll_my_boss, ll_my_cashier;
    private AutoRelativeLayout rl_my_wallet, rl_my_bankCard, rl_my_alipay, rl_my_cashier, rl_my_suggestion_boss, rl_my_setting, rl_my_instructions_boss;
    private AutoRelativeLayout rl_my_suggestion_cashier, rl_my_instructions_cashier, rl_my_modifyLoginpwd, rl_my_logout;
    private MyDialogBuilder myDialogBuilder;

    public static Fragment newInstance(int position) {
        MyFragment fragment = new MyFragment();

        return fragment;
    }

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initComponent(view);
        initData();
        return view;
    }

    private void initComponent(View view) {
        ll_my_boss = (AutoLinearLayout) view.findViewById(R.id.ll_my_boss);
        rl_my_wallet = (AutoRelativeLayout) view.findViewById(R.id.rl_my_wallet);
        rl_my_bankCard = (AutoRelativeLayout) view.findViewById(R.id.rl_my_bankCard);
        rl_my_alipay = (AutoRelativeLayout) view.findViewById(R.id.rl_my_alipay);
        rl_my_cashier = (AutoRelativeLayout) view.findViewById(R.id.rl_my_cashier);
        rl_my_suggestion_boss = (AutoRelativeLayout) view.findViewById(R.id.rl_my_suggestion_boss);
        rl_my_setting = (AutoRelativeLayout) view.findViewById(R.id.rl_my_setting);
        rl_my_instructions_boss = (AutoRelativeLayout) view.findViewById(R.id.rl_my_instructions_boss);

        ll_my_cashier = (AutoLinearLayout) view.findViewById(R.id.ll_my_cashier);
        rl_my_suggestion_cashier = (AutoRelativeLayout) view.findViewById(R.id.rl_my_suggestion_cashier);
        rl_my_instructions_cashier = (AutoRelativeLayout) view.findViewById(R.id.rl_my_instructions_cashier);
        rl_my_modifyLoginpwd = (AutoRelativeLayout) view.findViewById(R.id.rl_my_modifyLoginpwd);
        rl_my_logout = (AutoRelativeLayout) view.findViewById(R.id.rl_my_logout);
    }

    private void initData() {
        String accountType = SharedPreferencesUtils.getString(getActivity(), ConstantUtlis.SP_ACCOUNTTYPE, "");
        if (accountType.equals("站长")) {
            ll_my_cashier.setVisibility(View.GONE);
            ll_my_boss.setVisibility(View.VISIBLE);
            rl_my_wallet.setOnClickListener(this);
            rl_my_bankCard.setOnClickListener(this);
            rl_my_alipay.setOnClickListener(this);
            rl_my_cashier.setOnClickListener(this);
            rl_my_suggestion_boss.setOnClickListener(this);
            rl_my_setting.setOnClickListener(this);
            rl_my_instructions_boss.setOnClickListener(this);
        } else if (accountType.equals("收银员")) {
            ll_my_boss.setVisibility(View.GONE);
            ll_my_cashier.setVisibility(View.VISIBLE);
            rl_my_suggestion_cashier.setOnClickListener(this);
            rl_my_instructions_cashier.setOnClickListener(this);
            rl_my_modifyLoginpwd.setOnClickListener(this);
            rl_my_logout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //boss
            case R.id.rl_my_wallet://钱包
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            case R.id.rl_my_bankCard://我的银行卡
                startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                break;
            case R.id.rl_my_alipay://我的支付宝
                String zizhiRenzhengState = SharedPreferencesUtils.getString(getActivity(), ConstantUtlis.SP_AUTHENTIFICATIONSTATE, "");
                if (zizhiRenzhengState.equals("未上传资料")) {
                    myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                    myDialogBuilder
                            .withTitie("提示")
                            .withContene("您还没有进行资质认证，认证后方可绑定支付宝")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("去认证", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    startActivity(new Intent(getActivity(), NewQualificationActivity.class));//20171023修改为新的资质信息上传页
                                }
                            })
                            .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismiss();
                                }
                            }).show();
                } else if (zizhiRenzhengState.equals("未审核")) {
                    myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                    myDialogBuilder
                            .withTitie("提示")
                            .withContene("您的资质认证信息正在审核中,请耐心等待")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();

                                }
                            })
                            .show();
                } else if (zizhiRenzhengState.equals("审核未通过")) {
                    myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                    myDialogBuilder
                            .withTitie("提示")
                            .withContene("您的资质认证信息未通过审核，请重新进行认证！")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("去认证", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    startActivity(new Intent(getActivity(), NewQualificationActivity.class));//20171023修改为新的资质信息上传页
                                }
                            })
                            .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismiss();
                                }
                            }).show();
                } else if (zizhiRenzhengState.equals("审核通过")) {
                    startActivity(new Intent(getActivity(), MyAliPayActivity.class));
                }
                break;
            case R.id.rl_my_cashier://收银员管理
                startActivity(new Intent(getActivity(), ManageCashierActivity.class));
                break;
            case R.id.rl_my_suggestion_boss://意见反馈--boss
                startActivity(new Intent(getActivity(), SuggestionFeedbackActivity.class));


//                startActivity(new Intent(getActivity(), TestSwipeRecyclerViewActivity.class));//测试RecyclerView的侧滑


                //---------------------测试加油订单SUNMI打印格式
//                String jiaoyidanhao = "123456789123456789";
//
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
//                Bitmap image = CodeUtils.createImage("success", 400, 400, clubLogo);
//
//                //SUNMI的brand为SUNMI,这里其实可以不用判断了，因为这个请求只有SUNMI设备会调用。
////                if (deviceBrand.equals("SUNMI")) {
//                if (true) {
//
//                    // 1: Get BluetoothAdapter
//                    BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
//                    if (btAdapter == null) {
////                        Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), "请开启蓝牙!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    // 2: Get Sunmi's InnerPrinter BluetoothDevice
//                    BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
//                    if (device == null) {
////                        Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!", Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), "请确认蓝牙连接内置打印机!", Toast.LENGTH_LONG).show();
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
            case R.id.rl_my_setting://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_my_instructions_boss://使用说明--boss
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "使用说明");
                intent.putExtra("url", UrlUtlis.SHIYONGSHUOMINGURL);
                startActivity(intent);
                break;
            //cashier
            case R.id.rl_my_suggestion_cashier://意见反馈--cashier
                startActivity(new Intent(getActivity(), SuggestionFeedbackActivity.class));
                break;
            case R.id.rl_my_instructions_cashier://使用说明--cashier
                Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                intent1.putExtra("title", "使用说明");
                intent1.putExtra("url", UrlUtlis.SHIYONGSHUOMINGURL);
                startActivity(intent1);
                break;
            case R.id.rl_my_modifyLoginpwd://修改登录密码
                startActivity(new Intent(getActivity(), ModifyLoginpwdActivity.class));
                break;
            case R.id.rl_my_logout://退出登录
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                App.getInstance().exit();
                myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                myDialogBuilder
                        .withContene("您确定要退出登录吗")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                SharedPreferencesUtils.setString(getActivity(), ConstantUtlis.SP_LOGINSTATE, "失败");//登录状态
                                App.getInstance().exit();//这里不要简单的finish，而应该退出整个应用
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        }).show();
                break;
        }

    }
}
