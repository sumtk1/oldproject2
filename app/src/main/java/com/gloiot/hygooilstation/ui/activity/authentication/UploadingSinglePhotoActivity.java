package com.gloiot.hygooilstation.ui.activity.authentication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcaaa.photopicker.PhotoPicker;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.server.picture.UploadingSelectedPics;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.NoDoubleClickUtils;
import com.gloiot.hygooilstation.utils.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygooilstation.R.id.iv_toptitle_back;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.BUSINESS_LICENSE;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.LEGAL_PERSON_BACK;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.LEGAL_PERSON_FRONT;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.LEGAL_PERSON_SHOUCHI;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.PRINCIPAL_BACK;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.PRINCIPAL_FRONT;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.PRINCIPAL_GROUPPHOTO;
import static com.gloiot.hygooilstation.ui.activity.authentication.NewQualificationActivity.PRINCIPAL_SHOUCHI;

/**
 * 资质认证--上传单张照片
 * Created by Dlt on 2017/10/19 14:59
 */
public class UploadingSinglePhotoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(iv_toptitle_back)
    ImageView mIvToptitleBack;
    @Bind(R.id.tv_toptitle_right)
    TextView mTvToptitleRight;
    @Bind(R.id.iv_photo)
    ImageView mIvPhoto;
    @Bind(R.id.iv_delete)
    ImageView mIvDelete;
    @Bind(R.id.tv_warm_prompt)
    TextView mTvWarmPrompt;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;

    public static final int REQUEST_PICS = 6;//选取图片请求标识
    public static final int RESULT_SINGLEPHOTO = 7;//Activity返回标识

    private int flag;
    private String photoUrl = "";
    private boolean isHaveUploadedPhoto = false;//是否已经有上传的照片

    @Override
    public int initResource() {
        return R.layout.activity_uploading_single_photo;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        photoUrl = intent.getStringExtra("photoUrl");

        switch (flag) {
            //负责人
            case PRINCIPAL_FRONT:
                CommonUtlis.setTitleBar(this, false, "负责人身份证正面", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhegnzhegnmian);
                }
//                mTvWarmPrompt.setText("1.需提供负责人有效身份证件，所有信息轮廓清晰可见\n2.务必保证信息内容真实有效，不得做任何涂改、遮挡");
                getWarmPrompt("负责人身份证正面");
                break;
            case PRINCIPAL_BACK:
                CommonUtlis.setTitleBar(this, false, "负责人身份证反面", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhengfanmian);
                }
//                mTvWarmPrompt.setText("1.需提供负责人有效身份证件，所有信息轮廓清晰可见\n2.不得做任何涂改、遮挡，过期证件无效");
                getWarmPrompt("负责人身份证反面");
                break;
            case PRINCIPAL_SHOUCHI:
                CommonUtlis.setTitleBar(this, false, "负责人手持身份证", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shouchishenfenzheng);
                }
//                mTvWarmPrompt.setText("1.负责人手持证件人面部无遮挡，五官清晰可见\n2.身份证各项信息及头像均清晰可见，无遮挡");
                getWarmPrompt("负责人手持身份证");
                break;
            case PRINCIPAL_GROUPPHOTO:
                CommonUtlis.setTitleBar(this, false, "负责人与油站合照", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_yufuzerenhezhao);
                }
//                mTvWarmPrompt.setText("1.请上传油站负责人与油站的合照\n2.油站名需清晰可见，无遮挡物遮挡" +
//                        "\n3.照片画质为高清，负责人不可有遮挡，不可戴帽，不可戴墨镜，以便审核人员进行审核");
                getWarmPrompt("负责人与油站合照");
                break;
            //法人
            case LEGAL_PERSON_FRONT:
                CommonUtlis.setTitleBar(this, false, "法人身份证正面", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhegnzhegnmian);
                }
//                mTvWarmPrompt.setText("1.需提供法人有效身份证件，所有信息轮廓清晰可见\n2.务必保证信息内容真实有效，不得做任何涂改、遮挡");
                getWarmPrompt("法人身份证正面");
                break;
            case LEGAL_PERSON_BACK:
                CommonUtlis.setTitleBar(this, false, "法人身份证反面", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhengfanmian);
                }
//                mTvWarmPrompt.setText("1.需提供法人有效身份证件，所有信息轮廓清晰可见\n2.不得做任何涂改、遮挡，过期证件无效");
                getWarmPrompt("法人身份证反面");
                break;
            case LEGAL_PERSON_SHOUCHI:
                CommonUtlis.setTitleBar(this, false, "法人手持身份证", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shouchishenfenzheng);
                }
//                mTvWarmPrompt.setText("1.法人手持证件人面部无遮挡，五官清晰可见\n2.身份证各项信息及头像均清晰可见，无遮挡");
                getWarmPrompt("法人手持身份证");
                break;
            //营业执照
            case BUSINESS_LICENSE:
                CommonUtlis.setTitleBar(this, false, "营业执照上传", "");
                if (!photoUrl.isEmpty()) {
                    CommonUtlis.setDisplayImage(mIvPhoto, photoUrl, 0, R.drawable.pic_loading);
                } else {
                    CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_yinyizhizhaoshagnchuan);
                }
