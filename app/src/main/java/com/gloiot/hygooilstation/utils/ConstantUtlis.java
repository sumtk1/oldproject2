package com.gloiot.hygooilstation.utils;

/**
 * Created by JinzLin on 2016/9/13.
 */

public class ConstantUtlis {

    // 微信Appid
    public static String WXAPPID = "wx8c55439fc255912a";

    //阿里云信息
//    public static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
//    public static final String accessKeyId = "ynjAwXSJsm6tHvbW";
//    public static final String accessKeySecret = "lnrN4oWq90GPEXZnmarHj8HOgfQFVe";
//    public static final String aliyunBucketName = "zykshop";
//    public static final String aliyunPath1 = "http://zykshop.qqjlb.cn/";

    //2017.06.17更改
//    public static final String endpoint = "http://oss.glo-iot.com";
//    public static final String accessKeyId = "LTAILX1dJyVDfG7W";
//    public static final String accessKeySecret = "porFTxA7VyZOM2nlxHCJw47DaSF2jk";
//    public static final String aliyunBucketName = "qqwlw";
//    //class首的aliyunBucketName更改
//    public static final String aliyunPath1 = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/";

    //20180111更改
    public static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";
//    public static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final String accessKeyId = "LTAIfqTWsP5HAPoj";
    public static final String accessKeySecret = "i99ENWsdXFjcVgXZsEtWnxIn0f8Ibd";
    public static final String aliyunBucketName = "tj-hygo-shop";
    public static final String aliyunPath1 = "http://oss.zhenxuanzhuangyuan.com/";

    /**
     * 上传到阿里云的文件夹地址
     * 测试 ：debug
     * 正式 ：release
     */
    public static final String ALIYUNPACKAGENAME = "release";

    // 当前版本名
    public static String SP_VERSIONNAME = "1.2.1";
    // SharedPreferences存储空间
    public static String MYSP = "zyypayinfo";
    //引导页是否展示过
    public static String SP_ISGUIDEPAGESHOWED = "ISGUIDEPAGESHOWED";
    // 手机高度
    public static String SP_PHONEHEIGHT = "SP_PHONEHEIGHT";
    // 手机ID
    public static String SP_PHENEID = "SP_PHENEID";
    // 手机信息kv格式
    public static String SP_PHONEINFO_KV = "SP_PHONEINFO_KV";
    // 手机信息json格式
    public static String SP_PHONEINFO_JSON = "SP_PHONEINFO_JSON";
    // 用户随机码
    public static String SP_RANDOMCODE = "SP_RANDOMCODE";
    // 请求信息json格式(包括账号，随机码，手机id，手机型号，手机名称)
    public static String SP_INFO_JSON = "SP_INFO_JSON";
    // 账号认证状态(取值：未上传资料/未审核/审核未通过/审核通过)
    public static String SP_AUTHENTIFICATIONSTATE = "SP_AUTHENTIFICATIONSTATE";
    //账号登录状态
    public static String SP_LOGINSTATE = "SP_LOGINSTATE";
    //支付密码状态
    public static String SP_PAYPWDSTATE = "SP_PAYPWDSTATE";
    //用户账号
    public static String SP_USEACCOUNT = "SP_USEACCOUNT";
    //账号类别（站长/收银员）
    public static String SP_ACCOUNTTYPE = "SP_ACCOUNTTYPE";
    //油站id
    public static String SP_STATIONID = "SP_STATIONID";
    //油站名称
    public static String SP_STATIONNAME = "SP_STATIONNAME";
    //油站区域-省
    public static String SP_STATIONSHENG = "SP_STATIONSHENG";
    //油站区域-市
    public static String SP_STATIONSHI = "SP_STATIONSHI";
    //油站区域-区
    public static String SP_STATIONQU = "SP_STATIONQU";
    //油站区域-是否重置
    public static String SP_ISSTATIONAREARESET = "SP_ISSTATIONAREARESET";
    //油站区域-纬度
    public static String SP_STATIONLATITUDE = "SP_STATIONLATITUDE";
    //油站区域-经度
    public static String SP_STATIONLONGITUDE = "SP_STATIONLONGITUDE";
    //油站区域-经纬度是否重置
    public static String SP_ISSTATIONLATLNGRESET = "SP_ISSTATIONLATLNGRESET";
    //油站区域-位置信息详细描述
    public static String SP_STATIONLOCATIONDETAILINFO = "SP_STATIONLOCATIONDETAILINFO";
    //来自通知消息
    public static String SP_FROMNOTIFICATION = "SP_FROMNOTIFICATION";
    //是否需要刷新油站fragment的数据
    public static String SP_ISNEEDTOUPDATESTATIONDATA = "SP_ISNEEDTOUPDATESTATIONDATA";
    //是否重置登录密码
    public static String SP_ISLOGINPWDRESET = "SP_ISLOGINPWDRESET";
    //是否重置支付密码
    public static String SP_ISPAYPWDRESET = "SP_ISPAYPWDRESET";
    //是否是从登录页进入主页
    public static String SP_ISFROMLOGINTOMAIN = "SP_ISFROMLOGINTOMAIN";
    //是否是从油站编辑页进入主页（要重建MainActivity，因为暂时没有找到正确刷新轮播图的方法）
    public static String SP_ISFROMMODIFYTOMAIN = "SP_ISFROMMODIFYTOMAIN";

    // 用户手机号
    public static String SP_USERPHONE = "SP_USERPHONE";
    // 用户昵称
    public static String SP_NICKNAME = "SP_NICKNAME";
    // 用户头像
    public static String SP_USERIMG = "SP_USERIMG";


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
