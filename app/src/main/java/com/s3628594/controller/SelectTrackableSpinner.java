package com.s3628594.controller;

import android.view.View;
import android.widget.AdapterView;

import com.s3628594.view.TrackingFinder;

public class SelectTrackableSpinner implements AdapterView.OnItemSelectedListener {

    private TrackingFinder trackingFinder;

    public SelectTrackableSpinner(TrackingFinder trackingFinder) {
        this.trackingFinder = trackingFinder;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        trackingFinder.setSelectedTrackable(adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
