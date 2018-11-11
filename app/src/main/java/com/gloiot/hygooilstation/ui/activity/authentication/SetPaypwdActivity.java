package com.gloiot.hygooilstation.ui.activity.authentication;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.PwdInputView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SetPaypwdActivity extends BaseActivity {

    private TextView tv_paypwd_explain;
    private PwdInputView paypwd_pwd;
    private String mNewpaypwd, mConfirmpwd;
    private String scheduleflag;//进度标识
    private MyDialogBuilder myDialogBuilder;

    @Override
    public int initResource() {
        return R.layout.activity_set_paypwd;
    }

    @Override
    public void initComponent() {
        tv_paypwd_explain = (TextView) findViewById(R.id.tv_paypwd_explain);
        paypwd_pwd = (PwdInputView) findViewById(R.id.paypwd_pwd);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, false, "设置支付密码", "");
        tv_paypwd_explain.setText("请输入支付密码");
        scheduleflag = "new";
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

                if (scheduleflag.equals("new") && s.toString().length() == 6) {
                    mNewpaypwd = s.toString();
                    paypwd_pwd.setText("");
                    tv_paypwd_explain.setText("请再次输入支付密码");
                    scheduleflag = "confirm";
                } else if (scheduleflag.equals("confirm") && s.toString().length() == 6) {
                    mConfirmpwd = s.toString();
                    if (mConfirmpwd.equals(mNewpaypwd)) {
                        requestHandleArrayList.add(requestAction.setPaypwd(SetPaypwdActivity.this, MD5Util.Md5(mNewpaypwd), MD5Util.Md5(mConfirmpwd)));
                    } else {
                        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                        myDialogBuilder
                                .withIcon(R.mipmap.iconfont_gantanhao)
                                .withContene("两次输入的支付密码不一致，请重新输入")
                                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialogBuilder.dismissNoAnimator();
                                        paypwd_pwd.setText("");
                                        tv_paypwd_explain.setText("请输入支付密码");
                                        scheduleflag = "new";
                                        mNewpaypwd = "";
                                        mConfirmpwd = "";//把存储的内容清空
                                    }
                                }).show();

                    }

                }

            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SETPAYPWD:
                MToast.showToast(mContext, "设置支付密码成功");
//                String authenticationState = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_AUTHENTIFICATIONSTATE, "");//认证状态
//                if (authenticationState.equals("审核通过")){
//                    startActivity(new Intent(SetPaypwdActivity.this, MainActivity.class));
//                    finish();
//                }else {
//                    startActivity(new Intent(this, FinishAuthenticationActivity.class));
//                    finish();
//                }
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_PAYPWDSTATE, "已设置");//支付密码状态
                finish();

                break;
        }
    }
}
