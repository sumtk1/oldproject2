package com.gloiot.chatsdk.chatui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.DataBase.MessageChatCallBack;
import com.gloiot.chatsdk.DataBase.Widget.DataChange;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.ImMsgTextBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.keyboard.XhsEmoticonsKeyBoard;
import com.gloiot.chatsdk.chatui.keyboard.data.EmoticonEntity;
import com.gloiot.chatsdk.chatui.keyboard.emoji.EmojiBean;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.chatui.keyboard.interfaces.EmoticonClickListener;
import com.gloiot.chatsdk.chatui.keyboard.recorder.AudioRecorderButton;
import com.gloiot.chatsdk.chatui.keyboard.widget.EmoticonsEditText;
import com.gloiot.chatsdk.chatui.keyboard.widget.FuncLayout;
import com.gloiot.chatsdk.chatui.ui.adapter.ChattingListAdapter;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.network.request.NetWorkRequest;
import com.gloiot.chatsdk.socket.CallBackListener;
import com.gloiot.chatsdk.utils.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by jinzlin on 17/2/28.
 * 聊天管理类
 */

public class ChatUiIM implements FuncLayout.OnFuncKeyBoardListener, ChattingListAdapter.SendFailListener, CallBackListener.SendSingleMessageListener {


    private static ChatUiIM instance;
    private View animView;

    ListView lvChat;
    XhsEmoticonsKeyBoard ekBar;
    AudioRecorderButton mRecorderButton;

    public ListView getLvChat() {
        return lvChat;
    }

    public XhsEmoticonsKeyBoard getEkBar() {
        return ekBar;
    }

    public AudioRecorderButton getmRecorderButton() {
        return mRecorderButton;
    }


    private List<ImMsgBean> mDatas;
    private ChattingListAdapter chattingListAdapter;
    private Context context;
    private String receiveid;
    private HashMap userMap;
    private int page = 1, lastItem;

    public static ChatUiIM getInstance() {
        if (instance == null) {
            instance = new ChatUiIM();
        }
        return instance;
    }

    public void setView(View view, Context context, final HashMap userMap) {
        this.receiveid = (String) userMap.get("receiveId");
        this.userMap = userMap;
        this.context = context;
        init();
        lvChat = (ListView) view.findViewById(R.id.lv_chat);
        ekBar = (XhsEmoticonsKeyBoard) view.findViewById(R.id.ek_bar);
        mRecorderButton = (AudioRecorderButton) view.findViewById(R.id.btn_voice);
        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        //判断滚动到顶部
                        if (lvChat.getFirstVisiblePosition() == 0) {
                            //滚到顶部，当条数部位0或者为20的倍数时，加载数据
                            if (mDatas.size() != 0 && mDatas.size() % 20 == 0) {
                                getData(false, receiveid);
                            }
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        ekBar.reset();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = totalItemCount;
            }
        });
        broadcat();
        getData(true, receiveid);
        initEmoticonsKeyBoardBar();
        audioRecorder();
    }

    public void destroyBroadcast() {
        BroadcastManager.getInstance(context).destroy(MessageManager.NEW_MESSAGE);
        page = 1;
        lastItem = 0;
        //清除未读数
        IMDBManager.getInstance(context).NoReadNumClean(receiveid, new DataBaseCallBack() {
            @Override
            public void operationState(boolean flag) {

            }
        });

    }

    // 广播接收
    private void broadcat() {
        BroadcastManager.getInstance(context).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ImMsgBean imMsgBean = (ImMsgBean) intent.getExtras().getSerializable("data");
                ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
                imMsgTextBean = imMsgTextBean.typeCast(imMsgBean);

                String myid = (String) userMap.get("sendOutid");
                String friendid = (String) userMap.get("receiveId");
                String reId = imMsgTextBean.getReceiveid();
                String seId = imMsgTextBean.getSendid();

                if ((myid.equals(seId) && friendid.equals(reId)) || (myid.equals(reId) && friendid.equals(seId))) {
                    Log.e("broadcat", "------" + imMsgBean.getMsgid());

                    chattingListAdapter.addData(imMsgTextBean.typeCast(imMsgBean));
                    if (onceList3.size() < 20) {
                        onceList3.add(onceList3.size(), imMsgTextBean.typeCast(imMsgBean));
                    } else {
                        onceList3.add(20, imMsgTextBean.typeCast(imMsgBean));
                    }
                    lvChat.setSelection(lvChat.getCount() - 1);
                }
            }
        });
    }

    private void init() {
        mDatas = new ArrayList<>();
        onceList2 = new ArrayList<>();
        onceList3 = new ArrayList<>();
        chattingListAdapter = new ChattingListAdapter(context);
        chattingListAdapter.setConversationBehaviorListener(conversationBehaviorListener);
        chattingListAdapter.setUserInfoProvider(userInfoProvider);
        chattingListAdapter.setSendFailListener(this);
        NetWorkRequest.setSendSingleMessageListener(this);
    }

    List<ImMsgTextBean> onceList2;
    List<ImMsgTextBean> onceList3;

    private void getData(boolean isNewest, String receiveid) {
        List<ImMsgBean> onceList;
        onceList2.clear();
        IMDBManager imdbManager = IMDBManager.getInstance(context);
        if (isNewest) {
            mDatas.clear();
            onceList = imdbManager.queryChatMsg(receiveid, 0);
        } else {
            onceList = imdbManager.queryChatMsg(receiveid, onceList3.size());
        }
        Collections.reverse(onceList);
        for (ImMsgBean imMsgBean : onceList) {
            ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
            onceList2.add(imMsgTextBean.typeCast(imMsgBean));
            mDatas.add(imMsgTextBean.typeCast(imMsgBean));
        }
        if (onceList.size() == 20) {
            page++;
        }

        onceList2.addAll(onceList3);
        onceList3.clear();
        onceList3.addAll(onceList2);
        if (onceList.size() != 0) {
            chattingListAdapter.addData(onceList2);
        }

        if (isNewest) {
            lvChat.setAdapter(chattingListAdapter);
            lvChat.setSelection(lvChat.getCount() - 1); // 设置listview展示最后位置
        } else {
            // 设置listview的当前位置，如果不设置每次加载完后都会返回到list的第一项。
            lvChat.setSelection(lvChat.getCount() - lastItem);
        }
    }


    private void initEmoticonsKeyBoardBar() {
        // 初始化输入框
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        // 设置表情布局adapter
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(emoticonClickListener));
        // 功能布局监听（OnFuncPop功能布局弹起，OnFuncClose功能布局关闭）
        ekBar.addOnFuncKeyBoardListener(this);
        // 加号弹出的内容
