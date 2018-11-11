package com.gloiot.hygooilstation.ui.activity.trade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.githang.statusbar.StatusBarCompat;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收款
 */
public class GatheringActivity extends BaseActivity {

    private ImageView iv_erweima;
    private String type, personName, personPhonenum, stationId, stationName, stationPhonenum, stationLocation, stationCoordinate, stationIntro;
    private String erweimaInfo;
    public Bitmap mBitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.orange_FD853A));
    }

    @Override
    public int initResource() {
        return R.layout.activity_gathering;
    }

    @Override
    public void initComponent() {
        iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "收款", "");
        requestHandleArrayList.add(requestAction.erweima(this));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_ERWEIMA:
//                type = response.getString("类别");
//                personName = response.getString("负责人姓名");
//                personPhonenum = response.getString("负责人电话");
//                stationId = response.getString("油站id");
//                stationName = response.getString("油站名称");
//                stationPhonenum = response.getString("油站电话");
//                stationLocation = response.getString("油站地址");
//                stationCoordinate = response.getString("油站坐标");
//                stationIntro = response.getString("油站介绍");

                erweimaInfo = response.getString("二维码地");

//                Log.e("二维码", erweimaInfo + "---");

                if (TextUtils.isEmpty(erweimaInfo)) {
                    MToast.showToast(mContext, "返回无可用信息");
                    return;
                }

                Bitmap clubLogo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.clublogo_87);

                mBitmap = CodeUtils.createImage(erweimaInfo, 400, 400, clubLogo);
                iv_erweima.setImageBitmap(mBitmap);

                break;
        }
    }

}
