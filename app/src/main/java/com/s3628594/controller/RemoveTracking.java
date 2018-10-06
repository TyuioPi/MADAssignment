package com.s3628594.controller;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.s3628594.database.foodTruckDB;
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
        final Tracking tracking = TrackingImplementation.getSingletonInstance().getTrackingById(trackingId);
        TrackingImplementation.getSingletonInstance().removeTracking(tracking);
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodTruckDB.getSingletonInstance().deleteTracking(tracking);
            }
        }).start();

        // Display message
        Toast.makeText(activity, "Successfully Removed Tracking", Toast.LENGTH_SHORT).show();

        activity.finish();
    }
}
