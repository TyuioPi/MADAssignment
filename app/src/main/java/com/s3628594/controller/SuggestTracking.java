package com.s3628594.controller;

import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import com.s3628594.model.FoodTruck;
import com.s3628594.model.HTTPRequest;
import com.s3628594.model.MatchedTrackingAdapter;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.model.TrackingService;
import com.s3628594.view.TrackingFinder;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SuggestTracking implements View.OnClickListener {

    private TrackingFinder trackingFinder;
    private MatchedTrackingAdapter matchedTrackingAdapter;
    private Location deviceLocation = RequestLocation.deviceLocation;

    public SuggestTracking(TrackingFinder trackingFinder, MatchedTrackingAdapter matchedTrackingAdapter) {
        this.trackingFinder = trackingFinder;
        this.matchedTrackingAdapter = matchedTrackingAdapter;
    }

    @Override
    public void onClick(View view) {
        if (deviceLocation != null) {
            // Reset tracking suggestions
            trackingFinder.clearTrackingMatchedInfo();
            trackingFinder.clearTrackedMatchedList();

            // Search for tracking suggestions
            getSuggestionList();

            // Update the view
            matchedTrackingAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(trackingFinder, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    // Return list of suggested trackings
    private void getSuggestionList() {
        Date deviceTime = correctDateFormat();
        final int SEARCH_WINDOW = 1440; // Set search window to 1 day

        // Lists all tracking information of all trackings found
        List<TrackingService.TrackingInfo> trackingFound =
                TrackingService.getSingletonInstance(trackingFinder).getTrackingInfoForTimeRange(deviceTime, SEARCH_WINDOW, 0);

        // Temporary list to return all tracking information per tracking
        List<TrackingService.TrackingInfo> temporaryTrackingInfo = new ArrayList<>();

        // Lists only tracking information of a tracking when stationary
        ArrayList<String> matchedTracking = new ArrayList<>();

        // Check for trackables by category filter and match trackable ID to tracking ID from trackings found within
        // 1 day to deviceTime
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            for (TrackingService.TrackingInfo trackingInfo : trackingFound) {
                if (foodTruck.getTrackableId() == trackingInfo.trackableId) {
                    // Create suggestion list for all categories (no filter applied)
                    if (TrackableImplementation.getSingletonInstance().getSelectedCategoryList().size() == 0) {
                        if (createSuggestion(trackingInfo, temporaryTrackingInfo, matchedTracking,
                                deviceTime, deviceLocation)) {
                            // Create new empty list to separate each tracking
                            temporaryTrackingInfo = new ArrayList<>();
                            matchedTracking = new ArrayList<>();
                        }
                    } else {
                        // Create suggestion list with category filter applied
                        for (String filter : TrackableImplementation.getSingletonInstance().getSelectedCategoryList()) {
                            if (foodTruck.getTrackableCategory().equals(filter)) {
                                if (createSuggestion(trackingInfo, temporaryTrackingInfo, matchedTracking,
                                        deviceTime, deviceLocation)) {

                                    // Create new empty list to separate each tracking
                                    temporaryTrackingInfo = new ArrayList<>();
                                    matchedTracking = new ArrayList<>();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Check if a tracking suggestion has been made
    private boolean createSuggestion(TrackingService.TrackingInfo trackingInfo,
                                     List<TrackingService.TrackingInfo> temporaryTrackingInfo,
                                     ArrayList<String> matchedTracking,
                                     Date deviceTime, Location deviceLocation) {
        if (trackingInfo.stopTime == 0) {
            temporaryTrackingInfo.add(trackingInfo);
        } else {
            String walkingTime = getWalkingTimeToTracking(deviceLocation, trackingInfo.latitude, trackingInfo.longitude);
            long timeToArrival = trackingInfo.date.getTime() - deviceTime.getTime();

            // Check if it is possible to walk to the tracking
            if (walkingTime != null) {
                // Convert walking duration from seconds to milliseconds
                int convertWalkingTime = Integer.parseInt(walkingTime) * 1000;

                // Suggest the tracking if it currently exists and the user can reach the tracking within time
                if (trackingInfo.date.after(deviceTime) && convertWalkingTime < timeToArrival) { // Can add stop time to increase duration
                    if (convertWalkingTime < timeToArrival) {
                        temporaryTrackingInfo.add(trackingInfo);
                        getAllMatchedTracking(trackingInfo, matchedTracking);
                        trackingFinder.setTrackingMatchedList(matchedTracking);
                        trackingFinder.setTrackingMatchedInfo(temporaryTrackingInfo);
                        return true;
                    }
                } else {
                    temporaryTrackingInfo.clear();
                }
            } else {
                temporaryTrackingInfo.clear();
            }
        }
        return false;
    }

    private String getWalkingTimeToTracking(Location deviceLocation, Double trackingLat, Double trackingLng) {
        String deviceLat = Double.toString(deviceLocation.getLatitude());
        String deviceLng = Double.toString(deviceLocation.getLongitude());
        String endLat = Double.toString(trackingLat);
        String endLng = Double.toString(trackingLng);
        HTTPRequest httpRequest = new HTTPRequest(trackingFinder, deviceLat, deviceLng, endLat, endLng);
        httpRequest.start();

        // Wait until thread is complete before retrieving walking duration value
        try {
            httpRequest.getLatch().await();
            return httpRequest.getWalkingTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

    private Date correctDateFormat() {
        Date deviceTime = Calendar.getInstance().getTime();
        DateFormat dayFormat = new java.text.SimpleDateFormat("dd", Locale.getDefault());
        DateFormat monthFormat = new java.text.SimpleDateFormat("MM", Locale.getDefault());
        DateFormat temp = new java.text.SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());

        // Switch day and month to correct format
        String day = monthFormat.format(deviceTime);
        String month = dayFormat.format(deviceTime);

        // Convert month int to string name
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        String fixedMonth = months[Integer.parseInt(month) - 1];

        // Correct the day and month
        StringBuilder stringBuilder = new StringBuilder(deviceTime.toString());
        stringBuilder.replace(4,7, fixedMonth.substring(0,3));
        stringBuilder.replace(8,10, day);

        // Correct the date
        String tempDate = stringBuilder.substring(4);
        Date fixedDate = null;

        try {
            fixedDate = temp.parse(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fixedDate;
    }

}
