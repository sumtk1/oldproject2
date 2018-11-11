package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abcaaa.photopicker.PhotoPicker;
import com.abcaaa.photopicker.PhotoPreview;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.server.picture.AliOss;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.picsmagnify.SpaceImageDetailActivity;
import com.gloiot.hygooilstation.ui.widget.picsmagnify.SquareCenterImageView;
import com.gloiot.hygooilstation.utils.BitmapUtils;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.MyPromptDialogUtils;
import com.gloiot.hygooilstation.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 油站图片
 */
public class OilstationPicsActivity extends BaseActivity implements View.OnClickListener {
    private GridView gd_gridView;
    private TextView tv_toptitle_right, tv_uploading;
    private RelativeLayout rl_delete;
    private List<String> picList = new ArrayList<>();//获取传过来的图片url,最好不要从前面获取，因为如果不退回第一个调接口的地方，从编辑页面再次进来不会更新到最新数据
    private List<String> changeList = new ArrayList<>();// 上传或删除的图片集合
    private String stationPics;//所有图片的字符串拼接
    private String stationId;
    private String singlepic;//操作的单张图片
    private MyDialogBuilder myDialogBuilder;
    public List<String> mSelectedImage = new LinkedList<>();  //用户选择的图片，存储为图片的完整路径

    private ProgressDialog progressDialog;

    @Override
    public int initResource() {
        return R.layout.activity_oilstation_pics;
    }

