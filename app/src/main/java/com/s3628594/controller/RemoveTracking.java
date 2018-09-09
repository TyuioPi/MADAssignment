package com.s3628594.controller;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingImplementation;

public class RemoveTracking implements View.OnClickListener {

    private Activity activity;
    private String trackingId;

    public RemoveTracking(Activity activity, String trackingId) {
        this.activity = activity;
        this.trackingId = trackingId;
    }

    @Override
    public void onClick(View view) {
        // Remove tracking by tracking id
        Tracking tracking = TrackingImplementation.getSingletonInstance().getTrackingById(trackingId);
        TrackingImplementation.getSingletonInstance().removeTracking(tracking);

        // Display message
        Toast.makeText(activity, "Successfully Removed Tracking", Toast.LENGTH_SHORT).show();

        activity.finish();
    }
}
