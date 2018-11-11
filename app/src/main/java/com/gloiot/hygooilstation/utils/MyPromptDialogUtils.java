package com.gloiot.hygooilstation.utils;

import android.content.Context;
import android.view.View;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;

/**
 * Created by dlt on 2016/11/28.
 */

public class MyPromptDialogUtils {

    public static MyDialogBuilder myDialogBuilder;

    //弹出对话框根据输入内容给出提示信息
    public static void showPrompt(Context context,String prompt) {
        myDialogBuilder = MyDialogBuilder.getInstance(context);
        myDialogBuilder
                .withIcon(R.mipmap.iconfont_gantanhao)
                .withContene(prompt)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismissNoAnimator();
                    }
                }).show();
    }

}
