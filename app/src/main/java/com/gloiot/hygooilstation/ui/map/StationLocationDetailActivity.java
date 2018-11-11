package com.gloiot.hygooilstation.ui.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.AutoLayoutActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.map.AMapUtil;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class StationLocationDetailActivity extends AutoLayoutActivity implements GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener {

    private Context mContext;
    private TextView tv_toptitle_right;
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private Marker regeoMarker;
    private MarkerOptions mMarkerOption;

    private String originalLat, originalLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_location_detail);

        mContext = this;
        CommonUtlis.setTitleBar(this, true, "油站区域", "确定");
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);

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

            Intent intent = getIntent();


            originalLat = intent.getStringExtra("lat");
            originalLng = intent.getStringExtra("lng");

            LatLng mLatLng = new LatLng(Double.parseDouble(originalLat), Double.parseDouble(originalLng));
            addMarkersToMap(mLatLng);
//            setUpMap();
        }

        tv_toptitle_right.setOnClickListener(this);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

        double originalLatd = Double.parseDouble(originalLat);
        double originalLngd = Double.parseDouble(originalLng);

        latLonPoint = new LatLonPoint(originalLatd, originalLngd);

        getAddress(latLonPoint);

    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng) {

        mMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(mMarkerOption);
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
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
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
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 18));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                ToastUtil.show(ReGeocoderActivity.this, addressName);
            } else {
//                ToastUtil.show(ReGeocoderActivity.this, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_toptitle_right:

//                                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, mResult.getRegeocodeAddress().getFormatAddress()
//                        + "附近");
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHENG, mResult.getRegeocodeAddress().getProvince());
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONSHI, mResult.getRegeocodeAddress().getCity());
//                SharedPreferencesUtlis.setString(mContext, ConstantUtlis.SP_STATIONQU, mResult.getRegeocodeAddress().getDistrict());

                break;
        }

    }
}
