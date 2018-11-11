package com.gloiot.hygooilstation.ui.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * 测试地图模块要用的方法
 */
public class MapTestActivity extends AutoLayoutActivity implements GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener
        , AMap.OnMapClickListener, AMap.OnMapLongClickListener, AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMarkerDragListener {

    private Context mContext;
    private EditText et_searchinfo, et_latloninfo;
    private TextView tv_search, tv_onclickLocation, tv_nisearch;
    private TextView tv_toptitle_right;

    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private Marker mMarker, mChangeMarker;
    private MarkerOptions mMarkerOption;

    private String stationLat, stationLon;//获取到的纬度，经度
    private String stationLatLon;//油站纬度经度拼接字符串
    private LatLng mLatLng;//全局经纬度

    //逆地理编码
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
//    private Marker regeoMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        mContext = this;
        CommonUtlis.setTitleBar(this, true, "油站区域", "确定");

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


//            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                    .icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


//            mMarkerOption = new MarkerOptions();
            LatLng latLng = new LatLng(39.906901, 116.397972);
//            mMarkerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//            mMarkerOption.anchor(0.5f, 0.5f);
//            mMarkerOption.position(latLng);
//            mMarkerOption.draggable(true);


//            mMarker = aMap.addMarker(mMarkerOption);

            addMarkersToMap(latLng);

            setUpMap();
        }


        et_searchinfo = (EditText) findViewById(R.id.et_searchinfo);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_onclickLocation = (TextView) findViewById(R.id.tv_onclickLocation);

        et_searchinfo.setText("人民南路新安大厦");
        tv_onclickLocation.setText(et_searchinfo.getText().toString());
        tv_search.setOnClickListener(this);

        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_toptitle_right.setOnClickListener(this);


        //测试逆地理编码
        et_latloninfo = (EditText) findViewById(R.id.et_latloninfo);
        tv_nisearch = (TextView) findViewById(R.id.tv_nisearch);
        et_latloninfo.setText("逆地理编码(39.90865,116.39751)");
        tv_nisearch.setOnClickListener(this);


        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latlng) {

//        mMarkerOption = new MarkerOptions();
//        LatLng latLng = new LatLng(39.906901, 116.397972);
//        mMarkerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        mMarkerOption.anchor(0.5f, 0.5f);
//        mMarkerOption.position(latLng);
//        mMarkerOption.draggable(true);

        mMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .draggable(true);
        aMap.addMarker(mMarkerOption);
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

        GeocodeQuery query = new GeocodeQuery(name, "深圳市");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        //注意这里城市不能写死
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /**
             * 响应地理编码
             */
            case R.id.tv_search:
                getLatlon(et_searchinfo.getText().toString());
                break;
            /**
             * 响应逆地理编码
             */
            case R.id.tv_nisearch:
                getAddress(latLonPoint);
                break;
            /**
             * 确定提交
             */
            case R.id.tv_toptitle_right:


                stationLat = mLatLng.latitude + "";
                stationLon = mLatLng.longitude + "";
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLATITUDE, stationLat);//纬度
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, stationLon);//经度
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONLATLNGRESET, true);

                Log.e("油站经纬度", "stationLat--" + stationLat + ",stationLon--" + stationLon);

                finish();

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
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 17));//设置缩放的比例，已知可以到19，不知道可不可以更高
                mMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));

                MToast.showToast(MapTestActivity.this, addressName);

            } else {
//                ToastUtil.show(ReGeocoderActivity.this, R.string.no_result);
                MToast.showToast(MapTestActivity.this, "对不起，没有搜索到相关数据");
            }
        } else {
//            ToastUtil.showerror(this, rCode);
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
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                mMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));

//                addMarkersToMap(address.getLatLonPoint().);

                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();

                stationLat = address.getLatLonPoint() + "";//获取到纬度和经度

                MToast.showToast(MapTestActivity.this, addressName);
            } else {
//                ToastUtil.show(GeocoderActivity.this, R.string.no_result);
                MToast.showToast(MapTestActivity.this, "对不起，没有搜索到相关数据");
            }
        } else {
//            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * 对正在移动地图事件回调
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        tv_onclickLocation.setText("onCameraChange:" + cameraPosition.toString());
    }

    /**
     * 对移动地图结束事件回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        tv_onclickLocation.setText("onCameraChangeFinish:"
                + cameraPosition.toString());


//        VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、
//        LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
//        boolean isContain = latLngBounds.contains(Constants.SHANGHAI);// 判断上海经纬度是否包括在当前地图可见区域
//        if (isContain) {
//            ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//        } else {
//            ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//        }

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


        mMarkerOption.position(latLng);
        tv_onclickLocation.setText("单击, point=" + latLng);


//        mMarker = aMap.addMarker(mMarkerOption);

        addMarkersToMap(latLng);
        mLatLng = latLng;

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


        mMarkerOption.position(latLng);
        tv_onclickLocation.setText("long pressed, point=" + latLng);

        addMarkersToMap(latLng);


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


        mMarkerOption.position(mLatLng);
        tv_onclickLocation.setText("long pressed, point=" + mLatLng);

        addMarkersToMap(mLatLng);

    }
}
