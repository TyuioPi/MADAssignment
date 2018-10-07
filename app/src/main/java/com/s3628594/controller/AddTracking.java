package com.s3628594.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.s3628594.database.foodTruckDB;
import com.s3628594.model.FoodTruck;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingImplementation;
import com.s3628594.model.TrackingService;

import java.util.ArrayList;
import java.util.List;

public class AddTracking implements AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener{

    private Activity context;
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList;
    private List<TrackingService.TrackingInfo> trackingInfo;
    private ArrayList<String> tracking;
    private ReminderNotification reminder = new ReminderNotification();
    private String title, startTime, endTime, meetLoc, currLoc;
    private int trackableId;

    public AddTracking(Activity context, ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList) {
        this.context = context;
        this.trackingInfoList = trackingInfoList;
    }

    public AddTracking(Activity context, ArrayList<String> tracking,
                       List<TrackingService.TrackingInfo> trackingInfo) {
        this.context = context;
        this.trackingInfo = trackingInfo;
        this.tracking = tracking;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        title = tracking.get(0);
        trackableId = getTrackingTrackableId(title);
        startTime = tracking.get(1);
        endTime = tracking.get(2);
        meetLoc = tracking.get(3);
        currLoc = "";

        addNewTracking(trackingInfo);
        sortTrackingList();
        displayMessage();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Extract details from selected tracking
        String[] trackingInfo = adapterView.getAdapter().getItem(i).toString().split("\\[|,|\\]\\z");
        title = trackingInfo[1];
        trackableId = getTrackingTrackableId(title);
        startTime = trackingInfo[2];
        endTime = trackingInfo[3];
        meetLoc = String.format("%s, %s", trackingInfo[4], trackingInfo[5]);
        currLoc = "";

        // Add the tracking
        addNewTracking(addTrackingRoute(trackableId, startTime));

        sortTrackingList();
        displayMessage();
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

    // Notify user of successfully adding a tracking
    private void displayMessage() {
        Toast.makeText(context, "Successfully Added Tracking", Toast.LENGTH_SHORT).show();
    }

    // Sort the tracking list
    private void sortTrackingList() {
        TrackingImplementation.getSingletonInstance().setDateSortedTrackingList();
    }

    // Add tracking to memory and database
    private void addNewTracking(List<TrackingService.TrackingInfo> trackingRouteInfo) {
        final Tracking newTracking = new Tracking(trackableId, title, startTime, endTime, startTime, currLoc, meetLoc,
                trackingRouteInfo, true);
        TrackingImplementation.getSingletonInstance().addTracking(newTracking);
        reminder.scheduleReminder(context,newTracking);
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodTruckDB.getSingletonInstance().addItemtoTracking(newTracking);
            }
        }).start();
    }
}
