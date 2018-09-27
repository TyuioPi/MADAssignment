package com.s3628594.controller;

import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.widget.EditText;

import com.s3628594.geotracking.R;
import com.s3628594.model.FoodTruck;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.model.MatchedTrackingAdapter;
import com.s3628594.model.TrackingService;
import com.s3628594.view.TrackingFinder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchTracking implements View.OnClickListener {

    private TrackingFinder trackingFinder;
    private EditText timeEdit, dateEdit;
    private Date date;
    private MatchedTrackingAdapter matchedTrackingAdapter;

    public SearchTracking(TrackingFinder trackingFinder, EditText timeEdit, EditText dateEdit, MatchedTrackingAdapter matchedTrackingAdapter) {
        this.trackingFinder = trackingFinder;
        this.timeEdit = timeEdit;
        this.dateEdit = dateEdit;
        this.matchedTrackingAdapter = matchedTrackingAdapter;
    }

    @Override
    public void onClick(View view) {
        if (!timeEdit.getText().toString().equals(trackingFinder.getResources().getString(R.string.select_meet_time)) &&
                !dateEdit.getText().toString().equals(trackingFinder.getResources().getString(R.string.select_date))) {
            formatDate();

            // Reset tracking matches
            trackingFinder.clearTrackingMatchedInfo();
            trackingFinder.clearTrackedMatchedList();

            // Search for tracking matches
            findMatchedTracking();

            // Update the view
            matchedTrackingAdapter.notifyDataSetChanged();
        }
    }

    // Convert the date and time from EditText to Date
    private void formatDate() {
        String timePicked = timeEdit.getText().toString();
        String datePicked = dateEdit.getText().toString();
        timePicked = new StringBuilder(timePicked).insert(timePicked.length() - 3, ":00").toString();
        String searchDate = datePicked + " " + timePicked;
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        try {
            date = dateFormat.parse(searchDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void findMatchedTracking() {
        final int SEARCH_WINDOW = 60; // Set a default search window for tracking of +/- 1 hour

        System.out.println("DATE: " + date);

        // Lists all tracking information of all trackings found
        List<TrackingService.TrackingInfo> trackingFound =
                TrackingService.getSingletonInstance(trackingFinder).getTrackingInfoForTimeRange(date, SEARCH_WINDOW, 0);

        // Temporary list to return all tracking information per tracking
        List<TrackingService.TrackingInfo> temporaryTrackingInfo = new ArrayList<>();

        // Lists only tracking information of a tracking when stationary
        ArrayList<String> matchedTracking = new ArrayList<>();

        // Check for title and id match, add necessary tracking information to respective lists
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableName().contains(trackingFinder.getSelectedTrackable())) {
                for (TrackingService.TrackingInfo trackingInfo : trackingFound) {
                    if (trackingInfo.trackableId == foodTruck.getTrackableId()) {
                        if (trackingInfo.stopTime == 0) {
                            temporaryTrackingInfo.add(trackingInfo);
                        } else {
                            temporaryTrackingInfo.add(trackingInfo);
                            getAllMatchedTracking(trackingInfo, matchedTracking);
                            trackingFinder.setTrackingMatchedList(matchedTracking);
                            trackingFinder.setTrackingMatchedInfo(temporaryTrackingInfo);

                            // Create new empty list to separate each tracking
                            temporaryTrackingInfo = new ArrayList<>();
                            matchedTracking = new ArrayList<>();
                        }
                    }
                }
            }
        }
    }

    /* Extract tracking information of a stationary tracking to obtain the title, start time,
    ** end time and location as strings for displaying in our views
    */
    private void getAllMatchedTracking(TrackingService.TrackingInfo trackingInfo, ArrayList<String> matchedTracking) {
        Calendar calendar = Calendar.getInstance();
        android.icu.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Extract tracking info as string
        String title = "";
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableId() == trackingInfo.trackableId) {
                title = foodTruck.getTrackableName();
            }
        }
        Date startTime = trackingInfo.date;
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, trackingInfo.stopTime);
        Date endTime = calendar.getTime();
        String location = String.format("%s, %s", trackingInfo.latitude, trackingInfo.longitude);

        matchedTracking.add(title);
        matchedTracking.add(dateFormat.format(startTime));
        matchedTracking.add(dateFormat.format(endTime));
        matchedTracking.add(location);
    }
}
