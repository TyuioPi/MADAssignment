package com.s3628594.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

import static android.content.Context.LOCATION_SERVICE;

public class SuggestTracking implements View.OnClickListener {

    // TODO: Pass list of trackables to this class to get their lat/lng of meeting locations
    private static final String LOG_TAG = SuggestTracking.class.getName();
    private TrackingFinder trackingFinder;
    private MatchedTrackingAdapter matchedTrackingAdapter;
    private Location deviceLocation;

    public SuggestTracking(TrackingFinder trackingFinder, MatchedTrackingAdapter matchedTrackingAdapter) {
        this.trackingFinder = trackingFinder;
        this.matchedTrackingAdapter = matchedTrackingAdapter;
    }

    @Override
    public void onClick(View view) {
        if (checkLocationPermissions()) {
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
    }

    // Get device location permissions to retrieve device location
    private boolean checkLocationPermissions() {
        LocationManager lm = (LocationManager) trackingFinder.getSystemService(LOCATION_SERVICE);
        GetDeviceLocation getDeviceLocation = new GetDeviceLocation(this);
        // User has provided location permissions to allow for tracking suggestions
        if (ContextCompat.checkSelfPermission(trackingFinder, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                // Check if a provider is available to allow for tracking suggestions
                if ((lm.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                    lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, getDeviceLocation, null);
                    return true;
                } else {
                    // Display error message to show network connectivity failure
                    Toast.makeText(trackingFinder, "To use this service, enable device location", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "isProviderEnabled NullPointerException: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "getDeviceLocation SecurityException: " + e.getMessage());
            }
        } else {
            Toast.makeText(trackingFinder, "To use this service, enable location permissions", Toast.LENGTH_SHORT).show();
        }
        return false;
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
            int walkingTime = getWalkingTimeToTracking(deviceLocation, trackingInfo.latitude, trackingInfo.longitude);
            long timeToArrival = trackingInfo.date.getTime() - deviceTime.getTime();

            // Suggest the tracking if it currently exists and the user can reach the tracking within time
            if (trackingInfo.date.after(deviceTime) && walkingTime < timeToArrival) { // Can add stop time to increase duration
                if (walkingTime < timeToArrival) {
                    temporaryTrackingInfo.add(trackingInfo);
                    getAllMatchedTracking(trackingInfo, matchedTracking);
                    trackingFinder.setTrackingMatchedList(matchedTracking);
                    trackingFinder.setTrackingMatchedInfo(temporaryTrackingInfo);
                    return true;
                }
            } else {
                temporaryTrackingInfo.clear();
            }
        }
        return false;
    }

    private int getWalkingTimeToTracking(Location deviceLocation, Double trackingLat, Double trackingLng) {
        String deviceLat = Double.toString(deviceLocation.getLatitude());
        String deviceLng = Double.toString(deviceLocation.getLongitude());
        String endLat = Double.toString(trackingLat);
        String endLng = Double.toString(trackingLng);
        HTTPRequest httpRequest = new HTTPRequest(trackingFinder, deviceLat, deviceLng, endLat, endLng);
        httpRequest.start();

        // Wait until thread is complete before retrieving walking duration value
        try {
            httpRequest.getLatch().await();

            // Convert walking duration from seconds to milliseconds
            return httpRequest.getWalkingTime() * 1000;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
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

    // Set the device location when permissions have been received in GetDeviceLocation controller
    // via checkLocationPermissions() method
    public void setDeviceLocation(Location deviceLocation) {
        this.deviceLocation = deviceLocation;
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
