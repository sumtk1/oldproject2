package com.gloiot.hygooilstation.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.authentication.SetPaypwdActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.MySelectPopupWindow;
import com.gloiot.hygooilstation.ui.widget.keyboard.KeyboardUtil;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的--钱包--提现
 */
public class TixianActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_tixian_selectbank, rl_tixian_money;
    private EditText et_tixian_money;
    private TextView tv_extract_type, tv_tixian_selectbank, tv_tixian_forgetPaypwd, tv_tixian_comfirm;
    private MySelectPopupWindow selectWindow;
    private int selectPosition = -1;
    private List<String[]> banklist = new ArrayList<>();//银行卡/支付宝列表数据
    private MyDialogBuilder myDialogBuilder1;
    private String typeflag;
    private String bankName, personName, bankcardNum, point;
    private String paypwdState;
    public static Activity tiXianActivity;
    private TextView tv_tixian_explain;
    private String isQuotaExtract = "";//是否定额提取
    private String quotaMoney = "";//定额金额
    private String extractType = "";//提取类别：bankcard/alipay
    private String originalAlipayAccount = "", originalAlipayName = "";
    private String alipayAccount = "";//支付宝账号（做字符隐藏处理后的字符串）

    @Override
    protected void onResume() {
        super.onResume();
        paypwdState = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_PAYPWDSTATE, "");//支付密码状态
        if (paypwdState.equals("未设置")) {
            tv_tixian_forgetPaypwd.setVisibility(View.GONE);
        } else {
            tv_tixian_forgetPaypwd.setVisibility(View.VISIBLE);
            tv_tixian_forgetPaypwd.setOnClickListener(this);
        }

        if (SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_ISPAYPWDRESET, "").equals("true")) {
            MToast.showToast(mContext, "设置支付密码成功");
            SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_ISPAYPWDRESET, "false");
        }
    }

    @Override
    public int initResource() {
        return R.layout.activity_tixian;
    }

    @Override
    public void initComponent() {
        tv_extract_type = (TextView) findViewById(R.id.tv_extract_type);
        rl_tixian_selectbank = (RelativeLayout) findViewById(R.id.rl_tixian_selectbank);
        rl_tixian_money = (RelativeLayout) findViewById(R.id.rl_tixian_money);
        et_tixian_money = (EditText) findViewById(R.id.et_tixian_money);
        tv_tixian_selectbank = (TextView) findViewById(R.id.tv_tixian_selectbank);
        tv_tixian_forgetPaypwd = (TextView) findViewById(R.id.tv_tixian_forgetPaypwd);
        tv_tixian_comfirm = (TextView) findViewById(R.id.tv_tixian_comfirm);
        tv_tixian_explain = (TextView) findViewById(R.id.tv_tixian_explain);
    }

    @Override
    public void initData() {

        tiXianActivity = this;

        Intent intent = getIntent();
        typeflag = intent.getStringExtra("typeflag");//手动/自动
        extractType = intent.getStringExtra("extractType");//银行卡/支付宝

        if (extractType.equals("bankcard")) {
            CommonUtlis.setTitleBar(this, true, "提现到银行卡", "");
            tv_extract_type.setText("选择银行卡");
            tv_tixian_selectbank.setHint("请选择绑定的银行卡");
        } else if (extractType.equals("alipay")) {
            CommonUtlis.setTitleBar(this, true, "提现到支付宝", "");
            tv_extract_type.setText("选择支付宝账号");
            tv_tixian_selectbank.setHint("请选择绑定的支付宝账号");
        }
        if (typeflag.equals("manual")) {
            rl_tixian_money.setVisibility(View.VISIBLE);
            et_tixian_money.setOnClickListener(this);
        } else if (typeflag.equals("automatic")) {
            rl_tixian_money.setVisibility(View.GONE);
        }

        if (!banklist.isEmpty()) {
            rl_tixian_selectbank.setOnClickListener(this);
        } else {
            if (extractType.equals("bankcard")) {
                requestHandleArrayList.add(requestAction.getMybankcards(this));
            } else if (extractType.equals("alipay")) {
                requestHandleArrayList.add(requestAction.getAlipayAccountList(this));
            }
            rl_tixian_selectbank.setOnClickListener(this);
        }
        tv_tixian_comfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_tixian_selectbank:
                if (!banklist.isEmpty()) {
                    showSelectWindow();
                    selectPosition = -1;//每次调用后重置
                } else {
                    if (extractType.equals("bankcard")) {
                        MyPromptDialogUtils.showPrompt(mContext, "您还没有绑定的银行卡!");
                    } else if (extractType.equals("alipay")) {
                        MyPromptDialogUtils.showPrompt(mContext, "您还没有绑定的支付宝账号!");
                    }
                }
                break;
            case R.id.tv_tixian_forgetPaypwd:

                Intent intent1 = new Intent(this, ForgetPaypwdActivity.class);
                intent1.putExtra("fromFlag", typeflag);//从自动还是手动提现传过去的
                startActivity(intent1);

                break;
            case R.id.et_tixian_money://提现金额
                if (isQuotaExtract.equals("是")) {//定额提取

                } else {//非定额提取
                    KeyboardUtil keyboardUtil = new KeyboardUtil(TixianActivity.this);
                    keyboardUtil.attachTo(et_tixian_money);
                }
                break;
            case R.id.tv_tixian_comfirm:
                if (paypwdState.equals("未设置")) {
                    myDialogBuilder1 = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder1
                            .withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("您还未设置支付密码")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder1.dismissNoAnimator();
                                    startActivity(new Intent(TixianActivity.this, SetPaypwdActivity.class));
                                }
                            })
                            .show();
                } else {
                    if (typeflag.equals("manual")) {
                        if (verification_tixian_manual()) {
                            String money = et_tixian_money.getText().toString();
                            Intent intent = new Intent(this, TixianPayActivity.class);
                            intent.putExtra("money", money);
                            intent.putExtra("extractType", extractType);
                            if (extractType.equals("bankcard")) {
                                intent.putExtra("bankName", bankName);
                                intent.putExtra("personName", personName);
                                intent.putExtra("bankcardNum", bankcardNum);
                            } else if (extractType.equals("alipay")) {
                                intent.putExtra("originalAlipayAccount", originalAlipayAccount);
                                intent.putExtra("originalAlipayName", originalAlipayName);
                                intent.putExtra("alipayAccount", alipayAccount);
                            }
                            intent.putExtra("category", "手动");
                            intent.putExtra("type", "手动");
                            startActivity(intent);
                        }
                    } else if (typeflag.equals("automatic")) {
                        if (verification_tixian_automatic()) {
//                        String money = et_tixian_money.getText().toString();
//                        Intent intent = new Intent(this, TixianPayActivity.class);
//                        intent.putExtra("money", money);
//                        startActivity(intent);
                        }
                    }
                }

                break;
        }

    }

    //弹出选择框--选择银行
    private void showSelectWindow() {
        selectWindow = new MySelectPopupWindow(mContext, itemsOnClick, banklist);
        selectWindow.showAtLocation(findViewById(R.id.activity_tixian), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ListView listView = selectWindow.setListViewSingle(mContext);
        final CommonAdapter commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_popupwindow_singletext, banklist) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                try {
                    if (extractType.equals("bankcard")) {
                        holder.setText(R.id.tv_popsingletext_text, strings[3] + "*** **** ****" +
                                strings[2].substring(strings[2].length() - 4, strings[2].length()));
                    } else if (extractType.equals("alipay")) {
                        if (CommonUtlis.isInteger(strings[1]) && strings[1].length() == 11) {//十一位整数，默认是手机号
                            holder.setText(R.id.tv_popsingletext_text, strings[1].substring(0, 3) + " **** " + strings[1].substring(strings[1].length() - 4, strings[1].length()));
                        } else if (strings[1].contains("@") && strings[1].contains(".") && strings[1].indexOf("@") < strings[1].indexOf(".")) {//邮箱---@前三位的字符全部隐藏
                            int index = strings[1].indexOf("@");
                            holder.setText(R.id.tv_popsingletext_text, "****" + strings[1].substring(index - 3, strings[1].length()));
                        } else {//其他情况
                            holder.setText(R.id.tv_popsingletext_text, "****" + strings[1].substring(strings[1].length() - 3, strings[1].length()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
            }
        });
        selectWindow.setMaxHeight(listView);
        if (extractType.equals("bankcard")) {
            selectWindow.setTitle("选择银行卡");
        } else if (extractType.equals("alipay")) {
            selectWindow.setTitle("选择支付宝账号");
        }
    }

    //为弹出窗口实现监听类--选择银行/支付宝账号
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectWindow.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.ll_popupWindow_cancel:
                    selectPosition = -1;
                    bankName = "";
                    personName = "";
                    bankcardNum = "";

                    originalAlipayAccount = "";
                    originalAlipayName = "";
                    alipayAccount = "";
                    break;
                // 确认
                case R.id.tv_popupWindow_confirm:
                    if (selectPosition < 0) {
                        return;
                    }
                    //操作数组下标时应该用try-catch处理。
                    if (extractType.equals("bankcard")) {
                        String cardNum = banklist.get(selectPosition)[2].toString();
                        if (cardNum.length() > 15) {
                            String whichbank = banklist.get(selectPosition)[3].toString() + "(" + cardNum.substring(cardNum.length() - 4, cardNum.length()) + ")";//判断位数，防止出异常崩掉
                            tv_tixian_selectbank.setText(whichbank);
                        } else {
                            String whichbank = banklist.get(selectPosition)[2].toString() + cardNum;//只做测试用
                            tv_tixian_selectbank.setText(whichbank);
                        }
                        bankName = banklist.get(selectPosition)[3].toString();
                        personName = banklist.get(selectPosition)[1].toString();
                        bankcardNum = banklist.get(selectPosition)[2].toString();
                    } else if (extractType.equals("alipay")) {
                        String originalAccount = banklist.get(selectPosition)[1].toString();

                        if (CommonUtlis.isInteger(originalAccount) && originalAccount.length() == 11) {//十一位整数，默认是手机号
                            alipayAccount = originalAccount.substring(0, 3) + " **** " + originalAccount.substring(originalAccount.length() - 4, originalAccount.length());
                        } else if (originalAccount.contains("@") && originalAccount.contains(".") && originalAccount.indexOf("@") < originalAccount.indexOf(".")) {//邮箱---@前三位的字符全部隐藏
                            int index = originalAccount.indexOf("@");
                            alipayAccount = "****" + originalAccount.substring(index - 3, originalAccount.length());
                        } else {//其他情况
                            alipayAccount = "****" + originalAccount.substring(originalAccount.length() - 3, originalAccount.length());
                        }
                        tv_tixian_selectbank.setText(alipayAccount);

                        originalAlipayAccount = banklist.get(selectPosition)[1].toString();//账号
                        originalAlipayName = banklist.get(selectPosition)[2].toString();//姓名
                    }
                    selectPosition = -1;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MYBANKCARDS://银行卡
            case RequestAction.TAG_ALIPAYACCOUNTLIST://支付宝
                L.e(extractType + "信息", response.toString());
                isQuotaExtract = response.getString("定额提取");
                quotaMoney = response.getString("定额金额");
                if (isQuotaExtract.equals("是")) {//定额提取
                    et_tixian_money.setFocusable(false);
                    et_tixian_money.setFocusableInTouchMode(false);
                    et_tixian_money.setText(quotaMoney);
                } else {//非定额提取
                    et_tixian_money.setFocusableInTouchMode(true);
                    et_tixian_money.setFocusable(true);
                    et_tixian_money.requestFocus();
                }
                String tishiMessage = response.getString("提现规则");
                String tishiMessageProcess = "";
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (extractType.equals("bankcard")) {
                            String[] a = new String[5];
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            a[0] = jsonObject.getString("id");
                            a[1] = jsonObject.getString("银行卡姓名");
                            a[2] = jsonObject.getString("银行卡号");
                            a[3] = jsonObject.getString("银行名");
                            a[4] = jsonObject.getString("支行名称");
                            banklist.add(a);
                        } else if (extractType.equals("alipay")) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String[] s = new String[3];
                            s[0] = jsonObject.getString("id");
                            s[1] = jsonObject.getString("支付宝账号");
                            s[2] = jsonObject.getString("支付宝姓名");
                            banklist.add(s);
                        }
                    }
                    tv_tixian_explain.setVisibility(View.VISIBLE);
                    if (tishiMessage.contains("<br>")) {
                        String[] tishiMessageArray = tishiMessage.split("<br>");
                        for (int i = 0; i < tishiMessageArray.length; i++) {
                            tishiMessageProcess += tishiMessageArray[i];
                        }
                        tv_tixian_explain.setText("说明：" + tishiMessageProcess);
                    } else {
                        tv_tixian_explain.setText("说明：" + tishiMessage);
                    }
                } else {
                    tv_tixian_explain.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    //数据验证--手动提现
    private Boolean verification_tixian_manual() {
        if (TextUtils.isEmpty(tv_tixian_selectbank.getText().toString())) {
            if (extractType.equals("bankcard")) {
                MyPromptDialogUtils.showPrompt(mContext, "请选择绑定的银行卡");
            } else if (extractType.equals("alipay")) {
                MyPromptDialogUtils.showPrompt(mContext, "请选择绑定的支付宝账号");
            }
            return false;
        } else if (TextUtils.isEmpty(et_tixian_money.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入要提取的金额");
            return false;
        } else {
            return true;
        }
    }

    //数据验证--自动提现
    private Boolean verification_tixian_automatic() {
        if (TextUtils.isEmpty(tv_tixian_selectbank.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请选择绑定的银行");
            return false;
        } else {
            return true;
        }
    }

}
