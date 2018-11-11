package com.gloiot.hygooilstation.ui.activity.trade;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.uiview.TopMenuWindow;
import com.gloiot.hygooilstation.ui.widget.NumIncreaseTextView;
import com.gloiot.hygooilstation.ui.widget.PopUpFragment;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 交易--统计
 */
public class StatisticsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_statistics_stationName, tv_statistics_year, tv_statistics_stationTotalEarning;
    private ImageView iv_statistics_selectYear;
    private ListView lv_statistics_listview;
    private String stationId, stationName;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter mStatisticsAdapter;

    private PopupWindow mPopWindow;

    private static final int REQUEST_TO_TOP_MENU = 1;

    private static final String TAG = "StatisticsActivity";

    private String[] yearsList;

    private List<String[]> yearsArrayList = new ArrayList<>();

    private String selectYear;//当前页面显示的年份

    @Override
    public int initResource() {
        return R.layout.activity_statistics;
    }

    @Override
    public void initComponent() {
        tv_statistics_stationName = (TextView) findViewById(R.id.tv_statistics_stationName);
        tv_statistics_year = (TextView) findViewById(R.id.tv_statistics_year);
        tv_statistics_stationTotalEarning = (TextView) findViewById(R.id.tv_statistics_stationTotalEarning);
        iv_statistics_selectYear = (ImageView) findViewById(R.id.iv_statistics_selectYear);
        lv_statistics_listview = (ListView) findViewById(R.id.lv_statistics_listview);
    }

    @Override
    public void initData() {
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        stationName = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONNAME, "");
        tv_statistics_stationName.setText(stationName);

        Calendar c = Calendar.getInstance();//获取日历对象
        selectYear = c.get(Calendar.YEAR) + "";//初始化为系统当前年份

        tv_statistics_year.setText(selectYear + "年交易额：");

        requestHandleArrayList.add(requestAction.tradetongji(this, selectYear));
        CommonUtlis.setTitleBar(this, true, "统计", "");
        iv_statistics_selectYear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_statistics_selectYear:

//                showPopupWindow();

//                showTopMenu();

                if (yearsArrayList.isEmpty()) {
                    requestHandleArrayList.add(requestAction.getStatisticsYears(this));
                } else {
                    showStatisticsYearsWindow();
                }
                break;

//            case R.id.tv_2016:
//
//                MToast.showToast(mContext, "2016!");
//                mPopWindow.dismiss();
//
//                break;
//            case R.id.tv_2015:
//                MToast.showToast(mContext, "2015!");
//                mPopWindow.dismiss();
//                break;
//            case R.id.tv_2014:
//                MToast.showToast(mContext, "2014!");
//                mPopWindow.dismiss();
//                break;
            default:
                break;
        }
    }

    /**
     * 显示顶部菜单
     */
    private void showTopMenu() {
//        toActivity(TopMenuWindow.createIntent(mContext, new String[]{"2016年", "2015年", "2014年"}), REQUEST_TO_TOP_MENU, false);
        toActivity(TopMenuWindow.createIntent(mContext, yearsList), REQUEST_TO_TOP_MENU, false);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {

//        Log.e(" startActivityForResult","00000000-----");

        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (intent == null) {
                    Log.e(TAG, "toActivity  intent == null >> return;");
                    return;
                }
                //fragment中使用context.startActivity会导致在fragment中不能正常接收onActivityResult
                if (requestCode < 0) {
                    startActivity(intent);
                } else {
                    startActivityForResult(intent, requestCode);

//                    Log.e(" startActivityForResult","33333333-----");
                }
                if (showAnimation) {
//                    overridePendingTransition(R.anim.right_push_in, R.anim.hold);
                } else {
//                    overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
                }
            }
        });
    }


    public final boolean isAlive() {
//        return isAlive && context != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
        return true;
    }

    /**
     * 在UI线程中运行，建议用这个方法代替runOnUiThread
     *
     * @param action
     */
    public final void runUiThread(Runnable action) {
        if (isAlive() == false) {
            Log.e(TAG, "runUiThread  isAlive() == false >> return;");
            return;
        }
        runOnUiThread(action);
    }

    private void showPopupWindow() {

        View contentView = LayoutInflater.from(StatisticsActivity.this).inflate(R.layout.layout_selectyear_popupwindow, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv1 = (TextView) contentView.findViewById(R.id.tv_2016);
        TextView tv2 = (TextView) contentView.findViewById(R.id.tv_2015);
        TextView tv3 = (TextView) contentView.findViewById(R.id.tv_2014);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        //外部是否可以点击
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);

        mPopWindow.showAsDropDown(iv_statistics_selectYear);

    }

    private void processData() {
        mStatisticsAdapter = new CommonAdapter<String[]>(StatisticsActivity.this, R.layout.item_trade_tongji, list) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_month, change(strings[0]) + "月收入");
