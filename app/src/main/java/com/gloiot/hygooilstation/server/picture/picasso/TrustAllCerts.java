package com.gloiot.hygooilstation.server.picture.picasso;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 测试环境添加https全部信任
 * Created by Dlt on 2018/1/22 16:51
 */
public class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
