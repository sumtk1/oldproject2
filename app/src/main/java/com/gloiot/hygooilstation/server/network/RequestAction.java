package com.gloiot.hygooilstation.server.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.gloiot.hygooilstation.server.network.utlis.EnDecryptUtlis;
import com.gloiot.hygooilstation.server.network.utlis.JsonUtils;
import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MD5Util;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.gloiot.hygooilstation.utils.SharedPreferencesUtils.mContext;

/**
 * Created by JinzLin on 2016/7/15.
 */
public class RequestAction {

    public final static int TAG_LOGIN = 0;
    public static String FUNC_LOGIN = "login";

    public final static int TAG_SENDYZM = 1;
    public final String FUNC_SENDYZM = "zyzsms";

    public final static int TAG_FORGETPWD = 2;
    public final String FUNC_FORGETPWD = "forget_password";

    public final static int TAG_QUALIFICATIONINFO = 3;
    public final String FUNC_QUALIFICATIONINFO = "zizhixinxi";

    public final static int TAG_BANKCARDNAME = 4;
    public final String FUNC_BANKCARDNAME = "Bankcard";

    public final static int TAG_CERTIFICATEBANKCARD = 5;
    public final String FUNC_CERTIFICATEBANKCARD = "add_Bankcard";

    public final static int TAG_OILSTATIONINFO = 6;
    public final String FUNC_OILSTATIONINFO = "youzhan";

    public final static int TAG_MYOILS = 7;
    public final String FUNC_MYOILS = "my_youpin";

    public final static int TAG_AUDITLOGGING = 8;
    public final String FUNC_AUDITLOGGING = "youpin_select";

    public final static int TAG_OILSTYPE = 9;
    public final String FUNC_OILSTYPE = "youpinleixing";

    public final static int TAG_ADDOILS = 10;
    public final String FUNC_ADDOILS = "add_youpin";

    public final static int TAG_MODIFYOILS = 11;
    public final String FUNC_MODIFYOILS = "youpin_update";

    public final static int TAG_DELETEOILS = 12;
    public final String FUNC_DELETEOILS = "youpin_del";

    public final static int TAG_MODIFYOILSTATION = 13;
    public final String FUNC_MODIFYOILSTATION = "youzhan_update";

    public final static int TAG_STATIONPICS = 14;
    public final String FUNC_STATIONPICS = "youzhan_img";

    public final static int TAG_UPLOADINGSTATIONPICS = 15;
    public final String FUNC_UPLOADINGSTATIONPICS = "youzhan_add-img";

    public final static int TAG_DELETESTATIONPICS = 16;
    public final String FUNC_DELETESTATIONPICS = "youzhan_del-img";

    public final static int TAG_TODAYEARNING = 17;
    public final String FUNC_TODAYEARNING = "today";

    public final static int TAG_TRADETONGJI = 18;
    public final String FUNC_TRADETONGJI = "tongji";

    public final static int TAG_ERWEIMA = 19;
    public final String FUNC_ERWEIMA = "erweima";

    public final static int TAG_WALLET = 20;
    public final String FUNC_WALLET = "money";

    public final static int TAG_WALLETLIST = 21;
    public final String FUNC_WALLETLIST = "all_mingxi";

    public final static int TAG_WALLETITEMDETAIL = 22;
    public final String FUNC_WALLETITEMDETAILT = "mingxi";

    public final static int TAG_MYBANKCARDS = 23;
    public final String FUNC_MYBANKCARDS = "my_Bankcard";

    public final static int TAG_DELETEBANKCARD = 24;
    public final String FUNC_DELETEBANKCARD = "del_Bankcard";

    public final static int TAG_SUGGESTIONFEEDBACK = 25;
    public final String FUNC_SUGGESTIONFEEDBACK = "yijian";

    public final static int TAG_MODIFYLOGINPWD = 26;
    public final String FUNC_MODIFYLOGINPWD = "forget_pass";

    public final static int TAG_MODIFYPAYPWD = 27;
    public final String FUNC_MODIFYPAYPWD = "update_zhifupass";

    public final static int TAG_FORGETPAYPWD = 28;
    public final String FUNC_FORGETPAYPWD = "forget_zhifupass";

    public final static int TAG_SETPAYPWD = 29;
    public final String FUNC_SETPAYPWD = "shezhi_pass";