//        ekBar.addFuncView(new SimpleAppsGridView(context));

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        // 发送按钮
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
        // 添加表情toolbar左边的加号
        ekBar.getEmoticonsToolBarView().addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 添加表情toolbar
        ekBar.getEmoticonsToolBarView().addToolItemView(R.mipmap.icon_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 表情点击监听
     */
    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else {
                if (o == null) {
                    return;
                }
                String content = null;
                if (o instanceof EmojiBean) {
                    content = ((EmojiBean) o).emoji;
                } else if (o instanceof EmoticonEntity) {
                    content = ((EmoticonEntity) o).getContent();
                }

                if (TextUtils.isEmpty(content)) {
                    return;
                }
                int index = ekBar.getEtChat().getSelectionStart();
                Editable editable = ekBar.getEtChat().getText();
                editable.insert(index, content);
            }
        }
    };

    // 发送按钮
    private void OnSendBtnClick(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            // TODO 发送消息
            final int b = (new Random()).nextInt(1000000);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            final String data = "{" +
                    "    \"msgId\":\"uuid" + b + "\"," +
                    "    \"sendId\":\"" + userMap.get("sendOutid") + "\"," +
                    "    \"receiveId\":\"" + receiveid + "\"," +
                    "    \"sessionType\":\"单聊\"," +
                    "    \"msgType\":\"文本\"," +
                    "    \"message\":{\n" +
                    "        \"text\":\"" + msg + "\"}," +
                    "    \"pushData\":\"" + msg + "\"," +
                    "    \"sendTime\":\"" + currentTime + "\"" +
                    "}";


            IMDBManager.getInstance(context).insertChatMsg((new DataChange()).JsonToSystemBean(data), 1, new MessageChatCallBack() {
                @Override
                public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                    BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, imMsgBean);
                    NetWorkRequest.sendSingleMessage(userMap, msg, "文本", "uuid" + b);
                }

                @Override
                public void DoChatDBFault() {

                }
            });

            scrollToBottom();
        }
    }

    // 语音
    private void audioRecorder() {
        mRecorderButton = (AudioRecorderButton) ekBar.getBtnVoice();
        mRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Toast.makeText(context, "功能优化中。。。", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void scrollToBottom() {
        lvChat.requestLayout();
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
    }

    @Override
    public void OnFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }


    private ConversationBehaviorListener conversationBehaviorListener;
    private UserInfoProvider userInfoProvider;


    public void setConversationBehaviorListener(ConversationBehaviorListener conversationBehaviorListener) {
        this.conversationBehaviorListener = conversationBehaviorListener;
    }

    public void setUserInfoProvider(ChatUiIM.UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;

    }

    @Override
    public void resend(String msgId, String msgContent, String msgType) {
        // TODO 重新发送
        NetWorkRequest.sendSingleMessage(userMap, msgContent, "文本", msgId);
    }

    public interface ConversationBehaviorListener {
        boolean onUserPortraitClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onUserPortraitLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onMessageClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onMessageLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);
    }

    public interface UserInfoProvider {
        UserInfo getUserInfo(String receiveid);
    }


    /**
     * 发送消息结果回调
     *
     * @param result
     */
    @Override
    public void sendResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("msg").equals("发送成功")) {
                final String msgIdFse = jsonObject.getString("msgIdFse");
                IMDBManager.getInstance(context).UpDateMessageInfo(jsonObject.getString("msgId"),
                        msgIdFse,
                        jsonObject.getString("receiveId"), 1,
                        jsonObject.getString("time"), new MessageChatCallBack() {
                            @Override
                            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                                ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
                                chattingListAdapter.changeData(imMsgTextBean.typeCast(imMsgBean), msgIdFse);
                            }

                            @Override
                            public void DoChatDBFault() {

                            }
                        });
            } else {
                MToast.showToast(context, "消息发送失败");
                final String msgIdFse = jsonObject.getString("msgIdFse");
                IMDBManager.getInstance(context).UpDateMessageInfo(jsonObject.getString("msgId"),
                        msgIdFse,
                        jsonObject.getString("receiveId"), 2,
                        jsonObject.getString("time"), new MessageChatCallBack() {
                            @Override
                            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                                ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
                                chattingListAdapter.changeData(imMsgTextBean.typeCast(imMsgBean), msgIdFse);
                            }

                            @Override
                            public void DoChatDBFault() {

                            }
                        });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

