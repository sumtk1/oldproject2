package com.gloiot.chatsdk.bean.hygoilstationbean;

/**
 * Created by Dlt on 2017/6/16 11:56
 */
public class Message {
    public final static int MESSAGE_TYPE0 = 0;
    public final static int MESSAGE_TYPE1 = 1;

    private String messageId;   //消息id
    private String sendId;      //发送人
    private String receiveId;   //接收人
    private String messageType; //消息类型
    private String state;       //状态
    private String content;     //消息内容
    private String sendTime;    //发送时间
    private String receiveTime; //接收时间
    private String replyId;     //回复ID
    private String remark;      //备注

    public static int getMessageType0() {
        return MESSAGE_TYPE0;
    }

    public static int getMessageType1() {
        return MESSAGE_TYPE1;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", sendId='" + sendId + '\'' +
                ", receiveId='" + receiveId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", state='" + state + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                ", replyId='" + replyId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
