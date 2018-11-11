package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.NumIncreaseTextView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wallet_stationName, tv_toptitle_right, tv_wallet_manual, tv_wallet_automatic, tv_wallet_extract_to_alipay,
            tv_wallet_yijiesuan, tv_wallet_ketiqu, tv_wallet_weijiesuan;
    private NumIncreaseTextView tv_wallet_money;
    private MyDialogBuilder myDialogBuilder1, myDialogBuilder2, myDialogBuilder3;
    public static Activity walletActivity;
    private String isCanExtract = "";//是否开放提取
    private String isCanExtractToBankcard = "";//银行卡通道
    private String isCanExtractToAlipay = "";//支付宝通道
    private String extractType = "";//提取类别：bankcard/alipay

    @Override
    public int initResource() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initComponent() {
        tv_wallet_stationName = (TextView) findViewById(R.id.tv_wallet_stationName);
        tv_wallet_money = (NumIncreaseTextView) findViewById(R.id.tv_wallet_money);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_wallet_manual = (TextView) findViewById(R.id.tv_wallet_manual);
        tv_wallet_automatic = (TextView) findViewById(R.id.tv_wallet_automatic);
        tv_wallet_extract_to_alipay = (TextView) findViewById(R.id.tv_wallet_extract_to_alipay);
        tv_wallet_yijiesuan = (TextView) findViewById(R.id.tv_wallet_yijiesuan);
        tv_wallet_ketiqu = (TextView) findViewById(R.id.tv_wallet_ketiqu);
        tv_wallet_weijiesuan = (TextView) findViewById(R.id.tv_wallet_weijiesuan);
    }

    @Override
    public void initData() {
        walletActivity = this;
        String account = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_USEACCOUNT, "");
        CommonUtlis.setTitleBar(this, true, "钱包", "明细");
        requestHandleArrayList.add(requestAction.getWalletTotalAmount(this, account));
        tv_toptitle_right.setOnClickListener(this);
        tv_wallet_manual.setOnClickListener(this);
        tv_wallet_extract_to_alipay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_toptitle_right:
                startActivity(new Intent(this, WalletListActivity.class));

//                startActivity(new Intent(this, QualificationInfoForOldUserActivity.class));//测试老用户补充资质认证页面

                break;
            case R.id.tv_wallet_manual://手动提现（到银行卡）

                int versionCode = CommonUtlis.getVersionCode(mContext);
                requestHandleArrayList.add(requestAction.getAuthenticationAndPaypwdState(this, versionCode + ""));
                extractType = "bankcard";

//                //测试新的资质认证
//                startActivity(new Intent(WalletActivity.this, NewQualificationActivity.class));

                break;
            case R.id.tv_wallet_automatic://自动提现
