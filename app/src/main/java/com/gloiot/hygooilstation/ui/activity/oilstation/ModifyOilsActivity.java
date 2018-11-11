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
import com.gloiot.hygooilstation.utils.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 编辑油品
 */
public class ModifyOilsActivity extends BaseActivity implements View.OnClickListener {

    public static final int GET_MARKET = 1;
    public static final int GET_RANGLI = 2;

    private TextView tv_modifyoils_type, tv_modifyoils_ratio, tv_modifyoils_jiesuan, tv_modifyoils_comfirm, tv_modifyoils_delete;
    private EditText et_modifyoils_market, et_modifyoils_rangli;
    private MyDialogBuilder myDialogBuilder;
    private String originalStationId, originalType, originalMarket, originalYouhui, originalRatio;
    private String newMarket, newYouhui, newRatio;

    private String shi, rang;
    private String zhi, jiesuanzhi;
    private Double ranglibi, jiesuan;

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
                            tv_modifyoils_ratio.setText(ranglibi + "%");
                            tv_modifyoils_jiesuan.setText(jiesuan + "元/升");
                        } else {
                            tv_modifyoils_ratio.setText("");
                            tv_modifyoils_jiesuan.setText("");
                        }
                    } else {
                        tv_modifyoils_ratio.setText("");
                        tv_modifyoils_jiesuan.setText("");
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
                            tv_modifyoils_ratio.setText(ranglibi + "%");
                            tv_modifyoils_jiesuan.setText(jiesuan + "元/升");
                        } else {
                            tv_modifyoils_ratio.setText("");
                            tv_modifyoils_jiesuan.setText("");
                        }
                    } else {
                        tv_modifyoils_ratio.setText("");
                        tv_modifyoils_jiesuan.setText("");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_modify_oils;
    }

    @Override
    public void initComponent() {
        tv_modifyoils_type = (TextView) findViewById(R.id.tv_modifyoils_type);
        tv_modifyoils_ratio = (TextView) findViewById(R.id.tv_modifyoils_ratio);
        tv_modifyoils_jiesuan = (TextView) findViewById(R.id.tv_modifyoils_jiesuan);
        tv_modifyoils_comfirm = (TextView) findViewById(R.id.tv_modifyoils_comfirm);
        tv_modifyoils_delete = (TextView) findViewById(R.id.tv_modifyoils_delete);
        et_modifyoils_market = (EditText) findViewById(R.id.et_modifyoils_market);
        et_modifyoils_rangli = (EditText) findViewById(R.id.et_modifyoils_rangli);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, " 修改油品价格", "");
        Intent intent = getIntent();
        originalStationId = intent.getStringExtra("stationId");
        originalType = intent.getStringExtra("type");
        originalMarket = intent.getStringExtra("market");
        originalYouhui = intent.getStringExtra("youhui");
        originalRatio = intent.getStringExtra("ratio");

        shi = originalMarket;
        rang = originalYouhui;

        float shichang = Float.parseFloat(originalMarket);
        float rangli = Float.parseFloat(originalYouhui);
        String originalJiesuanzhi = (shichang - rangli) + "";
        double jieusuanDouble = Double.parseDouble(originalJiesuanzhi);
        BigDecimal b = new BigDecimal(jieusuanDouble);
        Double originalJiesuan = b.setScale(2, RoundingMode.HALF_UP).doubleValue();

        tv_modifyoils_type.setText(originalType);
        et_modifyoils_market.setText(originalMarket);
        et_modifyoils_rangli.setText(originalYouhui);
        tv_modifyoils_ratio.setText(originalRatio + "%");
        tv_modifyoils_jiesuan.setText(originalJiesuan + "元/升");
        tv_modifyoils_comfirm.setOnClickListener(this);
        tv_modifyoils_delete.setOnClickListener(this);

        //将编辑光标定位到文本最后
        String s = et_modifyoils_market.getText().toString();
        et_modifyoils_market.setSelection(s.length());

        et_modifyoils_market.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //小数点后位数控制
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 4) {//可以适当输入多位，位数会在最后计算时四舍五入
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 5);
                        et_modifyoils_market.setText(s);
                        et_modifyoils_market.setSelection(s.length());
                    }
                }
                //第一位为点
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_modifyoils_market.setText(s);
                    et_modifyoils_market.setSelection(2);
                }
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_modifyoils_market.setText(s.subSequence(0, 1));
                        et_modifyoils_market.setSelection(1);
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

        et_modifyoils_rangli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //小数点后位数控制
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 4) {//可以适当输入多位，位数会在最后计算时四舍五入
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 5);//小数点后最多输入4位
                        et_modifyoils_rangli.setText(s);
                        et_modifyoils_rangli.setSelection(s.length());
                    }
                }
                //第一位为点
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_modifyoils_rangli.setText(s);
                    et_modifyoils_rangli.setSelection(2);
                }
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_modifyoils_rangli.setText(s.subSequence(0, 1));
                        et_modifyoils_rangli.setSelection(1);
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
            case R.id.tv_modifyoils_comfirm:

                if (verification_modifyoils()) {
                    newMarket = et_modifyoils_market.getText().toString();
                    newYouhui = et_modifyoils_rangli.getText().toString();
                    newRatio = tv_modifyoils_ratio.getText().toString();
                    if (newMarket.equals(originalMarket) && newYouhui.equals(originalYouhui)) {
                        showPrompt("金额未更改");
                    } else {
                        requestHandleArrayList.add(requestAction.modifyoils(ModifyOilsActivity.this, originalStationId,
                                originalType, newMarket, newYouhui, newRatio.substring(0, newRatio.length() - 1)));
                    }
                }

                break;
            case R.id.tv_modifyoils_delete:
                finish();
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MODIFYOILS://编辑油品
                MToast.showToast(mContext, "修改油品价格成功");
                finish();
                break;
        }
    }

    //数据验证
    private Boolean verification_modifyoils() {
        if (TextUtils.isEmpty(et_modifyoils_market.getText().toString())) {
            showPrompt("请输入当前油品市场价");
            return false;
        } else if (TextUtils.isEmpty(et_modifyoils_rangli.getText().toString())) {
            showPrompt("请输入当前油品让利金额");
            return false;
        } else if (TextUtils.isEmpty(tv_modifyoils_ratio.getText().toString())) {
            showPrompt("油品市场价不能为零，请重新输入");
            return false;
        } else {
            return true;
        }
    }

    //弹出对话框根据输入内容给出提示信息
    private void showPrompt(String prompt) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder
                .withIcon(R.mipmap.iconfont_gantanhao)
                .withContene(prompt)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismissNoAnimator();
                    }
                }).show();
    }
}
