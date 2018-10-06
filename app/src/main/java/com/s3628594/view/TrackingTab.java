package com.s3628594.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.s3628594.controller.CreateEditTrackingActivity;
import com.s3628594.controller.DisplayRouteInfo;
import com.s3628594.controller.NewTracking;
import com.s3628594.database.foodTruckDB;
import com.s3628594.geotracking.R;
import com.s3628594.model.AddTrackingAdapter;
import com.s3628594.model.TrackingImplementation;

public class TrackingTab extends Fragment {

    private AddTrackingAdapter adapter;
    final Handler timeHandler = new Handler();
    private Runnable updater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tracking_tab, container, false);

        // FloatingActionButton for adding to tracking list
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new NewTracking(this));

        // Setup item list
        adapter = new AddTrackingAdapter(getActivity(), TrackingImplementation.getSingletonInstance().getTrackingList());
        ListView itemList = view.findViewById(R.id.tracking_list);
        itemList.setOnItemClickListener(new DisplayRouteInfo(getActivity()));
        itemList.setOnItemLongClickListener(new CreateEditTrackingActivity(getActivity()));
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        foodTruckDB.getSingletonInstance().viewTrackingData();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacks(updater);
    }

    // Check for any changes to our adapter view
    private void updateView() {
        updater = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                timeHandler.postDelayed(updater, 5000);
            }
        };
        timeHandler.post(updater);
    }
}
