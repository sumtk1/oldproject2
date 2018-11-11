package com.gloiot.hygooilstation.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, ConstantUtlis.WXAPPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String errCode = String.valueOf(resp.errCode);
            if (errCode.equals("0")) {
                MToast.showToast(WXPayEntryActivity.this, "支付成功");
            } else if (errCode.equals("-2")) {
                MToast.showToast(WXPayEntryActivity.this, "支付取消");
            } else {
                MToast.showToast(WXPayEntryActivity.this, "支付失败");
            }
            finish();
        }
    }
}