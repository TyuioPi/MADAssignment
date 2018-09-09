package com.s3628594.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.s3628594.geotracking.R;

import java.util.ArrayList;

public class AddTrackingAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<Tracking> trackingList;

    public AddTrackingAdapter(Activity context, ArrayList<Tracking> trackingList) {

        super(context, R.layout.custom_layout, trackingList);

        this.context = context;
        this.trackingList = trackingList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View mView = layoutInflater.inflate(R.layout.custom_layout, null, true);

        TextView tvTitle = mView.findViewById(R.id.food_truck_title);
        TextView tvStartTime = mView.findViewById(R.id.start_time);
        TextView tvMeetTime = mView.findViewById(R.id.meet_time);
        TextView tvEndTime = mView.findViewById(R.id.end_time);
        TextView tvLocation = mView.findViewById(R.id.location);

        tvTitle.setText(trackingList.get(i).getTitle());
        tvStartTime.setText(trackingList.get(i).getStartTime());

        if (trackingList.get(i).getMeetTime() == null) {
            tvMeetTime.setText(trackingList.get(i).getStartTime());
        } else {
            tvMeetTime.setText(trackingList.get(i).getMeetTime());
        }
        tvEndTime.setText(trackingList.get(i).getEndTime());
        tvLocation.setText(trackingList.get(i).getMeetLoc());

        return mView;
    }
}
