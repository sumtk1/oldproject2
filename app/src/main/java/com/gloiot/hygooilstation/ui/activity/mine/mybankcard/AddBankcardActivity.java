package com.gloiot.hygooilstation.ui.activity.mine.mybankcard;

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
import com.gloiot.hygooilstation.ui.widget.MySelectPopupWindow;
import com.gloiot.hygooilstation.utils.ClickFilter;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MaxLengthWatcher;
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
 * 我的--我的银行卡--添加银行卡(和资质认证的认证银行卡一样，可以整合为一个)
 */
public class AddBankcardActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_certificateBankcard_selectbank;
    private EditText et_certificateBankcard_cardNum, et_certificateBankcard_paperNum, et_certificateBankcard_phoneNum;
    private EditText et_certificateBankcard_branchName, et_certificateBankcard_chikaName;
    private TextView tv_certificateBankcard_selectbank, tv_certificateBankcard_cardType, tv_qualification_commit;
    private String bank, branchName, cardNum, cardType, chikaName, paperNum, phoneNum;
    private MySelectPopupWindow selectWindow;
    private int selectPosition = -1;
    private List<String[]> banklist = new ArrayList<>();//银行列表数据
    private String account;

    @Override
    public int initResource() {
        return R.layout.activity_certificate_bankcard;
    }

    @Override
    public void initComponent() {
        rl_certificateBankcard_selectbank = (RelativeLayout) findViewById(R.id.rl_certificateBankcard_selectbank);
        et_certificateBankcard_branchName = (EditText) findViewById(R.id.et_certificateBankcard_branchName);
        et_certificateBankcard_cardNum = (EditText) findViewById(R.id.et_certificateBankcard_cardNum);
        et_certificateBankcard_chikaName = (EditText) findViewById(R.id.et_certificateBankcard_chikaName);
        et_certificateBankcard_paperNum = (EditText) findViewById(R.id.et_certificateBankcard_paperNum);
        et_certificateBankcard_phoneNum = (EditText) findViewById(R.id.et_certificateBankcard_phoneNum);
        tv_certificateBankcard_selectbank = (TextView) findViewById(R.id.tv_certificateBankcard_selectbank);
        tv_certificateBankcard_cardType = (TextView) findViewById(R.id.tv_certificateBankcard_cardType);
        tv_qualification_commit = (TextView) findViewById(R.id.tv_qualification_commit);
    }

    @Override
    public void initData() {
        account = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_USEACCOUNT, "");
        CommonUtlis.setTitleBar(this, true, "添加银行卡", "");
        if (!banklist.isEmpty()) {
            rl_certificateBankcard_selectbank.setOnClickListener(this);
        } else {
            requestHandleArrayList.add(requestAction.getBankName(this, account));
            rl_certificateBankcard_selectbank.setOnClickListener(this);
        }
        tv_qualification_commit.setOnClickListener(this);
        et_certificateBankcard_phoneNum.addTextChangedListener(new MaxLengthWatcher(11, et_certificateBankcard_phoneNum));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_certificateBankcard_selectbank:
                showSelectWindow();
                selectPosition = -1;//每次调用后重置
                break;
            case R.id.tv_qualification_commit:
                if (verification_certificationBankcard()) {

                    if (ClickFilter.filter()) {
                        return;
                    }

                    bank = tv_certificateBankcard_selectbank.getText().toString();
                    branchName = et_certificateBankcard_branchName.getText().toString();
                    cardNum = et_certificateBankcard_cardNum.getText().toString();
                    cardType = tv_certificateBankcard_cardType.getText().toString();
                    chikaName = et_certificateBankcard_chikaName.getText().toString();
                    paperNum = et_certificateBankcard_paperNum.getText().toString();
                    phoneNum = et_certificateBankcard_phoneNum.getText().toString();
                    requestHandleArrayList.add(requestAction.certificateBankcard(this, bank, cardNum, branchName, cardType, chikaName, paperNum, phoneNum));
                }
                break;
        }

    }

    //弹出选择框--选择银行
    private void showSelectWindow() {
        selectWindow = new MySelectPopupWindow(mContext, itemsOnClick, banklist);
        selectWindow.showAtLocation(findViewById(R.id.activity_certificate_bankcard), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ListView listView = selectWindow.setListViewSingle(mContext);
        final CommonAdapter commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_popupwindow_singletext, banklist) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_popsingletext_text, strings[1]);
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
        selectWindow.setTitle("选择银行");
    }

    //为弹出窗口实现监听类--选择银行
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectWindow.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.ll_popupWindow_cancel:
                    selectPosition = -1;
                    break;
                // 确认
                case R.id.tv_popupWindow_confirm:
                    if (selectPosition < 0) {
                        return;
                    }
                    String whichbank = banklist.get(selectPosition)[1].toString();
                    tv_certificateBankcard_selectbank.setText(whichbank);
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
            case RequestAction.TAG_BANKCARDNAME:
//                Log.e("银行卡名称",response.toString()+"-----");
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[3];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("银行名称");
                        a[2] = jsonObject.getString("类别");
                        banklist.add(a);
                    }
                }
                break;
            case RequestAction.TAG_CERTIFICATEBANKCARD:
                MToast.showToast(mContext, "信息提交成功，请等待后台审核");
                finish();
                break;
            default:
                break;
        }
    }

    //数据验证
    private Boolean verification_certificationBankcard() {
        if (TextUtils.isEmpty(tv_certificateBankcard_selectbank.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请选择银行");
            return false;
        } else if (TextUtils.isEmpty(et_certificateBankcard_branchName.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入开户行名称");
            return false;
        } else if (et_certificateBankcard_branchName.getText().toString().length() > 18) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的开户行名称有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_certificateBankcard_cardNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入银行卡号");
            return false;
        } else if (et_certificateBankcard_cardNum.getText().toString().length() < 16 || et_certificateBankcard_cardNum.getText().toString().length() > 19) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的银行卡号有误");
            return false;
        } else if (TextUtils.isEmpty(et_certificateBankcard_chikaName.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入持卡人姓名");
            return false;
        } else if (et_certificateBankcard_chikaName.getText().toString().length() > 8) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的持卡人姓名有误！");
            return false;
        } else if (TextUtils.isEmpty(et_certificateBankcard_paperNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入身份证号码");
            return false;
        } else if (et_certificateBankcard_paperNum.getText().toString().length() != 18) {
            MyPromptDialogUtils.showPrompt(mContext, "身份证号有误，请您确认后重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_certificateBankcard_phoneNum.getText().toString())) {
            MyPromptDialogUtils.showPrompt(mContext, "请输入银行预留的手机号");
            return false;
        } else if (et_certificateBankcard_phoneNum.getText().toString().length() != 11) {
            MyPromptDialogUtils.showPrompt(mContext, "您输入的银行预留的手机号有误");
            return false;
        } else {
            return true;
        }
    }

}
