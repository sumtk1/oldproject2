package com.gloiot.chatsdk.chatui.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.ImMsgTextBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.FromUserMsgViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.ToUserMsgViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_MSG;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_MSG;


public class ChattingListAdapter<T extends ImMsgBean> extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 3;

    private final int FROM_MSG = 0;      //接收文字消息
    private final int TO_MSG = 1;        //发送文字消息

    private LayoutInflater mInflater;
    private List<T> mData;
    private List<T> newData;
    private Context mContext;

    public ChattingListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addData(List<T> bean) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(bean);
        this.notifyDataSetChanged();
    }

    public void addData(T bean) {
        addData(bean, true, false);
    }

    public void addData(T bean, boolean isNotifyDataSetChanged, boolean isFromHead) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (isFromHead) {
            mData.add(0, bean);
        } else {
            mData.add(bean);
        }
        if (isNotifyDataSetChanged) {
            this.notifyDataSetChanged();
        }
    }

    public void changeData(T bean, String msgIdFse) {
        if (bean.getMessageState() == 2) {
            int index = -1;
            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getMsgid().equals(msgIdFse)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                mData.remove(index);
                mData.add(index, bean);
            }
        } else if (bean.getMessageState() == 1) {
            ImMsgBean imMsgBean = new ImMsgBean();
            for (T t : mData) {
                if (t.getMsgid().equals(msgIdFse)) {
                    imMsgBean = t;
                    break;
                }
            }
            mData.remove(imMsgBean);
            mData.add(bean);
        }
        this.notifyDataSetChanged();
    }

