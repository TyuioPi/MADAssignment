package com.s3628594.controller;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.s3628594.model.FoodTruck;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingImplementation;
import com.s3628594.model.TrackingService;

import java.util.ArrayList;
import java.util.List;

public class AddTracking implements AdapterView.OnItemLongClickListener {

    private Activity context;
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList;

    public AddTracking(Activity context, ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList) {
        this.context = context;
        this.trackingInfoList = trackingInfoList;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Extract details from selected tracking
        String[] trackingInfo = adapterView.getAdapter().getItem(i).toString().split("\\[|,|\\]\\z");
        String title = trackingInfo[1];
        int trackableId = getTrackingTrackableId(title);
        String startTime = trackingInfo[2];
        String endTime = trackingInfo[3];
        String meetLoc = String.format("%s, %s", trackingInfo[4], trackingInfo[5]);
        String currLoc = "";

        // Add the tracking
        TrackingImplementation.getSingletonInstance().addTracking(
                new Tracking(trackableId, title, startTime, endTime, startTime, currLoc, meetLoc, addTrackingRoute(trackableId, startTime)));

        // Sort the tracking list
        TrackingImplementation.getSingletonInstance().setDateSortedTrackingList();

        // Display message
        Toast.makeText(context, "Successfully Added Tracking", Toast.LENGTH_SHORT).show();
        return true;
    }

    private int getTrackingTrackableId(String title) {

        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableName().equals(title)) {
                return foodTruck.getTrackableId();
            }
        }
        return 0;
    }

    // Add a tracking's route information to a list
    private List<TrackingService.TrackingInfo> addTrackingRoute(int trackableId, String startTime) {
        List<TrackingService.TrackingInfo> routeInfo = new ArrayList<>();

        for (List<TrackingService.TrackingInfo> tracking : trackingInfoList) {

            if (tracking.get(tracking.size() - 1).trackableId == trackableId) {
                android.icu.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String newDate = dateFormat.format(tracking.get(tracking.size() - 1).date);

                if (newDate.equals(startTime.trim())){
                    for(int j = 0; j < tracking.size(); j++) {
                        routeInfo.add(tracking.get(j));
                    }
                }
            }
        }
        return routeInfo;
    }
}
