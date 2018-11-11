package com.gloiot.hygooilstation.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.App;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.OnDataListener;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.login.LoginActivity;
import com.gloiot.hygooilstation.ui.activity.oilstation.ModifyOilstationActivity;
import com.gloiot.hygooilstation.ui.activity.oilstation.MyOilgunActivity;
import com.gloiot.hygooilstation.ui.activity.oilstation.MyolisActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.GlideImageLoader;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyLoadingDialog;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.loopj.android.http.RequestHandle;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gloiot.hygooilstation.utils.SharedPreferencesUtils.mContext;

/**
 * 油站
 * Created by dlt on 2016/9/14.
 */

public class OilstationFragment extends Fragment implements View.OnClickListener, OnDataListener {

    public static final String TAG = OilstationFragment.class.getSimpleName();

    private ImageView iv_myoilstation_bianji;
    private TextView tv_myOilstation_phoneNum, tv_myOilstation_location, tv_myOilstation_jianjie;
    private AutoRelativeLayout rl_myOilstation_myoil, rl_myOilstation_02;
    private RelativeLayout rl_myOilstation_myOilgun;
    private Banner myOilstation_banner;
    protected RequestAction requestAction;
    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();
    private MyLoadingDialog myLoadingDialog;
    private ArrayList<String> list = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private String stationName, phoneNum, stationLocation, stationIntro, stationId, stationLatitude, stationLongititude, stationArea;
    private String stationCoordinateState = "";//油站坐标标记状态
    private int picsAmount = -1;//初始化为-1
    private MyDialogBuilder myDialogBuilder;
    private boolean hasStarted = false;//判断是否是初始化Fragment
    private String accountType;

    public static Fragment newInstance(int position) {
        OilstationFragment fragment = new OilstationFragment();

        return fragment;
    }

    public OilstationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (SharedPreferencesUtils.getBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, false)) {
            list.clear();
            titleList.clear();

            requestHandleArrayList.add(requestAction.getoilstationInfo(this, getActivity()));
            SharedPreferencesUtils.setBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, false);
        } else {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oilstation, container, false);
        initComponent(view);
        initData();
        return view;
    }

    private void initComponent(View view) {
        iv_myoilstation_bianji = (ImageView) view.findViewById(R.id.iv_myoilstation_bianji);
        tv_myOilstation_phoneNum = (TextView) view.findViewById(R.id.tv_myOilstation_phoneNum);
        tv_myOilstation_location = (TextView) view.findViewById(R.id.tv_myOilstation_location);
        tv_myOilstation_jianjie = (TextView) view.findViewById(R.id.tv_myOilstation_jianjie);
        rl_myOilstation_myoil = (AutoRelativeLayout) view.findViewById(R.id.rl_myOilstation_myoil);
        rl_myOilstation_myOilgun = (RelativeLayout) view.findViewById(R.id.rl_myOilstation_myOilgun);
        rl_myOilstation_02 = (AutoRelativeLayout) view.findViewById(R.id.rl_myOilstation_02);
        myOilstation_banner = (Banner) view.findViewById(R.id.myOilstation_banner);
    }

    private void initData() {
        requestAction = new RequestAction();
        accountType = SharedPreferencesUtils.getString(getActivity(), ConstantUtlis.SP_ACCOUNTTYPE, "");
        if (accountType.equals("收银员")) {
            iv_myoilstation_bianji.setVisibility(View.GONE);
            rl_myOilstation_myoil.setOnClickListener(this);
        } else if (accountType.equals("站长")) {
            iv_myoilstation_bianji.setVisibility(View.VISIBLE);
            iv_myoilstation_bianji.setOnClickListener(this);
            rl_myOilstation_myoil.setOnClickListener(this);
            tv_myOilstation_phoneNum.setOnClickListener(this);
            tv_myOilstation_location.setOnClickListener(this);
        }
        rl_myOilstation_myOilgun.setOnClickListener(this);

//        tv_myOilstation_jianjie.setOnClickListener(this);
        rl_myOilstation_02.setOnClickListener(this);
        requestHandleArrayList.add(requestAction.getoilstationInfo(this, getActivity()));//初始化数据的时候必须请求一次数据
        SharedPreferencesUtils.setBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, false);
    }

    private void setBannerdata() {
        //设置banner样式
        myOilstation_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        myOilstation_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        myOilstation_banner.setImages(list);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        //设置banner动画效果
        myOilstation_banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        myOilstation_banner.setBannerTitles(titleList);
        //设置自动轮播，默认为true
        myOilstation_banner.isAutoPlay(true);
        //设置轮播时间
        myOilstation_banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        myOilstation_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        myOilstation_banner.start();
    }

    /**
     * 当没有油站图片时默认显示一张油站图片
     */
    private void setDefaultOilstaionPic() {
        List<String> defaultPicList = new ArrayList<>();
        List<String> defaultTitleList = new ArrayList<>();

        defaultTitleList.add(stationName);
        String defaultOilstationPic = "http://zyk-zyy.oss-cn-shenzhen.aliyuncs.com/2017051912110043003.jpg";//默认图片的阿里云固定地址
        defaultPicList.add(defaultOilstationPic);

        //设置banner样式
        myOilstation_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        myOilstation_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        myOilstation_banner.setImages(defaultPicList);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        //设置banner动画效果
        myOilstation_banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        myOilstation_banner.setBannerTitles(defaultTitleList);
        //设置自动轮播，默认为true
        myOilstation_banner.isAutoPlay(true);
        //设置轮播时间
        myOilstation_banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        myOilstation_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        myOilstation_banner.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_myOilstation_phoneNum:
            case R.id.tv_myOilstation_location:
            case R.id.rl_myOilstation_02:
            case R.id.iv_myoilstation_bianji:
                Intent intent = new Intent(getActivity(), ModifyOilstationActivity.class);
                intent.putExtra("stationId", stationId);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("stationLocation", stationLocation);
                intent.putExtra("stationIntro", stationIntro);
                intent.putExtra("stationLatitude", stationLatitude);//纬度
                intent.putExtra("stationLongititude", stationLongititude);//经度
                intent.putExtra("stationArea", stationArea);
                intent.putStringArrayListExtra("picList", list);//把图片list传过去
                startActivity(intent);
                SharedPreferencesUtils.setBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, true);
                break;
            case R.id.rl_myOilstation_myoil:
                Intent intent1 = new Intent(getActivity(), MyolisActivity.class);
                intent1.putExtra("id", stationId);
                intent1.putExtra("stationName", stationName);
                startActivity(intent1);
