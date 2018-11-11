package com.gloiot.hygooilstation.ui.activity.mine.mybankcard;

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
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.MToast;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBankCardActivity extends BaseActivity {

    private ListView lv_myBankCard_listview;
    private View footerView;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mBankCardAdapter;
    private MyDialogBuilder myDialogBuilder;

    private int deletePosition = -1;//记录删除的位置，初始化为-1

    @Override
    protected void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.getMybankcards(this));
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_bank_card;
    }

    @Override
    public void initComponent() {
        lv_myBankCard_listview = (ListView) findViewById(R.id.lv_myBankCard_listview);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "我的银行卡", "");
        footerView = View.inflate(mContext, R.layout.layout_footer_add_bankcard, null);
        lv_myBankCard_listview.addFooterView(footerView);
        lv_myBankCard_listview.setDividerHeight(0);
        lv_myBankCard_listview.setAdapter(mBankCardAdapter);
        //添加银行卡
        footerView.findViewById(R.id.tv_add_bankcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyBankCardActivity.this, AddBankcardActivity.class));
            }

        });
    }

    private void processData(Boolean isChanged) {
        if (isChanged) {
            mBankCardAdapter.notifyDataSetChanged();
        } else {
            mBankCardAdapter = new CommonAdapter<String[]>(MyBankCardActivity.this, R.layout.item_my_mybankcard, list) {
                @Override
                public void convert(final ViewHolder holder, String[] strings) {
                    holder.setText(R.id.tv_bankCard_name, strings[3]);
//                    holder.setText(R.id.tv_bankCard_type, strings[3]);
                    if (strings[2].length() > 15) {
                        holder.setText(R.id.tv_bankCard_cardNum, "*** **** **** " + strings[2].substring(strings[2].length() - 4, strings[2].length()));//防止字符串角标越界
                    }
                    if (strings[3].equals("中国工商银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_in_com_bank);
                    } else if (strings[3].equals("中国建设银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_construction_bank);
                    } else if (strings[3].equals("中国交通银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_communications_bank);
                    } else if (strings[3].equals("中国农业银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_agri_bank);
                    } else if (strings[3].equals("中国银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_china_bank);
                    } else if (strings[3].equals("中国招商银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_merchants_bank);
                    } else if (strings[3].equals("中信银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.ic_citic_bank);
                    } else if (strings[3].equals("北京银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.beijingyinghang);
                    } else if (strings[3].equals("光大银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.guangdayinghang);
                    } else if (strings[3].equals("广发银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.guangfa_yinghang);
                    } else if (strings[3].equals("华夏银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.huaxiayinghang);
                    } else if (strings[3].equals("民生银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.mingshengyinghang);
                    } else if (strings[3].equals("平安银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.pinganyinghang);
                    } else if (strings[3].equals("上海银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.shanghaiyinhang);
                    } else if (strings[3].equals("兴业银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.xingye_yinghang);
                    } else if (strings[3].equals("邮储银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.youzhenghcuxuyinghang);
                    } else if (strings[3].equals("浦发银行")) {
                        holder.setImageResource(R.id.iv_bankCard_image, R.mipmap.pufayinghang);
                    }
                    final int p = holder.getmPosition();//选中的位置
                    Button btn_delete = holder.getView(R.id.btn_bankCard_delete);
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
            lv_myBankCard_listview.setAdapter(mBankCardAdapter);
        }

    }

    View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int p = (int) v.getTag();
            int id = v.getId();
            if (id == R.id.btn_bankCard_delete) {   //删除银行卡
                MySwipe.closeAllLayout();
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withTitie("删除银行卡")
                        .withContene("确定删除该银行卡吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                final MyDialogBuilder myDialogBuilder1;
                                myDialogBuilder1 = MyDialogBuilder.getInstance(mContext);
                                myDialogBuilder1.setCancelable(false);
                                myDialogBuilder1.withTitie("删除银行卡")
                                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                        .setEtClick(1, null, "请输入登录密码", MyDialogBuilder.EtPwd)//注意选密码格式 EtPwd
                                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                EditText et_pwd = (EditText) myDialogBuilder1.getDialogView().findViewById(100 + 1);
//                                                et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);//密码应该不可见
                                                String pwd = MD5Util.Md5(et_pwd.getText().toString().trim());//密码需要加密
//                                                Log.e("删除银行卡", "密码=" + et_pwd.getText().toString().trim() + "，加密后=" + pwd);
                                                if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
//                                                    Log.e("删除银行卡", "判断到密码为空");
                                                    MToast.showToast(mContext, "请输入密码");
                                                } else {
                                                    myDialogBuilder1.dismissNoAnimator();
                                                    String cardNum = list.get(p)[2];//卡号
                                                    deletePosition = p;
                                                    requestHandleArrayList.add(requestAction.deletebankcard(MyBankCardActivity.this, cardNum, pwd));
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
            case RequestAction.TAG_MYBANKCARDS:
                if (!list.isEmpty()) {
                    list.clear();
                    mBankCardAdapter.notifyDataSetChanged();
                }
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[5];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("银行卡姓名");
                        a[2] = jsonObject.getString("银行卡号");
                        a[3] = jsonObject.getString("银行名");
                        a[4] = jsonObject.getString("支行名称");
                        list.add(a);
                    }
                    processData(false);
                } else {
//                    tv_listview_no.setText("无数据");
                }
                break;
            case RequestAction.TAG_DELETEBANKCARD:
                MToast.showToast(mContext, "删除银行卡成功");//然后需要刷新一遍数据,不需要再次调接口
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    processData(true);
                }
                deletePosition = -1;
                break;
        }
    }

}
