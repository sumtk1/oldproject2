package com.gloiot.hygooilstation.server.pay;

import android.content.Context;
import android.util.Log;

import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * Created by JinzLin on 2016/7/7.
 */
public class WXPay {

    private IWXAPI api;
    private String appId;
    private String payData;

    public WXPay(Context mContext, String payData){
        this.payData = payData;
        appId = ConstantUtlis.WXAPPID;
        api = WXAPIFactory.createWXAPI(mContext, appId);
        api.registerApp(appId);
    }

    public void startPay() {
        PayReq req = new PayReq();
        req.appId = appId;
        try {
            Log.e("WXPay",payData);
//            payData = "{\"appid\":\"wx8c55439fc255912a\",\"partnerid\":\"1339627701\",\"prepayid\":\"wx2016070814225335602539f00669456640\",\"noncestr\":\"SG0u9ZgmuFae46sM\",\"timestamp\":\"1467958970\",\"package\":\"Sign=WXPay\",\"sign\":\"E3224D491EB5F0E14C777DFDE3039954\",\"状态\":\"成功\"}";
//                        "{\"appid\":\"wx28e7af3ed58324ca\",\"partnerid\":\"1293334701\",\"prepayid\":\"wx20160709115923f67f7b19c30070335832\",\"noncestr\":\"y299hmyWTG6dK47I\",\"timestamp\":\"1468036757\",\"package\":\"Sign=WXPay\",\"sign\":\"0BB17F223398F3F36E33A82D675AB94D\",\"状态\":\"成功\"}"
            JSONObject jsonObject = new JSONObject(payData);
            req.partnerId = jsonObject.getString("partnerid");
            req.prepayId = jsonObject.getString("prepayid");
            req.nonceStr = jsonObject.getString("noncestr");
            req.timeStamp = jsonObject.getString("timestamp");
            req.packageValue = jsonObject.getString("package");
            req.sign = jsonObject.getString("sign");
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
