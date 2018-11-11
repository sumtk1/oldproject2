package com.gloiot.hygooilstation.server.picture;

import android.content.Context;
import android.graphics.Color;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by JinzLin on 2016/7/28.
 */
public abstract class ChoosePhotoUtlis {

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int THEME_DEFAULT = 0;
    private final int THEME_DARK = 1;
    private final int THEME_CYAN = 2;
    private final int THEME_ORANGE = 3;
    private final int THEME_GREEN = 4;
    private final int THEME_TEAL = 5;
    private final int THEME_CUSTOM = 6;

    private Context mContext;
    private ThemeConfig themeConfig = ThemeConfig.DEFAULT;
    private FunctionConfig.Builder functionConfigBuilder;
    private FunctionConfig functionConfig;
    private PauseOnScrollListener pauseOnScrollListener = null;
    private CoreConfig coreConfig;

    public ChoosePhotoUtlis(Context context) {
        mContext = context;
        functionConfigBuilder = new FunctionConfig.Builder();
    }

    public ChoosePhotoUtlis(Context context, int theme) {
        setTheme(theme);
        mContext = context;
        functionConfigBuilder = new FunctionConfig.Builder();
    }

    /**
     * 设置主题
     */
    public void setTheme(int theme) {
        switch (theme) {
            case THEME_DEFAULT:
                themeConfig = ThemeConfig.DEFAULT;
                break;
            case THEME_DARK:
                themeConfig = ThemeConfig.DARK;
                break;
            case THEME_CYAN:
                themeConfig = ThemeConfig.CYAN;
                break;
            case THEME_ORANGE:
                themeConfig = ThemeConfig.ORANGE;
                break;
            case THEME_GREEN:
                themeConfig = ThemeConfig.GREEN;
                break;
            case THEME_TEAL:
                themeConfig = ThemeConfig.TEAL;
                break;
            case THEME_CUSTOM:
                themeConfig = new ThemeConfig.Builder()
                        .setTitleBarBgColor(Color.rgb(0x05, 0x9E, 0xDA))
                        .setTitleBarTextColor(Color.WHITE)
                        .setTitleBarIconColor(Color.WHITE)
                        .setFabNornalColor(Color.RED)
                        .setFabPressedColor(Color.BLUE)
                        .setCheckNornalColor(Color.WHITE)
                        .setCheckSelectedColor(Color.BLUE)
//                        .setIconBack(R.mipmap.ic_action_previous_item)
//                        .setIconRotate(R.mipmap.ic_action_repeat)
//                        .setIconCrop(R.mipmap.ic_action_crop)
//                        .setIconCamera(R.mipmap.ic_action_camera)
                        .build();
                break;
        }
    }


    /**
     * 构建functionConfigBuilder
     */
    public void build() {
        functionConfig = functionConfigBuilder.build();
        coreConfig = new CoreConfig.Builder(mContext, new com.gloiot.hygooilstation.server.picture.PicassoImageLoader(), themeConfig)//用PicassoImageLoader有真机会出现部分图片加载不出的情况
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);
    }
//    public void build() {
//        functionConfig = functionConfigBuilder.build();
//        coreConfig = new CoreConfig.Builder(mContext, new GlideImageLoader(), themeConfig)//换用GlideImageLoader测试是否会出问题
//                .setFunctionConfig(functionConfig)
//                .setPauseOnScrollListener(pauseOnScrollListener)
//                .setNoAnimcation(false)
//                .build();
//        GalleryFinal.init(coreConfig);
//    }

    /**
     * 图片可编辑
     */
    public ChoosePhotoUtlis setEnableEdit() {
        functionConfigBuilder.setEnableEdit(true);
        return this;
    }

    /**
     * 图片可剪裁——只针对图片可编辑生效
     */
    public ChoosePhotoUtlis setEnableCrop() {
        functionConfigBuilder.setEnableCrop(true);
        return this;
    }

    /**
     * 图片可旋转——只针对图片可编辑生效
     */
    public ChoosePhotoUtlis setEnableRotate() {
        functionConfigBuilder.setEnableRotate(true);
        return this;
    }

    /**
     * 强制剪裁——只针对单选且图片可剪裁生效
     */
    public ChoosePhotoUtlis setForceCrop() {
        functionConfigBuilder.setForceCrop(true);
        return this;
    }

    /**
     * 强制剪裁后可编辑——只针对单选且图片可剪裁生效
     */
    public ChoosePhotoUtlis setForceCropEdit() {
        functionConfigBuilder.setForceCropEdit(true);
        return this;
    }

    /**
     * 剪裁框正方形——只针对图片可剪裁生效
     */
    public ChoosePhotoUtlis setCropSquare() {
        functionConfigBuilder.setCropSquare(true);
        return this;
    }

    /**
     * 剪裁覆盖原图——只针对图片可剪裁生效
     */
    public ChoosePhotoUtlis setCropReplaceSource() {
        functionConfigBuilder.setCropReplaceSource(true);
        return this;
    }

    /**
     * 旋转覆盖原图——只针对图片可旋转生效
     */
    public ChoosePhotoUtlis setRotateReplaceSource() {
        functionConfigBuilder.setRotateReplaceSource(true);
        return this;
    }

    /**
     * 启动预览
     */
    public ChoosePhotoUtlis setEnablePreview() {
        functionConfigBuilder.setEnablePreview(true);
        return this;
    }

    /**
     * 显示相机
     */
    public ChoosePhotoUtlis setEnableCamera() {
        functionConfigBuilder.setEnableCamera(true);
        return this;
    }

    /**
     * 多选最大数量
     *
     * @param maxSize 最大数量
     */
    public ChoosePhotoUtlis setMutiSelectMax(int maxSize) {
        functionConfigBuilder.setMutiSelectMaxSize(maxSize <= 0 ? 1 : maxSize);
        return this;
    }

    /**
     * 多选——先设置多选最大数量
     */
    public void setMutiSelect() {
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
    }

    /**
     * 单选
     */
    public void setSingleSelect() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
    }

    /**
     * 拍照
     */
    public void setOpenCamera() {
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(final int reqeustCode, final List<PhotoInfo> resultList) {
            if (resultList != null) {
                onSuccess(reqeustCode, resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            onFailure(requestCode, errorMsg);
        }

    };

    public abstract void onSuccess(int reqeustCode, List<PhotoInfo> resultList);

    public abstract void onFailure(int requestCode, String errorMsg);
}
