package com.gloiot.hygooilstation.utils;

/**
 * Created by dlt on 2016/11/2.
 */

public class ClickFilter {

//    public static final long INTERVAL = 1000L; //防止连续点击的时间间隔
//    private static long lastClickTime = 0L; //上一次点击的时间
//
//    public static boolean filter() {
//        long time = System.currentTimeMillis();
//        lastClickTime = time;
//        if ((time - lastClickTime) > INTERVAL) {
//            return false;
//        }
//        return true;
//    }

    private static long lastClickTime;
    public static boolean filter() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
