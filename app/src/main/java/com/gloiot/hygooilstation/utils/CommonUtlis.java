package com.gloiot.hygooilstation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by JinzLin on 2016/9/13.
 */

public class CommonUtlis {

    private static SharedPreferences preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
    private static SharedPreferences.Editor editor = preferences.edit();


    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     * @param tvmore
     */
    public static void setTitleBar(final Activity context, boolean isBack, String titleString, String tvmore) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }


    }

    /**
     * 验证身份证号是否符合规范
     *
     * @param s
     * @return
     */
    public static boolean isIDCardNumTrue(String s) throws ParseException {

        IDCard ic = new IDCard();
        if (ic.IDCardValidate(s).equals("")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 保存HashMap
     *
     * @param status
     * @param hashMap
     * @return
     */
    public static boolean saveMap(String status, HashMap<String, Object> hashMap) {
        editor.putInt(status, hashMap.size());
        int i = 0;
        for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
            editor.remove(status + "_key_" + i);
            editor.putString(status + "_key_" + i, entry.getKey());
            editor.remove(status + "_value_" + i);
            editor.putString(status + "_value_" + i, (String) entry.getValue());
//            Log.e("save", entry.getKey()+"="+entry.getValue());
            i++;
        }
        return editor.commit();
    }

    /**
     * 取出HashMap
     *
     * @param status
     * @return
     */
    public static HashMap<String, Object> loadMap(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int size = preferences.getInt(status, 0);
        for (int i = 0; i < size; i++) {
            String key = preferences.getString(status + "_key_" + i, null);
            String value = preferences.getString(status + "_value_" + i, null);
            hashMap.put(key, value);
        }
        return hashMap;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 显示加载中
     *
     * @param context
     */
    public static void showLoading(Activity context) {
//        RelativeLayout loading = (RelativeLayout) context.findViewById(R.id.in_loading);
//        loading.setClickable(true);
//        loading.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载中
     *
     * @param context
     */
    public static void unShowLoading(Activity context) {
//        RelativeLayout loading = (RelativeLayout) context.findViewById(R.id.in_loading);
//        loading.setVisibility(View.GONE);
    }

    /**
     * encode编译URL
     *
     * @param kv
     * @return
     */
    public static String encodeUtli(String kv) {
        try {
            kv = java.net.URLEncoder.encode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }

    /**
     * decode编译URL
     *
     * @param kv
     * @return
     */
    public static String decodeUtli(String kv) {
        try {
            kv = java.net.URLDecoder.decode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }

    /**
     * 根据当前的ListView的列表项计算列表的尺寸
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 被ScrollView包含的GridView高度设置为wrap_content时只显示一行
     * 此方法用于动态计算GridView的高度(根据item的个数)
     */
    public static void reMesureGridViewHeight(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);

            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    /**
     * 设置图片(自定义获取失败显示头像)
     *
     * @param imageView
     * @param imgUrl
     */
    public static void setDisplayImage(ImageView imageView, String imgUrl, int round, int resid) {

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resid) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resid) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resid) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 构建完成

        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
