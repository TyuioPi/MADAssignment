package com.s3628594.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

import com.s3628594.controller.AddTracking;
import com.s3628594.controller.Close;
import com.s3628594.controller.NextSuggestion;
import com.s3628594.geotracking.R;
import com.s3628594.model.TrackingService;

import java.util.ArrayList;
import java.util.List;

public class SuggestionDialog {

    private Activity activity;
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList;
    private ArrayList<ArrayList<String>> trackingMatchedList;
    private int position = 0;

    public SuggestionDialog(Activity activity, ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList,
                            ArrayList<ArrayList<String>> trackingMatchedList) {
        this.activity = activity;
        this.trackingInfoList = trackingInfoList;
        this.trackingMatchedList = trackingMatchedList;
    }

    public void createInfoDialog() {
        ArrayList<String> tracking;

        // Check if a tracking suggestion is available for display
        try {
            tracking = trackingMatchedList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.suggested_tracking);
            builder.setMessage(String.format("%s \n" +
                            activity.getString(R.string.start_time) + ": %s \n" +
                            activity.getString(R.string.meet_time) + ": " + activity.getString(R.string.empty) + "\n" +
                            activity.getString(R.string.end_time) + ": %s \n" +
                            activity.getString(R.string.location) + ": %s",
                    tracking.get(0), tracking.get(1), tracking.get(2), tracking.get(3)))
                    .setNegativeButton(R.string.dialog_selection_close, new Close(activity))
                    .setNeutralButton(R.string.next, new NextSuggestion(this, trackingMatchedList, position))
                    .setPositiveButton(R.string.dialog_selection_add, new AddTracking(activity, tracking, trackingInfoList.get(position)));
            builder.create();
            builder.show();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(activity, activity.getString(R.string.no_tracking), Toast.LENGTH_SHORT).show();
            e.getMessage();
        }
    }

    // Update the index of the suggested tracking list
    public void updatePosition() {
        if (position < trackingMatchedList.size() - 1) {
            position++;
        } else {
            position = 0;
        }
    }
}