    public final static int TAG_ISTRUEORIGINALPAYPWD = 30;
    public final String FUNC_ISTRUEORIGINALPAYPWD = "yuan_zhifumima";

    public final static int TAG_ISEQUALNEWANDCONFIRMPWD = 31;
    public final String FUNC_ISEQUALNEWANDCONFIRMPWD = "success_zhifupass";

    public final static int TAG_GETSHENG = 32;
    public final String FUNC_GETSHENG = "getCity";

    public final static int TAG_GETSHI = 33;
    public final String FUNC_GETSHI = "getCity";

    public final static int TAG_GETQU = 34;
    public final String FUNC_GETQU = "getCity";

    public final static int TAG_ADDCASHIER = 35;
    public final String FUNC_ADDCASHIER = "shouyy_add";

    public final static int TAG_DELETECASHIER = 36;
    public final String FUNC_DELETECASHIER = "shouyy_del";

    public final static int TAG_CASHIERLIST = 37;
    public final String FUNC_CASHIERLIST = "shouyy_select";

    public final static int TAG_WALLETTOTALAMOUNT = 38;
    public final String FUNC_WALLETTOTALAMOUNT = "qb_zong";

    public final static int TAG_WALLETDETAIL = 39;
    public final String FUNC_WALLETDETAIL = "qb_mingx";

    public final static int TAG_SETTLEMENTLIST = 40;
    public final String FUNC_SETTLEMENTLIST = "jiesuan_dan";

    public final static int TAG_SETTLEMENTDETAIL = 41;
    public final String FUNC_SETTLEMENTDETAIL = "jiesuan_mingxi";

    public final static int TAG_GOTOSETTLE = 42;
    public final String FUNC_GOTOSETTLE = "jiesuan";

    public final static int TAG_GETTIXIANEXPLAIN = 43;
    public final String FUNC_GETTIXIANEXPLAIN = "tixian_shuoming";

    public final static int TAG_TIXIANMANUAL = 44;
    public final String FUNC_TIXIANMANUAL = "tq_jifen_sd";

    public final static int TAG_AUTHENTICATIONANDPAYPWDSTATE = 45;
    public final String FUNC_AUTHENTICATIONANDPAYPWDSTATE = "panduan";

    public final static int TAG_STATIONOILSTYPE = 46;
    public final String FUNC_STATIONOILSTYPE = "yp_type_all";

    public final static int TAG_MYOILSGUNS = 47;
    public final String FUNC_MYOILSGUNS = "xiangxi_youqiang";

    public final static int TAG_ADDOILSGUN = 48;
    public final String FUNC_ADDOILSGUN = "add_yq";

    public final static int TAG_DELETEOILSGUN = 49;
    public final String FUNC_DELETEOILSGUN = "del_youqiang";

    public final static int TAG_STATISTICSDETAIL = 50;
    public final String FUNC_STATISTICSDETAIL = "tongji_mingxi";

    public final static int TAG_GETPRINTERDATA = 51;
    public final String FUNC_GETPRINTERDATA = "shibei";

    public final static int TAG_SETTLEMENTOFPRINT = 52;
    public final String FUNC_SETTLEMENTOFPRINT = "jiesuan_dayin";

    public final static int TAG_TRADINGDETAIL = 53;
    public final String FUNC_TRADINGDETAIL = "jyxiangqing";

    public final static int TAG_WALLETLISTMINGXI = 54;
    public final String FUNC_WALLETLISTMINGXI = "ming_xi";

    public final static int TAG_STATISTICSYEARS = 55;
    public final String FUNC_STATISTICSYEARS = "tongji_year";

    public final static int TAG_SETTLEMENTCONFIRM = 56;
    public final String FUNC_SETTLEMENTCONFIRM = "jiesuan_queren";

    public final static int TAG_REVOKE = 57;
    public final String FUNC_REVOKE = "chedan";

    public final static int TAG_RONGYUNREVOKE = 58;
    public final String FUNC_RONGYUNREVOKE = "chedan_rongcloud";

    public final static int TAG_GETQUALIFICATIONINFO = 59;
    public final String FUNC_GETQUALIFICATIONINFO = "show_zizhixinxi";

    public final static int TAG_NEWQUALIFICATIONINFO = 60;
    public final String FUNC_NEWQUALIFICATIONINFO = "zizhixinxi_new";

    public final static int TAG_ZIZHIWARMPROMPT = 61;
    public final String FUNC_ZIZHIWARMPROMPT = "warmPrompt";

