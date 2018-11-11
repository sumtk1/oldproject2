package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 添加油品
 */
public class AddOilsActivity extends BaseActivity implements View.OnClickListener {

    public static final int GET_MARKET = 1;
    public static final int GET_RANGLI = 2;

    private TextView tv_addoils_ratio, tv_addoils_comfirm, tv_addoils_jiesuan;
    private EditText et_addoils_market, et_addoils_rangli, et_addoils_type;
    private String stationId, stationName, type, market, youhui, ratio;

    private String shi = "", rang = "";
    private String zhi, jiesuanzhi;
    private Double ranglibi, jiesuan;

    private MyDialogBuilder myDialogBuilder;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_MARKET:
                    shi = (String) msg.obj;

                    if (!TextUtils.isEmpty(shi) && !TextUtils.isEmpty(rang)) {
                        float shichang = Float.parseFloat(shi);
                        float rangli = Float.parseFloat(rang);
                        if (shichang != 0) {
                            zhi = (rangli / shichang) * 100 + "";//让利
                            jiesuanzhi = (shichang - rangli) + "";//结算
                            double rangDouble = Double.parseDouble(zhi);
                            double jieusuanDouble = Double.parseDouble(jiesuanzhi);
                            BigDecimal b = new BigDecimal(rangDouble);
                            BigDecimal b1 = new BigDecimal(jieusuanDouble);
                            ranglibi = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            jiesuan = b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            tv_addoils_ratio.setText(ranglibi + "%");
                            tv_addoils_jiesuan.setText(jiesuan + "元/升");
                        } else {
                            tv_addoils_ratio.setText("");
                            tv_addoils_jiesuan.setText("");
                        }
                    } else {
                        tv_addoils_ratio.setText("");
                        tv_addoils_jiesuan.setText("");
                    }
                    break;
                case GET_RANGLI:
                    rang = (String) msg.obj;

                    if (!TextUtils.isEmpty(shi) && !TextUtils.isEmpty(rang)) {
                        float shichang = Float.parseFloat(shi);
                        float rangli = Float.parseFloat(rang);
                        if (shichang != 0) {
                            zhi = (rangli / shichang) * 100 + "";//让利
                            jiesuanzhi = (shichang - rangli) + "";//结算
                            double rangDouble = Double.parseDouble(zhi);
                            double jieusuanDouble = Double.parseDouble(jiesuanzhi);
                            BigDecimal b = new BigDecimal(rangDouble);
                            BigDecimal b1 = new BigDecimal(jieusuanDouble);
                            ranglibi = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            jiesuan = b1.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            tv_addoils_ratio.setText(ranglibi + "%");
                            tv_addoils_jiesuan.setText(jiesuan + "元/升");
                        } else {
                            tv_addoils_ratio.setText("");
                            tv_addoils_jiesuan.setText("");
                        }
                    } else {
                        tv_addoils_ratio.setText("");
                        tv_addoils_jiesuan.setText("");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_add_oils;
    }

    @Override
    public void initComponent() {
        tv_addoils_ratio = (TextView) findViewById(R.id.tv_addoils_ratio);
        tv_addoils_jiesuan = (TextView) findViewById(R.id.tv_addoils_jiesuan);
        tv_addoils_comfirm = (TextView) findViewById(R.id.tv_addoils_comfirm);
        et_addoils_type = (EditText) findViewById(R.id.et_addoils_type);
        et_addoils_market = (EditText) findViewById(R.id.et_addoils_market);
        et_addoils_rangli = (EditText) findViewById(R.id.et_addoils_rangli);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        stationId = intent.getStringExtra("stationId");
        stationName = intent.getStringExtra("stationName");
        CommonUtlis.setTitleBar(this, true, "添加油品", "");

        tv_addoils_comfirm.setOnClickListener(this);
        et_addoils_market.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //小数点后位数控制
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 4) {//可以适当输入多位，位数会在最后计算时四舍五入
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 5);
                        et_addoils_market.setText(s);
                        et_addoils_market.setSelection(s.length());
                    }
                }
                //第一位为点
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_addoils_market.setText(s);
                    et_addoils_market.setSelection(2);
                }
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_addoils_market.setText(s.subSequence(0, 1));
                        et_addoils_market.setSelection(1);
//                        return;
                    }
                }

                String shichangjia = s.toString();
                Message message = new Message();
                message.what = GET_MARKET;
                message.obj = shichangjia;
                mHandler.sendMessage(message);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_addoils_rangli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //小数点后位数控制
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 4) {//可以适当输入多位，位数会在最后计算时四舍五入
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 5);//小数点后最多输入4位
                        et_addoils_rangli.setText(s);
                        et_addoils_rangli.setSelection(s.length());
                    }
                }
                //第一位为点
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_addoils_rangli.setText(s);
                    et_addoils_rangli.setSelection(2);
                }
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_addoils_rangli.setText(s.subSequence(0, 1));
                        et_addoils_rangli.setSelection(1);
//                        return;
                    }
                }

                String ranglijia = s.toString();
                Message message = new Message();
                message.what = GET_RANGLI;
                message.obj = ranglijia;
                mHandler.sendMessage(message);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_addoils_comfirm:
                if (verification_addoils()) {
                    type = et_addoils_type.getText().toString();
                    market = et_addoils_market.getText().toString();
                    youhui = et_addoils_rangli.getText().toString();
                    ratio = tv_addoils_ratio.getText().toString();
                    requestHandleArrayList.add(requestAction.addoils(this, stationName, stationId, type + "#", market, youhui, ranglibi + ""));
                }
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_ADDOILS:
//                MToast.showToast(mContext, "数据提交成功，请等待后台审核");
//                finish();

                //已经提交成功，给提示请等待审核，然后点取消留在此页面？按需求来，管它是不是脑抽逻辑
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.setCancelable(false);//设置返回键不可点击
                myDialogBuilder
                        .withContene("数据提交成功，请等待后台审核")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                finish();
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                                et_addoils_market.setText("");
                                et_addoils_rangli.setText("");
                                et_addoils_type.setText("");
                            }
                        }).show();

                break;
            default:
                break;
        }
    }

    //数据验证
    private Boolean verification_addoils() {
        if (TextUtils.isEmpty(et_addoils_type.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入油品型号");
            return false;
        } else if (TextUtils.isEmpty(et_addoils_market.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入市场价");
            return false;
        } else if (TextUtils.isEmpty(et_addoils_rangli.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入让利金额");
            return false;
        } else if (TextUtils.isEmpty(tv_addoils_ratio.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "市场价不能为零，请重新输入");
            return false;
        } else {
            return true;
        }
    }

}