    @Override
    public void initComponent() {
        gd_gridView = (GridView) findViewById(R.id.gd_gridView);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
        tv_uploading = (TextView) findViewById(R.id.tv_uploading);
        rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        stationId = intent.getStringExtra("stationId");
//        picList = intent.getStringArrayListExtra("picList");
        requestHandleArrayList.add(requestAction.getStationpics(this));
        CommonUtlis.setTitleBar(this, true, "油站图片", "编辑");
        tv_toptitle_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_uploading:
                if (picList.size() < 5) {
                    checkPermission(new CheckPermListener() {
                        @Override
                        public void superPermission() {
                            selectPic();
                        }
                    }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.rl_delete:
                if (mSelectedImage.size() == 0) {
                    MToast.showToast(mContext, "请选择要删除的图片");
                } else {
                    myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                    myDialogBuilder
                            .withContene("您确定要删除这" + mSelectedImage.size() + "张图片吗?")
                            .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                            .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialogBuilder.dismissNoAnimator();
                                    for (int i = 0; i < mSelectedImage.size(); i++) {//只把选中的图片删除
                                        stationPics += mSelectedImage.get(i) + ",";
                                    }
                                    if (stationPics.substring(0, 4).equals("null")) {
                                        stationPics = stationPics.substring(4, stationPics.length() - 1);//因为默认最前面会有一个null，最后面会有一个逗号
                                    } else {
                                        stationPics = stationPics.substring(0, stationPics.length() - 1);//最后面会有一个逗号
                                    }
                                    requestHandleArrayList.add(requestAction.deleteStationpics(OilstationPicsActivity.this, stationPics));
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
                break;
            case R.id.tv_toptitle_right:
                if (tv_toptitle_right.getText().toString().equals("编辑")) {
                    tv_uploading.setVisibility(View.GONE);
                    rl_delete.setVisibility(View.VISIBLE);
                    rl_delete.setOnClickListener(this);
                    tv_toptitle_right.setText("取消");
                } else if (tv_toptitle_right.getText().toString().equals("取消")) {
                    mSelectedImage.clear();
                    gd_gridView.setAdapter(new MyGVAdapter(picList));//简单粗暴，直接重刷数据，否则还要把选中的图片背景换回来。
                    tv_toptitle_right.setText("编辑");
                    rl_delete.setVisibility(View.GONE);
                    if (picList.size() < 5) {
                        tv_uploading.setVisibility(View.VISIBLE);
                        tv_uploading.setOnClickListener(this);
                    }
                }
                break;
        }
    }

    //多张上传
    private void selectPic() {

        PhotoPicker.builder()
                .setPhotoCount(5 - picList.size())
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, PhotoPicker.REQUEST_CODE);

//        ChoosePhoto mChoosePhoto = new ChoosePhoto(mContext) {
//            @Override
//            protected void setPortraitonSuccess(String myPicUrl) {
//            }
//
//            @Override
//            protected void setPortraitonSuccess(String myPicUrl, boolean a) {
//            }
//
//            @Override
//            protected void setPortraitFailure() {
////                MToast.showToast(OilstationPicsActivity.this, "抱歉该图片格式不支持");
//            }
//
//            @Override
//            protected void setPicsSuccess(final List<String> picsUrl, boolean over) {//这个成功是从本地上传到阿里云成功
//                if (over) {
//                    for (int i = 0; i < picsUrl.size(); i++) {
////                        Log.e("全部上传成功", "数量：" + picsUrl.size() + "地址：" + picsUrl.get(i));
//                    }
//
//                    changeList = picsUrl;
//
//                    picList.addAll(changeList);//将上传的图片添加进去
//
//                    OilstationPicsActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < changeList.size(); i++) {//只把新增的图片上传，而不是所有上传
//                                stationPics += changeList.get(i) + ",";
//                            }
//                            if (stationPics.substring(0, 4).equals("null")) {
//                                stationPics = stationPics.substring(4, stationPics.length() - 1);//因为默认最前面会有一个null，最后面会有一个逗号
//                            } else {
//                                stationPics = stationPics.substring(0, stationPics.length() - 1);//最后面会有一个逗号
//                            }
//                            tv_uploading.setVisibility(View.GONE);//调接口上传到服务器过程中为防止可以继续点击上传，直接屏蔽上传。
//                            requestHandleArrayList.add(requestAction.uploadingStationpics(OilstationPicsActivity.this, stationPics));
//                        }
//                    });
//
//
//                }
//            }
//        };
//        mChoosePhoto.setMuti(5 - picList.size());
//        mChoosePhoto.setPics2();
    }

    int a = 0;//记录上传成功的图片数量

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            if (photos != null) {
                final List<Bitmap> images = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
//                    Log.e("选中图片路径", "==" + photos.get(i));
                    Bitmap image = BitmapFactory.decodeFile(photos.get(i));
                    images.add(image);
                }
                final List<String> picsUrl = new ArrayList<>();//上传阿里云成功后返回一个图片url，加入到这个集合
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("图片上传中，请稍候...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progressDialog.setCancelable(false);
                progressDialog.show();
                for (final Bitmap image : images) {
//                    Log.e("-----images", images.size() + "-");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new AliOss(OilstationPicsActivity.this, AliOss.setPicName("hygoilstationPics"), BitmapUtils.compressImage(image)) {//这里设置上传的地址
                                @Override
                                protected void uploadProgress(long currentSize, long totalSize) {

                                }

                                @Override
                                public void uploadSuccess(String myPicUrl) {
                                    picsUrl.add(myPicUrl);
                                    a++;
//                                    Log.e("上传阿里云成功-----imagesUrl", "==" + myPicUrl);
                                    if (a == images.size()) {

//                                        Log.e("遍历完全！", "==成功！！！");
                                        progressDialog.dismiss();

                                        changeList = picsUrl;
//                                        picList.addAll(changeList);//将上传的图片添加进去
                                        OilstationPicsActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (int i = 0; i < changeList.size(); i++) {//只把新增的图片上传，而不是所有上传
                                                    stationPics += changeList.get(i) + ",";
                                                }
                                                if (stationPics.substring(0, 4).equals("null")) {
                                                    stationPics = stationPics.substring(4, stationPics.length() - 1);//因为默认最前面会有一个null，最后面会有一个逗号
                                                } else {
                                                    stationPics = stationPics.substring(0, stationPics.length() - 1);//最后面会有一个逗号
                                                }
                                                tv_uploading.setVisibility(View.GONE);//调接口上传到服务器过程中为防止可以继续点击上传，直接屏蔽上传。
                                                requestHandleArrayList.add(requestAction.uploadingStationpics(OilstationPicsActivity.this, stationPics));
                                            }
                                        });

                                    } else {

                                    }
                                }

