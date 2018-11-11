package com.gloiot.hygooilstation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.socket.LinkServer;
import com.gloiot.hygooilstation.im.ChatEvent;
import com.gloiot.hygooilstation.server.picture.picasso.ImageDownLoader;
import com.gloiot.hygooilstation.server.picture.picasso.TrustAllCerts;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.NoDoubleClickUtils;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by JinzLin on 2016/8/9.
 */
public class App extends Application {

    private static Context context;//全局context
    private List<Activity> mList = new LinkedList<Activity>();
    private static App instance;
    public static OkHttpClient sOkHttpClient;

    // 用于存放倒计时时间
    public static Map<String, Long> timeMap;
//    private static Socket mSocket;
//
//    {
//        try {
//            mSocket = IO.socket(UrlUtlis.MESSAGE_SERVER_URL);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static Socket getSocket() {
//        return mSocket;
//    }

    public synchronized static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 日志调试打印
         * 测试 true，正式 false。
         */
        L.isDebug = true;

        context = getApplicationContext();
        SharedPreferencesUtils.init(this);
        AutoLayoutConifg.getInstance().useDeviceSize(); // 自动适配
        ZXingLibrary.initDisplayOpinion(this);
        NoDoubleClickUtils.initLastClickTime();//初始化防止多次点击工具
        initImageLoader(getApplicationContext());
        initPicasso();//picsso https支持
//        CrashReport.initCrashReport(getApplicationContext(), "222ba3cd25", false);//Bugly初始化（20170619），在测试阶段建议设置成true，发布时设置为false

        // 连接消息服务器
        LinkServer.init();
        SocketEvent.init(this);

        ChatEvent.init(this);

    }

    /**
     * list集合中添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * list集合中删除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mList.remove(mList.size() - 1);
    }

    /**
     * 退出list集合中所有activity
     */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 获取list集合中最后一个activity
     */
    public Activity getLastActivity() {
        return mList.get(mList.size() - 1);
    }

    /**
     * 缓存图片
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "hygoilstation/ImageLoader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    /**
     * 初始化picasso使用okhttp作为网络请求框架
     */
    private void initPicasso() {
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(getSOkHttpClient())).loggingEnabled(true)
                .build());
    }

    /**
     * 创建全局OkHttpClient对象
     * <p>
     * OkHttpClient 用于管理所有的请求，内部支持并发，
     * 所以我们不必每次请求都创建一个 OkHttpClient 对象，这是非常耗费资源的。接下来就是创建一个 Request 对象了
     *
     * @return
     */
    public static OkHttpClient getSOkHttpClient() {
        //创建okhttp的请求对象 参考地址  http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

        if (sOkHttpClient == null) {
            sOkHttpClient =
                    new OkHttpClient.Builder()
                            .readTimeout(20000, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(20000, TimeUnit.SECONDS)//设置写的超时时间
                            .connectTimeout(20000, TimeUnit.SECONDS)//设置连接超时时间
                            .sslSocketFactory(createSSLSocketFactory())    //添加信任所有证书
                            .hostnameVerifier(new HostnameVerifier() {     //信任规则全部信任
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            })
                            .build();
        }
        return sOkHttpClient;
    }

    /**
     * 测试环境https添加全部信任
     * okhttp的配置
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

}
