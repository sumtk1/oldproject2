package com.gloiot.hygooilstation.im;

import android.content.Context;
import android.content.SharedPreferences;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

/**
 * Created by jinzlin on 17/5/23.
 */
public class ChatEvent implements ChatUiIM.UserInfoProvider, ChatUiIM.ConversationBehaviorListener, SocketEvent.ConnectionStatusListener {

    private static ChatEvent instance;
    private Context context;
    private ChatUiIM chatUiIM;
    private SharedPreferences preferences;


    public ChatEvent(Context context) {
        this.context = context;
        chatUiIM = ChatUiIM.getInstance();
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        initListener();
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (ChatEvent.class) {
                if (instance == null) {
                    instance = new ChatEvent(context);
                }
            }
        }
    }

    public static ChatEvent getInstance() {
        return instance;
    }


    private void initListener() {
        chatUiIM.setUserInfoProvider(this);
        chatUiIM.setConversationBehaviorListener(this);
        SocketEvent.getInstance().setConnectionStatusListener(this);
    }


    @Override
    public UserInfo getUserInfo(String userId) {
        if (!userId.equals(preferences.getString(ConstantUtlis.SP_USERPHONE, ""))) {
            UserInfoManager.getInstance(context).getUserInfo(userId);
            return null;
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
            userInfo.setId(preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
            userInfo.setUrl(preferences.getString(ConstantUtlis.SP_USERIMG, ""));
            UserInfoCache.getInstance(context).putData(userInfo.getId(), userInfo);//存入缓存
            IMDBManager.getInstance(context).insertUserInfo(userInfo);//存入数据库
            return userInfo;
        }
    }

    @Override
    public boolean onUserPortraitClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public boolean onMessageClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public void onChanged(String result) {
        switch (result) {
            case "":
                MToast.showToast(context, "连接失败");
                break;
            case "随机码不正确":
//                MToast.showToast(context, "聊天链接失败");

//                DialogUtlis.oneBtnNormal(context, "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DialogUtlis.dismissDialog();
//                        CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(intent);
//                        SocketListener.getInstance().signoutRenZheng();
//                        IMDBManager.getInstance(context).ClearnData();
//                        ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
//                    }
//                });
                break;
        }
    }
}
