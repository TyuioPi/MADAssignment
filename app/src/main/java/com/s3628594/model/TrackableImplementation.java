package com.s3628594.model;

import java.util.ArrayList;
import java.util.Collections;

// Class to handle anything related to a trackable
public class TrackableImplementation {

    private ArrayList<FoodTruck> trackableList = new ArrayList<>();
    private ArrayList<String> categoryList = new ArrayList<>();
    private static TrackableImplementation INSTANCE = null;

    // Our singleton
    private TrackableImplementation() {}

    // Return the singleton
    public static TrackableImplementation getSingletonInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new TrackableImplementation();
        }
        return INSTANCE;
    }

    public void addFoodTruck(FoodTruck foodTruck) {
        trackableList.add(foodTruck);
    }

    public ArrayList<FoodTruck> getTrackableList() {
        return trackableList;
    }

    // Return trackable title's as array list
    public ArrayList<String> getFoodTruckTitle() {

        ArrayList<String> title = new ArrayList<>();
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            title.add(foodTruck.getTrackableName());
        }
        return title;
    }

    public void setCategoryList(String category) {

        if (!categoryList.contains(category)) {
            categoryList.add(category);
        }
        Collections.sort(categoryList, String.CASE_INSENSITIVE_ORDER);
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public void getWalkingTimeToTrackable() {

    }
}
