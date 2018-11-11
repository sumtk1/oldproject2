package com.gloiot.hygooilstation.ui.activity.authentication;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * 给老用户的资质信息页面（只增加上传一张负责人和油站合影）
 * Created by Dlt on 2017/8/10 13:58
 */
public class QualificationInfoForOldUserActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_HEZHAO = 5;

    @Bind(R.id.iv_qualification_shouchi)
    ImageView mIvQualificationShouchi;
    @Bind(R.id.iv_qualification_front)
    ImageView mIvQualificationFront;
    @Bind(R.id.iv_qualification_back)
    ImageView mIvQualificationBack;
    @Bind(R.id.tv_qualification_name)
    TextView mTvQualificationName;
    @Bind(R.id.tv_qualification_id_num)
    TextView mTvQualificationIdNum;
    @Bind(R.id.iv_qualification_zhizhao)
    ImageView mIvQualificationZhizhao;
    @Bind(R.id.iv_qualification_oilstation_groupphoto)
    ImageView mIvQualificationOilstationGroupphoto;

    private String name, idNum;
    private String shouchiPicUrl, frontPicUrl, backPicUrl, zhizhaoPicUrl, hezhaoPicUrl;

    @OnClick({R.id.iv_qualification_oilstation_groupphoto, R.id.tv_qualification_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qualification_oilstation_groupphoto:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        selectPic();
                    }
                }, R.string.camera_updatepics, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                break;
            case R.id.tv_qualification_next:
                if (TextUtils.isEmpty(hezhaoPicUrl)) {
                    MToast.showToast(mContext, "负责人与油站合照不能为空");
                } else {
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
        return R.layout.activity_qualification_info_for_old_user;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "资质信息", "");
        requestHandleArrayList.add(requestAction.getQualificationInfoForOldUser(this));
    }

    //油站负责人合照
    private void selectPic() {
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

                case REQUEST_CODE_HEZHAO:

                    new UploadingSelectedPics(mContext, photos) {
                        @Override
                        protected void setSinglePicSuccess(String picUrl) {
                            hezhaoPicUrl = picUrl;
                            QualificationInfoForOldUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PictureUtlis.loadImageViewHolder(mContext, hezhaoPicUrl, R.mipmap.but_tianjia, mIvQualificationOilstationGroupphoto);
                                }
                            });
                        }

                        @Override
                        protected void setSinglePicFailure() {
                            Log.e("上传照片", "上传油站负责人合照失败");
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

            case RequestAction.TAG_GETQUALIFICATIONINFO:
                L.e("获取资质认证信息", response.toString());
                name = response.getString("姓名");
                idNum = response.getString("身份证号");
                shouchiPicUrl = response.getString("手持身份证照");
                frontPicUrl = response.getString("身份证正面");
                backPicUrl = response.getString("身份证背面");
                zhizhaoPicUrl = response.getString("执照图片");
                mTvQualificationName.setText(name);
                mTvQualificationIdNum.setText(idNum);
                PictureUtlis.loadImageViewHolder(mContext, shouchiPicUrl, R.mipmap.but_tianjia, mIvQualificationShouchi);
                PictureUtlis.loadImageViewHolder(mContext, frontPicUrl, R.mipmap.but_tianjia, mIvQualificationFront);
                PictureUtlis.loadImageViewHolder(mContext, backPicUrl, R.mipmap.but_tianjia, mIvQualificationBack);
                PictureUtlis.loadImageViewHolder(mContext, zhizhaoPicUrl, R.mipmap.but_tianjia, mIvQualificationZhizhao);

                break;

            case RequestAction.TAG_NEWQUALIFICATIONINFO:

                MToast.showToast(this, "资质认证信息提交成功，请等待后台审核通过后可提现！");

                finish();
                break;
            default:
                break;
        }
    }


}
