package com.gloiot.hygooilstation.ui.activity.authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.gloiot.hygooilstation.ui.adapter.QualificationOtherLicenceAdapter;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.BitmapUtils;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.recyclerlistener.RecyclerItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 资质认证--其他资料(多张图)
 * Created by Dlt on 2017/10/21 10:10
 */
public class QualificationOtherLicenceActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_toptitle_back)
    ImageView mIvToptitleBack;
    @Bind(R.id.tv_toptitle_right)
    TextView mTvToptitleRight;
    @Bind(R.id.iv_default_pic)
    ImageView mIvDefaultPic;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_warm_prompt)
    TextView mTvWarmPrompt;

    public static final int RESULT_OTHERLICENCE = 8;

    private QualificationOtherLicenceAdapter mAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private List<String> changeList = new ArrayList<>();// 上传或删除的图片集合
    private String originalPics = "";//初始图片字符串，用于判断是否发生更改
    private String allPics = "";//所有图片的字符串拼接

    private ProgressDialog progressDialog;

    @Override
    public int initResource() {
        return R.layout.activity_qualification_other_licence;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, false, "上传附加材料", "");
        mIvToptitleBack.setVisibility(View.VISIBLE);

//        mockData();

        Intent intent = getIntent();
        originalPics = intent.getStringExtra("photoUrl");
        allPics = intent.getStringExtra("photoUrl");
        L.e("附加材料initData", "==" + allPics);

        if (!TextUtils.isEmpty(allPics)) {

            mIvDefaultPic.setVisibility(View.GONE);
            mTvToptitleRight.setText("确定");
            mTvToptitleRight.setTextColor(Color.parseColor("#449FFB"));

            try {
                if (allPics.contains("|")) {
                    String[] s = allPics.split("\\|");
                    for (int i = 0; i < s.length; i++) {
//                        L.e("s--", s[i]);
                        selectedPhotos.add(s[i]);
                    }
                } else {
                    selectedPhotos.add(allPics);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mIvDefaultPic.setVisibility(View.VISIBLE);
            mTvToptitleRight.setText("");
        }

        mAdapter = new QualificationOtherLicenceAdapter(mContext, selectedPhotos, 3);

//        for (int i = 0; i < selectedPhotos.size(); i++) {
//            L.e("11111---selectedPhotos", "i=" + i + "===" + selectedPhotos.get(i));
//        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mAdapter.getItemViewType(position) == QualificationOtherLicenceAdapter.TYPE_ADD) {
                            checkPermission(new CheckPermListener() {
                                @Override
                                public void superPermission() {
                                    PhotoPicker.builder()
                                            .setPhotoCount(QualificationOtherLicenceAdapter.MAX)
                                            .setShowCamera(true)
                                            .setPreviewEnabled(false)
                                            .setSelected(selectedPhotos)
                                            .start(QualificationOtherLicenceActivity.this);
                                }
                            }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        } else {

                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(QualificationOtherLicenceActivity.this);
                        }
                    }
                }));

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("提示类别", "附加材料上传");
        requestHandleArrayList.add(requestAction.getZizhiWarmPrompt(QualificationOtherLicenceActivity.this, hashMap));

    }

    private void mockData() {
        String s1 = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/app-minishop-img/yiyeshop201708050241232137176525.jpg";
        String s2 = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/app-minishop-img/yiyeshop201708050241231133683741.jpg";
        String s3 = "http://zykshop.qqjlb.cn/zyypayPics/zyyPay201705160612231802700228.jpg";

//        selectedPhotos.add(s1);
//        selectedPhotos.add(s2);
//        selectedPhotos.add(s3);
    }

    @OnClick({R.id.iv_toptitle_back, R.id.tv_toptitle_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toptitle_back:
                onBackTouched();
                break;
            case R.id.tv_toptitle_right:
                onCommitClick();
                break;
        }
    }

    /**
     * 确定按钮点击事件
     */
    private void onCommitClick() {
        if (originalPics.equals(allPics)) {
            MToast.showToast(mContext, "没有照片发生更改");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("附加材料", allPics);
            requestHandleArrayList.add(requestAction.uploadingZizhiPhoto(QualificationOtherLicenceActivity.this, hashMap));
        }
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

            a = 0;
            selectedPhotos.clear();
            allPics = "";

            if (photos != null) {

                if (requestCode == PhotoPicker.REQUEST_CODE) {

                    final List<Bitmap> images = new ArrayList<>();
                    for (int i = 0; i < photos.size(); i++) {
                        L.e("选中图片初始路径--选择上传", "==" + photos.get(i));
                        if (photos.get(i).contains("http") && photos.get(i).contains("hygoilstationPics")) {       //已经上传过的照片
                            selectedPhotos.add(photos.get(i));
                        }

                        Bitmap image = BitmapFactory.decodeFile(photos.get(i));
                        images.add(image);

                    }
                    final List<String> picsUrl = new ArrayList<>();//上传阿里云成功后返回一个图片url，加入到这个集合
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("图片上传中，请稍候...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progressDialog.setCancelable(false);
                    progressDialog.show();

                    for (int i = 0; i < images.size(); i++) {
                        L.e("images", "i=" + i + "--" + images.get(i));
                    }

                    L.e("test", "111111111111");
                    for (final Bitmap image : images) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (image != null) {

                                    new AliOss(QualificationOtherLicenceActivity.this, AliOss.setPicName("hygoilstationPics"), BitmapUtils.compressImage(image)) {//这里设置上传的地址
                                        @Override
                                        protected void uploadProgress(long currentSize, long totalSize) {

                                        }

                                        @Override
                                        public void uploadSuccess(String myPicUrl) {
                                            L.e("上传阿里云成功-----imagesUrl", "==" + myPicUrl);
                                            picsUrl.add(myPicUrl);
                                            a++;
                                            L.e("test", "222222222" + ",a=" + a + ",images.size()=" + images.size());
                                            if (a == images.size()) {
                                                progressDialog.dismiss();
                                                changeList = picsUrl;
                                                QualificationOtherLicenceActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        selectedPhotos.addAll(changeList);

                                                        for (int i = 0; i < selectedPhotos.size(); i++) {//只把新增的图片上传，而不是所有上传
                                                            allPics += selectedPhotos.get(i) + "|";
                                                        }

                                                        L.e("for后 allpics", allPics);

                                                        if (allPics.substring(0, 4).equals("null")) {
                                                            L.e("0-4", "null---");
                                                            allPics = allPics.substring(4, allPics.length() - 1);//因为默认最前面会有一个null，最后面会有一个分隔符
                                                        } else {
                                                            L.e("0-4", "bushi null---");
                                                            allPics = allPics.substring(0, allPics.length() - 1);//最后面会有一个分隔符
                                                        }

                                                        L.e("if后 allpics", allPics);

//                                                        for (int i = 0; i < changeList.size(); i++) {
//                                                            L.e("changeList", "i=" + i + ",---" + changeList.get(i));
//                                                        }
                                                        mAdapter.notifyDataSetChanged();

                                                        if (TextUtils.isEmpty(allPics)) {
                                                            mIvDefaultPic.setVisibility(View.VISIBLE);
                                                            mTvToptitleRight.setText("");
                                                        } else {
                                                            mIvDefaultPic.setVisibility(View.GONE);
                                                            mTvToptitleRight.setText("确定");
                                                            mTvToptitleRight.setTextColor(Color.parseColor("#449FFB"));
                                                        }

                                                    }
                                                });


                                                a = 0;//重置


                                            } else {
//                                                progressDialog.dismiss();
//                                                int failureAmount = images.size() - a;
//                                                MToast.showToast(mContext, failureAmount + "张未上传成功");

                                            }
                                        }

                                        @Override
                                        protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                            a++;
                                        }
                                    }.start();

                                } else {
                                    a++;
                                }

                            }

                        }).start();
                    }

                } else if (requestCode == PhotoPreview.REQUEST_CODE) {

                    for (int i = 0; i < photos.size(); i++) {
                        L.e("选中图片初始路径--预览", "==" + photos.get(i));
                    }
                    final List<String> previewBackList = photos; //预览返回的列表

                    QualificationOtherLicenceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < previewBackList.size(); i++) {
                                allPics += previewBackList.get(i) + "|";
                            }
                            L.e("allpics", "aaaaaaa=" + allPics);
                            try {
                                if (allPics.substring(0, 4).equals("null")) {
                                    allPics = allPics.substring(4, allPics.length() - 1);//因为默认最前面会有一个null，最后面会有一个分隔符
                                } else {
                                    allPics = allPics.substring(0, allPics.length() - 1);//最后面会有一个分隔符
                                }
                            } catch (StringIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            selectedPhotos.addAll(previewBackList);

                            mAdapter.notifyDataSetChanged();

                            if (TextUtils.isEmpty(allPics)) {
                                mIvDefaultPic.setVisibility(View.VISIBLE);
//                                mTvToptitleRight.setText("");
                            } else {
                                mIvDefaultPic.setVisibility(View.GONE);
//                                mTvToptitleRight.setText("确定");
//                                mTvToptitleRight.setTextColor(Color.parseColor("#449FFB"));
                            }

                        }
                    });

                }


            }

        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_ZIZHIWARMPROMPT://温馨提示
                L.e("温馨提示", response.toString());
                mTvWarmPrompt.setText(response.getString("温馨提示"));
                break;
            case RequestAction.TAG_ZIZHIUPLOADINGPHOTO:
                L.e("资质认证照片上传", response.toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("photoUrl", allPics);
                QualificationOtherLicenceActivity.this.setResult(RESULT_OTHERLICENCE, resultIntent);//结果码用于标识返回自哪个Activity
                QualificationOtherLicenceActivity.this.finish();
                break;
        }
    }

    /**
     * 返回按钮事件
     */
    private void onBackTouched() {
        if (!originalPics.equals(allPics)) {

            final MyDialogBuilder dialogBuilder = MyDialogBuilder.getInstance(mContext);
            dialogBuilder
                    .withContene("请保存您的修改信息，返回将放弃修改!")
                    .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                    .setBtnClick("保存", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismissNoAnimator();

                            onCommitClick();
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
                            finish();
                        }
                    })
                    .show();

        } else {

            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            finish();

        }
    }

    //重写系统返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//其中event.getRepeatCount() == 0 是重复次数，点返回键时，防止点的过快，触发两次后退事件，做此设置
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {//保留这个判断，增强程序健壮性
            onBackTouched();
        }
        return false;
    }

}