    public final static int TAG_ZIZHIUPLOADINGPHOTO = 62;
    public final String FUNC_ZIZHIUPLOADINGPHOTO = "oil_qualificationUp";

    public final static int TAG_QUERYPRINCIPALNAME = 63;
    public final String FUNC_QUERYPRINCIPALNAME = "oil_selZFBname";

    public final static int TAG_BINDINGALIPAYACCOUNT = 64;
    public final String FUNC_BINDINGALIPAYACCOUNT = "oil_addZFBaccount";

    public final static int TAG_ALIPAYACCOUNTLIST = 65;
    public final String FUNC_ALIPAYACCOUNTLIST = "oil_selZFBaccount";

    public final static int TAG_DELETEALIPAYACCOUNT = 66;
    public final String FUNC_DELETEALIPAYACCOUNT = "oil_delZFBaccount";

    public final static int TAG_EXTRACTTOALIPAYACCOUNT = 67;
    public final String FUNC_EXTRACTTOALIPAYACCOUNT = "tq_jifen_zfb";

    private static SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences();
    // 登录后随机码
    private static String randomCode = sp.getString(ConstantUtlis.SP_RANDOMCODE, "");
    //当前版本号（版本名）
    private static String versionName = sp.getString(ConstantUtlis.SP_VERSIONNAME, "");

    private static RequestParams getParams(String func, HashMap<String, Object> hashMap) {
        randomCode = randomCode.equals("") ? MD5Util.Md5(func) : randomCode;
        RequestParams params = new RequestParams();
        params.add("func", func);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return params;
    }

    //显示请求信息
    private void showRequstInfo(HashMap<String, Object> hashMap, String tag) {
        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            L.e(tag, e.getKey() + "-" + e.getValue());
        }
    }

    // 登录
    public RequestHandle userLogin(OnDataListener onDataListener, String account, String pwd) {
        String randomCode = MD5Util.Md5(FUNC_LOGIN);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        showRequstInfo(hashMap, "登录");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_LOGIN);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_LOGIN, params, onDataListener);
    }

    // 发送验证码
    public RequestHandle sendyzm(OnDataListener onDataListener, String account, String phoneNum) {
        String randomCode = MD5Util.Md5(FUNC_SENDYZM);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("手机号", phoneNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SENDYZM);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_SENDYZM, params, onDataListener);
    }

    // 忘记密码
    public RequestHandle forgetPwd(OnDataListener onDataListener, String account, String phoneNum, String yzm, String pwd) {
        String randomCode = MD5Util.Md5(FUNC_FORGETPWD);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("手机号", phoneNum);
        hashMap.put("验证码", yzm);
        hashMap.put("密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        showRequstInfo(hashMap, "忘记密码");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_FORGETPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_FORGETPWD, params, onDataListener);
    }

    // 资质信息
    public RequestHandle qualificationInfo(OnDataListener onDataListener, String account, String shouchi, String front, String back,
                                           String name, String IDNum, String zhizhaoPic) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("随机码", randomCode);
        hashMap.put("手持身份证照", shouchi);
        hashMap.put("身份证正面", front);
        hashMap.put("身份证背面", back);
        hashMap.put("姓名", name);
        hashMap.put("身份证号", IDNum);
        hashMap.put("执照图片", zhizhaoPic);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        showRequstInfo(hashMap, "资质认证");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_QUALIFICATIONINFO);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_QUALIFICATIONINFO, params, onDataListener);
    }

    // 获取银行名称
    public RequestHandle getBankName(OnDataListener onDataListener, String account) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("随机码", randomCode);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        showRequstInfo(hashMap, "获取银行名称");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_BANKCARDNAME);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_BANKCARDNAME, params, onDataListener);
    }

    // 资质认证--添加银行卡（银行卡认证）
    public RequestHandle certificateBankcard(OnDataListener onDataListener, String bank, String cardNum, String branchName,
                                             String cardType, String chichaName, String paperNum, String phoneNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("银行", bank);
        hashMap.put("卡号", cardNum);
        hashMap.put("支行名称", branchName);
        hashMap.put("卡类型", cardType);
        hashMap.put("持卡人姓名", chichaName);
        hashMap.put("证件号码", paperNum);
        hashMap.put("电话号码", phoneNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "资质认证-添加银行卡");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_CERTIFICATEBANKCARD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_CERTIFICATEBANKCARD, params, onDataListener);
    }

    // 油站信息
    public RequestHandle getoilstationInfo(OnDataListener onDataListener, Context mContext) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_OILSTATIONINFO);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_OILSTATIONINFO, params, onDataListener);
    }

    // 我的油品
    public RequestHandle getmyoils(OnDataListener onDataListener, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MYOILS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_MYOILS, params, onDataListener);
    }

    // 油品审核记录
