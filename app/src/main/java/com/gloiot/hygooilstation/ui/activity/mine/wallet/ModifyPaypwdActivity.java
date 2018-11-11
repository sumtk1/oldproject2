package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.mine.setting.SettingActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.PwdInputView;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改/重设支付密码
 */
public class ModifyPaypwdActivity extends BaseActivity implements BaseActivity.RequestErrorCallback {

    private TextView tv_paypwd_explain, tv_showpwd;
    private PwdInputView paypwd_pwd;
    private String mOriginalpwd, mNewpaypwd, mConfirmpwd;
    private String typeflag;//类型标识
    private String scheduleflag;//进度标识
    private String fromFlag;//当类型标识为重设时，需要判断从自动提现还是手动提现页面进去的
    private String fromActivity;//从哪个页面进入的，从而决定设置成功后要finish掉哪几个页面
    private MyDialogBuilder myDialogBuilder;

    @Override
    public int initResource() {
        return R.layout.activity_modify_paypwd;
    }

    @Override
    public void initComponent() {
        tv_paypwd_explain = (TextView) findViewById(R.id.tv_paypwd_explain);
        tv_showpwd = (TextView) findViewById(R.id.tv_showpwd);
        paypwd_pwd = (PwdInputView) findViewById(R.id.paypwd_pwd);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        typeflag = intent.getStringExtra("type");
//        fromActivity = intent.getStringExtra("fromActivity");//暂时有两个取值：forget，setting

        if (typeflag.equals("重设")) {
            fromFlag = intent.getStringExtra("fromFlag");
            CommonUtlis.setTitleBar(this, true, "重设支付密码", "");
            tv_paypwd_explain.setText("请输入新的支付密码");
            scheduleflag = "new";
        } else if (typeflag.equals("修改")) {
            CommonUtlis.setTitleBar(this, true, "修改支付密码", "");
            tv_paypwd_explain.setText("请输入原支付密码");
            scheduleflag = "original";
        }

        setRequestErrorCallback(this);

        //设置是否显示密码
        paypwd_pwd.setDisplayPasswords(false);
        //设置方框的圆角度数
        paypwd_pwd.setRadiusBg(0);

        //设置监听内部输入的字符
        paypwd_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.e("支付密码beforeTextChanged", "start-" + start + ",after-" + after + ",count-" + count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tv_showpwd.setText(s.toString());
//                Log.e("支付密码onTextChanged", "start-" + start + ",before-" + before + ",count-" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.e("支付密码afterTextChanged", "Editable-" + s);

                if (typeflag.equals("重设")) {
                    if (scheduleflag.equals("new") && s.toString().length() == 6) {
                        mNewpaypwd = s.toString();
                        paypwd_pwd.setText("");
                        tv_paypwd_explain.setText("请再次填写以确认");
                        scheduleflag = "confirm";
                    } else if (scheduleflag.equals("confirm") && s.toString().length() == 6) {
                        mConfirmpwd = s.toString();
                        if (mConfirmpwd.equals(mNewpaypwd)) {
                            requestHandleArrayList.add(requestAction.setPaypwd(ModifyPaypwdActivity.this, MD5Util.Md5(mNewpaypwd), MD5Util.Md5(mConfirmpwd)));
                        } else {
                            myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                            myDialogBuilder
                                    .withIcon(R.mipmap.iconfont_gantanhao)
                                    .withContene("两次输入的新密码不一致，请重置")
                                    .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                    .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            myDialogBuilder.dismissNoAnimator();
                                            paypwd_pwd.setText("");
                                            tv_paypwd_explain.setText("请输入新的支付密码");
                                            scheduleflag = "new";
                                            mNewpaypwd = "";
                                            mConfirmpwd = "";//把存储的内容清空
                                        }
                                    }).show();

                        }

                    }
                } else if (typeflag.equals("修改")) {
                    if (scheduleflag.equals("original") && s.toString().length() == 6) {
                        mOriginalpwd = MD5Util.Md5(s.toString());
                        requestHandleArrayList.add(requestAction.istrueOriginalpaypwd(ModifyPaypwdActivity.this, mOriginalpwd));//判断原支付密码是否正确
                    } else if (scheduleflag.equals("new") && s.toString().length() == 6) {
                        mNewpaypwd = s.toString();
                        paypwd_pwd.setText("");
                        tv_paypwd_explain.setText("请再次填写以确认");
                        scheduleflag = "confirm";
                    } else if (scheduleflag.equals("confirm") && s.toString().length() == 6) {
                        mConfirmpwd = s.toString();
                        if (mConfirmpwd.equals(mNewpaypwd)) {
                            requestHandleArrayList.add(requestAction.isequalNewAndConfirmPwd(ModifyPaypwdActivity.this, MD5Util.Md5(mNewpaypwd),
                                    MD5Util.Md5(mConfirmpwd)));
                        } else {
                            myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                            myDialogBuilder
                                    .withIcon(R.mipmap.iconfont_gantanhao)
                                    .withContene("两次输入的密码不一致，请重置")
                                    .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                    .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            myDialogBuilder.dismissNoAnimator();
                                            paypwd_pwd.setText("");
                                            tv_paypwd_explain.setText("请输入新的支付密码");
                                            scheduleflag = "new";
                                        }
                                    }).show();

                        }

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
//                MToast.showToast(mContext, "设置支付密码成功");
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_ISPAYPWDRESET, "true");

                if (typeflag.equals("重设")) {
                    ForgetPaypwdActivity.forgetPaypwdActivity.finish();
                    TixianActivity.tiXianActivity.finish();
                    Intent intent = new Intent(this, TixianActivity.class);
                    intent.putExtra("typeflag", fromFlag);
                    startActivity(intent);
                } else if (typeflag.equals("修改")) {

                }

                finish();
                break;
            case RequestAction.TAG_ISTRUEORIGINALPAYPWD://判断原支付密码是否正确
                paypwd_pwd.setText("");
                tv_paypwd_explain.setText("请输入新支付密码");
                scheduleflag = "new";
                break;
            case RequestAction.TAG_ISEQUALNEWANDCONFIRMPWD://新密码和确认密码是否相同
                MToast.showToast(mContext, "修改支付密码成功");
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_ISTRUEORIGINALPAYPWD:
                paypwd_pwd.setText("");
                MToast.showToast(mContext, response.getString("状态"));
                break;
            default:
                MToast.showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
