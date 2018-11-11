package com.gloiot.hygooilstation.ui.activity.mine;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的---意见反馈
 */
public class SuggestionFeedbackActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup rg_suggestion_style;
    private EditText et_suggestion_tellme;
    private TextView tv_suggestion_num;
    private TextView tv_suggestion_commit;
    private int sum = 200;
    private int type;//radiogroup选中的位置
    private String suggestiongType;
    private MyDialogBuilder myDialogBuilder;

    @Override
    public int initResource() {
        return R.layout.activity_suggestion_feedback;
    }

    @Override
    public void initComponent() {
        rg_suggestion_style = (RadioGroup) findViewById(R.id.rg_suggestion_style);
        et_suggestion_tellme = (EditText) findViewById(R.id.et_suggestion_tellme);
        tv_suggestion_num = (TextView) findViewById(R.id.tv_suggestion_num);
        tv_suggestion_commit = (TextView) findViewById(R.id.tv_suggestion_commit);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "意见反馈", "");
        tv_suggestion_commit.setOnClickListener(this);
        numLimit();

        //反馈状态选择  1-功能异常  2-其他问题
        rg_suggestion_style.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_suggestion_function:
                        type = 1;
                        break;
                    case R.id.rb_suggestion_other:
                        type = 2;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_suggestion_commit:
                if (type != 1 && type != 2) {
                    MToast.showToast(mContext, "请选择反馈类型");
                } else if (TextUtils.isEmpty(et_suggestion_tellme.getText().toString())) {
                    MToast.showToast(mContext, "反馈内容不能为空");
                } else {
                    myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder
                            .withTitie("提示")
                            .withContene("是否提交反馈？")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onCommit();
                                    myDialogBuilder.dismissNoAnimator();
                                }
                            })
                            .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismiss();
                                }
                            }).show();
                }

                break;
            default:
                break;
        }

    }

    private void onCommit() {
        String suggestions = et_suggestion_tellme.getText().toString();
//        Log.e("意见反馈", "type--" + type);
        if (type == 1) {
            suggestiongType = "功能异常";
        } else if (type == 2) {
            suggestiongType = "其他问题";
        }
        requestHandleArrayList.add(requestAction.suggestionFeedback(this, suggestiongType, suggestions));
    }

    private void numLimit() {
        et_suggestion_tellme.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_suggestion_num.setText(s.length() + "");
                selectionStart = et_suggestion_tellme.getSelectionStart();
                selectionEnd = et_suggestion_tellme.getSelectionEnd();
                if (temp.length() > sum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_suggestion_tellme.setText(s);
                    et_suggestion_tellme.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SUGGESTIONFEEDBACK:
                MToast.showToast(mContext, "提交成功");
                finish();
                break;
        }
    }

}
