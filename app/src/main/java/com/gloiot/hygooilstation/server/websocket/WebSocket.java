package com.gloiot.hygooilstation.server.websocket;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.gloiot.hygooilstation.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by JinzLin on 2016/9/28.
 */

public class WebSocket {

    public static Context mContext;
    private Boolean isConnected = false;    // 判断是否连接
    private String EVENT_LINKREPEAT = "LinkRepeat"; // 其他设备登录监听
    private String EVENT_AUTH_RESULT = "authResult";    // 认证监听
    private String EVENT_MESSAGE = "zykmessage";    // 消息监听
    private MessageCallback messageCallback;    // 消息回调
    private Socket mSocket;

    private String userid;  // 用户id
    private String random; // 随机码

    public volatile static WebSocket instance;

    public static WebSocket getInstance(Context context, String userid, String random) {

        if (instance == null) {
            synchronized (WebSocket.class) {
                if (instance == null) {
                    instance = new WebSocket(context, userid, random);
                }
            }
        }
        return instance;
    }


    public WebSocket(Context context, String userid, String random) {
        this.userid = userid;
        this.random = random;
        mContext = context;
        if (!isConnected) {
//            mSocket = App.getSocket();
            initEvent();
        }

    }


    private void initEvent() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);                    // 连接
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);              // 断开连接
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);         // 连接错误
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);       // 连接超时


        mSocket.on(Socket.EVENT_RECONNECT, onReconnect);                // 重新连接
        mSocket.on(Socket.EVENT_ERROR, onError);                        // 连接错误（暂无调用）
        mSocket.connect();
    }

    private void initEvent2() {
        mSocket.on(EVENT_AUTH_RESULT, onAuthResult);                    // 用户认证
        mSocket.on(EVENT_MESSAGE, onMessage); // 监听消息

        mSocket.on(EVENT_LINKREPEAT, onLinkRepeat);
    }

    // onError-自带
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("------------", "onError");
                }
            });
        }
    };

    // onReconnect-自带
    private Emitter.Listener onReconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("------------", "onReconnect");
                }
            });
        }
    };


    // 连接成功-自带
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        Log.e("--", "已连接");
                        i = 0;
                        isConnected = true;
                        initEvent2();

                        JSONObject json = new JSONObject();
                        try {
                            json.put("userid", userid);
                            json.put("random", random);

                            Log.e("---", "用户认证");
                            mSocket.emit("auth", json.toString()); // 发送验证消息
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    };


    // 用户认证监听
    private Emitter.Listener onAuthResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("onAuthResult", "" + args[0]);
                    if (args[0].equals("认证成功")) {
                        mSocket.off(EVENT_AUTH_RESULT, onAuthResult); // 认证成功断开监听
                    }
                }
            });
        }
    };

    // 消息监听
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msgid = args[0].toString();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("msgid", msgid);
                        json.put("resultid", MD5Util.Md5(msgid));
                        Log.e("json.toString()", json.toString());
                        mSocket.emit("messageReceipt" + msgid, json.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (messageCallback != null) {
                        messageCallback.onSuccess(msgid, args[1].toString());
                    }
                }
            });
        }
    };


    // 连接断开
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("--", "断开连接，请检查你的网络连接");
                    cancelMessageEvent();
                }
            });
        }
    };

    // 连接错误
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("--", "连接出错");
//                    Toast.makeText(mContext, "连接出错", Toast.LENGTH_LONG).show();
                    cancelMessageEvent();
                }
            });
        }
    };


    // 其他设备连接
    private Emitter.Listener onLinkRepeat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("--", "连接重复");
                    isConnected = false;
//                    Toast.makeText(mContext, "其他设备登录", Toast.LENGTH_LONG).show();
                    cancelMessageEvent();
                    cancelSocket();
                }
            });
        }
    };


    int i = 0;

    // 取消消息监听关闭连接
    public void cancelMessageEvent() {
        isConnected = false;
        if (i == 0) {
            mSocket.off(EVENT_MESSAGE, onMessage);
            i++;
        }
    }

    public void cancelSocket() {
        isConnected = false;

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        mSocket.off(Socket.EVENT_ERROR, onError);         // 连接错误
        mSocket.off(Socket.EVENT_RECONNECT, onReconnect);

        mSocket.off(EVENT_LINKREPEAT, onLinkRepeat);
        instance = null;
    }


    // 监听消息回调
    public void mCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public interface MessageCallback {
        void onSuccess(String msgid, String data);

        void onFail(String data);
    }
}
