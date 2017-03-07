package com.tripfortrip.tripfortrip._00_Main;

/**
 * Created by Qoo on 2017/3/2.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripfortrip.tripfortrip.R;

public class BagFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.bag_fragment, container, false);
        return view;
    }
}
