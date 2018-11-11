package com.gloiot.hygooilstation.server.picture;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;

/**
 * Created by Dlt on 2017/5/16 16:51
 */
public class ChoosePhotoNew {

    private Context mContext;
    private List<String> photos = null;

    private ProgressDialog progressDialog;

    public ChoosePhotoNew(Context context, List<String> photos) {
        this.photos = photos;
        this.mContext = context;
    }




}
