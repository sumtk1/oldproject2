package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.TimeButton;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MaxLengthWatcher;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记支付密码
 */
public class ForgetPaypwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_forgetPaypwd_account, et_forgetPaypwd_IDNum, et_forgetPaypwd_phoneNum, et_forgetPaypwd_yzm;
    private TimeButton tv_forgetPaypwd_getyzm;
    private TextView tv_forgetPaypwd_next;
    private String mAccount, mIDNum, mPhoneNum, yzm;
    private String fromFlag;//从自动还是手动提现传过去的
    public static Activity forgetPaypwdActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_forgetPaypwd_getyzm.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_forget_paypwd;
    }

    @Override
    public void initComponent() {
        et_forgetPaypwd_account = (EditText) findViewById(R.id.et_forgetPaypwd_account);
        et_forgetPaypwd_IDNum = (EditText) findViewById(R.id.et_forgetPaypwd_IDNum);
        et_forgetPaypwd_phoneNum = (EditText) findViewById(R.id.et_forgetPaypwd_phoneNum);
        et_forgetPaypwd_yzm = (EditText) findViewById(R.id.et_forgetPaypwd_yzm);
        tv_forgetPaypwd_getyzm = (TimeButton) findViewById(R.id.tv_forgetPaypwd_getyzm);
        tv_forgetPaypwd_next = (TextView) findViewById(R.id.tv_forgetPaypwd_next);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "忘记支付密码", "");
        tv_forgetPaypwd_getyzm.setTextAfter("秒后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
        tv_forgetPaypwd_getyzm.setOnClickListener(this);
        tv_forgetPaypwd_next.setOnClickListener(this);

        forgetPaypwdActivity = this;

        Intent intent = getIntent();
        fromFlag = intent.getStringExtra("fromFlag");
        et_forgetPaypwd_phoneNum.addTextChangedListener(new MaxLengthWatcher(11, et_forgetPaypwd_phoneNum));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_forgetPaypwd_getyzm:
                mAccount = et_forgetPaypwd_account.getText().toString();
                mPhoneNum = et_forgetPaypwd_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPhoneNum) && mPhoneNum.length() == 11) {
                    tv_forgetPaypwd_getyzm.setCondition(true);
                    requestHandleArrayList.add(requestAction.sendyzm(this, mAccount, mPhoneNum));
                } else if (TextUtils.isEmpty(mAccount)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入账号");
                } else if (TextUtils.isEmpty(mPhoneNum)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入手机号");
                } else if (mPhoneNum.length() != 11) {
                    MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误，请重新输入");
                }
                break;
            case R.id.tv_forgetPaypwd_next:

                if (verification_forgetPaypwd()) {
                    mAccount = et_forgetPaypwd_account.getText().toString();
                    mIDNum = et_forgetPaypwd_IDNum.getText().toString();
                    mPhoneNum = et_forgetPaypwd_phoneNum.getText().toString();
                    yzm = et_forgetPaypwd_yzm.getText().toString();

                    requestHandleArrayList.add(requestAction.forgetPaypwd(this, mAccount, mIDNum, mPhoneNum, yzm));
                }

                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SENDYZM:
//                Log.e("验证码成功返回结果：", response + "");
                break;
            case RequestAction.TAG_FORGETPAYPWD:
                Intent intent = new Intent(this, ModifyPaypwdActivity.class);
                intent.putExtra("type", "重设");
                intent.putExtra("fromFlag", fromFlag);
                startActivity(intent);
                break;

        }
    }


    //页面数据验证
    private boolean verification_forgetPaypwd() {
        if (TextUtils.isEmpty(et_forgetPaypwd_account.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "账号不能为空，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPaypwd_IDNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "身份证号不能为空，请重新输入");
            return false;
        } else if (et_forgetPaypwd_IDNum.getText().length() != 18) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的身份证号有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPaypwd_phoneNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "负责人手机号不能为空，请重新输入");
            return false;
        } else if (et_forgetPaypwd_phoneNum.getText().length() != 11) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPaypwd_yzm.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "验证码不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        tv_forgetPaypwd_getyzm.onDestroy();
        super.onDestroy();
    }

}