//    public RequestHandle auditlogging(OnDataListener onDataListener, String stationId) {
//        String randomCode = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("油站id", stationId);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));
//
//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
////        for (Map.Entry<String, Object> e : entry) {
////            Log.e("油品审核记录", e.getKey() + "-" + e.getValue());
////        }
//
//        RequestParams params = new RequestParams();
//        params.add("func", FUNC_AUDITLOGGING);
//        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
//
//        return HttpManager.doPost(TAG_AUDITLOGGING, params, onDataListener);
//    }

    // 油品审核记录---------改为有刷新加载的请求 20170612修改
    public RequestHandle auditlogging(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                      int page, int requesTag, int showLoad, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "油品审核记录");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_AUDITLOGGING);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    // 油品类型
    public RequestHandle oilstype(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_OILSTYPE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_OILSTYPE, params, onDataListener);
    }

    //添加油品
    public RequestHandle addoils(OnDataListener onDataListener, String stationName, String stationId, String type, String markdet, String youhui, String ratio) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站名称", stationName);
        hashMap.put("油站id", stationId);
        hashMap.put("类型", type);
        hashMap.put("市场价", markdet);
        hashMap.put("优惠价", youhui);
        hashMap.put("优惠比例", ratio);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "添加油品");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ADDOILS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_ADDOILS, params, onDataListener);
    }

    //编辑油品
    public RequestHandle modifyoils(OnDataListener onDataListener, String stationId, String type, String markdet, String youhui, String ratio) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.put("油品型号", type);
        hashMap.put("市场价", markdet);
        hashMap.put("优惠价", youhui);
        hashMap.put("优惠比例", ratio);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "编辑油品");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MODIFYOILS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_MODIFYOILS, params, onDataListener);
    }

    //删除油品
    public RequestHandle deleteoils(OnDataListener onDataListener, String stationId, String oilsId, String type) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.put("油品id", oilsId);
        hashMap.put("油品型号", type);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "删除油品");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETEOILS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_DELETEOILS, params, onDataListener);
    }

    //编辑油站信息
    public RequestHandle modifyoilstation(OnDataListener onDataListener, String location, String phoneNum, String intro, String longitude,
                                          String latitude, String area) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站地址", location);
        hashMap.put("油站座机", phoneNum);
        hashMap.put("油站简介", intro);
        hashMap.put("油站经度", longitude);
        hashMap.put("油站纬度", latitude);
        hashMap.put("油站区域", area);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            L.e("油站编辑", e.getKey() + "-" + e.getValue());
        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MODIFYOILSTATION);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_MODIFYOILSTATION, params, onDataListener);
    }

    //油站图片
    public RequestHandle getStationpics(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_STATIONPICS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_STATIONPICS, params, onDataListener);
    }

    //上传油站图片
    public RequestHandle uploadingStationpics(OnDataListener onDataListener, String pics) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站图片", pics);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "上传油站图片");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_UPLOADINGSTATIONPICS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_UPLOADINGSTATIONPICS, params, onDataListener);
    }

    //删除油站图片
    public RequestHandle deleteStationpics(OnDataListener onDataListener, String pics) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站图片", pics);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "删除油站图片");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETESTATIONPICS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_DELETESTATIONPICS, params, onDataListener);
    }

    //交易--今日收益
    public RequestHandle todayearning(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_TODAYEARNING);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_TODAYEARNING, params, onDataListener);
    }

    //交易--今日收益 ------------------改为有分页刷新加载的请求方式(不使用pullToRefreshLayout)
    public RequestHandle todayearningNew(OnDataListener onDataListener, int requestType, int page, int requesTag, int showLoad) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_TODAYEARNING);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, requestType);
    }

    //交易--统计
    public RequestHandle tradetongji(OnDataListener onDataListener, String year) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("年份", year);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_TRADETONGJI);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_TRADETONGJI, params, onDataListener);
    }

    //二维码(根据返回的信息生成二维码)
    public RequestHandle erweima(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("获取生成二维码要用的信息", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ERWEIMA);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ERWEIMA, params, onDataListener);
    }

    //我的--钱包
    public RequestHandle getmyWallet(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLET);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_WALLET, params, onDataListener);
    }

    //我的--钱包--明细列表
    public RequestHandle getmyWalletlist(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLETLIST);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_WALLETLIST, params, onDataListener);
    }

    //我的--钱包--条目明细详情
    public RequestHandle walletitemDetail(OnDataListener onDataListener, String oddnumId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("交易单号", oddnumId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("钱包条目明细", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLETITEMDETAILT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_WALLETITEMDETAIL, params, onDataListener);
    }

    //我的--我的银行卡
    public RequestHandle getMybankcards(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "我的银行卡");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MYBANKCARDS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_MYBANKCARDS, params, onDataListener);
    }

    //我的--删除银行卡
    public RequestHandle deletebankcard(OnDataListener onDataListener, String cardNum, String pwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("银行卡号", cardNum);
        hashMap.put("密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETEBANKCARD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_DELETEBANKCARD, params, onDataListener);
    }

    //我的--意见反馈
    public RequestHandle suggestionFeedback(OnDataListener onDataListener, String feedbackType, String problemDetail) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("反馈类别", feedbackType);
        hashMap.put("问题描述", problemDetail);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "意见反馈");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SUGGESTIONFEEDBACK);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_SUGGESTIONFEEDBACK, params, onDataListener);
    }

    //我的--修改登录密码
    public RequestHandle modifyLoginpwd(OnDataListener onDataListener, String originalPwd, String newPwd, String confirmPwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("原密码", originalPwd);
        hashMap.put("新密码", newPwd);
        hashMap.put("再次输入", confirmPwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MODIFYLOGINPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_MODIFYLOGINPWD, params, onDataListener);
    }

    //我的--修改支付密码
    public RequestHandle modifyPaypwd(OnDataListener onDataListener, String account, String phoneNum, String yzm) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("随机码", randomCode);
        hashMap.put("手机号", phoneNum);
        hashMap.put("验证码", yzm);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("修改支付密码", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MODIFYPAYPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_MODIFYPAYPWD, params, onDataListener);
    }

    //我的--忘记支付密码
    public RequestHandle forgetPaypwd(OnDataListener onDataListener, String account, String idNum, String phoneNum, String yzm) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("账号", account);
        hashMap.put("随机码", randomCode);
        hashMap.put("身份证号", idNum);
        hashMap.put("手机号", phoneNum);
        hashMap.put("验证码", yzm);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("忘记支付密码", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_FORGETPAYPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_FORGETPAYPWD, params, onDataListener);
    }

    //我的--设置支付密码
    public RequestHandle setPaypwd(OnDataListener onDataListener, String payPwd, String confirmPwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("支付密码", payPwd);
        hashMap.put("再次输入密码", confirmPwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SETPAYPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_SETPAYPWD, params, onDataListener);
    }

    //我的--设置--判断原支付密码是否正确
    public RequestHandle istrueOriginalpaypwd(OnDataListener onDataListener, String payPwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("原支付密码", payPwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ISTRUEORIGINALPAYPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ISTRUEORIGINALPAYPWD, params, onDataListener);
    }

    //我的--设置--判断新的支付密码和确认密码是否相等，相等则修改成功
    public RequestHandle isequalNewAndConfirmPwd(OnDataListener onDataListener, String newPaypwd, String confirmPaypwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("新的支付密码", newPaypwd);
        hashMap.put("再次输入", confirmPaypwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ISEQUALNEWANDCONFIRMPWD);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ISEQUALNEWANDCONFIRMPWD, params, onDataListener);
    }

    //获取省
    public RequestHandle getSheng(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETSHENG);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETSHENG, params, onDataListener);
    }

    //获取市
    public RequestHandle getShi(OnDataListener onDataListener, String shengName) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("省份", shengName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETSHI);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETSHI, params, onDataListener);
    }

    //获取区
    public RequestHandle getQu(OnDataListener onDataListener, String shengName, String cityName) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("省份", shengName);
        hashMap.put("市名", cityName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETQU);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETQU, params, onDataListener);
    }

    //收银员信息（列表数据）
    public RequestHandle getCashierList(OnDataListener onDataListener, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_CASHIERLIST);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_CASHIERLIST, params, onDataListener);
    }

    //添加收银员
    public RequestHandle addCashier(OnDataListener onDataListener, String stationId, String name, String phoneNum, String pwd) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.put("姓名", name);
        hashMap.put("手机号", phoneNum);
        hashMap.put("密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("添加收银员", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ADDCASHIER);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ADDCASHIER, params, onDataListener);
    }

    //删除收银员
    public RequestHandle deleteCashier(OnDataListener onDataListener, String stationId, String cashierAccount) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.put("收银员账号", cashierAccount);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETECASHIER);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_DELETECASHIER, params, onDataListener);
    }

    //钱包总额
    public RequestHandle getWalletTotalAmount(OnDataListener onDataListener, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLETTOTALAMOUNT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_WALLETTOTALAMOUNT, params, onDataListener);
    }

    //钱包明细
