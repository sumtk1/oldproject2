package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.activity.MainActivity;
import com.gloiot.hygooilstation.ui.map.StationLatLngActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.gloiot.hygooilstation.ui.activity.MainActivity.mainActivity;

/**
 * 油站编辑
 */
public class ModifyOilstationActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_modifyoilstation_image, rl_modifyoilstation_area, rl_modifyoilstation_coordinate;
    private EditText et_modifyoilstation_phoneNum, et_modifyoilstation_location, et_modifyoilstation_content;
    private EditText et_modifyoilstation_quhao, et_modifyoilstation_zuojihao;
    private TextView tv_toptitle_right, tv_modifyoilstation_area, tv_modifyoilstation_coordinate;
    private ImageView iv_toptitle_back;
    private String stationId;
    private MyDialogBuilder myDialogBuilder;
    private ArrayList<String> picList = new ArrayList<>();
    private String originalphoneNum, originalLocation, originalIntro, originalLatitude, originalLongitude, originalArea;//初始值
    private String phoneNum, location, intro, latitude, longitude, area;//修改后的值
    private TextView tv_modifyOlistation_num;
    private int sum = 150;

    @Override
    protected void onResume() {
        super.onResume();
        boolean isStationAreaReset = SharedPreferencesUtils.getBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, false);
        String stationProvince = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHENG, "");
        String statinoCity = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHI, "");
        String stationDistrict = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONQU, "");
        et_modifyoilstation_location.setText(SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, ""));
        if (isStationAreaReset) {
            tv_modifyoilstation_area.setText(stationProvince + "-" + statinoCity + "-" + stationDistrict);
            SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, false);

//            et_modifyoilstation_location.setText("");//省市区变动时，将详细地址清空，让用户重新输入

        } else {
            if (!TextUtils.isEmpty(originalArea)) {
                tv_modifyoilstation_area.setText(originalArea);
            }
        }

        String stationLat = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "");//纬度
        String stationLng = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "");//经度

        if (!TextUtils.isEmpty(stationLat) && !TextUtils.isEmpty(stationLng)) {
            tv_modifyoilstation_coordinate.setText("已标记");
        } else {
            tv_modifyoilstation_coordinate.setText("");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String stationDistrict = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONQU, "");
        if (!TextUtils.isEmpty(stationDistrict)) {
            SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISSTATIONAREARESET, true);
        }
        SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, et_modifyoilstation_location.getText().toString());
    }

    @Override
    public int initResource() {
        return R.layout.activity_modify_oilstation;
    }

    @Override
    public void initComponent() {
        rl_modifyoilstation_image = (RelativeLayout) findViewById(R.id.rl_modifyoilstation_image);
        rl_modifyoilstation_area = (RelativeLayout) findViewById(R.id.rl_modifyoilstation_area);
        rl_modifyoilstation_coordinate = (RelativeLayout) findViewById(R.id.rl_modifyoilstation_coordinate);
        et_modifyoilstation_phoneNum = (EditText) findViewById(R.id.et_modifyoilstation_phoneNum);
        et_modifyoilstation_quhao = (EditText) findViewById(R.id.et_modifyoilstation_quhao);
        et_modifyoilstation_zuojihao = (EditText) findViewById(R.id.et_modifyoilstation_zuojihao);
        et_modifyoilstation_location = (EditText) findViewById(R.id.et_modifyoilstation_location);
        et_modifyoilstation_content = (EditText) findViewById(R.id.et_modifyoilstation_content);
        iv_toptitle_back = (ImageView) findViewById(R.id.iv_toptitle_back);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_modifyoilstation_area = (TextView) findViewById(R.id.tv_modifyoilstation_area);
        tv_modifyoilstation_coordinate = (TextView) findViewById(R.id.tv_modifyoilstation_coordinate);
        tv_modifyOlistation_num = (TextView) findViewById(R.id.tv_modifyOlistation_num);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, false, "编辑", "保存");//重写返回按钮的点击事件
        iv_toptitle_back.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        stationId = intent.getStringExtra("stationId");
        originalphoneNum = intent.getStringExtra("phoneNum");
        originalLocation = intent.getStringExtra("stationLocation");
        originalIntro = intent.getStringExtra("stationIntro");
        picList = intent.getStringArrayListExtra("picList");
        originalLatitude = intent.getStringExtra("stationLatitude");//纬度
        originalLongitude = intent.getStringExtra("stationLongititude");//经度
        originalArea = intent.getStringExtra("stationArea");

        SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLOCATIONDETAILINFO, originalLocation);//油站地址先存起来
        SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLATITUDE, originalLatitude);
        SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, originalLongitude);//第一次初始化的时候就直接存起来

        if (!TextUtils.isEmpty(originalArea)) {
            if (originalArea.contains("-")) {
                String[] s = originalArea.split("\\-");
                for (int i = 0; i < s.length; i++) {
//                    Log.e("s--", s[i]);
                }
                if (s.length == 1) {//不确定会不会出问题，看测试的数据
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHENG, s[0]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHI, s[0]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONQU, s[0]);
                } else if (s.length == 2) {
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHENG, s[0]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHI, s[1]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONQU, s[1]);
                } else if (s.length == 3) {
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHENG, s[0]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONSHI, s[1]);
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_STATIONQU, s[2]);
                } else {

                }
            }

        }

