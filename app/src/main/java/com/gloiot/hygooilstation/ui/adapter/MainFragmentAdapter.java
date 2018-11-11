package com.gloiot.hygooilstation.ui.adapter;

import android.support.v4.app.Fragment;

import com.gloiot.hygooilstation.ui.fragment.MessageFragment;
import com.gloiot.hygooilstation.ui.fragment.MyFragment;
import com.gloiot.hygooilstation.ui.fragment.OilstationFragment;
import com.gloiot.hygooilstation.ui.fragment.TradeFragment;
import com.gloiot.hygooilstation.ui.widget.fragmentnavigator.FragmentNavigatorAdapter;


/**
 * Created by Dlt on 2017/3/18 12:53
 */
public class MainFragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"OilStation", "Message", "Trade", "Mine"};

    @Override
    public Fragment onCreateFragment(int position) {

        switch (position) {
            case 0:
                return OilstationFragment.newInstance(position);
            case 1:
                return MessageFragment.newInstance(position);
            case 2:
                return TradeFragment.newInstance(position);
            case 3:
                return MyFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public String getTag(int position) {

        switch (position) {
            case 0:
                return OilstationFragment.TAG;
            case 1:
                return MessageFragment.TAG;
            case 2:
                return TradeFragment.TAG;
            case 3:
                return MyFragment.TAG;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
