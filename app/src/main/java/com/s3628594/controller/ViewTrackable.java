package com.s3628594.controller;

import android.view.View;
import android.widget.AdapterView;

import com.s3628594.model.FoodTruck;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.view.TrackableInfoDialog;
import com.s3628594.view.TrackableTab;

public class ViewTrackable implements AdapterView.OnItemClickListener {

    // Private variables
    private TrackableTab trackableTab;
    private String item;

    public ViewTrackable(TrackableTab trackableTab){
        this.trackableTab = trackableTab;
    }

    // Display trackable information in a dialog
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getAdapter().getItem(i).toString();
        TrackableInfoDialog trackableInfoDialog = new TrackableInfoDialog(trackableTab, this);
        trackableInfoDialog.createInfoDialog();
    }

    // Return the selected item from trackable list
    public FoodTruck getSelectedItem() {
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableName().equals(item)) {
                return foodTruck;
            }
        }
        return null;
    }
}