//    public RequestHandle getWalletDetail(OnDataListener onDataListener, String stationId) {
//        String randomCode = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("油站id", stationId);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));
//
//        RequestParams params = new RequestParams();
//        params.add("func", FUNC_WALLETDETAIL);
//        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
//        return HttpManager.doPost(TAG_WALLETDETAIL, params, onDataListener);
//    }

    //钱包明细---------改为有刷新加载的请求 20170609修改
    public RequestHandle getWalletDetail(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                         int page, int requesTag, int showLoad, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("qianbao明细", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLETDETAIL);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //获取结算列表数据
//    public RequestHandle getSettlementList(OnDataListener onDataListener) {
//        String randomCode = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));
//
//        RequestParams params = new RequestParams();
//        params.add("func", FUNC_SETTLEMENTLIST);
//        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
//        return HttpManager.doPost(TAG_SETTLEMENTLIST, params, onDataListener);
//    }

    //获取结算列表数据---------改为有刷新加载的请求 20170609修改
    public RequestHandle getSettlementList(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                           int page, int requesTag, int showLoad) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SETTLEMENTLIST);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //获取结算列表中条目明细
//    public RequestHandle getSettlementItemDetail(OnDataListener onDataListener, String oddNum) {
//        String randomCode = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("结算单号", oddNum);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));
//
//        RequestParams params = new RequestParams();
//        params.add("func", FUNC_SETTLEMENTDETAIL);
//        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
//        return HttpManager.doPost(TAG_SETTLEMENTDETAIL, params, onDataListener);
//    }

    //获取结算列表中条目明细---------改为有刷新加载的请求 20170610修改
    public RequestHandle getSettlementItemDetail(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                                 int page, int requesTag, int showLoad, String oddNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.put("结算单号", oddNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SETTLEMENTDETAIL);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //去结算
    public RequestHandle gotoSettle(OnDataListener onDataListener, String lasttime, String thisTime, String jiaoyi, String daozhang, String personName, String totalNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("上次结算时间", lasttime);
        hashMap.put("本次结算时间", thisTime);
        hashMap.put("交易金额", jiaoyi);
        hashMap.put("实到金额", daozhang);
        hashMap.put("结算人", personName);
        hashMap.put("交易累计", totalNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GOTOSETTLE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GOTOSETTLE, params, onDataListener);
    }

    //获取提现说明（日结、周结、月结、笔结）
    public RequestHandle getTixianType(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETTIXIANEXPLAIN);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETTIXIANEXPLAIN, params, onDataListener);
    }

    //提现--手动
    public RequestHandle tixianManual(OnDataListener onDataListener, String point, String pwd, String bankName, String personName,
                                      String cardNum, String category, String type) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("积分", point);
        hashMap.put("密码", pwd);
        hashMap.put("银行名", bankName);
        hashMap.put("银行卡姓名", personName);
        hashMap.put("银行卡号", cardNum);
        hashMap.put("类别", category);//手动/自动
        hashMap.put("类型", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            L.e("手动提现到银行卡", e.getKey() + "-" + e.getValue());
        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_TIXIANMANUAL);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_TIXIANMANUAL, params, onDataListener);
    }

    //获取资质认证和支付密码状态(20170810修改，多传一个版本号的参数，用于区分旧版本，做限制)
    public RequestHandle getAuthenticationAndPaypwdState(OnDataListener onDataListener, String versionNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "获取资质认证状态相关信息");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_AUTHENTICATIONANDPAYPWDSTATE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_AUTHENTICATIONANDPAYPWDSTATE, params, onDataListener);
    }

    //获取油站的油品型号
    public RequestHandle getStationOilstype(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_STATIONOILSTYPE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_STATIONOILSTYPE, params, onDataListener);
    }

    //获取油品对应的油枪
    public RequestHandle getOilsguns(OnDataListener onDataListener, String oilsId, String stationId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油品id", oilsId);
        hashMap.put("油站id", stationId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_MYOILSGUNS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_MYOILSGUNS, params, onDataListener);
    }

    //添加油枪
    public RequestHandle addOilsguns(OnDataListener onDataListener, String stationId, String oilsId, String oilstype, String oilgunNum, String printerNum, String printerNumQiantai) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油站id", stationId);
        hashMap.put("油品id", oilsId);
        hashMap.put("油品型号", oilstype);
        hashMap.put("油枪号", oilgunNum);
        hashMap.put("打印机设备编码", printerNum);
        hashMap.put("前台编码", printerNumQiantai);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("添加油枪", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ADDOILSGUN);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ADDOILSGUN, params, onDataListener);
    }

    //删除油枪
    public RequestHandle deleteOilsguns(OnDataListener onDataListener, String oilgunId) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("油枪id", oilgunId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETEOILSGUN);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_DELETEOILSGUN, params, onDataListener);
    }

    //统计明细
