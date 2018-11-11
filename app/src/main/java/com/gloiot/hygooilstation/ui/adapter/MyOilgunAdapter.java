package com.gloiot.hygooilstation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.swipe.MySwipe;
import com.gloiot.hygooilstation.ui.widget.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dlt on 2016/11/18.
 */

public class MyOilgunAdapter extends BaseAdapter {

    private Context mContext;
    private List<String[]> list = new ArrayList<>();
    private String oilsType;
    private MyDialogBuilder myDialogBuilder;
    private int deletePosition = -1;

    public MyOilgunAdapter(Context context, List<String[]> data, String oilsType) {
        this.mContext = context;
        this.list = data;
        this.oilsType = oilsType;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
//        return list.get(position);
        return null != list ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView tv_oilstype, tv_oilgunNum;
        private Button btn_oilgun_delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        ViewHolder viewHolder = null;
//
//        if (null == convertView) {
//
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.item_eachoils_gun, null);
//
//            viewHolder = new ViewHolder();
//            viewHolder.tv_oilstype = (TextView) convertView.findViewById(R.id.tv_oilstype);
//            viewHolder.tv_oilgunNum = (TextView) convertView.findViewById(R.id.tv_oilgunNum);
//            viewHolder.btn_oilgun_delete = (Button) convertView.findViewById(R.id.btn_oilgun_delete);
//
//            convertView.setTag(viewHolder);
//
//        } else {
//
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//
//        String[] strings = (String[]) getItem(position);
//        viewHolder.tv_oilstype.setText(oilsType);
//        viewHolder.tv_oilgunNum.setText(strings[1] + "号");
//
//
//
//        if (oilsType.equals("0#")) {
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_f29305);
////            viewHolder.tv_oilgunNum.setText(strings[1] + "号");
//
//        } else if (oilsType.equals("92#")) {
//
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_6ed12b);
//        } else if (oilsType.equals("93#")) {
//
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_20dc91);
//        } else if (oilsType.equals("95#")) {
//
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_d824cf);
//        } else if (oilsType.equals("97#")) {
//
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
//        } else if (oilsType.equals("98#")) {
//
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_992fdf);
//        } else {
//
//            viewHolder.tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
//        }
//
//        final int p = position;//选中的位置
//        Button btn_delete = viewHolder.btn_oilgun_delete;
//        SwipeLayout s = (SwipeLayout) convertView;
//        s.close(false, false);
//        s.getFrontView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        s.setSwipeListener(MySwipe.mSwipeListener);
//        btn_delete.setTag(p);
//        btn_delete.setOnClickListener(onActionClick);
//
//
//
//        return convertView;


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.item_eachoils_gun, null);

        TextView tv_oilstype = (TextView) rowview.findViewById(R.id.tv_oilstype);
        TextView tv_oilgunNum = (TextView) rowview.findViewById(R.id.tv_oilgunNum);
        Button btn_delete = (Button) rowview.findViewById(R.id.btn_oilgun_delete);

        String[] strings = (String[]) getItem(position);
        tv_oilstype.setText(oilsType);
        tv_oilgunNum.setText(strings[1] + "号");


        if (oilsType.equals("0#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_f29305);

        } else if (oilsType.equals("92#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_6ed12b);
        } else if (oilsType.equals("93#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_20dc91);
        } else if (oilsType.equals("95#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_d824cf);
        } else if (oilsType.equals("97#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
        } else if (oilsType.equals("98#")) {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_992fdf);
        } else {

            tv_oilstype.setBackgroundResource(R.drawable.shape_circle_30_176bea);
        }

        final int p = position;//选中的位置
        SwipeLayout s = (SwipeLayout) convertView;
        s.close(false, false);
        s.getFrontView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        s.setSwipeListener(MySwipe.mSwipeListener);
        btn_delete.setTag(p);
        btn_delete.setOnClickListener(onActionClick);


        return rowview;

    }

    View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int p = (int) v.getTag();
            int id = v.getId();
            if (id == R.id.btn_oilgun_delete) {   //删除油枪
                MySwipe.closeAllLayout();
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                        .withContene("确定要删除这个油枪吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                deletePosition = p;

//                                onConfirmEvent(deletePosition);
//                                requestHandleArrayList.add(requestAction.deleteOilsguns(MyEachoilsGunActivity.this, list.get(p)[0]));

                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        })
                        .show();

            }
        }
    };


}