//                mTvWarmPrompt.setText("请上传清晰彩色原件扫描或者数码照片，如复印需要加盖公章，确保信息展示完整、清晰，并真实有效");
                getWarmPrompt("营业执照上传");
                break;
        }

        mIvToptitleBack.setVisibility(View.VISIBLE);

        if (photoUrl.isEmpty()) {
            mTvConfirm.setText("选择图片");
//            mIvDelete.setVisibility(View.GONE);
        } else {
            mTvConfirm.setText("重新选择");
//            mIvDelete.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取相应的温馨提示
     *
     * @param flagString
     */
    private void getWarmPrompt(String flagString) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("提示类别", flagString);
        requestHandleArrayList.add(requestAction.getZizhiWarmPrompt(UploadingSinglePhotoActivity.this, hashMap));
    }

    @OnClick({R.id.iv_toptitle_back, R.id.tv_toptitle_right, R.id.iv_delete, R.id.tv_confirm})
    @Override
    public void onClick(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
        switch (v.getId()) {
            case R.id.iv_toptitle_back:
                onBackTouched();
                break;
            case R.id.tv_toptitle_right:
                onCommitClick();
                break;
            case R.id.iv_delete:
//                photoUrl = "";
//                mIvDelete.setVisibility(View.GONE);
//                mTvConfirm.setText("选择图片");
//                isHaveUploadedPhoto = true;
//                mTvToptitleRight.setText("确定");
//                mTvToptitleRight.setTextColor(Color.parseColor("#449FFB"));
//                switch (flag) {
//                    //负责人
//                    case PRINCIPAL_FRONT:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhegnzhegnmian);
//                        break;
//                    case PRINCIPAL_BACK:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhengfanmian);
//                        break;
//                    case PRINCIPAL_SHOUCHI:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shouchishenfenzheng);
//                        break;
//                    case PRINCIPAL_GROUPPHOTO:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_yufuzerenhezhao);
//                        break;
//                    //法人
//                    case LEGAL_PERSON_FRONT:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhegnzhegnmian);
//                        break;
//                    case LEGAL_PERSON_BACK:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shenfenzhengfanmian);
//                        break;
//                    case LEGAL_PERSON_SHOUCHI:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_shouchishenfenzheng);
//                        break;
//                    //营业执照
//                    case BUSINESS_LICENSE:
//                        CommonUtlis.setDisplayImage(mIvPhoto, "", 0, R.drawable.pic_yinyizhizhaoshagnchuan);
//                        break;
//                }
                break;
            case R.id.tv_confirm:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }
    }

    /**
     * 选取照片
     */
    private void selectPic() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, REQUEST_PICS);
    }

    /**
     * 确定按钮点击事件
     */
    private void onCommitClick() {
        if (isHaveUploadedPhoto) {
            HashMap<String, Object> hashMap = new HashMap<>();

            switch (flag) {
                //负责人
                case PRINCIPAL_FRONT:
                    hashMap.put("身份证正面", photoUrl);
                    break;
                case PRINCIPAL_BACK:
                    hashMap.put("身份证背面", photoUrl);
                    break;
                case PRINCIPAL_SHOUCHI:
                    hashMap.put("手持身份证照", photoUrl);
                    break;
                case PRINCIPAL_GROUPPHOTO:
                    hashMap.put("油站合影", photoUrl);
                    break;
                //法人
                case LEGAL_PERSON_FRONT:
                    hashMap.put("法人身份证正面", photoUrl);
                    break;
                case LEGAL_PERSON_BACK:
                    hashMap.put("法人身份证反面", photoUrl);
                    break;
                case LEGAL_PERSON_SHOUCHI:
                    hashMap.put("法人手持身份证照", photoUrl);
                    break;
                //营业执照
                case BUSINESS_LICENSE:
                    hashMap.put("执照图片", photoUrl);
                    break;
            }
            uploadingPhoto(hashMap);

        }
    }

    /**
     * 上传资质认证照片
     *
     * @param hashMap
     */
    private void uploadingPhoto(HashMap<String, Object> hashMap) {
        requestHandleArrayList.add(requestAction.uploadingZizhiPhoto(UploadingSinglePhotoActivity.this, hashMap));
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
                case REQUEST_PICS:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            photoUrl = picUrl;
                            UploadingSinglePhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, photoUrl, R.drawable.pic_loading, mIvPhoto);
                                    isHaveUploadedPhoto = true;
                                    mTvToptitleRight.setText("确定");
                                    mTvToptitleRight.setTextColor(Color.parseColor("#449FFB"));
                                    mTvConfirm.setText("重新选择");
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            L.e("上传照片", "上传照片失败");
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
            case RequestAction.TAG_ZIZHIWARMPROMPT://温馨提示
                L.e("温馨提示", response.toString());
                mTvWarmPrompt.setText(response.getString("温馨提示"));
                break;
            case RequestAction.TAG_ZIZHIUPLOADINGPHOTO://上传照片
                Intent resultIntent = new Intent();
                resultIntent.putExtra("photoUrl", photoUrl);
                UploadingSinglePhotoActivity.this.setResult(RESULT_SINGLEPHOTO, resultIntent);//结果码用于标识返回自哪个Activity
                UploadingSinglePhotoActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 返回按钮事件
     */
    private void onBackTouched() {
        if (isHaveUploadedPhoto) {

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