//                SharedPreferencesUtlis.setBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, true);//返回不需要刷新数据
                break;
            case R.id.rl_myOilstation_myOilgun:
                startActivity(new Intent(getActivity(), MyOilgunActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void onStart(int requestTag, int showLoad) {
        myLoadingDialog = MyLoadingDialog.getInstance(getActivity());
        myLoadingDialog.show();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        if (myLoadingDialog != null) {
            myLoadingDialog.dismiss();
        }
        switch (requestTag) {
            case RequestAction.TAG_OILSTATIONINFO:
                Log.e("油站信息", response.toString());
                picsAmount = Integer.parseInt(response.getString("条数"));
                stationName = response.getString("油站名称");
                stationId = response.getString("油站id");
                SharedPreferencesUtils.setString(getActivity(), ConstantUtlis.SP_STATIONNAME, stationName);//将油站名称存到本地
                SharedPreferencesUtils.setString(getActivity(), ConstantUtlis.SP_STATIONID, stationId);//将油站id存到本地
                phoneNum = response.getString("油站电话");
                stationLocation = response.getString("油站地址");
                stationIntro = response.getString("油站介绍");
                stationLatitude = response.getString("纬度");
                stationLongititude = response.getString("经度");
                stationCoordinateState = response.getString("标记状态");//油站坐标是否标记，取值：已标记/未标记  20171024修改
                stationArea = response.getString("油站区域");
                SharedPreferencesUtils.setString(getActivity(), ConstantUtlis.SP_AUTHENTIFICATIONSTATE, response.getString("资质认证状态"));//认证状态
                tv_myOilstation_phoneNum.setText(phoneNum);
                tv_myOilstation_location.setText(stationLocation);
                tv_myOilstation_jianjie.setText(stationIntro);
//                Log.e("油站图片条数", picsAmount + "");
                if (picsAmount > 0) {
//                    String catchSize = GlideCacheUtil.getInstance().getCacheSize(getActivity());
//                    Log.e("获取Glide缓存reques=", catchSize);
//                    GlideCacheUtil.getInstance().clearImageAllCache(getActivity());
//                    Log.e("清除Glide缓存后，requess=", GlideCacheUtil.getInstance().getCacheSize(getActivity()));
                    JSONArray jsonArray = response.getJSONArray("油站图片信息");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String a = jsonObject.getString("图片");
                        list.add(a);
//                        Log.e("油站图片", list.get(i));
                        titleList.add(stationName);
                    }
                    myOilstation_banner.setVisibility(View.VISIBLE);
                    setBannerdata();
                    picsAmount = -1;//重置
                } else {
//                    myOilstation_banner.setVisibility(View.GONE);
                    myOilstation_banner.setVisibility(View.VISIBLE);
                    setDefaultOilstaionPic();
                    picsAmount = -1;//重置
                }

                if (stationCoordinateState.equals("未标记")) {
                    myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                    myDialogBuilder.setCancelable(false);//设置返回键不可点击
                    myDialogBuilder
                            .withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("请先标记油站坐标后方可正常使用功能")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    Intent intent = new Intent(getActivity(), ModifyOilstationActivity.class);
                                    intent.putExtra("stationId", stationId);
                                    intent.putExtra("phoneNum", phoneNum);
                                    intent.putExtra("stationLocation", stationLocation);
                                    intent.putExtra("stationIntro", stationIntro);
                                    intent.putExtra("stationLatitude", stationLatitude);//纬度
                                    intent.putExtra("stationLongititude", stationLongititude);//经度
                                    intent.putExtra("stationArea", stationArea);
                                    intent.putStringArrayListExtra("picList", list);//把图片list传过去
                                    startActivity(intent);
                                    SharedPreferencesUtils.setBoolean(getActivity(), ConstantUtlis.SP_ISNEEDTOUPDATESTATIONDATA, true);

                                }
                            }).show();
                }

                break;
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else {
                if (myLoadingDialog != null) {
                    myLoadingDialog.dismiss();
                }
                if (response.getString("状态").equals("随机码不正确")) {
                    myDialogBuilder = MyDialogBuilder.getInstance(getActivity());
                    myDialogBuilder.setCancelable(false);//设置返回键不可点击
                    myDialogBuilder
                            .withIcon(R.mipmap.iconfont_gantanhao)
                            .withContene("该账号在其他设备登录\n请您重新登录")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    SharedPreferencesUtils.setString(getActivity(), ConstantUtlis.SP_LOGINSTATE, "失败");
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    App.getInstance().exit();//这里不要简单的finish，而应该退出整个应用
                                }
                            }).show();

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

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        if (myLoadingDialog != null) {
            myLoadingDialog.dismiss();
        }
//        MToast.showToast(mContext, "请求失败");
        MToast.showToast(mContext, "网络好像有点问题，请检查后重试");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelRequestHandle();
    }
}
