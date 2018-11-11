package com.gloiot.hygooilstation.ui.widget.swipe;

import java.util.HashSet;

/**
 * Created by JinzLin on 2016/6/13.
 * listview 侧滑
 */
public class MySwipe {

    static HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    public static SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener(){

        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
            mUnClosedLayouts.add(swipeLayout);
        }
    };

    public static void closeAllLayout() {
        if(mUnClosedLayouts.size() == 0)
            return;

        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }

    public static int getUnClosedCount(){
        return mUnClosedLayouts.size();
    }
}
