package com.gloiot.hygooilstation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.widget.swipe.OnItemClickListener;

import java.util.List;

/**
 * Created by Dlt on 2017/2/7 15:25
 */
public class TestSwipeAdapter extends SwipeMenuAdapter<TestSwipeAdapter.DefaultViewHolder> {

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(String title) {
            this.tvTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    private List<String> titles;

    private OnItemClickListener mOnItemClickListener;

    public TestSwipeAdapter(List<String> titles) {
        this.titles = titles;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe, parent, false);
    }

    @Override
    public TestSwipeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(TestSwipeAdapter.DefaultViewHolder holder, int position) {
        holder.setData(titles.get(position));
        holder.setOnItemClickListener(mOnItemClickListener);
    }
}