//                holder.setText(R.id.tv_money, strings[1]);

                NumIncreaseTextView tv_statistics_money = holder.getView(R.id.tv_money);
                float moneyFloat = Float.parseFloat(strings[1]);
                tv_statistics_money.setDuration(1000);
                tv_statistics_money.setNumber(moneyFloat);
                tv_statistics_money.showNumberWithAnimation();
            }
        };
        lv_statistics_listview.setAdapter(mStatisticsAdapter);
        //设置分隔线
        lv_statistics_listview.setDivider(new ColorDrawable(Color.rgb(243, 243, 243)));//#F3F3F3
        lv_statistics_listview.setDividerHeight(30);

        lv_statistics_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(StatisticsActivity.this, StatisticsDetailActivity.class);
                intent.putExtra("month", list.get(position)[0]);
                intent.putExtra("year", selectYear);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case REQUEST_TO_TOP_MENU:
                if (data != null) {
                    switch (data.getIntExtra(TopMenuWindow.RESULT_POSITION, -1)) {
                        case 0:
                            MToast.showToast(mContext, "2016年");
                            break;
                        case 1:
                            MToast.showToast(mContext, "2015年");
                            break;
                        case 2:
                            MToast.showToast(mContext, "2014年");
                            break;
                        case 3:
                            MToast.showToast(mContext, "2013年");
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;

        }
    }

    public String change(String s) {
        switch (s) {
            case "1":
                return "一";
            case "2":
                return "二";
            case "3":
                return "三";
            case "4":
                return "四";
            case "5":
                return "五";
            case "6":
                return "六";
            case "7":
                return "七";
            case "8":
                return "八";
            case "9":
                return "九";
            case "10":
                return "十";
            case "11":
                return "十一";
            case "12":
                return "十二";
            default:
                return s;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_TRADETONGJI:
//                Log.e("统计", response.toString());

                selectYear = response.getString("年份");
                tv_statistics_year.setText(selectYear + "年交易额：");

                String jiaoyie = response.getString("交易额");
                tv_statistics_stationTotalEarning.setText(jiaoyie);

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[2];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("月份");
                        a[1] = jsonObject.getString("金额");
                        list.add(a);
                    }
                    processData();
                }
                break;

            case RequestAction.TAG_STATISTICSYEARS:

//                Log.e("统计年份列表", response.toString() + "---");

                JSONArray yearsArray = response.getJSONArray("年份列表");

                String b = "";

                int c = yearsArray.length();

                yearsList = new String[c];

                for (int i = 0; i < yearsArray.length(); i++) {

                    b = yearsArray.get(i) + "";

                    yearsList[i] = b;

                    String[] z = new String[1];
                    z[0] = b;
                    yearsArrayList.add(z);

                }

                for (int y = 0; y < yearsArrayList.size(); y++) {

//                    Log.e("yearsArrayList", yearsArrayList.get(y)[0] + "--" + y);

                }

//                showTopMenu();

                showStatisticsYearsWindow();


                break;
            default:
                break;
        }
    }

    private void showStatisticsYearsWindow() {

        PopUpFragment.build(StatisticsActivity.this.getSupportFragmentManager())
                .setGetListCallBack(new PopUpFragment.GetListCallBack() {
                    @Override
                    public List<String[]> getList() {
                        return yearsArrayList;
                    }
                })
                .setListviewItemClickListener(new PopUpFragment.ItemClickListenerCallBack() {
                    @Override
                    public void addOnItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (yearsArrayList.get(position)[0]) {
                            case "2014":
                                list.clear();
                                requestHandleArrayList.add(requestAction.tradetongji(StatisticsActivity.this, "2014"));
                                break;
                            case "2015":
                                list.clear();
                                requestHandleArrayList.add(requestAction.tradetongji(StatisticsActivity.this, "2015"));
                                break;
                            case "2016":
                                list.clear();
                                requestHandleArrayList.add(requestAction.tradetongji(StatisticsActivity.this, "2016"));
                                break;
                            case "2017":
                                list.clear();
                                requestHandleArrayList.add(requestAction.tradetongji(StatisticsActivity.this, "2017"));
                                break;
                            case "2018":
                                list.clear();
                                requestHandleArrayList.add(requestAction.tradetongji(StatisticsActivity.this, "2018"));
                                break;
                        }
                    }
                })
                .show();

    }

}
