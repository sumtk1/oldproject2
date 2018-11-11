package com.gloiot.chatsdk.network.request;

import android.util.Log;

import com.gloiot.chatsdk.network.HttpManager;
import com.gloiot.chatsdk.network.OnDataListener;
import com.gloiot.chatsdk.network.utils.EnDecryptUtlis;
import com.gloiot.chatsdk.network.utils.JsonUtils;
import com.gloiot.chatsdk.socket.CallBackListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * 创建者 zengming.
 * 功能：网络请求集合类
 */
public class NetWorkRequest {

    private static CallBackListener.SendSingleMessageListener sendSingleMessageListener;

    public static void setSendSingleMessageListener(CallBackListener.SendSingleMessageListener sendSingleMessageListener) {
        NetWorkRequest.sendSingleMessageListener = sendSingleMessageListener;
    }

    /**
     * 发送单聊消息
     *
     * @param userMap        发送者ID（sendOutid）
     * @param userMap        发送者随机码（sendCode）
     * @param userMap        发送者昵称（sendName）
     * @param userMap        接收者账号（receiveId）
     * @param messagecontent 消息内容
     * @param msgType        消息类型
     * @param msgIdFse       前台定义的辨识消息id
     */
    public static void sendSingleMessage(final HashMap userMap, String messagecontent, String msgType, final String msgIdFse) {
        HashMap<String, Object> hashMapInfo = new HashMap<>();
        hashMapInfo.putAll(userMap);
        hashMapInfo.put("content", messagecontent);
        hashMapInfo.put("msgType", msgType);
        hashMapInfo.put("msgIdFse", msgIdFse);

        Iterator ite = hashMapInfo.keySet().iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            Log.e("-hashMapInfo-", key + "==>" + hashMapInfo.get(key));
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("func", "singleChat");
        hashMap.put("words", userMap.get("sendCode") + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMapInfo).toString(), (String) userMap.get("sendCode")));

        Iterator ite2 = hashMap.keySet().iterator();
        while (ite2.hasNext()) {
            String key = (String) ite2.next();
            Log.e("--hashMap--", key + "==>" + hashMap.get(key));
        }

        HttpManager.getRequestQueue().add(HttpManager.doPost(1, hashMap, new OnDataListener() {
            @Override
            public void onStart(int requestTag, int showLoad) {
                Log.e("----", "onStart: " + requestTag);
            }

            @Override
            public void onSuccess(int requestTag, JSONObject response, int showLoad) {
                Log.e("-----", "onSuccess: " + response);
                try{
                    if (response.getString("msg").equals("发送成功")){
                        sendSingleMessageListener.sendResult(response.toString());
                    } else {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentTime = sdf.format(new Date());
                        sendSingleMessageListener.sendResult("{\"msg\":\"请求失败\",\n" +
                                "    \"msgId\":\"" + msgIdFse + "\",\n" +
                                "    \"receiveId\":\"" + userMap.get("receiveId") + "\",\n" +
                                "    \"time\":\"" + currentTime + "\",\n" +
                                "    \"msgIdFse\":\"" + msgIdFse + "\"}");
                    }
                } catch (Exception e){

                }
            }

            @Override
            public void onFailure(int requestTag, int showLoad) {
                Log.e("-----", "onFailure: ");
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                sendSingleMessageListener.sendResult("{\"msg\":\"请求失败\",\n" +
                        "    \"msgId\":\"" + msgIdFse + "\",\n" +
                        "    \"receiveId\":\"" + userMap.get("receiveId") + "\",\n" +
                        "    \"time\":\"" + currentTime + "\",\n" +
                        "    \"msgIdFse\":\"" + msgIdFse + "\"}");
            }

            @Override
            public void onCancel(int requestTag, int showLoad) {

            }
        }, -1));
    }
}
