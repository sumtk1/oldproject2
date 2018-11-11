package com.gloiot.hygooilstation.ui.activity.authentication;

import android.content.Intent;
import android.view.View;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

/**
 * 资质认证--认证完成
 */
public class FinishAuthenticationActivity extends BaseActivity {

    private MyDialogBuilder myDialogBuilder;

    @Override
    public int initResource() {
        return R.layout.activity_finish_authentication;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
//        QualificationInfoActivity.contextQualificationInfo.finish();//使点击返回键回到登录页
        String authenticationState = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_AUTHENTIFICATIONSTATE, "");
        if (authenticationState.equals("审核未通过")) {
            myDialogBuilder = MyDialogBuilder.getInstance(mContext);
            myDialogBuilder
                    .withIcon(R.mipmap.iconfont_gantanhao)
                    .withContene("您的资质认证未通过审核，请重新填写相关信息")
                    .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                    .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialogBuilder.dismissNoAnimator();
                            startActivity(new Intent(FinishAuthenticationActivity.this, QualificationInfoActivity.class));
                            finish();
                        }
                    }).show();
        }

    }
}
