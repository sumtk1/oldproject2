package com.gloiot.hygooilstation.ui.activity.mine.cashier;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MaxLengthWatcher;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加收银员
 */
public class AddCashierActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_addCashier_name, et_addCashier_phoneNum, et_addCashier_pwd;
    private TextView tv_addCashier_confirm;

    @Override
    public int initResource() {
        return R.layout.activity_add_cashier;
    }

    @Override
    public void initComponent() {
        et_addCashier_name = (EditText) findViewById(R.id.et_addCashier_name);
        et_addCashier_phoneNum = (EditText) findViewById(R.id.et_addCashier_phoneNum);
        et_addCashier_pwd = (EditText) findViewById(R.id.et_addCashier_pwd);
        tv_addCashier_confirm = (TextView) findViewById(R.id.tv_addCashier_confirm);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "添加收银员", "");
        tv_addCashier_confirm.setOnClickListener(this);
        et_addCashier_phoneNum.addTextChangedListener(new MaxLengthWatcher(11, et_addCashier_phoneNum));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addCashier_confirm:
                if (verification_addCashier()) {
                    String stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
                    String name = et_addCashier_name.getText().toString();
                    String phoneNum = et_addCashier_phoneNum.getText().toString();
                    String pwd = et_addCashier_pwd.getText().toString();
                    requestHandleArrayList.add(requestAction.addCashier(this, stationId, name, phoneNum, pwd));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_ADDCASHIER:
                MToast.showToast(mContext, "添加收银员成功");
                finish();
                break;
        }
    }

    private Boolean verification_addCashier() {
        if (TextUtils.isEmpty(et_addCashier_name.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "收银员姓名不能为空");
            return false;
        } else if (et_addCashier_name.getText().toString().length() > 8) {
            MyPromptDialogUtils.showPrompt(mContext, "输入收银员姓名有误");
            return false;
        } else if (TextUtils.isEmpty(et_addCashier_phoneNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "手机号不能为空");
            return false;
        } else if (et_addCashier_phoneNum.getText().toString().length() != 11) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的手机号有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_addCashier_pwd.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "密码不能为空");
            return false;
        } else if (et_addCashier_pwd.getText().toString().length() < 6) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的密码不能少于六位");
            return false;
        } else if (et_addCashier_pwd.getText().toString().length() > 16) {
            MyPromptDialogUtils.showPrompt(mContext, "密码输入过长");
            return false;
        } else {
            return true;
        }
    }

}
