package com.gloiot.hygooilstation.ui.activity.authentication;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.abcaaa.photopicker.PhotoPicker;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.server.picture.UploadingSelectedPics;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.ClickFilter;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygooilstation.R.id.et_qualification_idNum;
import static com.gloiot.hygooilstation.R.id.et_qualification_name;

/**
 * 给新用户的资质信息页面（需要上传所有必要信息）
 * Created by Dlt on 2017/8/10 13:58
 */
public class QualificationInfoForNewUserActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_SHOUCHI = 1;
    public static final int REQUEST_CODE_FRONT = 2;
    public static final int REQUEST_CODE_BACK = 3;
    public static final int REQUEST_CODE_ZHIZHAO = 4;
    public static final int REQUEST_CODE_HEZHAO = 5;

    @Bind(R.id.iv_qualification_shouchi)
    ImageView mIvQualificationShouchi;
    @Bind(R.id.iv_qualification_front)
    ImageView mIvQualificationFront;
    @Bind(R.id.iv_qualification_back)
    ImageView mIvQualificationBack;
    @Bind(et_qualification_name)
    EditText mEtQualificationName;
    @Bind(et_qualification_idNum)
    EditText mEtQualificationIdNum;
    @Bind(R.id.iv_qualification_zhizhao)
    ImageView mIvQualificationZhizhao;
    @Bind(R.id.iv_qualification_oilstation_groupphoto)
    ImageView mIvQualificationOilstationGroupphoto;

    private String name, idNum;
    private String shouchiPicUrl, frontPicUrl, backPicUrl, zhizhaoPicUrl, hezhaoPicUrl;

    @OnClick({R.id.iv_qualification_shouchi, R.id.iv_qualification_front, R.id.iv_qualification_back,
            R.id.iv_qualification_zhizhao, R.id.iv_qualification_oilstation_groupphoto, R.id.tv_qualification_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qualification_shouchi:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic1();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_front:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic2();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_back:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic3();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_zhizhao:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic4();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.iv_qualification_oilstation_groupphoto:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic5();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.tv_qualification_next:
                String name = mEtQualificationName.getText().toString().trim();
                String idNum = mEtQualificationIdNum.getText().toString().trim();

                if (qualification_verification()) {

                    if (ClickFilter.filter()) {
                        return;
                    }

                    requestHandleArrayList.add(requestAction.newQualificationInfo(this, shouchiPicUrl, frontPicUrl, backPicUrl,
                            name, idNum, zhizhaoPicUrl, hezhaoPicUrl, "", "", "", "", ""));
                }
                break;
        }
    }

    @Override
    public int initResource() {
        return R.layout.activity_qualification_info_for_new_user;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "资质信息", "");
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
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_ZHIZHAO);
    }

    //油站负责人合照
    private void selectPic5() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_CODE_HEZHAO);
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

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            shouchiPicUrl = picUrl;
                            QualificationInfoForNewUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, shouchiPicUrl, R.mipmap.but_tianjia, mIvQualificationShouchi);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传手持身份证照片失败");
                        }
                    }.setSinglePic();
                    break;
                case REQUEST_CODE_FRONT:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            frontPicUrl = picUrl;
                            QualificationInfoForNewUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, frontPicUrl, R.mipmap.but_tianjia, mIvQualificationFront);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传身份证正面照片失败");
                        }
                    }.setSinglePic();

                    break;
                case REQUEST_CODE_BACK:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            backPicUrl = picUrl;
                            QualificationInfoForNewUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, backPicUrl, R.mipmap.but_tianjia, mIvQualificationBack);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传身份证背面照片失败");
                        }
                    }.setSinglePic();

                    break;
                case REQUEST_CODE_ZHIZHAO:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            zhizhaoPicUrl = picUrl;
                            QualificationInfoForNewUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, zhizhaoPicUrl, R.mipmap.but_tianjia, mIvQualificationZhizhao);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传执照照片失败");
                        }
                    }.setSinglePic();

                    break;

                case REQUEST_CODE_HEZHAO:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            hezhaoPicUrl = picUrl;
                            QualificationInfoForNewUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, hezhaoPicUrl, R.mipmap.but_tianjia, mIvQualificationOilstationGroupphoto);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传油站负责人合照失败");
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
            case RequestAction.TAG_NEWQUALIFICATIONINFO:
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
        if (TextUtils.isEmpty(mEtQualificationName.getText().toString().trim())) {
            MToast.showToast(mContext, "姓名不能为空");
            return false;
        } else if (mEtQualificationName.getText().toString().trim().length() > 8) {
            MToast.showToast(mContext, "您输入的姓名有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(mEtQualificationIdNum.getText().toString().trim())) {
            MToast.showToast(mContext, "身份证号不能为空");
            return false;
        } else if (mEtQualificationIdNum.getText().toString().trim().length() != 18) {
            MToast.showToast(mContext, "身份证号有误，请您确认后重新输入");
            return false;
        } else if (TextUtils.isEmpty(shouchiPicUrl)) {
            MToast.showToast(mContext, "手持身份证照不能为空");
            return false;
        } else if (TextUtils.isEmpty(frontPicUrl)) {
            MToast.showToast(mContext, "身份证正面照不能为空");
            return false;
        } else if (TextUtils.isEmpty(backPicUrl)) {
            MToast.showToast(mContext, "手持身份证背面照不能为空");
            return false;
        } else if (TextUtils.isEmpty(zhizhaoPicUrl)) {
            MToast.showToast(mContext, "执照图片不能为空");
            return false;
        } else if (TextUtils.isEmpty(hezhaoPicUrl)) {
            MToast.showToast(mContext, "负责人与油站合照不能为空");
            return false;
        } else {
            return true;
        }
    }


}
