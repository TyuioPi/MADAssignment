package com.s3628594.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.s3628594.controller.ViewTrackable;
import com.s3628594.geotracking.R;
import com.s3628594.model.FoodTruck;
import com.s3628594.model.TrackableImplementation;

import java.util.ArrayList;

public class TrackableTab extends android.support.v4.app.Fragment {

    private ArrayAdapter adapter, filteredAdapter;
    private ListView itemList;
    private ArrayList<String> filteredNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trackable_tab, container, false);

        // Setup item list
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                                     TrackableImplementation.getSingletonInstance().getFoodTruckTitle());
        itemList = view.findViewById(R.id.trackable_list);
        setMyAdapter(adapter);

        // Set up listener
        itemList.setOnItemClickListener(new ViewTrackable(this));

        return view;
    }

    // Return food truck titles after category filter
    public ArrayList<String> getFilteredFoodTruckName(String filter) {
        for (FoodTruck foodTruck : TrackableImplementation.getSingletonInstance().getTrackableList()) {
            if (foodTruck.getTrackableCategory().equals(filter) && !filteredNames.contains(filter)) {
                filteredNames.add(foodTruck.getTrackableName());
            }
        }
        return filteredNames;
    }

    // Empty the food truck title in filter list
    public void clearFilter() {
        filteredNames.clear();
    }

    // Set our adapter
    public void setMyAdapter(ArrayAdapter adapter) {
        itemList.setAdapter(adapter);
    }

    // Create a new adapter for filtering the food truck list
    public void createFilterAdapter() {
        filteredAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, filteredNames);
    }

    // Get adapter with original food truck list
    public ArrayAdapter getAdapter() {
        return adapter;
    }

    // Get adapter with filtered food truck list
    public ArrayAdapter getFilteredAdapter() {
        return filteredAdapter;
    }
}
