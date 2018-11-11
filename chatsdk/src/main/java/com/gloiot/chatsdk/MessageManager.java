package com.gloiot.chatsdk;

import android.content.Context;
import android.util.Log;

import com.gloiot.chatsdk.broadcast.BroadcastManager;


/**
 * Created by jinzlin on 17/5/18.
 * 消息管理
 */
public class MessageManager {

    private static MessageManager instance;
    private Context context;

    public static final String NEW_MESSAGE = "GLOIOT_OILSTATION_NEW_MESSAGE";
    public static final String REFRESH_USERINFO = "GLOIOT_OILSTATION_REFRESH_USERINFO";

    public static final String LINK_CHANGED_SUCCEED = "OILSTATION_LINK_CHANGED_SUCCEED";
    public static final String LINK_CHANGED_FAULT = "OILSTATION_LINK_CHANGED_FAULT";
    public static final String LINK_CHANGED_DONW = "OILSTATION_LINK_CHANGED_DONW";

    public MessageManager(Context context) {
        this.context = context;
    }

    public static MessageManager getInstance(Context context) {
        if (instance == null) {
            instance = new MessageManager(context);
        }
        return instance;
    }

    public void setMessage(final String data) {
        Log.e("油站setMessage===", "data=" + data);
//        IMDBManager.getInstance(context).insertChatMsg((new DataChange()).JsonToSystemBean(data), 0, new MessageChatCallBack() {
//            @Override
//            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
//                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, imMsgBean);
//            }
//
//            @Override
//            public void DoChatDBFault() {
//
//            }
//        });

        BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, data);

    }
}
