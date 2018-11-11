package com.gloiot.hygooilstation.server.picture;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.gloiot.hygooilstation.ui.widget.ActionSheet;
import com.gloiot.hygooilstation.utils.BitmapUtlis;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.amap.api.mapcore2d.p.i;


/**
 * Created by JinzLin on 2016/7/28.
 */
public abstract class ChoosePhoto {

    private Context mContext;
    private com.gloiot.hygooilstation.server.picture.ChoosePhotoUtlis choosePhotoUtlis;
    private int mutiMax;
    private boolean muti;
    private ProgressDialog progressDialog;

    public ChoosePhoto(Context mContext) {
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
    }


    public ChoosePhoto setMuti(int mutiMax) {
        this.mutiMax = mutiMax;
//        showActionSheet();
        return this;
    }

    /**
     * 选择上传头像/或单张图片
     * 上传头像时，要求强制裁剪成正方形，单张普通图片无此要求
     *
     * @return
     */
    public ChoosePhoto setPortrait(boolean isPortrait) {
        muti = false;
        choosePhotoUtlis = new com.gloiot.hygooilstation.server.picture.ChoosePhotoUtlis(mContext, 6) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                final Bitmap image = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());

                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle("图片上传中，请稍候...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AliOss(mContext, AliOss.setPicName("hygoilstationPics"), BitmapUtlis.compressImage(image)) {//这里设置上传的地址
                            @Override
                            protected void uploadProgress(long currentSize, long totalSize) {
                                progressDialog.setProgress((int) currentSize);
                                progressDialog.setMax((int) totalSize);
                                if (currentSize == totalSize) {
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void uploadSuccess(String myPicUrl) {
                                setPortraitonSuccess(myPicUrl);
                            }

                            @Override
                            protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                setPortraitFailure();
                            }
                        }.start();
                    }
                }).start();
            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        if (isPortrait) {//上传头像时，强制裁剪成正方形
            choosePhotoUtlis.setEnableCrop().setForceCrop();
        } else {
            choosePhotoUtlis.setEnableCrop().setForceCropEdit();//强制剪裁后可编辑——只针对单选且图片可剪裁生效
        }
        showActionSheet();
        return this;
    }

    /**
     * 选择上传多张图片
     *
     * @return
     */
    int a = 0;//记录上传成功的图片数量

    public ChoosePhoto setPics() {
        muti = true;
        choosePhotoUtlis = new com.gloiot.hygooilstation.server.picture.ChoosePhotoUtlis(mContext, 6) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {

                final List<Bitmap> images = new ArrayList<>();
                for (int i = 0; i < resultList.size(); i++) {
                    Bitmap image = BitmapFactory.decodeFile(resultList.get(i).getPhotoPath());
                    images.add(image);
                }
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle("图片上传中，请稍候...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                final List<String> picsUrl = new ArrayList<>();

                for (final Bitmap image : images) {
                    Log.e("-----images", images.size() + "-");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new AliOss(mContext, AliOss.setPicName("hygoilstationPics"), BitmapUtlis.compressImage(image)) {//这里设置上传的地址
                                @Override
                                protected void uploadProgress(long currentSize, long totalSize) {
                                    progressDialog.setProgress((int) currentSize);
                                    progressDialog.setMax((int) totalSize);
                                    if (currentSize == totalSize) {
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void uploadSuccess(String myPicUrl) {
                                    picsUrl.add(myPicUrl);
                                    a++;
                                    Log.e("-----images", myPicUrl + "-");
                                    if (a == images.size()) {
//                                        setPortraitonSuccess(myPicUrl, true);

                                        setPicsSuccess(picsUrl, true);
                                    } else {
//                                        setPortraitonSuccess(myPicUrl, false);
                                        setPicsSuccess(picsUrl, false);

                                    }
                                }

                                @Override
                                protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                    setPortraitFailure();
                                    a++;
                                }
                            }.start();

                        }

                    }).start();
                }
//
//                for (int i = 0; i < picsUrl.size(); i++) {
//                    Log.e("aaaaaaaaaaaaaaa.", picsUrl.get(i) + "bbbbbb");
//                }

//                setPicsSuccess(picsUrl);


            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
//        choosePhotoUtlis.setEnableCrop().setForceCrop();//裁剪操作
        showActionSheet();
        return this;
    }

    /**
     * 多张上传--修改版(图片压缩时间过久可能会造成ANR)
     */
    int num = 0;//记录上传成功的图片数量
    List<byte[]> picsStream = new ArrayList<>();

    public ChoosePhoto setPics1() {
        muti = true;
//        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        choosePhotoUtlis = new com.gloiot.hygooilstation.server.picture.ChoosePhotoUtlis(mContext, 6) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                for (int i = 0; i < resultList.size(); i++) {
                    Bitmap image = BitmapFactory.decodeFile(resultList.get(i).getPhotoPath());
                    picsStream.add(BitmapUtlis.compressImage(image));
                }
                Thread thread = Thread.currentThread();
                Log.e("threadInfo", "当前线程信息(show)name：" + thread.getName() + "---ID:" + thread.getId() + "---priority" + thread.getPriority());
                progressDialog.setTitle("图片上传中,请稍候");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final List<String> picsUrl = new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AliOss(mContext, AliOss.setPicName("hygoilstationPics"), picsStream.get(num)) {//这里设置上传的地址
                            @Override
                            protected void uploadProgress(long currentSize, long totalSize) {
                                //Thread thread = Thread.currentThread();
                                //Log.d("threadInfo", "当前线程信息(progress)name：" + thread.getName() + "---ID:" + thread.getId() + "---priority" + thread.getPriority());
                                progressDialog.setProgress((int) currentSize);
                                progressDialog.setMax((int) totalSize);
                            }

                            @Override
                            public void uploadSuccess(String myPicUrl) {
                                picsUrl.add(myPicUrl);
                                num++;
                                if (i < picsStream.size()) {
                                    //  Thread thread = Thread.currentThread();
                                    //  Log.d("threadInfo", "当前线程信息(if)name：" + thread.getName() + "---ID:" + thread.getId() + "---priority" + thread.getPriority());
                                    this.setPicurName(AliOss.setPicName("hygoilstationPics"));
                                    this.setPicurlpath(picsStream.get(num));
                                    this.start();
                                } else {
                                    //Thread thread = Thread.currentThread();
                                    //Log.d("threadInfo", "当前线程信息(else)name：" + thread.getName() + "---ID:" + thread.getId() + "---priority" + thread.getPriority());
                                    progressDialog.dismiss();
                                    setPicsSuccess(picsUrl, true);
                                }
                            }

                            @Override
                            protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                setPortraitFailure();
                            }
                        }.start();
                    }
                }).start();
            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        showActionSheet();
        return this;
    }

    /**
     * 用加载圆代替进度条，将压缩放入子线程处理
     */
    Integer b = 0;

    public ChoosePhoto setPics2() {
        muti = true;
        choosePhotoUtlis = new com.gloiot.hygooilstation.server.picture.ChoosePhotoUtlis(mContext, 6) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
//                long time = System.currentTimeMillis();
                progressDialog.setMessage("图片上传中,请稍候");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final List<String> picsUrl = new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //long time = System.currentTimeMillis();
                        for (int i = 0; i < resultList.size(); i++) {
                            Bitmap image = BitmapFactory.decodeFile(resultList.get(i).getPhotoPath());
                            picsStream.add(BitmapUtlis.compressImage(image));
                        }
                        //Log.e("123", "压缩耗时---" + (System.currentTimeMillis() - time));
                        for (int i = 0; i < picsStream.size(); i++) {
                            new AliOss(mContext, AliOss.setPicName("hygoilstationPics"), picsStream.get(i)) {//这里设置上传的地址
                                @Override
                                protected void uploadProgress(final long currentSize, final long totalSize) {
                                }

                                @Override
                                public void uploadSuccess(String myPicUrl) {
                                    picsUrl.add(myPicUrl);
                                    synchronized (b) {
                                        b++;
                                        if (b == picsStream.size()) {
                                            setPicsSuccess(picsUrl, true);
                                            progressDialog.dismiss();
                                        }
                                    }
                                }

                                @Override
                                protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                    setPortraitFailure();
                                }
                            }.start();
                        }