//                startActivity(new Intent(this, AutomaticTixianActivity.class));
                break;
            case R.id.tv_wallet_extract_to_alipay://提取到支付宝
                int versionCode1 = CommonUtlis.getVersionCode(mContext);
                requestHandleArrayList.add(requestAction.getAuthenticationAndPaypwdState(this, versionCode1 + ""));
                extractType = "alipay";
                break;
            default:
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case RequestAction.TAG_WALLET:
////                tv_wallet_stationName.setText(response.getString("油站名称"));
////                tv_wallet_money.setText(response.getString("金额"));
//
//                tv_wallet_stationName.setText(SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONNAME, ""));
//
//                String moneyString = response.getString("金额");
//                float moneyFloat = Float.parseFloat(moneyString);
//                tv_wallet_money.setDuration(1000);
//                tv_wallet_money.setNumber(moneyFloat);
//                tv_wallet_money.showNumberWithAnimation();
//                break;

            case RequestAction.TAG_WALLETTOTALAMOUNT:
                L.e("钱包", response.toString() + "---");
                tv_wallet_stationName.setText(SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONNAME, ""));
                isCanExtract = response.getString("开放提取");//选项有“是”/“否”
                String moneyString = response.getString("总金额");
                float moneyFloat = Float.parseFloat(moneyString);
                tv_wallet_money.setDuration(1000);
                tv_wallet_money.setNumber(moneyFloat);
                tv_wallet_money.showNumberWithAnimation();

                String weijiesuan = response.getString("未结算金额");
                String yijiesuan = response.getString("已结算金额");
                String ketixian = response.getString("可提现金额");
                tv_wallet_weijiesuan.setText(weijiesuan);
                tv_wallet_ketiqu.setText(ketixian);
                tv_wallet_yijiesuan.setText(yijiesuan);

                isCanExtractToBankcard = response.getString("银行卡通道");
                isCanExtractToAlipay = response.getString("支付宝通道");

                if (isCanExtractToBankcard.equals("是")) {
                    tv_wallet_manual.setVisibility(View.VISIBLE);
                } else {
                    tv_wallet_manual.setVisibility(View.GONE);
                }
                if (isCanExtractToAlipay.equals("是")) {
                    tv_wallet_extract_to_alipay.setVisibility(View.VISIBLE);
                } else {
                    tv_wallet_extract_to_alipay.setVisibility(View.GONE);
                }
                break;
            case RequestAction.TAG_AUTHENTICATIONANDPAYPWDSTATE:

                L.e("认证状态相关信息", response.toString() + "---");

                String authenticationState = response.getString("认证状态");
                String pwdState = response.getString("密码状态");//支付密码的状态：已设置/未设置。

                String stationGroupPhotoState = response.getString("油站合影状态");//油站合影状态：已上传/未上传

                String stationLegalPersonState = response.getString("油站法人状态");

                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_AUTHENTIFICATIONSTATE, authenticationState);//认证状态
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_PAYPWDSTATE, pwdState);//支付密码状态

                if (authenticationState.equals("未上传资料")) {

                    myDialogBuilder1 = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder1
                            .withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("先进行资质认证才可提现！")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder1.dismissNoAnimator();
//                                    startActivity(new Intent(WalletActivity.this, QualificationInfoActivity.class));
//                                    startActivity(new Intent(WalletActivity.this, QualificationInfoForNewUserActivity.class));//20170810修改为新的资质信息上传页


                                    startActivity(new Intent(WalletActivity.this, NewQualificationActivity.class));//20171023修改为新的资质信息上传页
                                }
                            })
                            .show();

                } else if (authenticationState.equals("未审核")) {
                    MyPromptDialogUtils.showPrompt(mContext, "您的资质认证信息正在审核中，请等待！");
                } else if (authenticationState.equals("审核未通过")) {
                    myDialogBuilder2 = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder2
                            .withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("您的资质认证信息未通过审核，请重新进行认证！")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder2.dismissNoAnimator();
//                                    startActivity(new Intent(WalletActivity.this, QualificationInfoActivity.class));
//                                    startActivity(new Intent(WalletActivity.this, QualificationInfoForNewUserActivity.class));//20170810修改为新的资质信息上传页


                                    startActivity(new Intent(WalletActivity.this, NewQualificationActivity.class));//20171023修改为新的资质信息上传页
                                }
                            })
                            .show();

                } else if (authenticationState.equals("审核通过")) {
//                    if (stationGroupPhotoState.equals("已上传")) {

                    if (stationLegalPersonState.equals("已上传")) {//20171023 判断油站法人状态，更为全面
                        if (isCanExtract.equals("是")) {
                            Intent intent = new Intent(this, TixianActivity.class);
                            intent.putExtra("typeflag", "manual");//手动或自动提取
                            intent.putExtra("extractType", extractType);//银行卡还是支付宝
                            startActivity(intent);
                        } else {
                            MToast.showToast(mContext, "功能正在优化中");
                        }

                    } else {
                        myDialogBuilder3 = MyDialogBuilder.getInstance(mContext);
                        myDialogBuilder3
                                .withIcon(R.mipmap.iconfont_gantanhao)
                                .withContene("您的资质认证信息未完善，需要补充上传资料！")
                                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialogBuilder3.dismissNoAnimator();

//                                        startActivity(new Intent(WalletActivity.this, QualificationInfoForOldUserActivity.class));//为老用户补充的资质信息上传页面

                                        startActivity(new Intent(WalletActivity.this, NewQualificationActivity.class));//20171023修改为新的资质信息上传页
                                    }
                                })
                                .show();

                    }

                }

                break;
        }
    }

}
