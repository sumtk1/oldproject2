package com.gloiot.hygooilstation.ui.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.AutoLayoutActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.gloiot.hygooilstation.utils.map.AMapUtil;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 油站坐标
 */
public class StationLatLngActivity extends AutoLayoutActivity implements GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener
        , AMap.OnMapClickListener, AMap.OnMapLongClickListener, AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMarkerDragListener {

    public static final int GETDETAIL_FINISH = 1;

    private Context mContext;
    private TextView tv_toptitle_right;
    private TextView tv_onclickLocation, tv_onclickLocationInfo;
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;//初次地理编码或逆地理编码的返回值
    private String addressNameFinish;//提交时坐标点对应的逆地理编码返回值
    private AMap aMap;
    private MapView mapView;
    private Marker mMarker;
    private MarkerOptions mMarkerOption;

    private String type, keywords, originalProvince, originalCity, originalDistrict, originalLat, originalLng;

    //逆地理编码
//    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private LatLonPoint latLonPoint;

    private String stationLat, stationLon;//获取到的纬度，经度
    private LatLng mLatLng;//全局经纬度对象

    private RegeocodeResult mResult;

    private boolean isFordetailInfo = false;//是否仅仅为了获取省市区信息。true的话只为获取结果，false需要操作地图．

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETDETAIL_FINISH:
//                    Log.e("油站经纬度提交时", "stationLat--" + stationLat + ",stationLon--" + stationLon + ",locationDetailInfo--" + addressNameFinish);
//                    Log.e("油站经纬度提交时", "province--" + SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONSHENG, "") + ",city--" +
//                            SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONSHI, "") + ",district--" +
//                            SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONQU, ""));
                    SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, true);//保证区域及时刷新
                    StationLatLngActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_lat_lng);

        mContext = this;
        CommonUtlis.setTitleBar(this, true, "油站区域", "确定");

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("dili")) {
            keywords = intent.getStringExtra("keywords");
        }
        originalProvince = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHENG, "");
        originalCity = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHI, "");
        originalDistrict = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONQU, "");
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_onclickLocation = (TextView) findViewById(R.id.tv_onclickLocation);
        tv_onclickLocationInfo = (TextView) findViewById(R.id.tv_onclickLocationInfo);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
//            LatLng latLng = new LatLng(39.906901, 116.397972);

            //            originalLat = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "39.906901");
//            originalLng = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "116.397972");

            if (TextUtils.isEmpty(SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "39.906901"))) {
                originalLat = "39.906901";
            } else {
                originalLat = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "39.906901");
            }
            if (TextUtils.isEmpty(SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "116.397972"))) {
                originalLng = "116.397972";
            } else {
                originalLng = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "116.397972");
            }

//            Log.e("初始化地图时经纬度--", originalLat + "--" + originalLng + "---");

            mLatLng = new LatLng(Double.parseDouble(originalLat), Double.parseDouble(originalLng));
            addMarkersToMap(mLatLng);
            setUpMap();
        }

        tv_toptitle_right.setOnClickListener(this);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

        if (type.equals("dili")) {//如果是地理编码
            getLatlon(keywords);
        } else if (type.equals("nidili")) {//逆地理编码

//            String lat = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "23.906901");
//            String lng = SharedPreferencesUtlis.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "115.397972");

            if (!TextUtils.isEmpty(originalLat) && !TextUtils.isEmpty(originalLng)) {

                double originalLatd = Double.parseDouble(originalLat);
                double originalLngd = Double.parseDouble(originalLng);

                latLonPoint = new LatLonPoint(originalLatd, originalLngd);

                getAddress(latLonPoint);

            }
        }

    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng) {

        mMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        mMarker = aMap.addMarker(mMarkerOption);
        mLatLng = latlng;
    }

    /**
     * amap添加一些事件监听器
     */
    private void setUpMap() {
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        aMap.setOnMarkerClickListener(this);//绑定标注点击事件
        aMap.setOnMarkerDragListener(this);//绑定标记拖拽事件
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        showDialog();

        GeocodeQuery query = new GeocodeQuery(name, originalCity);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        //注意这里城市不能写死
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {//逆地理编码时要展示地图，为true，只为获取位置信息，不用显示地图，为false。不用。理解错了。
        showDialog();

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火星坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /**
             * 确定提交
             */
            case R.id.tv_toptitle_right:

                stationLat = mLatLng.latitude + "";
                stationLon = mLatLng.longitude + "";
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLATITUDE, stationLat);//纬度
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, stationLon);//经度
//                SharedPreferencesUtlis.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONLATLNGRESET, true);
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, mResult.getRegeocodeAddress().getFormatAddress()
//                        + "附近");
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHENG, mResult.getRegeocodeAddress().getProvince());
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHI, mResult.getRegeocodeAddress().getCity());
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONQU, mResult.getRegeocodeAddress().getDistrict());
//
//                Intent intent = new Intent(this, StationLocationDetailActivity.class);
//                intent.putExtra("lat", stationLat);
//                intent.putExtra("lng", stationLon);
//                startActivity(intent);

                LatLonPoint latLonPointFinish = new LatLonPoint(mLatLng.latitude, mLatLng.longitude);
                isFordetailInfo = true;
                getAddress(latLonPointFinish);//这里的执行不在主线程，应该用handler处理结果
