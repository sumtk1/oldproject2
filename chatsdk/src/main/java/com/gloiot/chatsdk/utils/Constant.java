package com.gloiot.chatsdk.utils;

public class Constant {

//    public static final String CHAT_SERVER_URL = "http://121.201.67.222:12223";//测试 20171229

//    public static final String CHAT_SERVER_URL = "http://jiayou.qqjlb.cn:9999"; // 正式----油站

    public static final String CHAT_SERVER_URL = "http://other.zhenxuanzhuangyuan.com:9999"; // 正式----油站


    // 消息的sendID
    public static final String MESSAGE_SYSTEM = "001";
    public static final String MESSAGE_CHANPIN = "002";
    public static final String MESSAGE_ZHANGDAN = "003";
    public static final String MESSAGE_DINGDAN = "004";

    // 认证的msg
    public static final String RENZHENG_SUCCESS = "认证成功";
    public static final String RENZHENG_RANDOM_ERROR = "随机码不正确";
    public static final String RENZHENG_FAILURE = "认证失败";


    // 数据库MESSAGE表
    public static String DB_MESSAGE = "DB_message";
    public static String DB_MESSAGE_MESSAGEID = "messageid";
    public static String DB_MESSAGE_SENDID = "sendid";
    public static String DB_MESSAGE_RECEIVEID = "receiveid";
    public static String DB_MESSAGE_MESSAGETYPE = "messagetype";
    public static String DB_MESSAGE_STATE = "state";
    public static String DB_MESSAGE_CONTENT = "content";
    public static String DB_MESSAGE_SENDTIME = "sendtime";
    public static String DB_MESSAGE_RECEIVEIDTIME = "receivetime";
    public static String DB_MESSAGE_REPLYID = "replyid";
    public static String DB_MESSAGE_REMARK = "remark";
    public static int DB_MESSAGE_MESSAGEID_TAG = 1;
    public static int DB_MESSAGE_SENDID_TAG = 2;
    public static int DB_MESSAGE_RECEIVEID_TAG = 3;
    public static int DB_MESSAGE_MESSAGETYPE_TAG = 4;
    public static int DB_MESSAGE_STATE_TAG = 5;
    public static int DB_MESSAGE_CONTENT_TAG = 6;
    public static int DB_MESSAGE_SENDTIME_TAG = 7;
    public static int DB_MESSAGE_RECEIVEIDTIME_TAG = 8;
    public static int DB_MESSAGE_REPLYID_TAG = 9;
    public static int DB_MESSAGE_REMARK_TAG = 10;

}
