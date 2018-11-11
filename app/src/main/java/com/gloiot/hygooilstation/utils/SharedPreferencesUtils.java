package com.gloiot.hygooilstation.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by JinzLin on 2016/7/6.
 */
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils mSharedPreferencesUtlis;
    public static Context mContext;
    private SharedPreferences mPreferences;

    public static void init(Context context) {
        mContext = context;
        mSharedPreferencesUtlis = new SharedPreferencesUtils();
    }

    public static SharedPreferencesUtils getInstance() {

        if (mSharedPreferencesUtlis == null) {
            mSharedPreferencesUtlis = new SharedPreferencesUtils();
        }
        return mSharedPreferencesUtlis;
    }

    private SharedPreferencesUtils() {
        mSharedPreferencesUtlis = this;
        mPreferences = mContext.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }


    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static Integer getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void setInt(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(ConstantUtlis.MYSP, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

}