//
                    }
                }).start();
            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        showActionSheet();
        return this;
    }

    /**
     * 选择上传头像成功
     *
     * @param myPicUrl
     */
    protected abstract void setPortraitonSuccess(String myPicUrl);

    /**
     * 选择上传头像成功
     *
     * @param myPicUrl
     */
    protected abstract void setPortraitonSuccess(String myPicUrl, boolean a);

    /**
     * 选择上传头像失败
     */
    protected abstract void setPortraitFailure();

    /**
     * 选择上传多张图片成功
     *
     * @param picsUrl
     */
    protected abstract void setPicsSuccess(List<String> picsUrl, boolean over);


    /**
     * 弹出选择框
     */
    private void showActionSheet() {
//        choosePhotoUtlis.setEnableEdit().setEnableCrop().setCropSquare();//不能强行设置裁剪框正方形，否则身份证照片会出问题
        choosePhotoUtlis.setEnableEdit().setEnableCrop();

        ActionSheet.createBuilder(mContext, ((FragmentActivity) mContext).getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0://相册
                                if (muti) {//多选
                                    choosePhotoUtlis.setMutiSelectMax(mutiMax).build();
                                    choosePhotoUtlis.setMutiSelect();
                                } else {
                                    choosePhotoUtlis.build();
                                    choosePhotoUtlis.setSingleSelect();
                                }
                                break;
                            case 1://拍照
                                choosePhotoUtlis.build();
                                choosePhotoUtlis.setOpenCamera();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

}
