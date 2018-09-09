package com.s3628594.controller;

import android.app.Dialog;
import android.content.DialogInterface;

import com.s3628594.view.TrackableTab;

import java.util.ArrayList;

public class FilterByCategory implements Dialog.OnClickListener {

    private ArrayList mSelectedCategories;
    private String[] categoryFilter;
    private TrackableTab trackableTab;

    public FilterByCategory(ArrayList mSelectedCategories, String[] categoryFilter, TrackableTab trackableTab) {
        this.mSelectedCategories = mSelectedCategories;
        this.categoryFilter = categoryFilter;
        this.trackableTab = trackableTab;
    }
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        trackableTab.clearFilter();

        // Pass the category filter value to return food trucks in the category
        for (Object category : mSelectedCategories) {
            trackableTab.getFilteredFoodTruckName(categoryFilter[Integer.parseInt(category.toString())]);
        }

        // Create our filter adapter and set it to our trackable tab for updates to the view
        trackableTab.createFilterAdapter();
        trackableTab.setMyAdapter(trackableTab.getFilteredAdapter());
        trackableTab.getFilteredAdapter().notifyDataSetChanged();
    }
}
