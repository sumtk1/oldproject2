package com.gloiot.hygooilstation.ui.activity.trade;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.swipe.MySwipe;
import com.gloiot.hygooilstation.ui.widget.swipe.SwipeLayout;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 今日收益(用listview做，数据多时出现数据错乱)
 */
public class TodayearningActivity extends BaseActivity implements BaseActivity.RequestErrorCallback {

    private TextView tv_listview_no;
    //    private NumIncreaseTextView tv_todayearning_money;
    private TextView tv_todayearning_jiaoyi, tv_todayearning_daozhang;
    private ListView lv_todayearning_listview;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mTodayearningAdapter;
    private String totalMoney, daozhangMoney;
    private MyDialogBuilder myDialogBuilder;
    //    private int deletePosition = -1;//记录删除的位置，初始化为-1
    private String deleteOddNum;//删除位置的交易单号

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        list.clear();
//        requestHandleArrayList.add(requestAction.todayearning(this));
//    }

    @Override
    public int initResource() {
        return R.layout.activity_todayearning;
    }

    @Override
    public void initComponent() {
//        tv_todayearning_money = (NumIncreaseTextView) findViewById(R.id.tv_todayearning_money);
        tv_todayearning_jiaoyi = (TextView) findViewById(R.id.tv_todayearning_jiaoyi);
        tv_todayearning_daozhang = (TextView) findViewById(R.id.tv_todayearning_daozhang);
        lv_todayearning_listview = (ListView) findViewById(R.id.lv_todayearning_listview);
        tv_listview_no = (TextView) findViewById(R.id.tv_listview_no);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "今日收益", "");

        requestHandleArrayList.add(requestAction.todayearning(this));

        setRequestErrorCallback(this);

        //虚拟假数据，测试
//        String[] a = {"123467777777", "2016-12-24 11:05:10", "95.0", "100", "97#", "6.75"};
//        String[] a1 = {"123468888888", "2016-12-23 11:05:10", "95.0", "100", "97#", "6.75"};
//        list.add(a);
//        list.add(a1);
//        processData(false);

    }

    private void processData(Boolean isChanged) {
        if (isChanged) {
            mTodayearningAdapter.notifyDataSetChanged();
        } else {
            mTodayearningAdapter = new CommonAdapter<String[]>(TodayearningActivity.this, R.layout.item_trade_todayearning_withrevoke, list) {
                @Override
                public void convert(final ViewHolder holder, String[] strings) {

                    holder.setText(R.id.tv_oddNum, "交易单号：" + strings[0]);
                    holder.setText(R.id.tv_time, strings[1]);
                    holder.setText(R.id.tv_type, strings[4]);
                    holder.setText(R.id.tv_oilsPrice, strings[5] + "/L");
                    holder.setText(R.id.tv_jiaoyi, strings[2]);
                    holder.setText(R.id.tv_daozhang, strings[3]);

                    final int p = holder.getmPosition();//选中的位置
                    Button btn_delete = holder.getView(R.id.btn_revoke);
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
            lv_todayearning_listview.setAdapter(mTodayearningAdapter);

            //设置分隔线
            lv_todayearning_listview.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
            lv_todayearning_listview.setDividerHeight(16);

        }

    }

    View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int p = (int) v.getTag();
            int id = v.getId();
            if (id == R.id.btn_revoke) {   //撤单
                MySwipe.closeAllLayout();
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                        .withContene("确定要撤单吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();

                                deleteOddNum = list.get(p)[0];//商品订单号

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date curDate = new Date(System.currentTimeMillis());
                                String currentTime = formatter.format(curDate);//获取手机系统时间是个漏洞，用户可手动更改时间。

//                                formatter.setTimeZone(TimeZone.getTimeZone("GMT+08"));
//                                String currentTime = formatter.format(new Date());//获取东八区北京网络时间。。发现并没有用，只是在现在系统时间上加一个小时？

//                                deletePosition = p;
//                                Log.e("currentTime-", currentTime + "--");
                                requestHandleArrayList.add(requestAction.rongyunRevoke(TodayearningActivity.this, deleteOddNum, currentTime));

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
            case RequestAction.TAG_TODAYEARNING:

//                log.e("今日收益", "--" + response.toString());

                totalMoney = response.getString("总金额");
                daozhangMoney = response.getString("到账金额");
                tv_todayearning_jiaoyi.setText(totalMoney);
                tv_todayearning_daozhang.setText(daozhangMoney);

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[6];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("商品订单号");
                        a[1] = jsonObject.getString("录入时间");
                        a[2] = jsonObject.getString("交易金额");
                        a[3] = jsonObject.getString("到账金额");
                        a[4] = jsonObject.getString("油品型号");
                        a[5] = jsonObject.getString("市场价");
                        list.add(a);
                    }
                    processData(false);
                } else {
                    tv_listview_no.setText("无数据");
                }
                break;
            case RequestAction.TAG_RONGYUNREVOKE:

//                log.e("融云撤单", "--" + response.toString());

                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.setCancelable(false);
                myDialogBuilder.withTitie("撤单")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setEtClick(1, null, "请输入客户收到的验证码", MyDialogBuilder.EtNum)//注意选数字格式 EtNum
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText et_pwd = (EditText) myDialogBuilder.getDialogView().findViewById(100 + 1);
                                String yzm = et_pwd.getText().toString().trim();
                                if (TextUtils.isEmpty(yzm)) {
                                    MToast.showToast(mContext, "请输入验证码");
                                } else {
                                    myDialogBuilder.dismissNoAnimator();

                                    requestHandleArrayList.add(requestAction.revoke(TodayearningActivity.this, deleteOddNum, yzm));

                                }
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        })
                        .show();

                break;
            case RequestAction.TAG_REVOKE:
                MToast.showToast(mContext, "撤单成功");//然后需要刷新一遍数据,不需要再次调接口。因为头部除列表外有数据同步更新，所以还是要重新请求。
//                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
//                    list.remove(deletePosition);
//                    processData(true);
//                }
//                deletePosition = -1;

                list.clear();
                requestHandleArrayList.add(requestAction.todayearning(this));

                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_RONGYUNREVOKE:
                MyPromptDialogUtils.showPrompt(mContext, response.getString("状态"));
//                MToast.showToast(mContext, response.getString("状态"));
                break;
            default:
                MToast.showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
