package com.gloiot.hygooilstation.ui.activity.mine.cashier;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteCashierActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_deleteCashier_name, tv_deleteCashier_account, tv_deleteCashier_phoneNum, tv_deleteCashier_commit;
    private String stationId, cashierAccount;
    private MyDialogBuilder myDialogBuilder;

    @Override
    public int initResource() {
        return R.layout.activity_delete_cashier;
    }

    @Override
    public void initComponent() {
        tv_deleteCashier_name = (TextView) findViewById(R.id.tv_deleteCashier_name);
        tv_deleteCashier_account = (TextView) findViewById(R.id.tv_deleteCashier_account);
        tv_deleteCashier_phoneNum = (TextView) findViewById(R.id.tv_deleteCashier_phoneNum);
        tv_deleteCashier_commit = (TextView) findViewById(R.id.tv_deleteCashier_commit);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "收银员管理", "");
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        Intent intent = getIntent();
        cashierAccount = intent.getStringExtra("account");
        tv_deleteCashier_account.setText(cashierAccount);
        tv_deleteCashier_name.setText(intent.getStringExtra("name"));
        tv_deleteCashier_phoneNum.setText(intent.getStringExtra("phoneNum"));
        tv_deleteCashier_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_deleteCashier_commit:
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                        .withContene("您确定删除当前收银员吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                requestHandleArrayList.add(requestAction.deleteCashier(DeleteCashierActivity.this, stationId, cashierAccount));
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_DELETECASHIER:
                MToast.showToast(mContext, "删除收银员成功");
                finish();
                break;
        }
    }
}
