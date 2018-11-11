package com.gloiot.hygooilstation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.hygooilstation.App;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.OnDataListener;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.login.LoginActivity;
import com.gloiot.hygooilstation.ui.widget.LoadDialog;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.EasyPermissions;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyLoadingDialog;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by JinzLin on 2016/8/6.
 */
public abstract class BaseActivity extends AutoLayoutActivity implements OnDataListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "BaseActivity";
    protected static final int RC_PERM = 123;

    protected Context mContext;

    protected RequestAction requestAction;
    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();

    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;

    private MyLoadingDialog myLoadingDialog;
    private MyDialogBuilder myDialogBuilder;

    public void setRequestErrorCallback(RequestErrorCallback requestErrorCallback) {
        this.requestErrorCallback = requestErrorCallback;
    }

    protected RequestErrorCallback requestErrorCallback;

    // 请求状态！=成功回调
    public interface RequestErrorCallback {
        void requestErrorcallback(int requestTag, JSONObject response) throws Exception;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        editor = preferences.edit();
        requestAction = new RequestAction();
        App.getInstance().addActivity(this);
        setContentView(initResource());
        ButterKnife.bind(this);
        initComponent();
        initData();

        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().removeActivity(this);
        cancelRequestHandle();
        requestErrorCallback = null;
    }

    /**
     * 初始化布局资源文件
     */
    public abstract int initResource();

    /**
     * 初始化组件
     */
    public abstract void initComponent();

    /**
     * 初始化数据
     */
    public abstract void initData();


    //判断是否登录
    public boolean check_login() {
        if (preferences.getString(ConstantUtlis.SP_LOGINSTATE, "").equals("成功")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 请求开始
     *
     * @param requestTag 请求标志
     */
    @Override
    public void onStart(int requestTag, int showLoad) {
        L.d(TAG, "onStart: " + requestTag);
        if (showLoad == 0 || showLoad == 1) {
            LoadDialog.show(mContext, requestHandleArrayList);
        }
    }

    /**
     * 请求成功(过滤 状态=成功)
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     * @throws JSONException
     */
    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

    }

    /**
     * 请求成功
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     */
    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        L.d(TAG, "requestTag: " + requestTag + " onSuccess: " + response);
        if (showLoad == 0 || showLoad == 2) {
            LoadDialog.dismiss(mContext);
        }
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else if (response.getString("状态").equals("随机码不正确")) {
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.setCancelable(false);//设置返回键不可点击
                myDialogBuilder
                        .withIcon(R.mipmap.iconfont_gantanhao)
                        .withContene("该账号在其他设备登录\n请您重新登录")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_LOGINSTATE, "失败");
                                startActivity(new Intent(mContext, LoginActivity.class));
                                App.getInstance().exit();//这里不要简单的finish，而应该退出整个应用
                            }
                        }).show();

            } else {
                if (requestErrorCallback != null) {
                    requestErrorCallback.requestErrorcallback(requestTag, response);
                } else {
                    MToast.showToast(mContext, response.getString("状态"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求失败
     *
     * @param requestTag    请求标志
     * @param errorResponse 错误请求返回
     */
    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        L.e(TAG, "onFailure: " + requestTag + " errorResponse: " + errorResponse);
//        MToast.showToast(mContext, "请求超时,请检查你的网络!");
        MToast.showToast(mContext, "网络好像有点问题，请检查后重试");
        LoadDialog.dismiss(mContext);
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {

    }

    /**
     * 取消网络请求
     */
    public void cancelRequestHandle() {
        if (requestHandleArrayList.size() != 0) {
            for (int i = 0; i < requestHandleArrayList.size(); i++) {
                requestHandleArrayList.get(i).cancel(true);
            }
        }
    }


    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms);
    }

}
