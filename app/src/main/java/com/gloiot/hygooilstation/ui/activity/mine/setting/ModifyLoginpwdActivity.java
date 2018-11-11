package com.gloiot.hygooilstation.ui.activity.mine.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.App;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.MainActivity;
import com.gloiot.hygooilstation.ui.activity.login.LoginActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改登录密码
 */
public class ModifyLoginpwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_modifyLoginpwd_original, et_modifyLoginpwd_new, et_modifyLoginpwd_confirm;
    private TextView tv_modifyLoginpwd_commit;
    private String mOriginalpwd, mNewpwd, mConfirmpwd;

    @Override
    public int initResource() {
        return R.layout.activity_modify_loginpwd;
    }

    @Override
    public void initComponent() {
        et_modifyLoginpwd_original = (EditText) findViewById(R.id.et_modifyLoginpwd_original);
        et_modifyLoginpwd_new = (EditText) findViewById(R.id.et_modifyLoginpwd_new);
        et_modifyLoginpwd_confirm = (EditText) findViewById(R.id.et_modifyLoginpwd_confirm);
        tv_modifyLoginpwd_commit = (TextView) findViewById(R.id.tv_modifyLoginpwd_commit);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "修改登录密码", "");
        tv_modifyLoginpwd_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_modifyLoginpwd_commit:
                if (verification_modifyLoginpwd()) {
                    mOriginalpwd = MD5Util.Md5(et_modifyLoginpwd_original.getText().toString());
                    mNewpwd = MD5Util.Md5(et_modifyLoginpwd_new.getText().toString());
                    mConfirmpwd = MD5Util.Md5(et_modifyLoginpwd_confirm.getText().toString());
                    requestHandleArrayList.add(requestAction.modifyLoginpwd(this, mOriginalpwd, mNewpwd, mConfirmpwd));
                }
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MODIFYLOGINPWD:

                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_LOGINSTATE, "失败");
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_ISLOGINPWDRESET, "true");
                startActivity(new Intent(ModifyLoginpwdActivity.this, LoginActivity.class));
                MainActivity.mainActivity.finish();
                App.getInstance().exit();

                break;
        }
    }

    //数据验证
    private Boolean verification_modifyLoginpwd() {
        if (TextUtils.isEmpty(et_modifyLoginpwd_original.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "原登录密码不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_modifyLoginpwd_new.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入新登录密码");
            return false;
        } else if (et_modifyLoginpwd_new.getText().length() < 6) {
            MyPromptDialogUtils.showPrompt(mContext, "新登录密码不能少于六位");
            return false;
        } else if (TextUtils.isEmpty(et_modifyLoginpwd_confirm.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请再次输入新登录密码");
            return false;
        } else if (!et_modifyLoginpwd_confirm.getText().toString().equals(et_modifyLoginpwd_new.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "密码不一致，请重新输入");
            return false;
        } else {
            return true;
        }
    }

}
