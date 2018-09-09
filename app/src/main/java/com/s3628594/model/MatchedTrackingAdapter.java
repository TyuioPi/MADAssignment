package com.s3628594.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.s3628594.geotracking.R;

import java.util.ArrayList;

public class MatchedTrackingAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<ArrayList<String>> trackingMatchedList;

    public MatchedTrackingAdapter(Activity context, ArrayList<ArrayList<String>> trackingMatchedList) {

        super(context, R.layout.custom_layout, trackingMatchedList);

        this.context = context;
        this.trackingMatchedList = trackingMatchedList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View mView = layoutInflater.inflate(R.layout.custom_layout, null, true);

        TextView tvTitle = mView.findViewById(R.id.food_truck_title);
        TextView tvStartTime = mView.findViewById(R.id.start_time);
        TextView tvEndTime = mView.findViewById(R.id.end_time);
        TextView tvLocation = mView.findViewById(R.id.location);

        tvTitle.setText(trackingMatchedList.get(i).get(0));
        tvStartTime.setText(trackingMatchedList.get(i).get(1));
        tvEndTime.setText(trackingMatchedList.get(i).get(2));
        tvLocation.setText(trackingMatchedList.get(i).get(3));
        return mView;
    }
}