//    public RequestHandle statisticsDetail(OnDataListener onDataListener, String year, String month) {
//        String randomCode = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("年份", year);
//        hashMap.put("月份", month);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));
//
//        RequestParams params = new RequestParams();
//        params.add("func", FUNC_STATISTICSDETAIL);
//        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
//        return HttpManager.doPost(TAG_STATISTICSDETAIL, params, onDataListener);
//    }

    //统计明细（按月份）---------改为有刷新加载的请求 20170610修改
    public RequestHandle statisticsDetail(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                          int page, int requesTag, int showLoad, String year, String month) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", 1 + "");
        }
        hashMap.put("年份", year);
        hashMap.put("月份", month);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_STATISTICSDETAIL);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(requesTag, params, onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //获取打印机编号数据
    public RequestHandle getPrinterData(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETPRINTERDATA);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETPRINTERDATA, params, onDataListener);
    }

    //结算打印
    public RequestHandle settlementOfPrint(OnDataListener onDataListener, String jiaoyi, String daozhang, String person, String totalNums, String settlementOdds, String startTime,
                                           String endTime, String printerNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("金额", jiaoyi);
        hashMap.put("到账金额", daozhang);
        hashMap.put("结算人", person);
        hashMap.put("交易累计", totalNums);
        hashMap.put("结算单号", settlementOdds);
        hashMap.put("开始时间", startTime);
        hashMap.put("结束时间", endTime);
        hashMap.put("打印设备编码", printerNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SETTLEMENTOFPRINT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_SETTLEMENTOFPRINT, params, onDataListener);
    }

    //消息--交易提醒--详情
    public RequestHandle getTradingDetail(OnDataListener onDataListener, String goodsOrderNum) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("商品订单号", goodsOrderNum);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("交易详情", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_TRADINGDETAIL);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_TRADINGDETAIL, params, onDataListener);
    }

    //钱包--明细--条目明细
    public RequestHandle getWalletListDetail(OnDataListener onDataListener, String id) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_WALLETLISTMINGXI);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_WALLETLISTMINGXI, params, onDataListener);
    }

    //交易--统计--年份
    public RequestHandle getStatisticsYears(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_STATISTICSYEARS);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_STATISTICSYEARS, params, onDataListener);
    }

    //交易--结算--结算确认
    public RequestHandle settlementConfirm(OnDataListener onDataListener, String lastTime) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("上次结算时间", lastTime);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_SETTLEMENTCONFIRM);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_SETTLEMENTCONFIRM, params, onDataListener);
    }

    //交易--今日收益--撤单
    public RequestHandle revoke(OnDataListener onDataListener, String oddNums, String yzm) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("商品订单号", oddNums);
        hashMap.put("验证码", yzm);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_REVOKE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_REVOKE, params, onDataListener);
    }

    //交易--今日收益--融云撤单
    public RequestHandle rongyunRevoke(OnDataListener onDataListener, String oddNums, String currentTime) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("商品订单号", oddNums);
        hashMap.put("当前时间", currentTime);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("融云撤单", e.getKey() + "-" + e.getValue());
