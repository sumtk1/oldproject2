package com.gloiot.hygooilstation.ui.activity;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.utils.CommonUtlis;

public class WebViewActivity extends BaseActivity {

    private WebView myWebView;
    private String title, url;

    @Override
    public int initResource() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initComponent() {
        myWebView = (WebView) findViewById(R.id.myWebView);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        CommonUtlis.setTitleBar(this, true, title, "");

        processData();
    }

    private void processData() {

        myWebView.loadUrl(url);
        WebSettings webSettings = myWebView.getSettings();
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);          //支持缩放
        webSettings.setJavaScriptEnabled(true);    //启用JS脚本
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}
