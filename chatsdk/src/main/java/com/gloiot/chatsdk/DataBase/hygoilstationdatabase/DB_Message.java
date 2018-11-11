package com.gloiot.chatsdk.DataBase.hygoilstationdatabase;

import android.database.Cursor;
import android.util.Log;

import com.gloiot.chatsdk.bean.hygoilstationbean.Message;
import com.gloiot.chatsdk.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Dlt on 2017/6/16 13:53
 */
public class DB_Message {

    private DBManager dbManager;

    public DB_Message(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * 判断消息是否存在
     * @param receiveId
     * @param msgId
     * @return
     */
    public boolean ifMsgId(String receiveId, String msgId) {
        Cursor cursorMsgId = dbManager.queryTables(Constant.DB_MESSAGE, Constant.DB_MESSAGE_RECEIVEID + " = '" + receiveId + "' and " + Constant.DB_MESSAGE_MESSAGEID + " = '" + msgId + "'");
        for (cursorMsgId.moveToFirst(); !cursorMsgId.isAfterLast(); cursorMsgId.moveToNext()) {
            if (msgId.equals(cursorMsgId.getString(Constant.DB_MESSAGE_MESSAGEID_TAG))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取消息记录
     * @param receiveId
     * @return
     */
    public ArrayList<Message> getMessageDB(String receiveId) {
        ArrayList<Message> list = new ArrayList<>();
        Cursor cursorMessage = dbManager.queryTables(Constant.DB_MESSAGE, Constant.DB_MESSAGE_RECEIVEID + " = '" + receiveId + "'");
        for (cursorMessage.moveToFirst(); !cursorMessage.isAfterLast(); cursorMessage.moveToNext()) {
            Message message = new Message();
            message.setMessageId(cursorMessage.getString(Constant.DB_MESSAGE_MESSAGEID_TAG));
            message.setSendId(cursorMessage.getString(Constant.DB_MESSAGE_SENDID_TAG));
            message.setReceiveId(cursorMessage.getString(Constant.DB_MESSAGE_RECEIVEID_TAG));
            message.setMessageType(cursorMessage.getString(Constant.DB_MESSAGE_MESSAGETYPE_TAG));
            message.setState(cursorMessage.getString(Constant.DB_MESSAGE_STATE_TAG));
            message.setContent(cursorMessage.getString(Constant.DB_MESSAGE_CONTENT_TAG));
            message.setSendTime(cursorMessage.getString(Constant.DB_MESSAGE_SENDTIME_TAG));
            message.setReceiveTime(cursorMessage.getString(Constant.DB_MESSAGE_RECEIVEIDTIME_TAG));
            message.setReplyId(cursorMessage.getString(Constant.DB_MESSAGE_REPLYID_TAG));
            message.setRemark(cursorMessage.getString(Constant.DB_MESSAGE_REMARK_TAG));
            list.add(message);
        }
        Log.e("-----", list.size()+"");
        return list;
    }

    /**
     * 添加消息
     * @param list
     */
    public void addMessageDB(ArrayList<Message> list) {
        Log.e("addMessageDB-----", list.size()+"");
        dbManager.addMessage(list);
    }

    /**
     * 清空消息
     * @param receiveId
     */
    public void deleteMessageDB(String receiveId) {
        dbManager.deleteTables(Constant.DB_MESSAGE, Constant.DB_MESSAGE_RECEIVEID + " = '" + receiveId + "'");
    }

}
