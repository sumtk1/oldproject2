package com.gloiot.hygooilstation.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gloiot.hygooilstation.R;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;

import java.util.List;

/**
 * Created by linruihui on 2016/10/31.
 */
public class PopUpFragment extends Fragment {

    //是否已经关闭
    boolean isDismiss = true;

    View decorView;
    //添加进入的view
    View realView;
    //添加进入的第一个view
    View pop_layout;

    //弹窗口listview的list数据
    private List<String[]> mList;
    private ListView mListView;


    public static PopUpFragment newItemInstance() {
        PopUpFragment fragment = new PopUpFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            //判断键盘是否弹出，隐藏软键盘
            //有时会空指针异常?
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager.isActive()) {
                View focusView = getActivity().getCurrentFocus();
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        realView = inflater.inflate(R.layout.title_fragment_popup, container, false);
        initViews(realView);
        decorView = getActivity().getWindow().getDecorView();
        ((ViewGroup) decorView).addView(realView);
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(dp2px(getActivity(), 125), LinearLayout.LayoutParams.WRAP_CONTENT);

        /**
         * statusBarHeight 状态栏高度
         * 46 = 50 - 4  标题栏高度-三角形高度
         * 86 = 50+40-4+1
         */
        layoutParam.setMargins(0, statusBarHeight + dp2px(getActivity(), 87), dp2px(getActivity(), 6), 0);
        pop_layout.setLayoutParams(layoutParam);
        pop_layout.setVisibility(View.VISIBLE);
        realView.setBackgroundColor(Color.parseColor("#70000000"));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(final View view) {
        pop_layout = view.findViewById(R.id.pop_child_layout2);
        pop_layout.setVisibility(View.INVISIBLE);
        realView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        //设置mList
        mList = getList();
        if (mList != null) {

            mListView = (ListView) view.findViewById(R.id.title_f_list);
            CommonAdapter<String[]> commonAdapter = new CommonAdapter<String[]>(getActivity(), R.layout.item_pop, mList) {
                @Override
                public void convert(ViewHolder holder, final String[] strings) {
                    holder.setText(R.id.tv_xq, strings[0] + "年");
//                    holder.setImageResource(R.id.iv_xq, Integer.parseInt(strings[1]));
                }
            };
            mListView.setAdapter(commonAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addOnItemClick(parent, view, position, id);
                    ((PopUpFragment) (getActivity().getSupportFragmentManager().findFragmentByTag("PopUpFragment"))).dismiss();
                }

            });

        }


    }


    //设置listview点击事件回调

    ItemClickListenerCallBack mItemClickListenerCallBack;

    public interface ItemClickListenerCallBack {
        void addOnItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private void addOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        mItemClickListenerCallBack.addOnItemClick(parent, view, position, id);
    }

    public void setListviewItemClickListenerCallBack(ItemClickListenerCallBack mItemClickListenerCallBack) {
        this.mItemClickListenerCallBack = mItemClickListenerCallBack;
    }


    //设置list回调
    public interface GetListCallBack {
        List<String[]> getList();
    }

    private List<String[]> getList() {
        if (mGetListCallBack == null) {
            return null;
        } else {
            return mGetListCallBack.getList();
        }
    }

    public GetListCallBack mGetListCallBack;

    public void setGetListCallBack(GetListCallBack mGetListCallBack) {
        this.mGetListCallBack = mGetListCallBack;
    }


    private void show(final FragmentManager manager, final String tag) {
        if (manager.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss = false;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(PopUpFragment.this, tag);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //回退异常?
                //getChildFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(PopUpFragment.this);
                transaction.commitAllowingStateLoss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) decorView).removeView(realView);

    }

    public static Builder build(FragmentManager fragmentManager) {
        Builder builder = new Builder(fragmentManager);
        return builder;
    }

    public static class Builder {

        FragmentManager fragmentManager;

        //默认tag
        String tag = "PopUpFragment";


        ItemClickListenerCallBack mItemClickListenerCallBack;

        GetListCallBack mGetListCallBack;


        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }


        public Builder setListviewItemClickListener(ItemClickListenerCallBack mItemClickListenerCallBack) {
            this.mItemClickListenerCallBack = mItemClickListenerCallBack;
            return this;
        }


        public Builder setGetListCallBack(GetListCallBack mGetListCallBack) {
            this.mGetListCallBack = mGetListCallBack;
            return this;
        }


        public void show() {
            PopUpFragment fragment;
            fragment = PopUpFragment.newItemInstance();
            fragment.setGetListCallBack(mGetListCallBack);
            fragment.setListviewItemClickListenerCallBack(mItemClickListenerCallBack);
            fragment.show(fragmentManager, tag);


        }

    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