//    public void changeData(T bean, String msgIdFse) {
//        if (newData == null) {
//            newData = new ArrayList<>();
//        }
//        newData.clear();
//        for (T t : mData) {
//            if (t.getMsgid().equals(msgIdFse)) {
//                newData.add(bean);
//            } else {
//                newData.add(t);
//            }
//        }
//        mData.clear();
//        mData.addAll(newData);
//        this.notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        int msgType = -1;
        switch (mData.get(position).getMsgtype()) {
            case FROM_USER_MSG:
                msgType = 0;
                break;
            case TO_USER_MSG:
                msgType = 1;
                break;
        }
        return msgType;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final T bean = mData.get(position);
        Log.e("view", bean.toString()+"-");
        int type = getItemViewType(position);
        View holderView;
        switch (type) {
            case FROM_MSG:
                FromUserMsgViewHolder fromUserMsgViewHolder;
                if (view == null) {
                    fromUserMsgViewHolder = new FromUserMsgViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_msgfrom_list_item, null);
                    fromUserMsgViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    fromUserMsgViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    fromUserMsgViewHolder.msgContent = (TextView) holderView.findViewById(R.id.tv_chat_content);
                    fromUserMsgViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    holderView.setTag(fromUserMsgViewHolder);
                    view = holderView;
                } else {
                    fromUserMsgViewHolder = (FromUserMsgViewHolder) view.getTag();
                }
                try {
                    fromMsgUserLayout(fromUserMsgViewHolder, (ImMsgTextBean) bean, position);
                } catch (Exception e) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case TO_MSG:
                ToUserMsgViewHolder toUserMsgViewHolder;
                if (view == null) {
                    toUserMsgViewHolder = new ToUserMsgViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_msgto_list_item, null);
                    toUserMsgViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    toUserMsgViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    toUserMsgViewHolder.msgContent = (TextView) holderView.findViewById(R.id.tv_chat_content);
                    toUserMsgViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    toUserMsgViewHolder.sendFail = (ImageView) holderView.findViewById(R.id.mysend_fail_img);
                    holderView.setTag(toUserMsgViewHolder);
                    view = holderView;
                } else {
                    toUserMsgViewHolder = (ToUserMsgViewHolder) view.getTag();
                }
                try {
                    toMsgUserLayout(toUserMsgViewHolder, (ImMsgTextBean) bean, position);
                } catch (Exception e) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case -1:
                if (view == null) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
        }
        return view;
    }

    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final ImMsgTextBean bean, final int position) {
        UserInfo userInfo = UserInfoCache.getInstance(mContext).getUserInfo(bean.getSendid());
        if (null == userInfo) {//缓存没有数据，从数据库获取信息
            userInfo = IMDBManager.getInstance(mContext).queryUserInfo(bean.getSendid());
        }
        if (null != userInfo) { //缓存或数据库中有数据
            UserInfoCache.getInstance(mContext).putData(bean.getSendid(), userInfo);//再存储一次（缓存为空，向其中存数据库数据）
            holder.chatName.setText(userInfo.getName());
            if (!TextUtils.isEmpty(userInfo.getUrl())){
                Glide.with(mContext).load(userInfo.getUrl()).into(holder.headIcon);
            }
            chatClickListener(position, holder, bean);
        } else {//本地不存在，需要联网更新（异步）
            userInfo(bean.getSendid());//开启联网，返回值为null
        }
        setContent(holder.msgContent, bean.getPushdata());
        chatClickListener(position, holder, bean);
    }

    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ImMsgTextBean bean, final int position) {
        UserInfo userInfo = UserInfoCache.getInstance(mContext).getUserInfo(bean.getSendid());
        if (null == userInfo) {//缓存没有数据，从数据库获取信息
            userInfo = IMDBManager.getInstance(mContext).queryUserInfo(bean.getSendid());
        }
        if (null != userInfo) { //缓存或数据库中有数据
            UserInfoCache.getInstance(mContext).putData(bean.getSendid(), userInfo);//再存储一次（缓存为空，向其中存数据库数据）
            holder.chatName.setText(userInfo.getName());
            if (!TextUtils.isEmpty(userInfo.getUrl())){
                Glide.with(mContext).load(userInfo.getUrl()).into(holder.headIcon);
            }
        } else {//本地不存在，需要联网更新（异步）
            userInfo(bean.getSendid());//开启联网，返回值为null
        }
        setContent(holder.msgContent, bean.getPushdata());
        chatClickListener(position, holder, bean);

        switch (bean.getMessageState()) {
            case 0:
                holder.sendFail.setVisibility(View.GONE);
                holder.sendFail.setBackgroundResource(R.mipmap.xsearch_loading);
                break;
            case 1:
                holder.sendFail.setVisibility(View.GONE);
                break;
            case 2:
                holder.sendFail.setVisibility(View.VISIBLE);
                holder.sendFail.setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                holder.sendFail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onMoreClick(v)){
                            return;
                        }
                        sendFailListener.resend(bean.getMsgid(), bean.getPushdata(), bean.getMsgtype());
                    }
                });
                break;
            default:
                break;
        }
//        if (bean.getMessageState() == 0) {
//            holder.sendFail.setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
//        } else {
//            holder.sendFail.setVisibility(View.GONE);
//        }

    }

    // 设置文字内容包含表情
    public void setContent(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }


    private UserInfo userInfo(String userId) {
        return userInfoProvider.getUserInfo(userId);
    }

    private void chatClickListener(final int position, final ChatViewHolder holder, final ImMsgBean bean) {
        holder.headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationBehaviorListener.onUserPortraitClick(bean, position, holder);
            }
        });

        holder.chatContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationBehaviorListener.onMessageClick(bean, position, holder);
            }
        });
    }

    private ChatUiIM.ConversationBehaviorListener conversationBehaviorListener;
    private ChatUiIM.UserInfoProvider userInfoProvider;
    private SendFailListener sendFailListener;

    public void setConversationBehaviorListener(ChatUiIM.ConversationBehaviorListener conversationBehaviorListener) {
        this.conversationBehaviorListener = conversationBehaviorListener;
    }

    public void setUserInfoProvider(ChatUiIM.UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }


    public void setSendFailListener(SendFailListener sendFailListener) {
        this.sendFailListener = sendFailListener;
    }

    public interface SendFailListener {
        void resend(String msgId, String msgContent, String msgType);
    }

    /**
     * 防止重复点击
     * @param v
     * @return
     */
    private long lastTime; // 上一次点击时间
    private long delay = 1000; // 默认1秒
    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}