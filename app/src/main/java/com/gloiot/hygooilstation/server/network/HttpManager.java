package com.gloiot.hygooilstation.server.network;

import com.gloiot.hygooilstation.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.gloiot.hygooilstation.utils.UrlUtlis;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by JinzLin on 2016/8/9.
 */
public class HttpManager {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String URL = UrlUtlis.URL;

    private static HttpManager httpManager;

    public static void init(String url) {
        URL = url;
    }

    public static HttpManager getInstance() {
//        if (httpManager == null){
        httpManager = new HttpManager();
//        }
        return httpManager;
    }

    /**
     * 普通请求（无分页刷新加载）
     *
     * @param requestTag
     * @param params
     * @param onDataListener
     * @return
     */
    public static RequestHandle doPost(final int requestTag, RequestParams params, final com.gloiot.hygooilstation.server.network.OnDataListener onDataListener) {
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 5000);
        return client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, 0);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onDataListener.onSuccess(requestTag, response, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onDataListener.onFailure(requestTag, errorResponse, 0);
            }
        });
    }


    /**
     * 有分页刷新加载的请求方式(用到pullToRefreshLayout)
     *
     * @param requestTag
     * @param params
     * @param onDataListener
     * @param showLoad            加载框状态， 0表示弹出收起，1表示只弹出，2表示只收起
     * @param pullToRefreshLayout
     * @param requsetType         当数据位0时表示普通请求，为1时表示刷新请求，为2时表示加载请求
     * @return
     */
    public static RequestHandle doPost(final int requestTag, RequestParams params, final com.gloiot.hygooilstation.server.network.OnDataListener onDataListener, final int showLoad,
                                       final PullToRefreshLayout pullToRefreshLayout, final int requsetType) {
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 5000);
        return client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (requsetType == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
//                else if (requsetType == 2){
//                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
                onDataListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (requsetType == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                } else if (requsetType == 2) {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                onDataListener.onFailure(requestTag, errorResponse, showLoad);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                onDataListener.onCancel(requestTag, showLoad);
            }
        });
    }


    /**
     * 有分页刷新加载的请求方式（不用pullToRefreshLayout）
     *
     * @param requestTag
     * @param params
     * @param onDataListener
     * @param showLoad       加载框状态， 0表示弹出收起，1表示只弹出，2表示只收起
     * @param requsetType    当数据位0时表示普通请求，为1时表示刷新请求，为2时表示加载请求
     * @return
     */
    public static RequestHandle doPost(final int requestTag, RequestParams params, final com.gloiot.hygooilstation.server.network.OnDataListener onDataListener, final int showLoad,
                                       final int requsetType) {
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 5000);
        return client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onDataListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onDataListener.onFailure(requestTag, errorResponse, showLoad);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                onDataListener.onCancel(requestTag, showLoad);
            }
        });
    }

}
