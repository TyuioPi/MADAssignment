package com.s3628594.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.s3628594.model.Tracking;
import com.s3628594.view.EditTrackingActivity;

// Create a new intent for functionality of editing a tracking in the tracking list
public class CreateEditTrackingActivity implements AdapterView.OnItemLongClickListener {

    private Activity context;

    public CreateEditTrackingActivity(Activity context) {
        this.context = context;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, EditTrackingActivity.class);
        Tracking tracking = (Tracking) adapterView.getItemAtPosition(i);

        // Get values of clicked tracking
        String trackingId = tracking.getTrackingId();
        String trackingStartTime = tracking.getStartTime();
        String trackingEndTime = tracking.getEndTime();

        // Pass data through intent
        intent.putExtra("trackingId", trackingId);
        intent.putExtra("trackingStartTime", trackingStartTime);
        intent.putExtra("trackingEndTime", trackingEndTime);

        context.startActivity(intent);

        return true;
    }
}