//        }

        RequestParams params = new RequestParams();
        params.add("func", FUNC_RONGYUNREVOKE);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_RONGYUNREVOKE, params, onDataListener);
    }

    //获取老用户的资质认证信息
    public RequestHandle getQualificationInfoForOldUser(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_GETQUALIFICATIONINFO);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_GETQUALIFICATIONINFO, params, onDataListener);
    }

    // 新的提交资质信息（20170810修改）(20171020再次修改，多传入法人信息等及版本号)
    public RequestHandle newQualificationInfo(OnDataListener onDataListener, String shouchi, String front, String back,
                                              String name, String IDNum, String zhizhaoPic, String hezhaoPic,
                                              String farenFront, String farenBack, String farenShouchi, String fujiaCailiao, String versionCode) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("手持身份证照", shouchi);
        hashMap.put("身份证正面", front);
        hashMap.put("身份证背面", back);
        hashMap.put("姓名", name);
        hashMap.put("身份证号", IDNum);
        hashMap.put("执照图片", zhizhaoPic);
        hashMap.put("油站合影", hezhaoPic);
        hashMap.put("法人身份证正面", farenFront);
        hashMap.put("法人身份证反面", farenBack);
        hashMap.put("法人手持身份证照", farenShouchi);
        hashMap.put("附加材料", fujiaCailiao);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "新的资质认证");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_NEWQUALIFICATIONINFO);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_NEWQUALIFICATIONINFO, params, onDataListener);
    }

    // 资质认证---温馨提示
    public RequestHandle getZizhiWarmPrompt(OnDataListener onDataListener, HashMap<String, Object> hashMap) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "资质认证---温馨提示");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ZIZHIWARMPROMPT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_ZIZHIWARMPROMPT, params, onDataListener);
    }

    // 资质认证---上传照片
    public RequestHandle uploadingZizhiPhoto(OnDataListener onDataListener, HashMap<String, Object> hashMap) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "资质认证---上传照片");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ZIZHIUPLOADINGPHOTO);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));

        return HttpManager.doPost(TAG_ZIZHIUPLOADINGPHOTO, params, onDataListener);
    }

    //获取负责人姓名
    public RequestHandle getPrincipalName(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        RequestParams params = new RequestParams();
        params.add("func", FUNC_QUERYPRINCIPALNAME);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_QUERYPRINCIPALNAME, params, onDataListener);
    }

    //绑定支付宝账号
    public RequestHandle bindingAlipayAccount(OnDataListener onDataListener, String alipayAccount, String alipayName) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("支付宝账号", alipayAccount);
        hashMap.put("姓名", alipayName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "绑定支付宝账号");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_BINDINGALIPAYACCOUNT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_BINDINGALIPAYACCOUNT, params, onDataListener);
    }

    //获取已绑定的支付宝账号列表
    public RequestHandle getAlipayAccountList(OnDataListener onDataListener) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "获取已绑定的支付宝账号列表");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_ALIPAYACCOUNTLIST);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_ALIPAYACCOUNTLIST, params, onDataListener);
    }

    //解绑支付宝账号
    public RequestHandle removeAlipayAccount(OnDataListener onDataListener, String alipayAccount, String payPwd, String id) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("支付宝账号", alipayAccount);
        hashMap.put("登录密码", payPwd);
        hashMap.put("id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "解绑支付宝账号");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_DELETEALIPAYACCOUNT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_DELETEALIPAYACCOUNT, params, onDataListener);
    }

    //提取到支付宝
    public RequestHandle extractToAlipayAccount(OnDataListener onDataListener, String jifen, String payPwd, String alipayAccount, String alipayName,
                                                String leibie, String leixing) {
        String randomCode = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_RANDOMCODE, "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("版本号", versionName);
        hashMap.put("积分", jifen);
        hashMap.put("密码", payPwd);
        hashMap.put("支付宝账号", alipayAccount);
        hashMap.put("支付宝姓名", alipayName);
        hashMap.put("类别", leibie);
        hashMap.put("类型", leixing);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_INFO_JSON));

        showRequstInfo(hashMap, "提取到支付宝");

        RequestParams params = new RequestParams();
        params.add("func", FUNC_EXTRACTTOALIPAYACCOUNT);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        return HttpManager.doPost(TAG_EXTRACTTOALIPAYACCOUNT, params, onDataListener);
    }

}
