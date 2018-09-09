package com.s3628594.controller;

import android.content.DialogInterface;

import com.s3628594.view.TrackableTab;

public class RemoveFilter implements DialogInterface.OnClickListener {

    private TrackableTab trackableTab;

    public RemoveFilter(TrackableTab trackableTab) {
        this.trackableTab = trackableTab;
    }

    // Switch to our original adapter with full list of food trucks
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        trackableTab.setMyAdapter(trackableTab.getAdapter());
    }
}
