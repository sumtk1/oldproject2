package com.gloiot.hygooilstation.ui.activity.authentication;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.L;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.NoDoubleClickUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygooilstation.ui.activity.authentication.QualificationOtherLicenceActivity.RESULT_OTHERLICENCE;
import static com.gloiot.hygooilstation.ui.activity.authentication.UploadingSinglePhotoActivity.RESULT_SINGLEPHOTO;

/**
 * 新的资质认证
 * Created by Dlt on 2017/10/19 14:24
 */
public class NewQualificationActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_principal_name)
    EditText mEtPrincipalName;
    @Bind(R.id.et_principal_id_num)
    EditText mEtPrincipalIdNum;
    @Bind(R.id.tv_hint_principal_front)
    TextView mTvHintPrincipalFront;
    @Bind(R.id.tv_state_principal_front)
    TextView mTvStatePrincipalFront;
    @Bind(R.id.tv_hint_principal_back)
    TextView mTvHintPrincipalBack;
    @Bind(R.id.tv_state_principal_back)
    TextView mTvStatePrincipalBack;
    @Bind(R.id.tv_hint_principal_shouchi)
    TextView mTvHintPrincipalShouchi;
    @Bind(R.id.tv_state_principal_shouchi)
    TextView mTvStatePrincipalShouchi;
    @Bind(R.id.tv_hint_principal_group_photo)
    TextView mTvHintPrincipalGroupPhoto;
    @Bind(R.id.tv_state_principal_group_photo)
    TextView mTvStatePrincipalGroupPhoto;
    @Bind(R.id.tv_hint_legal_person_front)
    TextView mTvHintLegalPersonFront;
    @Bind(R.id.tv_state_legal_person_front)
    TextView mTvStateLegalPersonFront;
    @Bind(R.id.tv_hint_legal_person_back)
    TextView mTvHintLegalPersonBack;
    @Bind(R.id.tv_state_legal_person_back)
    TextView mTvStateLegalPersonBack;
    @Bind(R.id.tv_hint_legal_person_shouchi)
    TextView mTvHintLegalPersonShouchi;
    @Bind(R.id.tv_state_legal_person_shouchi)
    TextView mTvStateLegalPersonShouchi;
    @Bind(R.id.tv_hint_business_license)
    TextView mTvHintBusinessLicense;
    @Bind(R.id.tv_state_business_license)
    TextView mTvStateBusinessLicense;
    @Bind(R.id.tv_hint_other_information)
    TextView mTvHintOtherInformation;
    @Bind(R.id.tv_state_other_information)
    TextView mTvStateOtherInformation;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;

    public static final int PRINCIPAL_FRONT = 11;//负责人身份证前面
    public static final int PRINCIPAL_BACK = 12;//背面
    public static final int PRINCIPAL_SHOUCHI = 13;//手持
    public static final int PRINCIPAL_GROUPPHOTO = 14;//和油站合照
    public static final int LEGAL_PERSON_FRONT = 15;//法人身份证前面
    public static final int LEGAL_PERSON_BACK = 16;//背面
    public static final int LEGAL_PERSON_SHOUCHI = 17;//手持
    public static final int BUSINESS_LICENSE = 18;//营业执照
    public static final int INDUSTRY_LICENCE = 19;//行业许可证

    private String principalName = "", principalIDNum = "";
    private String principalFrontUrl = "", principalBackUrl = "", principalShouchiUrl = "", principalGroupPhotoUrl = "", legalPersonFrontUrl = "",
            legalPersonBackUrl = "", legalPersonShouchiUrl = "", businessLicenseUrl = "";
    private String otherLicence = "";//其他材料

    @Override
    public int initResource() {
        return R.layout.activity_new_qualification;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "资质认证", "");