                                @Override
                                protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                    a++;
                                }
                            }.start();

                        }

                    }).start();
                }
            }

        }


    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_STATIONPICS:
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("油站图片信息");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String a = jsonObject.getString("图片");
                        picList.add(a);
                        L.e("获取油站图片", picList.get(i));
                    }
                    if (picList.size() < 5) {
                        tv_uploading.setVisibility(View.VISIBLE);
                        tv_uploading.setOnClickListener(this);
                    } else {
                        tv_uploading.setVisibility(View.GONE);
                    }
                    gd_gridView.setAdapter(new MyGVAdapter(picList));
                } else {
                    tv_uploading.setVisibility(View.VISIBLE);
                    tv_uploading.setOnClickListener(this);
                }
                break;
            case RequestAction.TAG_UPLOADINGSTATIONPICS:
                MToast.showToast(mContext, "上传图片成功");
                picList.addAll(changeList);//已经在调接口之前添加进去了，不要重复添加。(感觉在这里添加更合理，0516更改。)
                if (picList.size() < 5) {
                    tv_uploading.setVisibility(View.VISIBLE);
                    tv_uploading.setOnClickListener(this);
                } else {
                    tv_uploading.setVisibility(View.GONE);
                }
                gd_gridView.setAdapter(new MyGVAdapter(picList));
                changeList.clear();
                stationPics = "";//必须要清空
                break;
            case RequestAction.TAG_DELETESTATIONPICS:
                MToast.showToast(mContext, "删除图片成功");
                picList.removeAll(mSelectedImage);
                gd_gridView.setAdapter(new MyGVAdapter(picList));
                mSelectedImage.clear();
                stationPics = "";
                break;
        }
    }

    /**
     * gridview适配器
     */
    class MyGVAdapter extends BaseAdapter {

        private List<String> list = new ArrayList<>();

        public MyGVAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(OilstationPicsActivity.this, R.layout.item_stationpics_grid, null);

            final SquareCenterImageView mImageView = (SquareCenterImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识

            OilstationPicsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PictureUtlis.loadImageViewHolder(mContext, list.get(position), R.mipmap.jiazaitu, mImageView);
                }
            });

            mImageView.setOnClickListener(new View.OnClickListener() {
                //选择，则将图片变暗
                @Override
                public void onClick(View v) {

                    if (tv_toptitle_right.getText().toString().equals("取消")) {

                        //已经选择过
                        if (mSelectedImage.contains(list.get(position))) {
                            mSelectedImage.remove(list.get(position));
                            mSelect.setVisibility(View.GONE);
                            mImageView.setColorFilter(null);
                        } else {//未选择过该图片

                            if ((picList.size() - mSelectedImage.size()) == 1) {
                                MyPromptDialogUtils.showPrompt(mContext, "至少要有一张油站图片");
                            } else {
                                mSelectedImage.add(list.get(position));
                                mSelect.setVisibility(View.VISIBLE);
                                mImageView.setColorFilter(Color.parseColor("#77000000"));
                            }
                        }

                    } else if (tv_toptitle_right.getText().toString().equals("编辑")) {

                        Intent intent = new Intent(OilstationPicsActivity.this, SpaceImageDetailActivity.class);
                        intent.putExtra("images", (ArrayList<String>) picList);
                        intent.putExtra("position", position);
                        int[] location = new int[2];
                        mImageView.getLocationOnScreen(location);
                        intent.putExtra("locationX", location[0]);
                        intent.putExtra("locationY", location[1]);

                        intent.putExtra("width", mImageView.getWidth());
                        intent.putExtra("height", mImageView.getHeight());
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            });

            //已经选择过的图片，显示出选择过的效果
            if (mSelectedImage.contains(list.get(position))) {
                mSelect.setVisibility(View.VISIBLE);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
            return view;
        }
    }

}
