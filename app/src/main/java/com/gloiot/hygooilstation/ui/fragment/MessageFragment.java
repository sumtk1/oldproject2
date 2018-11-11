package com.gloiot.hygooilstation.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.bean.Message;
import com.gloiot.hygooilstation.bean.MessageSystem;
import com.gloiot.hygooilstation.bean.MessageTixian;
import com.gloiot.hygooilstation.bean.MessageTrading;
import com.gloiot.hygooilstation.ui.activity.MainActivity;
import com.gloiot.hygooilstation.ui.activity.message.TradingDetailActivity;
import com.gloiot.hygooilstation.utils.L;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 消息
 * Created by dlt on 2016/9/14.
 */

public class MessageFragment extends Fragment {

    public static final String TAG = MessageFragment.class.getSimpleName();

    private RelativeLayout rl_message_empty;
    private ListView lv_message_listView;
    private CommonAdapter mMessageAdapter;
    private MainActivity mainActivity;
    private ArrayList<Message> list = new ArrayList<>();
    private boolean isSetAdapter = false;

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;


    public static Fragment newInstance(int position) {
        MessageFragment fragment = new MessageFragment();

        return fragment;
    }

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initComponent(view);
        initData();
        return view;
    }

    private void initComponent(View v) {
        rl_message_empty = (RelativeLayout) v.findViewById(R.id.rl_message_empty);
        lv_message_listView = (ListView) v.findViewById(R.id.lv_message_listView);
    }

    private void initData() {
        mainActivity = (MainActivity) getActivity();
        list = mainActivity.list;
        isSetAdapter = mainActivity.isSetAdapter;

        if (list.size() != 0) {
            rl_message_empty.setVisibility(View.GONE);
        } else {
            rl_message_empty.setVisibility(View.VISIBLE);
        }

        ProcessData();
        if (isSetAdapter) {
            lv_message_listView.setAdapter(mMessageAdapter);

            lv_message_listView.setSelection(list.size() - 1);//默认滚动到最后一个item
        }

        lv_message_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (list.get(position).getMessageType().equals("trading")) {//交易提醒可以跳转
                    Intent intent = new Intent(getActivity(), TradingDetailActivity.class);

                    MessageTrading messageTrading = getMsgTrading(list.get(position).getContent());

                    String goodsOrderNum = messageTrading.getJiaoyi();

                    intent.putExtra("goodsOrderNum", goodsOrderNum);

                    startActivity(intent);
                }

            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.gloiot.hygooilstation.MESSAGEDATA_CHANGE");
        localReceiver = new LocalReceiver();
        mainActivity.localBroadcastManager.registerReceiver(localReceiver, intentFilter);//注册本地广播监听器
    }

    private void ProcessData() {
        mMessageAdapter = new CommonAdapter<Message>(getActivity(), R.layout.item_message, list) {

            @Override
            public void convert(ViewHolder holder, Message messages) {

                if (messages.getMessageType().equals("system")) {//系统消息

                    holder.setVisible(R.id.rl_item_message_top1, true);
                    holder.setVisible(R.id.rl_item_message_top2, false);
                    holder.setVisible(R.id.rl_item_message_jiaoyi, false);
                    holder.setVisible(R.id.rl_item_message_system, true);
                    holder.setVisible(R.id.ll_item_message_tixian, false);

                    holder.setText(R.id.tv_item_message_time, messages.getSendTime());
                    MessageSystem messageSystem = getMsgSystem(messages.getContent());

                    holder.setImageResource(R.id.iv_item_message, R.mipmap.xitongtongzhi);
                    holder.setText(R.id.tv_item_message_type, "系统消息");
                    holder.setText(R.id.tv_item_message_record, messageSystem.getText());
                } else if (messages.getMessageType().equals("trading")) {//交易提醒

                    holder.setVisible(R.id.rl_item_message_top1, true);
                    holder.setVisible(R.id.rl_item_message_top2, false);
                    holder.setVisible(R.id.rl_item_message_jiaoyi, true);
                    holder.setVisible(R.id.rl_item_message_system, false);
                    holder.setVisible(R.id.ll_item_message_tixian, false);

                    holder.setText(R.id.tv_item_message_time, messages.getSendTime());
                    MessageTrading messageTrading = getMsgTrading(messages.getContent());

                    holder.setImageResource(R.id.iv_item_message, R.mipmap.jiaoyitixing);
                    holder.setText(R.id.tv_item_message_type, "交易提醒");
                    if (!TextUtils.isEmpty(messageTrading.getMoney())) {
                        if (messageTrading.getMoney().substring(0, 1).equals("-")) {
                            holder.setTextColorRes(R.id.tv_item_message_points, R.color.blue_478AF5);
                            holder.setText(R.id.tv_item_message_points, messageTrading.getMoney());
                        } else {
                            holder.setTextColorRes(R.id.tv_item_message_points, R.color.orange_FF873D);
                            holder.setText(R.id.tv_item_message_points, "+" + messageTrading.getMoney());
                        }
                    }
                    holder.setText(R.id.tv_item_message_store, messageTrading.getText());
                } else {//提现消息

                    holder.setVisible(R.id.rl_item_message_top1, false);
                    holder.setVisible(R.id.rl_item_message_top2, true);
                    holder.setVisible(R.id.rl_item_message_jiaoyi, false);
                    holder.setVisible(R.id.rl_item_message_system, false);
                    holder.setVisible(R.id.ll_item_message_tixian, true);

                    MessageTixian messageTixian = getMsgTixian(messages.getContent());

                    holder.setText(R.id.tv_item_message_tixian_titile, messageTixian.getTitle());
                    holder.setText(R.id.tv_item_message_tixian_money, "￥" + messageTixian.getMoney());
                    holder.setText(R.id.tv_item_message_tixian_bank, messageTixian.getBank());
                    holder.setText(R.id.tv_item_message_tixian_tixiantime, messageTixian.getTixianTime());
                    holder.setText(R.id.tv_item_message_tixian_daozhangtime, messageTixian.getDaozhangTime());
                    holder.setText(R.id.tv_item_message_tixian_remark, messageTixian.getExtra());
                }
            }
        };
    }

    /**
     * 获得系统消息
     *
     * @param data
     * @return
     */
    private MessageSystem getMsgSystem(String data) {
        MessageSystem messageSystem = new MessageSystem();
        try {
            JSONObject jsonObject = new JSONObject(data);
            messageSystem.setType(jsonObject.getString("type"));
            String text = jsonObject.getString("text");
            if (text.contains("<br/>")) {
                text = text.replace("<br/>", "\n");
            }
            messageSystem.setText(text);
            messageSystem.setExtra(jsonObject.getString("extra"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageSystem;
    }

    /**
     * 获取交易提醒
     *
     * @param data
     * @return
     */
    private MessageTrading getMsgTrading(String data) {
        MessageTrading messageTrading = new MessageTrading();
        try {
            JSONObject jsonObject = new JSONObject(data);
            messageTrading.setType(jsonObject.getString("type"));
            messageTrading.setText(jsonObject.getString("text"));
            messageTrading.setMoney(jsonObject.getString("money"));
            messageTrading.setExtra(jsonObject.getString("extra"));
            messageTrading.setJiaoyi(jsonObject.getString("jiaoyi"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageTrading;
    }

    /**
     * 获取提现消息
     *
     * @param data
     * @return
     */
    private MessageTixian getMsgTixian(String data) {
        MessageTixian messageTixian = new MessageTixian();
        try {
            JSONObject jsonObject = new JSONObject(data);
            messageTixian.setType(jsonObject.getString("type"));
            messageTixian.setBank(jsonObject.getString("yinhang"));
            messageTixian.setTixianTime(jsonObject.getString("tixian_time"));
            messageTixian.setDaozhangTime(jsonObject.getString("daozhang_time"));
            messageTixian.setMoney(jsonObject.getString("money"));
            messageTixian.setExtra(jsonObject.getString("text"));
            messageTixian.setTitle(jsonObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageTixian;
    }

    //本地广播接收器
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            rl_message_empty.setVisibility(View.GONE);
            mMessageAdapter.notifyDataSetChanged();
            L.e("Main--MessageFragment收到广播", "发送时间：" + list.get(list.size() - 1).getSendTime());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.localBroadcastManager.unregisterReceiver(localReceiver);
//        Log.e("MessageFragment", "onDestroy.....");
    }
}