//        mockData();

        requestHandleArrayList.add(requestAction.getQualificationInfoForOldUser(this));

    }

    private void mockData() {
        principalFrontUrl = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/app-minishop-img/yiyeshop201708050241232137176525.jpg";
        principalBackUrl = "http://qqwlw.oss-cn-shenzhen.aliyuncs.com/app-minishop-img/yiyeshop201708050241231133683741.jpg";
        principalShouchiUrl = "http://zykshop.qqjlb.cn/zyypayPics/zyyPay201705160612231802700228.jpg";
        principalGroupPhotoUrl = "http://zykshop.qqjlb.cn/zyypayPics/zyyPay201705160612161839522650.jpg";

    }

    @OnClick({R.id.rl_principal_front, R.id.rl_principal_back, R.id.rl_principal_shouchi, R.id.rl_principal_group_photo, R.id.rl_legal_person_front,
            R.id.rl_legal_person_back, R.id.rl_legal_person_shouchi, R.id.rl_business_license, R.id.rl_other_information, R.id.tv_confirm})
    @Override
    public void onClick(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
        Intent intent;
        switch (v.getId()) {
            //---------------------------负责人
            case R.id.rl_principal_front:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", PRINCIPAL_FRONT);
                intent.putExtra("photoUrl", principalFrontUrl);
                startActivityForResult(intent, PRINCIPAL_FRONT);
                break;
            case R.id.rl_principal_back:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", PRINCIPAL_BACK);
                intent.putExtra("photoUrl", principalBackUrl);
                startActivityForResult(intent, PRINCIPAL_BACK);
                break;
            case R.id.rl_principal_shouchi:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", PRINCIPAL_SHOUCHI);
                intent.putExtra("photoUrl", principalShouchiUrl);
                startActivityForResult(intent, PRINCIPAL_SHOUCHI);
                break;
            case R.id.rl_principal_group_photo:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", PRINCIPAL_GROUPPHOTO);
                intent.putExtra("photoUrl", principalGroupPhotoUrl);
                startActivityForResult(intent, PRINCIPAL_GROUPPHOTO);
                break;
            //---------------------------法人
            case R.id.rl_legal_person_front:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", LEGAL_PERSON_FRONT);
                intent.putExtra("photoUrl", legalPersonFrontUrl);
                startActivityForResult(intent, LEGAL_PERSON_FRONT);
                break;
            case R.id.rl_legal_person_back:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", LEGAL_PERSON_BACK);
                intent.putExtra("photoUrl", legalPersonBackUrl);
                startActivityForResult(intent, LEGAL_PERSON_BACK);
                break;
            case R.id.rl_legal_person_shouchi:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", LEGAL_PERSON_SHOUCHI);
                intent.putExtra("photoUrl", legalPersonShouchiUrl);
                startActivityForResult(intent, LEGAL_PERSON_SHOUCHI);
                break;
            //---------------------------营业执照
            case R.id.rl_business_license:
                intent = new Intent(NewQualificationActivity.this, UploadingSinglePhotoActivity.class);
                intent.putExtra("flag", BUSINESS_LICENSE);
                intent.putExtra("photoUrl", businessLicenseUrl);
                startActivityForResult(intent, BUSINESS_LICENSE);
                break;
            //---------------------------其他材料
            case R.id.rl_other_information:
                intent = new Intent(NewQualificationActivity.this, QualificationOtherLicenceActivity.class);
                intent.putExtra("photoUrl", otherLicence);
                startActivityForResult(intent, INDUSTRY_LICENCE);
                break;
            case R.id.tv_confirm:
                if (verifyData()) {
                    int versionCode = CommonUtlis.getVersionCode(mContext);
                    requestHandleArrayList.add(requestAction.newQualificationInfo(this, principalShouchiUrl, principalFrontUrl, principalBackUrl, principalName,
                            principalIDNum, businessLicenseUrl, principalGroupPhotoUrl, legalPersonFrontUrl, legalPersonBackUrl, legalPersonShouchiUrl,
                            otherLicence, versionCode + ""));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoUrl = "";
        if (data != null) {
            photoUrl = data.getStringExtra("photoUrl");
        }
        switch (resultCode) {
            case RESULT_SINGLEPHOTO:
                switch (requestCode) {
                    case PRINCIPAL_FRONT:
                        if (!photoUrl.isEmpty()) {
                            mTvHintPrincipalFront.setText("");
                            mTvStatePrincipalFront.setText("已上传");
                            principalFrontUrl = photoUrl;
                        } else {
                            mTvHintPrincipalFront.setText("负责人身份证正面照片");
                            mTvStatePrincipalFront.setText("");
                            principalFrontUrl = photoUrl;
                        }
                        break;
                    case PRINCIPAL_BACK:
                        if (!photoUrl.isEmpty()) {
                            mTvHintPrincipalBack.setText("");
                            mTvStatePrincipalBack.setText("已上传");
                            principalBackUrl = photoUrl;
                        } else {
                            mTvHintPrincipalBack.setText("负责人身份证反面照片");
                            mTvStatePrincipalBack.setText("");
                            principalBackUrl = photoUrl;
                        }
                        break;
                    case PRINCIPAL_SHOUCHI:
                        if (!photoUrl.isEmpty()) {
                            mTvHintPrincipalShouchi.setText("");
                            mTvStatePrincipalShouchi.setText("已上传");
                            principalShouchiUrl = photoUrl;
                        } else {
                            mTvHintPrincipalShouchi.setText("请上传手持身份证照片");
                            mTvStatePrincipalShouchi.setText("");
                            principalShouchiUrl = photoUrl;
                        }
                        break;
                    case PRINCIPAL_GROUPPHOTO:
                        if (!photoUrl.isEmpty()) {
                            mTvHintPrincipalGroupPhoto.setText("");
                            mTvStatePrincipalGroupPhoto.setText("已上传");
                            principalGroupPhotoUrl = photoUrl;
                        } else {
                            mTvHintPrincipalGroupPhoto.setText("请上传负责人和油站合照");
                            mTvStatePrincipalGroupPhoto.setText("");
                            principalGroupPhotoUrl = photoUrl;
                        }
                        break;
                    case LEGAL_PERSON_FRONT:
                        if (!photoUrl.isEmpty()) {
                            mTvHintLegalPersonFront.setText("");
                            mTvStateLegalPersonFront.setText("已上传");
                            legalPersonFrontUrl = photoUrl;
                        } else {
                            mTvHintLegalPersonFront.setText("法人身份证正面照片(非必填)");
                            mTvStateLegalPersonFront.setText("");
                            legalPersonFrontUrl = photoUrl;
                        }
                        break;
                    case LEGAL_PERSON_BACK:
                        if (!photoUrl.isEmpty()) {
                            mTvHintLegalPersonBack.setText("");
                            mTvStateLegalPersonBack.setText("已上传");
                            legalPersonBackUrl = photoUrl;
                        } else {
                            mTvHintLegalPersonBack.setText("法人身份证反面照片(非必填)");
                            mTvStateLegalPersonBack.setText("");
                            legalPersonBackUrl = photoUrl;
                        }
                        break;
                    case LEGAL_PERSON_SHOUCHI:
                        if (!photoUrl.isEmpty()) {
                            mTvHintLegalPersonShouchi.setText("");
                            mTvStateLegalPersonShouchi.setText("已上传");
                            legalPersonShouchiUrl = photoUrl;
                        } else {
                            mTvHintLegalPersonShouchi.setText("法人手持身份证照片(非必填)");
                            mTvStateLegalPersonShouchi.setText("");
                            legalPersonShouchiUrl = photoUrl;
                        }
                        break;
                    case BUSINESS_LICENSE://营业执照
                        if (!photoUrl.isEmpty()) {
                            mTvHintBusinessLicense.setText("");
                            mTvStateBusinessLicense.setText("已上传");
                            businessLicenseUrl = photoUrl;
                        } else {
                            mTvHintBusinessLicense.setText("请上传营业执照照片");
                            mTvStateBusinessLicense.setText("");
                            businessLicenseUrl = photoUrl;
                        }
                        break;
                }
                break;
            case RESULT_OTHERLICENCE:
                if (!photoUrl.isEmpty()) {
                    otherLicence = photoUrl;
                    L.e("附加材料返回1", otherLicence);
                    if (otherLicence.contains("|")) {
                        String[] s = otherLicence.split("\\|");
                        for (int i = 0; i < s.length; i++) {
                            L.e("附加材料返回2", s[i]);
                        }
                        mTvStateOtherInformation.setText("已上传" + s.length + "张");
                    } else {
                        mTvStateOtherInformation.setText("已上传1张");
                    }
                    mTvHintOtherInformation.setText("");
                } else {
                    otherLicence = photoUrl;
                    mTvHintOtherInformation.setText("请上传附加材料（非必填）");
                    mTvStateOtherInformation.setText("");
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETQUALIFICATIONINFO:
                L.e("获取资质认证信息", response.toString());
                principalName = response.getString("姓名");
                principalIDNum = response.getString("身份证号");
                principalShouchiUrl = response.getString("手持身份证照");
                principalFrontUrl = response.getString("身份证正面");
                principalBackUrl = response.getString("身份证背面");
                principalGroupPhotoUrl = response.getString("油站合影");
                businessLicenseUrl = response.getString("执照图片");
                legalPersonFrontUrl = response.getString("法人身份证正面");
                legalPersonBackUrl = response.getString("法人身份证反面");
                legalPersonShouchiUrl = response.getString("法人手持身份证照");
                otherLicence = response.getString("附加材料");

                mEtPrincipalName.setText(principalName);
                mEtPrincipalName.setSelection(principalName.length());
                mEtPrincipalIdNum.setText(principalIDNum);

                if (!TextUtils.isEmpty(principalFrontUrl)) {
                    mTvHintPrincipalFront.setText("");
                    mTvStatePrincipalFront.setText("已上传");
                } else {
                    mTvHintPrincipalFront.setText("负责人身份证正面照片");
                    mTvStatePrincipalFront.setText("");
                }

                if (!TextUtils.isEmpty(principalBackUrl)) {
                    mTvHintPrincipalBack.setText("");
                    mTvStatePrincipalBack.setText("已上传");
                } else {
                    mTvHintPrincipalBack.setText("负责人身份证反面照片");
                    mTvStatePrincipalBack.setText("");
                }

                if (!TextUtils.isEmpty(principalShouchiUrl)) {
                    mTvHintPrincipalShouchi.setText("");
                    mTvStatePrincipalShouchi.setText("已上传");
                } else {
                    mTvHintPrincipalShouchi.setText("请上传手持身份证照片");
                    mTvStatePrincipalShouchi.setText("");
                }

                if (!TextUtils.isEmpty(principalGroupPhotoUrl)) {
                    mTvHintPrincipalGroupPhoto.setText("");
                    mTvStatePrincipalGroupPhoto.setText("已上传");
                } else {
                    mTvHintPrincipalGroupPhoto.setText("请上传负责人和油站合照");
                    mTvStatePrincipalGroupPhoto.setText("");
                }

                if (!TextUtils.isEmpty(legalPersonFrontUrl)) {
                    mTvHintLegalPersonFront.setText("");
                    mTvStateLegalPersonFront.setText("已上传");
                } else {
                    mTvHintLegalPersonFront.setText("法人身份证正面照片");
                    mTvStateLegalPersonFront.setText("");
                }

                if (!TextUtils.isEmpty(legalPersonBackUrl)) {
                    mTvHintLegalPersonBack.setText("");
                    mTvStateLegalPersonBack.setText("已上传");
                } else {
                    mTvHintLegalPersonBack.setText("法人身份证反面照片");
                    mTvStateLegalPersonBack.setText("");
                }

                if (!TextUtils.isEmpty(legalPersonShouchiUrl)) {
                    mTvHintLegalPersonShouchi.setText("");
                    mTvStateLegalPersonShouchi.setText("已上传");
                } else {
                    mTvHintLegalPersonShouchi.setText("法人手持身份证照片");
                    mTvStateLegalPersonShouchi.setText("");
                }

                if (!TextUtils.isEmpty(businessLicenseUrl)) {
                    mTvHintBusinessLicense.setText("");
                    mTvStateBusinessLicense.setText("已上传");
                } else {
                    mTvHintBusinessLicense.setText("请上传营业执照照片");
                    mTvStateBusinessLicense.setText("");
                }

                if (!TextUtils.isEmpty(otherLicence)) {

                    if (otherLicence.contains("|")) {
                        String[] s = otherLicence.split("\\|");
//                        for (int i = 0; i < s.length; i++) {
//                            Log.e("s--", s[i]);
//                        }
                        mTvStateOtherInformation.setText("已上传" + s.length + "张");
                    } else {
                        mTvStateOtherInformation.setText("已上传1张");
                    }

                    mTvHintOtherInformation.setText("");

                } else {
                    mTvHintOtherInformation.setText("请上传附加材料（非必填）");
                    mTvStateOtherInformation.setText("");
                }

                break;

            case RequestAction.TAG_NEWQUALIFICATIONINFO:

                MToast.showToast(this, "资质认证信息提交成功，请等待后台审核通过后可提现！");

                finish();
                break;
            default:
                break;
        }
    }

    //页面数据验证

    private boolean verifyData() {

        principalName = mEtPrincipalName.getText().toString().trim();
        principalIDNum = mEtPrincipalIdNum.getText().toString().trim();

        if (TextUtils.isEmpty(principalName)) {
            MToast.showToast(mContext, "请输入负责人姓名");
            return false;
        } else if (principalName.length() > 8 || principalName.length() < 2) {
            MToast.showToast(mContext, "您输入的姓名有误，请重新输入");
            return false;
        } else if (TextUtils.isEmpty(principalIDNum)) {
            MToast.showToast(mContext, "请输入负责人身份证号");
            return false;
        } else if (principalIDNum.length() != 18) {
//            MToast.showToast(mContext, "身份证号有误，请您确认后重新输入");
            MToast.showToast(mContext, "负责人身份证号码不合法");
            return false;
        } else if (TextUtils.isEmpty(principalFrontUrl)) {
            MToast.showToast(mContext, "请上传负责人身份证正面图片");
            return false;
        } else if (TextUtils.isEmpty(principalBackUrl)) {
            MToast.showToast(mContext, "请上传负责人身份证反面图片");
            return false;
        } else if (TextUtils.isEmpty(principalShouchiUrl)) {
            MToast.showToast(mContext, "请上传负责人手持身份证图片");
            return false;
        } else if (TextUtils.isEmpty(principalGroupPhotoUrl)) {
            MToast.showToast(mContext, "请上传负责人与油站合照");
            return false;
        }else if (TextUtils.isEmpty(legalPersonFrontUrl)) {
            MToast.showToast(mContext, "请上传法人身份证正面图片");
            return false;
        } else if (TextUtils.isEmpty(legalPersonBackUrl)) {
            MToast.showToast(mContext, "请上传法人身份证反面图片");
            return false;
        } else if (TextUtils.isEmpty(legalPersonShouchiUrl)) {
            MToast.showToast(mContext, "请上传法人手持身份证图片");
            return false;
        } else if (TextUtils.isEmpty(businessLicenseUrl)) {
            MToast.showToast(mContext, "请上传营业执照图片");
            return false;
        } else {
            return true;
        }
    }
}
