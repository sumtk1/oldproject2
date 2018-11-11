package com.gloiot.hygooilstation.ui.activity.mine.alipay;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.swipe.MySwipe;
import com.gloiot.hygooilstation.ui.widget.swipe.SwipeLayout;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的支付宝
 * Created by Dlt on 2017/12/23 15:00
 */
public class MyAliPayActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ListView mListView;

    private View footerView;
    private List<String[]> list = new ArrayList<String[]>();
    private CommonAdapter mAdapter;
    private MyDialogBuilder myDialogBuilder;
    private int deletePosition = -1;//记录删除的位置，初始化为-1

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.getAlipayAccountList(MyAliPayActivity.this));
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_ali_pay;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "我的支付宝", "");
        footerView = View.inflate(mContext, R.layout.layout_footer_add_alipay, null);
        mListView.addFooterView(footerView);
        mListView.setDividerHeight(0);
        mListView.setAdapter(mAdapter);
        //添加支付宝账号
        footerView.findViewById(R.id.tv_add_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyAliPayActivity.this, AddAlipayAccountActivity.class));
            }

        });
    }

    private void processData(Boolean isChanged) {

        if (isChanged) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CommonAdapter<String[]>(MyAliPayActivity.this, R.layout.item_alipay, list) {
                @Override
                public void convert(final ViewHolder holder, String[] strings) {

                    holder.setText(R.id.tv_alipay_name, strings[2]);

                    try {
                        if (CommonUtlis.isInteger(strings[1]) && strings[1].length() == 11) {//十一位整数，默认是手机号
                            holder.setText(R.id.tv_alipay_account, strings[1].substring(0, 3) + " **** " + strings[1].substring(strings[1].length() - 4, strings[1].length()));
                        } else if (strings[1].contains("@") && strings[1].contains(".") && strings[1].indexOf("@") < strings[1].indexOf(".")) {//邮箱---@前三位的字符全部隐藏
                            int index = strings[1].indexOf("@");
                            holder.setText(R.id.tv_alipay_account, "****" + strings[1].substring(index - 3, strings[1].length()));
                        } else {//其他情况
                            holder.setText(R.id.tv_alipay_account, "****" + strings[1].substring(strings[1].length() - 3, strings[1].length()));
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    final int p = holder.getmPosition();//选中的位置
                    Button btn_delete = holder.getView(R.id.btn_delete);
                    SwipeLayout s = (SwipeLayout) holder.getConvertView();
                    s.close(false, false);
                    s.getFrontView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    s.setSwipeListener(MySwipe.mSwipeListener);
                    btn_delete.setTag(p);
                    btn_delete.setOnClickListener(onActionClick);
                }
            };
            mListView.setAdapter(mAdapter);
        }
    }

    View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int p = (int) v.getTag();
            int id = v.getId();
            if (id == R.id.btn_delete) {   //解绑
                MySwipe.closeAllLayout();
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withTitie("解绑支付宝账号")
                        .withContene("确定解绑该支付宝账号吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                final MyDialogBuilder myDialogBuilder1;
                                myDialogBuilder1 = MyDialogBuilder.getInstance(mContext);
                                myDialogBuilder1.setCancelable(false);
                                myDialogBuilder1.withTitie("解绑支付宝账号")
                                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                        .setEtClick(1, null, "请输入登录密码", MyDialogBuilder.EtPwd)//注意选密码格式 EtPwd
                                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                EditText et_pwd = (EditText) myDialogBuilder1.getDialogView().findViewById(100 + 1);
//                                                et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);//密码应该不可见
                                                String pwd = MD5Util.Md5(et_pwd.getText().toString().trim());//密码需要加密
                                                L.e("删除支付宝账号", "密码=" + et_pwd.getText().toString().trim() + "，加密后=" + pwd);
                                                if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
                                                    MToast.showToast(mContext, "请输入密码");
                                                } else {
                                                    myDialogBuilder1.dismissNoAnimator();
                                                    String alipayAccount = list.get(p)[1];//支付宝账号
                                                    String id = list.get(p)[0];//支付宝id
                                                    deletePosition = p;

                                                    requestHandleArrayList.add(requestAction.removeAlipayAccount(MyAliPayActivity.this,
                                                            alipayAccount, pwd, id));
                                                }
                                            }
                                        })
                                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                myDialogBuilder1.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        })
                        .show();

            }

        }
    };

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_ALIPAYACCOUNTLIST:
                L.e("已绑定支付宝", response.toString());
                if (!list.isEmpty()) {
                    list.clear();
                    mAdapter.notifyDataSetChanged();
                }
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String[] s = new String[3];
                        s[0] = jsonObject.getString("id");
                        s[1] = jsonObject.getString("支付宝账号");
                        s[2] = jsonObject.getString("支付宝姓名");
                        list.add(s);
                    }
                    processData(false);
                } else {
                    L.e("已绑定的支付宝账号", "为空");
                }
                break;
            case RequestAction.TAG_DELETEALIPAYACCOUNT:
                MToast.showToast(mContext, response.getString("状态"));
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    processData(true);
                }
                deletePosition = -1;
                break;
            default:
                break;
        }
    }

}
