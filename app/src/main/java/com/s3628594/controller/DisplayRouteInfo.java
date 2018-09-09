package com.s3628594.controller;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingService;

public class DisplayRouteInfo implements AdapterView.OnItemClickListener {

    public DisplayRouteInfo() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final String LOG_TAG = DisplayRouteInfo.class.getName();
        Tracking tracking = (Tracking) adapterView.getAdapter().getItem(i);

        for (TrackingService.TrackingInfo routeInfo : tracking.getRouteInfo()) {
            Log.i(LOG_TAG, routeInfo.toString());
        }
    }
}
