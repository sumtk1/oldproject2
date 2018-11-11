package com.gloiot.hygooilstation.ui.activity.mine.setting;

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
import com.gloiot.hygooilstation.ui.activity.mine.wallet.ModifyPaypwdActivity;
import com.gloiot.hygooilstation.ui.widget.TimeButton;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改支付密码第一步
 */
public class ModifyPaypwdFirstActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_modifypaypwd_first_account, et_modifypaypwd_first_phoneNum, et_modifypaypwd_first_yzm;
    private TimeButton tv_modifyLoginpwd_first_getyzm;
    private TextView tv_modifyLoginpwd_first_next;
    private String mAccount, mPhoneNum, yzm;
    public static Activity modifyPaypwdFirstActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_modifyLoginpwd_first_getyzm.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_modify_paypwd_first;
    }

    @Override
    public void initComponent() {
        et_modifypaypwd_first_account = (EditText) findViewById(R.id.et_modifypaypwd_first_account);
        et_modifypaypwd_first_phoneNum = (EditText) findViewById(R.id.et_modifypaypwd_first_phoneNum);
        et_modifypaypwd_first_yzm = (EditText) findViewById(R.id.et_modifypaypwd_first_yzm);
        tv_modifyLoginpwd_first_getyzm = (TimeButton) findViewById(R.id.tv_modifyLoginpwd_first_getyzm);
        tv_modifyLoginpwd_first_next = (TextView) findViewById(R.id.tv_modifyLoginpwd_first_next);
    }

    @Override
    public void initData() {
        modifyPaypwdFirstActivity = this;
        CommonUtlis.setTitleBar(this, true, "修改支付密码", "");
        tv_modifyLoginpwd_first_getyzm.setTextAfter("秒后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
        tv_modifyLoginpwd_first_getyzm.setOnClickListener(this);
        tv_modifyLoginpwd_first_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_modifyLoginpwd_first_getyzm:
                mAccount = et_modifypaypwd_first_account.getText().toString();
                mPhoneNum = et_modifypaypwd_first_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPhoneNum) && mPhoneNum.length() == 11) {
                    tv_modifyLoginpwd_first_getyzm.setCondition(true);
                    requestHandleArrayList.add(requestAction.sendyzm(this, mAccount, mPhoneNum));
                } else if (TextUtils.isEmpty(mAccount)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入账号");
                } else if (TextUtils.isEmpty(mPhoneNum)) {
                    MyPromptDialogUtils.showPrompt(mContext, "请输入手机号");
                } else if (mPhoneNum.length() != 11) {
                    MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误，请重新输入");
                }
                break;

            case R.id.tv_modifyLoginpwd_first_next:

                if (verification_modifyPaypwdfirst()) {
                    mAccount = et_modifypaypwd_first_account.getText().toString();
                    mPhoneNum = et_modifypaypwd_first_phoneNum.getText().toString();//重新获取一遍，防止获取验证码后又有更改，而上传错误
                    yzm = et_modifypaypwd_first_yzm.getText().toString();
                    requestHandleArrayList.add(requestAction.modifyPaypwd(this, mAccount, mPhoneNum, yzm));
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
            case RequestAction.TAG_MODIFYPAYPWD:
                Intent intent = new Intent(this, ModifyPaypwdActivity.class);
                intent.putExtra("type", "修改");
//                intent.putExtra("fromActivity", "forget");//从忘记支付密码页面进入
                startActivity(intent);
                break;
        }
    }

    //页面数据验证
    private boolean verification_modifyPaypwdfirst() {
        if (TextUtils.isEmpty(et_modifypaypwd_first_account.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_modifypaypwd_first_phoneNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "手机号不能为空");
            return false;
        } else if (et_modifypaypwd_first_phoneNum.getText().length() != 11) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误");
            return false;
        } else if (TextUtils.isEmpty(et_modifypaypwd_first_yzm.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入验证码");
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        tv_modifyLoginpwd_first_getyzm.onDestroy();
        super.onDestroy();
    }
}
