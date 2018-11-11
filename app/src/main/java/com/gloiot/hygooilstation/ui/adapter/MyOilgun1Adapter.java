package com.gloiot.hygooilstation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.widget.swipe.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Dlt on 2017/2/7 17:25
 */
public class MyOilgun1Adapter extends SwipeMenuAdapter<MyOilgun1Adapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_oilstype;
        TextView tv_oilgunNum;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_oilstype = (TextView) itemView.findViewById(R.id.tv_oilstype);
            tv_oilgunNum = (TextView) itemView.findViewById(R.id.tv_oilgunNum);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(String oilsType, String oilgunNum) {
            this.tv_oilstype.setText(oilsType);
            this.tv_oilgunNum.setText(oilgunNum + "号");
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
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    private String oilsType;
    private List<String[]> list;
    private OnItemClickListener mOnItemClickListener;

    public MyOilgun1Adapter(String oilsType, List<String[]> list) {
        this.oilsType = oilsType;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eachoils_gun1, parent, false);
    }

    @Override
    public MyOilgun1Adapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MyOilgun1Adapter.ViewHolder holder, int position) {
        holder.setData(oilsType, list.get(position)[1]);
        holder.setOnItemClickListener(mOnItemClickListener);
    }

}
