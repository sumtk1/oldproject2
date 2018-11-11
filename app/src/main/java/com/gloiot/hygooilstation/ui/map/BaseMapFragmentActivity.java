package com.gloiot.hygooilstation.ui.map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gloiot.hygooilstation.R;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;

public class BaseMapFragmentActivity extends FragmentActivity {
    private AMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map_fragment);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
    }
}
