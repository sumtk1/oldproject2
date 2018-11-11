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
 * 今日收益列表（RecyclerView）适配器
 * Created by Dlt on 2017/6/2 15:32
 */
public class TodayEarning1Adapter extends SwipeMenuAdapter<TodayEarning1Adapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_oddNum, tv_time, tv_type, tv_oilsPrice, tv_jiaoyi, tv_daozhang;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_oddNum = (TextView) itemView.findViewById(R.id.tv_oddNum);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_oilsPrice = (TextView) itemView.findViewById(R.id.tv_oilsPrice);
            tv_jiaoyi = (TextView) itemView.findViewById(R.id.tv_jiaoyi);
            tv_daozhang = (TextView) itemView.findViewById(R.id.tv_daozhang);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(String oddNum, String time, String type, String oilsPrice, String jiaoyi, String daozhang) {
            this.tv_oddNum.setText("交易单号：" + oddNum);
            this.tv_time.setText(time);
            this.tv_type.setText(type);
            this.tv_oilsPrice.setText(oilsPrice + "/L");
            this.tv_jiaoyi.setText(jiaoyi);
            this.tv_daozhang.setText(daozhang);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    private List<String[]> list;
    private OnItemClickListener mOnItemClickListener;

    public TodayEarning1Adapter(List<String[]> list) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade_todayearning_withrevoke1, parent, false);
    }

    @Override
    public TodayEarning1Adapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(TodayEarning1Adapter.ViewHolder holder, int position) {
        holder.setData(list.get(position)[0], list.get(position)[1], list.get(position)[4],
                list.get(position)[5], list.get(position)[2], list.get(position)[3]);
        holder.setOnItemClickListener(mOnItemClickListener);
    }

}
