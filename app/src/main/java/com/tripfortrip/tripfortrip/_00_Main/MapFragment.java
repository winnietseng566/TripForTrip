package com.tripfortrip.tripfortrip._00_Main;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripfortrip.tripfortrip.R;

public class MapFragment extends Fragment implements LocationListener{


    static final int MIN_TIME = 5000;  // 位置更新條件：5000毫秒
    static final float MIN_DIST = 0;   // 位置更新條件：5公尺
    LocationManager mgr;               // 定位管理員

    boolean isGPSEnabled;        // GPS定位是否可用
    boolean isNetworkEnabled;    // 網路定位是否可用


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        return view;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
