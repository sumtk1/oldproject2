package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.widget.ListView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.zhy.base.adapter.abslistview.CommonAdapter;

/**
 * 油站区域
 */
public class OilstationAreaActivity extends BaseActivity {

    private ListView lv_listview;
    private CommonAdapter mOilstationAreaAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_listview_mgtop20;
    }

    @Override
    public void initComponent() {

        lv_listview = (ListView) findViewById(R.id.lv_listview);

    }

    @Override
    public void initData() {



    }
}
