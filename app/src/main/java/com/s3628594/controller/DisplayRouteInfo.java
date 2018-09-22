package com.s3628594.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingService;
import com.s3628594.view.MapsActivity;

public class DisplayRouteInfo implements AdapterView.OnItemClickListener {

    private Activity context;

    public DisplayRouteInfo(Activity context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final String LOG_TAG = DisplayRouteInfo.class.getName();
        Tracking tracking = (Tracking) adapterView.getAdapter().getItem(i);

        for (TrackingService.TrackingInfo routeInfo : tracking.getRouteInfo()) {
            Log.i(LOG_TAG, routeInfo.toString());
        }

        Intent intent = new Intent(context, MapsActivity.class);
        context.startActivity(intent);
    }
}
