package com.gloiot.hygooilstation.im;

import android.content.Context;
import android.util.Log;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygooilstation.server.network.OnDataListener;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dlt on 2017/6/15 14:53
 */
public class UserInfoManager implements OnDataListener {

    protected ArrayList<RequestHandle> requestHandleArrayList;
    protected RequestAction requestAction;
    private static UserInfoManager instance;
    private Context context;

    private UserInfoManager(Context context) {
        requestHandleArrayList = new ArrayList<>();
        requestAction = new RequestAction();
        this.context = context;
    }

    public static UserInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfoManager(context);
        }
        return instance;
    }

    //联网获取用户信息
    public void getUserInfo(String userId) {
//        if (userId.contains("商家")){
        UserInfoCache.getInstance(context).putData(userId, new UserInfo(userId, "", ""));//存入缓存
//        requestHandleArrayList.add(requestAction.GetUserInfo(this, userId, "用户"));
        requestHandleArrayList.add(requestAction.getoilstationInfo(this, context));
//        } else {
//            requestHandleArrayList.add(requestAction.GetUserInfo(this, userId, "客户"));
//        }
    }


    @Override
    public void onStart(int requestTag, int showLoad) {

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        Log.e("1471471", "onSuccess: " + response.toString());
        switch (requestTag) {
            case RequestAction.TAG_OILSTATIONINFO://原来是获取用户信息。这里只是为了不报错，先找个TAG.
                try {
                    if ("成功".equals(response.getString("状态"))) {
                        JSONArray array = response.getJSONArray("userInfo");
                        if (array.length() == 0) {
                            return;
                        }
                        UserInfo info = new UserInfo();
                        JSONObject object = (JSONObject) array.get(0);
                        info.setName(object.getString("name"));
                        info.setId(object.getString("id"));
                        info.setUrl(object.getString("icon"));
                        UserInfoCache.getInstance(context).putData(object.getString("id"), info);//存入缓存
                        // TODO: 2017/5/26   可以考虑新开线程存储数据，或者用线程池，如果阻塞时间过长的话。
                        IMDBManager.getInstance(context).insertUserInfo(info);//存入数据库

                        BroadcastManager.getInstance(context).sendBroadcast(MessageManager.REFRESH_USERINFO, response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {

    }

    @Override
    public void onCancel(int requestTag, int showLoad) {

    }
}
