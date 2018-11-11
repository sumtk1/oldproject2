package com.gloiot.hygooilstation.ui.activity.mine.alipay;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonInputUtils;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加支付宝账号
 * Created by Dlt on 2017/12/21 16:43
 */
public class AddAlipayAccountActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_alipay_name)
    TextView mTvAlipayName;
    @Bind(R.id.et_alipay_account)
    EditText mEtAlipayAccount;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;

    @Override
    public int initResource() {
        return R.layout.activity_add_alipay_account;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "添加支付宝账号", "");
        CommonInputUtils.filterBlank(mEtAlipayAccount);//过滤空格
        requestHandleArrayList.add(requestAction.getPrincipalName(AddAlipayAccountActivity.this));
    }

    @OnClick(R.id.tv_confirm)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (!TextUtils.isEmpty(mEtAlipayAccount.getText().toString().trim())) {
                    String alipayAccount = mEtAlipayAccount.getText().toString();
                    String alipayName = mTvAlipayName.getText().toString();
                    requestHandleArrayList.add(requestAction.bindingAlipayAccount(AddAlipayAccountActivity.this, alipayAccount, alipayName));
                } else {
                    MToast.showToast(mContext, "支付宝账号不能为空");
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_QUERYPRINCIPALNAME:
                L.e("负责人姓名", response.toString());
                String principalName = response.getString("负责人姓名");
                mTvAlipayName.setText(principalName);
                break;
            case RequestAction.TAG_BINDINGALIPAYACCOUNT:
                L.e("绑定支付宝账号", response.toString());
                MToast.showToast(mContext, "添加成功");
                finish();
                break;
            default:
                break;
        }
    }

}
