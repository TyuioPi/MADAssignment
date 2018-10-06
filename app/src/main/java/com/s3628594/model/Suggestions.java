package com.s3628594.model;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.s3628594.controller.RequestLocation;
import com.s3628594.database.foodTruckDB;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Suggestions {

    public static List<Tracking> SuggestionList = new ArrayList<>();
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList = new ArrayList<>();
    private ArrayList<ArrayList<String>> trackingMatchedList = new ArrayList<>();

    private Location deviceLocation = RequestLocation.deviceLocation;
    private Context context;
    private static Suggestions INSTANCE;

    private Suggestions() {
    }

    public static Suggestions getSingletonInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Suggestions();
        }
        return INSTANCE;
    }

    private void addTracking() {
        List<TrackingService.TrackingInfo> routeInfo = new ArrayList<>();
        for (int i = 0; i < SuggestionList.size(); i++) {
            routeInfo = trackingInfoList.get(i);
            Log.d("route", routeInfo.toString());
            SuggestionList.get(i).setRouteInfo(routeInfo);
        }
    }

    public void getSuggestionList(Context context) {
        this.context = context;
        Date deviceTime = correctDateFormat();
        final int SEARCH_WINDOW = 1440; // Set search window to 1 day

        // Lists all tracking information of all trackings found
        List<TrackingService.TrackingInfo> trackingFound =
                TrackingService.getSingletonInstance(context).getTrackingInfoForTimeRange(deviceTime, SEARCH_WINDOW, 0);

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
        addTracking();

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
                        trackingMatchedList.add(matchedTracking);
                        trackingInfoList.add(temporaryTrackingInfo);
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
        HTTPRequest httpRequest = new HTTPRequest(context, deviceLat, deviceLng, endLat, endLng);
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
        int trackableId = getTrackingTrackableId(title);
        String location = String.format("%s, %s", trackingInfo.latitude, trackingInfo.longitude);
        String currLoc = "";

        matchedTracking.add(title);
        matchedTracking.add(dateFormat.format(startTime));
        matchedTracking.add(dateFormat.format(endTime));
        matchedTracking.add(location);
        Tracking newTracking = new Tracking(trackableId,title, dateFormat.format(startTime), dateFormat.format(endTime),
                dateFormat.format(startTime), currLoc, location, null,false);
        SuggestionList.add(newTracking);


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

    private int getTrackingTrackableId(String title) {

        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableName().equals(title)) {
                return foodTruck.getTrackableId();
            }
        }
        return 0;
    }











}





