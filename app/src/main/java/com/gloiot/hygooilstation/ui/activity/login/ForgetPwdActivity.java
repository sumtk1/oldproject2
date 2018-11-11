package com.gloiot.hygooilstation.ui.activity.login;

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
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MaxLengthWatcher;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_forgetPwd_account, et_forgetPwd_phoneNum, et_forgetPwd_yzm, et_forgetPwd_newPwd, et_forgetPwd_confirmPwd;
    private TextView tv_confirm;
    private TimeButton tv_forgetPwd_getyzm;
    private String mAccount, mPhoneNum, yzm, newPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_forgetPwd_getyzm.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initComponent() {
        et_forgetPwd_account = (EditText) findViewById(R.id.et_forgetPwd_account);
        et_forgetPwd_phoneNum = (EditText) findViewById(R.id.et_forgetPwd_phoneNum);
        et_forgetPwd_yzm = (EditText) findViewById(R.id.et_forgetPwd_yzm);
        et_forgetPwd_newPwd = (EditText) findViewById(R.id.et_forgetPwd_newPwd);
        et_forgetPwd_confirmPwd = (EditText) findViewById(R.id.et_forgetPwd_confirmPwd);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_forgetPwd_getyzm = (TimeButton) findViewById(R.id.tv_forgetPwd_getyzm);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "忘记密码", "");
        tv_forgetPwd_getyzm.setTextAfter("秒后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
        tv_forgetPwd_getyzm.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        et_forgetPwd_phoneNum.addTextChangedListener(new MaxLengthWatcher(11, et_forgetPwd_phoneNum));//手机号只能输入11位
        et_forgetPwd_newPwd.addTextChangedListener(new MaxLengthWatcher(16, et_forgetPwd_newPwd));//新密码最多输入16位
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_forgetPwd_getyzm:
                mAccount = et_forgetPwd_account.getText().toString();
                mPhoneNum = et_forgetPwd_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPhoneNum) && mPhoneNum.length() == 11) {
                    tv_forgetPwd_getyzm.setCondition(true);
                    requestHandleArrayList.add(requestAction.sendyzm(this, mAccount, mPhoneNum));
                } else if (TextUtils.isEmpty(mAccount)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入账号");
                } else if (TextUtils.isEmpty(mPhoneNum)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入手机号");
                } else if (mPhoneNum.length() != 11) {
                    MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误，请重新输入");
                }
                break;
            case R.id.tv_confirm:
                if (forgetPwd_verification()) {
                    mAccount = et_forgetPwd_account.getText().toString();
                    mPhoneNum = et_forgetPwd_phoneNum.getText().toString();
                    yzm = et_forgetPwd_yzm.getText().toString();
                    newPwd = MD5Util.Md5(et_forgetPwd_newPwd.getText().toString());
                    requestHandleArrayList.add(requestAction.forgetPwd(this, mAccount, mPhoneNum, yzm, newPwd));
                }
                break;
            default:
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
            case RequestAction.TAG_FORGETPWD:
                MToast.showToast(mContext, "您的密码已重置");
//                Log.e("忘记密码成功返回结果：", response + "");
                Intent intent = new Intent(this, LoginActivity.class);
//                intent.putExtra("flag", flag);
                startActivity(intent);
//                LoginActivity.contextLogin.finish();
                finish();
                break;
        }
    }


    //页面数据验证
    private boolean forgetPwd_verification() {
        if (TextUtils.isEmpty(et_forgetPwd_account.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPwd_phoneNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "手机号不能为空");
            return false;
        } else if (et_forgetPwd_phoneNum.getText().length() != 11) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPwd_yzm.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPwd_newPwd.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "新的密码不能为空");
            return false;
        } else if (et_forgetPwd_newPwd.getText().length() < 6) {
            MyPromptDialogUtils.showPrompt(mContext, "新密码不能少于六位");
            return false;
        } else if (et_forgetPwd_newPwd.getText().length() > 16) {
            MyPromptDialogUtils.showPrompt(mContext, "新密码输入过长，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_forgetPwd_confirmPwd.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "确认密码不能为空");
            return false;
        } else if (!et_forgetPwd_newPwd.getText().toString().equals(et_forgetPwd_confirmPwd.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "密码不一致，请重新输入");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        tv_forgetPwd_getyzm.onDestroy();
        super.onDestroy();
    }
}
