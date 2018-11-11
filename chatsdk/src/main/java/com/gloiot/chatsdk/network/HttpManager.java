package com.gloiot.chatsdk.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 网络连接管理类
 */
public class HttpManager {

    private static String URL;

    private static RequestQueue mRequestQueue;

    public static void init(Context context, String url) {
        URL = url;
        if (mRequestQueue == null) {
            synchronized (HttpManager.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        mRequestQueue.start();
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            throw new RuntimeException("请先初始化mRequestQueue");
        return mRequestQueue;
    }

    public static Request doPost(final int requestTag, final Map<String, String> map, final OnDataListener onDataListener, final int showLoad) {
        onDataListener.onStart(requestTag, showLoad);

        Request request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onDataListener.onSuccess(requestTag, jsonObject, showLoad);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onDataListener.onFailure(requestTag, showLoad);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        request.setTag(requestTag);

        return request;
    }
}
