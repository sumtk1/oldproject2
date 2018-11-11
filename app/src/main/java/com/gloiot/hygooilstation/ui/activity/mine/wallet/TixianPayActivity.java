package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.PwdInputView;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现支付
 */
public class TixianPayActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    private TextView tv_tixianpay_money;
    private ImageView iv_tixianpay_cancel;
    private PwdInputView paypwd_pwd;
    private String bankName = "", personName = "", bankcardNum = "", money = "", category = "", type = "";
    private String originalAlipayAccount = "", originalAlipayName = "", alipayAccount = "";
    private String extractType = "";//提取类别：bankcard/alipay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.bg_translucence));
    }

    @Override
    public int initResource() {
        return R.layout.activity_tixian_pay;
    }

    @Override
    public void initComponent() {
        iv_tixianpay_cancel = (ImageView) findViewById(R.id.iv_tixianpay_cancel);
        tv_tixianpay_money = (TextView) findViewById(R.id.tv_tixianpay_money);
        paypwd_pwd = (PwdInputView) findViewById(R.id.paypwd_pwd);
    }

    @Override
    public void initData() {

        paypwd_pwd.setOnClickListener(this);
        Intent intent = getIntent();
        extractType = intent.getStringExtra("extractType");
        category = intent.getStringExtra("category");
        if (category.equals("手动")) {
            money = intent.getStringExtra("money");
            type = intent.getStringExtra("type");
            if (extractType.equals("bankcard")) {
                bankName = intent.getStringExtra("bankName");
                personName = intent.getStringExtra("personName");
                bankcardNum = intent.getStringExtra("bankcardNum");
            } else if (extractType.equals("alipay")) {
                originalAlipayAccount = intent.getStringExtra("originalAlipayAccount");
                originalAlipayName = intent.getStringExtra("originalAlipayName");
                alipayAccount = intent.getStringExtra("alipayAccount");
            }
            tv_tixianpay_money.setText("￥" + money);
        } else if (category.equals("自动")) {

        }

        iv_tixianpay_cancel.setOnClickListener(this);

        setRequestErrorCallback(this);

        //设置是否显示密码
        paypwd_pwd.setDisplayPasswords(false);
        //设置方框的圆角度数
        paypwd_pwd.setRadiusBg(0);

        //设置监听内部输入的字符
        paypwd_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    String paypwd = s.toString();
                    //调接口去支付
                    if (category.equals("手动")) {
                        if (extractType.equals("bankcard")) {//提现到银行卡
                            requestHandleArrayList.add(requestAction.tixianManual(TixianPayActivity.this, money,
                                    MD5Util.Md5(paypwd), bankName, personName, bankcardNum, category, type));
                        } else if (extractType.equals("alipay")) {//提现到支付宝
                            requestHandleArrayList.add(requestAction.extractToAlipayAccount(TixianPayActivity.this, money,
                                    MD5Util.Md5(paypwd), originalAlipayAccount, originalAlipayName, category, type));
                        }
                    } else if (category.equals("自动")) {

                    }

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tixianpay_cancel:
                finish();
                break;
            case R.id.paypwd_pwd:
//                KeyboardUtil keyboardUtil = new KeyboardUtil(TixianPayActivity.this, true);
//                keyboardUtil.attachTo(paypwd_pwd);
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_TIXIANMANUAL://提取到银行卡
            case RequestAction.TAG_EXTRACTTOALIPAYACCOUNT://提取到支付宝
                L.e("手动提现到" + extractType, response.toString());
                String shouxufei = response.getString("手续费");
                Intent intent = new Intent(this, TixianFinishedActivity.class);
                intent.putExtra("extractType", extractType);
                if (extractType.equals("bankcard")) {
                    try {
                        intent.putExtra("bankName", bankName + " 尾号" + bankcardNum.substring(bankcardNum.length() - 4, bankcardNum.length()));
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        L.e("银行卡号", "角标越界");
                    }
                } else if (extractType.equals("alipay")) {
                    intent.putExtra("bankName", alipayAccount);
                }
                intent.putExtra("money", money);
                intent.putExtra("shouxufei", shouxufei);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_TIXIANMANUAL:
            case RequestAction.TAG_EXTRACTTOALIPAYACCOUNT:
                paypwd_pwd.setText("");
                MToast.showToast(mContext, response.getString("状态"));
                break;
            default:
                MToast.showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
