package com.s3628594.controller;

import android.content.Intent;
import android.view.View;

import com.s3628594.view.TrackingFinder;
import com.s3628594.view.TrackingTab;

// Create a new intent for functionality of searching and adding a new tracking
public class NewTracking implements View.OnClickListener {

    private TrackingTab trackingTab;

    public NewTracking(TrackingTab trackingTab) {
        this.trackingTab = trackingTab;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(trackingTab.getActivity(), TrackingFinder.class);
        trackingTab.getActivity().startActivity(intent);
    }
}
