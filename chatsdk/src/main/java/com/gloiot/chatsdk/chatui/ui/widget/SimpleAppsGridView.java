package com.gloiot.chatsdk.chatui.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendAdapter;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;

import java.util.ArrayList;


/**
 * 加号弹出的内容
 */
public class SimpleAppsGridView extends RelativeLayout {

    protected View view;

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        ArrayList<ExtendBean> mExtendBeanList = new ArrayList<>();
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_image_bg, "图片"));
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_camera_bg, "拍照"));
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_transferpoints_bg, "转让积分"));
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_redpacket_bg, "红包"));
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_location_bg, "位置"));
        ExtendAdapter adapter = new ExtendAdapter(getContext(), mExtendBeanList);
        gv_apps.setAdapter(adapter);
    }
}