//        et_modifyoilstation_phoneNum.setText(originalphoneNum);

        try {
            if (originalphoneNum.contains("-")) {
                String[] phone = originalphoneNum.split("-");
                et_modifyoilstation_quhao.setText(phone[0]);
                et_modifyoilstation_zuojihao.setText(phone[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        et_modifyoilstation_content.setText(originalIntro);

        if (!TextUtils.isEmpty(originalIntro)) {
            tv_modifyOlistation_num.setText(originalIntro.length() + "");
        }

        iv_toptitle_back.setOnClickListener(this);
        tv_toptitle_right.setOnClickListener(this);
        rl_modifyoilstation_image.setOnClickListener(this);
        rl_modifyoilstation_area.setOnClickListener(this);
        rl_modifyoilstation_coordinate.setOnClickListener(this);

        //将编辑光标定位到文本最后
        String s1 = et_modifyoilstation_quhao.getText().toString();
        et_modifyoilstation_quhao.setSelection(s1.length());
        String s2 = et_modifyoilstation_location.getText().toString();
        et_modifyoilstation_location.setSelection(s2.length());
        String s3 = et_modifyoilstation_content.getText().toString();
        et_modifyoilstation_content.setSelection(s3.length());

//        numLimit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_modifyoilstation_image:
                Intent intent = new Intent(this, OilstationPicsActivity.class);
                intent.putExtra("stationId", stationId);
                intent.putStringArrayListExtra("picList", picList);//把图片list传过去
                startActivity(intent);
                break;
            case R.id.rl_modifyoilstation_area://区域
                startActivity(new Intent(this, OilstationProvinceActivity.class));
                break;
            case R.id.rl_modifyoilstation_coordinate://坐标
                if (TextUtils.isEmpty(tv_modifyoilstation_area.getText().toString())) {
                    showPrompt("请选择油站区域");
                } else {
                    if (TextUtils.isEmpty(et_modifyoilstation_location.getText().toString())) {
                        showPrompt("请输入油站地址");
                    } else {
//                        startActivity(new Intent(this, BaseMapFragmentActivity.class));//测试高德调用基本地图
//                        startActivity(new Intent(this, StationLocationFragmentActivity.class));

                        getLocation();//测试定位/地图相关的功能

                    }
                }
                break;
            case R.id.tv_toptitle_right://保存
                if (verification_modifyoilstation()) {

                    getData_onBackOrCommit();

                    if (phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) && intro.equals(originalIntro)
                            && tv_modifyoilstation_area.getText().toString().equals(originalArea) && latitude.equals(originalLatitude) && longitude.equals(originalLongitude)) {
                        //此处还应该加上图片list的判断。后续有时间完善
//                        MToast.showToast(mContext, "当前页面还没有做任何修改");
                    } else {
                        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                        myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                                .withContene("您确认要修改油站信息吗?")
                                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                                .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialogBuilder.dismissNoAnimator();
                                        requestHandleArrayList.add(requestAction.modifyoilstation(ModifyOilstationActivity.this, location,
                                                phoneNum, intro, longitude, latitude, area));
                                    }
                                })
                                .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialogBuilder.dismiss();
                                    }
                                })
                                .show();
                    }
                }
                break;
            case R.id.iv_toptitle_back://返回按钮

                getData_onBackOrCommit();
                if ((phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) && intro.equals(originalIntro)
                        && tv_modifyoilstation_area.getText().toString().equals(originalArea) && latitude.equals(originalLatitude) && longitude.equals(originalLongitude))
                        || (phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) &&
                        intro.equals("") && latitude.equals("") && longitude.equals(""))) {
                /*if (phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) && intro.equals(originalIntro)
                        && tv_modifyoilstation_area.getText().toString().equals(originalArea) && latitude.equals(originalLatitude) && longitude.equals(originalLongitude)) {*/
                    View view = getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if (mainActivity != null) {
                        mainActivity.finish();
                    }
                    startActivity(new Intent(ModifyOilstationActivity.this, MainActivity.class));
                    SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, true);
                    finish();
                } else {
                    final MyDialogBuilder dialogBuilder = MyDialogBuilder.getInstance(mContext);
                    dialogBuilder
                            .withContene("请保存您的修改信息，返回将放弃修改?")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("保存", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismissNoAnimator();
                                    if (verification_modifyoilstation()) {
                                        requestHandleArrayList.add(requestAction.modifyoilstation(ModifyOilstationActivity.this, location,
                                                phoneNum, intro, longitude, latitude, area));
                                    }
                                }
                            })
                            .setBtnClick("返回", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    View view = getWindow().peekDecorView();
                                    if (view != null) {
                                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }
                                    if (mainActivity != null) {
                                        mainActivity.finish();
                                    }
                                    startActivity(new Intent(ModifyOilstationActivity.this, MainActivity.class));
                                    SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, true);
                                    finish();

                                }
                            })
                            .show();
                }
                break;
        }
    }


    private void getData_onBackOrCommit() {
        phoneNum = et_modifyoilstation_quhao.getText().toString().trim() + "-" + et_modifyoilstation_zuojihao.getText().toString().trim();
        location = et_modifyoilstation_location.getText().toString();//提交坐标对应的位置信息，而不是用户输入的关键字信息．．应该允许再次编辑．提交这个．．
        intro = et_modifyoilstation_content.getText().toString();
        latitude = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLATITUDE, "");
        longitude = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONLONGITUDE, "");
        String province = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHENG, "");
        String city = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONSHI, "");
        String district = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONQU, "");
        area = province + "-" + city + "-" + district;
    }

    //重写系统返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//其中event.getRepeatCount() == 0 是重复次数，点返回键时，防止点的过快，触发两次后退事件，做此设置
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {//保留这个判断，增强程序健壮性

            getData_onBackOrCommit();

            if ((phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) && intro.equals(originalIntro)
                    && tv_modifyoilstation_area.getText().toString().equals(originalArea) && latitude.equals(originalLatitude) && longitude.equals(originalLongitude))
                    || (phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) &&
                    intro.equals("") && latitude.equals("") && longitude.equals(""))) {
                /*if (phoneNum.equals(originalphoneNum) && et_modifyoilstation_location.getText().toString().equals(originalLocation) && intro.equals(originalIntro)
                        && tv_modifyoilstation_area.getText().toString().equals(originalArea) && latitude.equals(originalLatitude) && longitude.equals(originalLongitude)) {*/
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (mainActivity != null) {
                    mainActivity.finish();
                }
                startActivity(new Intent(ModifyOilstationActivity.this, MainActivity.class));
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, true);
                finish();
            } else {
                final MyDialogBuilder dialogBuilder = MyDialogBuilder.getInstance(mContext);
                dialogBuilder
                        .withContene("请保存您的修改信息，返回将放弃修改!")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("保存", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismissNoAnimator();
                                if (verification_modifyoilstation()) {
                                    requestHandleArrayList.add(requestAction.modifyoilstation(ModifyOilstationActivity.this, location,
                                            phoneNum, intro, longitude, latitude, area));
                                }
                            }
                        })
                        .setBtnClick("返回", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                View view = getWindow().peekDecorView();
                                if (view != null) {
                                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                if (mainActivity != null) {
                                    mainActivity.finish();
                                }
                                startActivity(new Intent(ModifyOilstationActivity.this, MainActivity.class));
                                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, true);
                                finish();
                            }
                        })
                        .show();
            }

        }
        return false;
    }

    private void getLocation() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {//应该是只有定位时才需要这个权限
//                startActivity(new Intent(ModifyOilstationActivity.this, getLocationActivity.class));  //测试定位
//                startActivity(new Intent(ModifyOilstationActivity.this, MapTestActivity.class));//测试地图相关的方法

//                startActivity(new Intent(ModifyOilstationActivity.this, StationLatLngActivity.class));

                Intent intent = new Intent(ModifyOilstationActivity.this, StationLatLngActivity.class);
                if (tv_modifyoilstation_coordinate.getText().toString().equals("已标记") && originalLocation.equals(et_modifyoilstation_location.getText().toString())
                        && originalArea.equals(tv_modifyoilstation_area.getText().toString())) {
                    intent.putExtra("type", "nidili");//逆地理编码
                } else {
                    String locationKeywords = et_modifyoilstation_location.getText().toString();
                    intent.putExtra("type", "dili");//地理编码
                    intent.putExtra("keywords", locationKeywords);//搜索关键字
                }
                startActivity(intent);
            }
        }, R.string.location, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MODIFYOILSTATION:
                MToast.showToast(mContext, "修改成功");
                if (mainActivity != null) {
                    mainActivity.finish();
                }
                startActivity(new Intent(ModifyOilstationActivity.this, MainActivity.class));
                SharedPreferencesUtils.setBoolean(mContext, ConstantUtlis.SP_ISFROMMODIFYTOMAIN, true);
                finish();
                break;
        }
    }

    private Boolean verification_modifyoilstation() {
        if (TextUtils.isEmpty(et_modifyoilstation_quhao.getText().toString().trim())) {
            showPrompt("油站电话区号不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_modifyoilstation_zuojihao.getText().toString().trim())) {
            showPrompt("油站电话不能为空");
            return false;
        } else if (TextUtils.isEmpty(tv_modifyoilstation_area.getText().toString())) {
            showPrompt("油站区域不能为空");
            return false;
        } else if (TextUtils.isEmpty(et_modifyoilstation_location.getText().toString().trim())) {
            showPrompt("请输入油站详细地址");
            return false;
        } else if (TextUtils.isEmpty(tv_modifyoilstation_coordinate.getText().toString())) {
            showPrompt("请在地图中标记油站位置");
            return false;
        } else {
            return true;
        }
    }

    //弹出对话框根据输入内容给出提示信息
    private void showPrompt(String prompt) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder
                .withIcon(R.mipmap.iconfont_gantanhao)
                .withContene(prompt)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtnClick("知道了", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismissNoAnimator();
                    }
                }).show();
    }

    private void numLimit() {
        et_modifyoilstation_content.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_modifyOlistation_num.setText(s.length() + "");
                selectionStart = et_modifyoilstation_content.getSelectionStart();
                selectionEnd = et_modifyoilstation_content.getSelectionEnd();
                if (temp.length() > sum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_modifyoilstation_content.setText(s);
                    et_modifyoilstation_content.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }
}
