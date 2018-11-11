package com.gloiot.hygooilstation.ui.activity.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gloiot.hygooilstation.App;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.MainActivity;
import com.gloiot.hygooilstation.ui.activity.login.LoginActivity;
import com.gloiot.hygooilstation.ui.activity.mine.wallet.ModifyPaypwdActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import static com.gloiot.hygooilstation.ui.activity.MainActivity.mBuilder;
import static com.gloiot.hygooilstation.ui.activity.MainActivity.mNotifyMgr;

/**
 * 我的--设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_setting_modifyLoginpwd, rl_setting_modifyPaypwd, rl_setting_logout;
    private MyDialogBuilder myDialogBuilder;
    public static Activity settingActivity;

    @Override
    public int initResource() {
        return R.layout.activity_setting;
    }

    @Override
    public void initComponent() {
        rl_setting_modifyLoginpwd = (RelativeLayout) findViewById(R.id.rl_setting_modifyLoginpwd);
        rl_setting_modifyPaypwd = (RelativeLayout) findViewById(R.id.rl_setting_modifyPaypwd);
        rl_setting_logout = (RelativeLayout) findViewById(R.id.rl_setting_logout);
    }

    @Override
    public void initData() {
        settingActivity = this;
        CommonUtlis.setTitleBar(this, true, "设置", "");
        rl_setting_modifyLoginpwd.setOnClickListener(this);
        rl_setting_modifyPaypwd.setOnClickListener(this);
        rl_setting_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_setting_modifyLoginpwd:
                startActivity(new Intent(this, ModifyLoginpwdActivity.class));
                break;
            case R.id.rl_setting_modifyPaypwd:
//                startActivity(new Intent(this, ModifyPaypwdFirstActivity.class));
//0210需求变更，修改支付密码不需要进行第一步账号手机号验证码的验证，直接进入原密码等的验证
                Intent intent = new Intent(this, ModifyPaypwdActivity.class);
                intent.putExtra("type", "修改");
                startActivity(intent);

                break;
            case R.id.rl_setting_logout:
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder
                        .withContene("您确定要退出登录吗")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_LOGINSTATE, "失败");//登录状态

                                if (mBuilder != null) {
                                    mNotifyMgr.cancel(001);
                                }
                                MainActivity.mainActivity.finish();//因为MainActivity没有加入到这个集合中，需手动finish.
                                App.getInstance().exit();//这里不要简单的finish，而应该退出整个应用。0323修改，注意一定要先finish MainActivity，在调用这个方法。

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