//                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                if (isFordetailInfo) {
//                    Log.e("逆地理回调确定提交时", "here------------1");
                    addressNameFinish = result.getRegeocodeAddress().getFormatAddress()
                            + "";
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, addressNameFinish);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHENG, result.getRegeocodeAddress().getProvince());
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHI, result.getRegeocodeAddress().getCity());
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONQU, result.getRegeocodeAddress().getDistrict());
                    isFordetailInfo = false;//重置
//                    Log.e("确定提交时逆地理回调", "addressNameFinish--" + addressNameFinish);

                    mHandler.sendEmptyMessage(GETDETAIL_FINISH);

                } else {
//                    Log.e("逆地理回调初次定位", "here------------0");
                    mResult = result;
                    addressName = result.getRegeocodeAddress().getFormatAddress()
                            + "附近";
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(latLonPoint), 18));
                    mMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                    MToast.showToast(StationLatLngActivity.this, addressName);
//                    Log.e("逆地理回调初次定位", "addressName--" + addressName);
                }
            } else {
                if (isFordetailInfo) {
                    MToast.showToast(StationLatLngActivity.this, "抱歉，位置信息数据提交出错");
                } else {
                    MToast.showToast(StationLatLngActivity.this, "对不起，没有搜索到相关数据");
//                    Log.e("逆地理回调", "没有搜索到相关数据");
                }
            }
        } else {
//            ToastUtil.showerror(this, rCode);
//            Log.e("逆地理回调", "rCode--" + rCode);
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 18));
                mMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));

                mLatLng = new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());


                addMarkersToMap(mLatLng);

//                List addressList = result.getGeocodeAddressList();

//                addMarkersToMap(address.getLatLonPoint().);

                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();

//                stationLat = address.getLatLonPoint() + "";//获取到纬度和经度

                MToast.showToast(StationLatLngActivity.this, addressName);
            } else {
//                ToastUtil.show(GeocoderActivity.this, R.string.no_result);
                getCityLatlon(originalCity);
                MToast.showToast(StationLatLngActivity.this, "对不起，没有搜索到相关数据，请您手动标记");
            }
        } else {
//            ToastUtil.showerror(this, rCode);
        }

    }

    /**
     * 响应地理编码失败后，粗略定位到所在城市
     */
    public void getCityLatlon(final String originalCity) {
        showDialog();
        GeocodeQuery query = new GeocodeQuery(originalCity, originalCity);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 对正在移动地图事件回调
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {//貌似是地图中心点的坐标
//        tv_onclickLocation.setText("onCameraChange:" + cameraPosition.toString());
    }

    /**
     * 对移动地图结束事件回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {//仍然是地图中心点坐标
//        tv_onclickLocation.setText("onCameraChangeFinish:" + cameraPosition.toString());
    }

    /**
     * 对单击地图事件回调
     */
    @Override
    public void onMapClick(LatLng latLng) {

        /**
         * 清空地图上所有已经标注的marker
         */
        if (aMap != null) {
            aMap.clear();
        }

//        tv_onclickLocationInfo.setText(latLng.describeContents());
//        SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, "");
//        tv_onclickLocationInfo.setText(latLng.);

//        mMarkerOption.position(latLng);//下面要调，多余了

//        tv_onclickLocation.setText("单击, point=" + latLng);

//        mMarker = aMap.addMarker(mMarkerOption);

        addMarkersToMap(latLng);
        mLatLng = latLng;

//        LatLonPoint lp = new LatLonPoint(mLatLng.latitude, mLatLng.longitude);
//
//        getAddress(lp );

    }

    /**
     * 对长按地图事件回调
     */
    @Override
    public void onMapLongClick(LatLng latLng) {

        /**
         * 清空地图上所有已经标注的marker
         */
        if (aMap != null) {
            aMap.clear();
        }


//        mMarkerOption.position(latLng);//多余

//        tv_onclickLocation.setText("long pressed, point=" + latLng);

        addMarkersToMap(latLng);

        mLatLng = latLng;

    }

    /**
     * 标记点点击事件回调
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * 标记点被拖动时的三个回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        mLatLng = marker.getPosition();

        /**
         * 清空地图上所有已经标注的marker
         */
        if (aMap != null) {
            aMap.clear();
        }

//        mMarkerOption.position(mLatLng);//多余

//        tv_onclickLocation.setText("long pressed, point=" + mLatLng);

        addMarkersToMap(mLatLng);

    }

}
