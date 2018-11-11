package com.gloiot.hygooilstation.ui.activity.authentication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcaaa.photopicker.PhotoPicker;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.server.picture.UploadingSelectedPics;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.ClickFilter;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.PictureUtlis;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 资质认证--资质信息
 */
public class QualificationInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_SHOUCHI = 1;
    public static final int REQUEST_CODE_FRONT = 2;
    public static final int REQUEST_CODE_BACK = 3;
    public static final int REQUEST_CODE_ZHIZHAO = 4;

    private EditText et_qualification_idNum;
    private EditText et_qualification_name;
    private ImageView iv_qualification_shouchi, iv_qualification_front, iv_qualification_back, iv_qualification_zhizhao;
    private TextView tv_qualification_next;
    private MyDialogBuilder myDialogBuilder;
    private String shouchiPicUrl, frontPicUrl, backPicUrl, zhizhaoPicUrl;
    public static Activity contextQualificationInfo;

    @Override
    public int initResource() {
        return R.layout.activity_qualification_info;
    }

    @Override
    public void initComponent() {
        et_qualification_name = (EditText) findViewById(R.id.et_qualification_name);
        et_qualification_idNum = (EditText) findViewById(R.id.et_qualification_idNum);
        iv_qualification_shouchi = (ImageView) findViewById(R.id.iv_qualification_shouchi);
        iv_qualification_front = (ImageView) findViewById(R.id.iv_qualification_front);
        iv_qualification_back = (ImageView) findViewById(R.id.iv_qualification_back);
        iv_qualification_zhizhao = (ImageView) findViewById(R.id.iv_qualification_zhizhao);
        tv_qualification_next = (TextView) findViewById(R.id.tv_qualification_next);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "资质信息", "");
        iv_qualification_shouchi.setOnClickListener(this);
        iv_qualification_front.setOnClickListener(this);
        iv_qualification_back.setOnClickListener(this);
        iv_qualification_zhizhao.setOnClickListener(this);
        tv_qualification_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_qualification_shouchi:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic1();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_front:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic2();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_back:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic3();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_zhizhao:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic4();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.tv_qualification_next:
                String account = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_USEACCOUNT, "");
                String name = et_qualification_name.getText().toString();
                String idNum = et_qualification_idNum.getText().toString();

                if (qualification_verification()) {

                    if (ClickFilter.filter()) {
                        return;
                    }

                    requestHandleArrayList.add(requestAction.qualificationInfo(this, account, shouchiPicUrl, frontPicUrl, backPicUrl,
                            name, idNum, zhizhaoPicUrl));
                }
                break;
        }

    }

    //手持
    private void selectPic1() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_SHOUCHI);
    }

    //正面
    private void selectPic2() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_FRONT);
    }

    //背面
    private void selectPic3() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_BACK);
    }


    //执照
    private void selectPic4() {
//        ChoosePhoto mChoosePhoto = new ChoosePhoto(mContext) {
//            @Override
//            protected void setPortraitonSuccess(String myPicUrl) {
//
//            }
//
//            @Override
//            protected void setPortraitonSuccess(String myPicUrl, boolean a) {
//            }
//
//            @Override
//            protected void setPortraitFailure() {
////                Log.e("上传图片失败", "上传图片失败");
//            }
//
//            @Override
//            protected void setPicsSuccess(final List<String> picsUrl, boolean over) {//这个成功是从本地上传到阿里云成功
//                if (over) {
//                    zhizhaoPicUrl = picsUrl.get(0);
//
//                    QualificationInfoActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            PictureUtlis.loadImageViewHolder(mContext, zhizhaoPicUrl, R.mipmap.but_tianjia, iv_qualification_zhizhao);
//                        }
//                    });
////                    Log.e("设置身份证执照图片成功", zhizhaoPicUrl);
//                }
//            }
//        };
//        mChoosePhoto.setMuti(1);
//        mChoosePhoto.setPics2();

        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_ZHIZHAO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0);
            }
            switch (requestCode) {
                case REQUEST_CODE_SHOUCHI:

//                    if (photos != null) {
//                        Log.e("选中图片路径", "==" + photos);
//                        final Bitmap image = BitmapFactory.decodeFile(photos);
//                        progressDialog = new ProgressDialog(mContext);
//                        progressDialog.setMessage("图片上传中，请稍候...");
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////                progressDialog.setCancelable(false);
//                        progressDialog.show();
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new AliOss(mContext, AliOss.setPicName("hygoilstationPics"), BitmapUtils.compressImage(image)) {//这里设置上传的地址
//                                    @Override
//                                    protected void uploadProgress(long currentSize, long totalSize) {
//
//                                    }
//
//                                    @Override
//                                    public void uploadSuccess(String myPicUrl) {
//
//                                        progressDialog.dismiss();
//                                        shouchiPicUrl = myPicUrl;
//                                        QualificationInfoActivity.this.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                PictureUtlis.loadImageViewHolder(mContext, shouchiPicUrl, R.mipmap.but_tianjia, iv_qualification_shouchi);
//                                            }
//                                        });
//
//                                    }
//
//                                    @Override
//                                    protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//
//                                    }
//                                }.start();
//                            }
//
//                        }).start();
//                    }
                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            shouchiPicUrl = picUrl;
                            QualificationInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, shouchiPicUrl, R.mipmap.but_tianjia, iv_qualification_shouchi);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            Log.e("上传照片", "上传手持身份证照片失败");
                        }
                    }.setSinglePic();
                    break;
                case REQUEST_CODE_FRONT:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            frontPicUrl = picUrl;
                            QualificationInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, frontPicUrl, R.mipmap.but_tianjia, iv_qualification_front);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            Log.e("上传照片", "上传身份证正面照片失败");
                        }
                    }.setSinglePic();


                    break;
                case REQUEST_CODE_BACK:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            backPicUrl = picUrl;
                            QualificationInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, backPicUrl, R.mipmap.but_tianjia, iv_qualification_back);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            Log.e("上传照片", "上传身份证背面照片失败");
                        }
                    }.setSinglePic();


                    break;
                case REQUEST_CODE_ZHIZHAO:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            zhizhaoPicUrl = picUrl;
                            QualificationInfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, zhizhaoPicUrl, R.mipmap.but_tianjia, iv_qualification_zhizhao);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            Log.e("上传照片", "上传执照照片失败");
                        }
                    }.setSinglePic();

                    break;
            }
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_QUALIFICATIONINFO:
//                startActivity(new Intent(this, CertificateBankcardActivity.class));

                MToast.showToast(this, "资质认证信息提交成功，请等待后台审核通过后可提现！");

                finish();
                break;
            default:
                break;
        }
    }

    //页面数据验证
    private boolean qualification_verification() {
        if (TextUtils.isEmpty(et_qualification_name.getText().toString().trim())) {
            showPrompt("姓名不能为空");
            return false;
        } else if (et_qualification_name.getText().toString().length() > 8) {
            showPrompt("您输入的姓名有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(et_qualification_idNum.getText().toString())) {
            showPrompt("身份证号不能为空");
            return false;
        } else if (et_qualification_idNum.getText().toString().length() != 18) {
            showPrompt("身份证号有误，请您确认后重新输入");
            return false;
        } else if (TextUtils.isEmpty(shouchiPicUrl)) {
            showPrompt("手持身份证照不能为空");
            return false;
        } else if (TextUtils.isEmpty(frontPicUrl)) {
            showPrompt("身份证正面照不能为空");
            return false;
        } else if (TextUtils.isEmpty(backPicUrl)) {
            showPrompt("手持身份证背面照不能为空");
            return false;
        } else if (TextUtils.isEmpty(zhizhaoPicUrl)) {
            showPrompt("执照图片不能为空");
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

    //简单验证身份证号输入是否正确
    private boolean isIDCard(String data) {
        String regex = "(/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/)";
        return data.matches(regex);
    }

}
