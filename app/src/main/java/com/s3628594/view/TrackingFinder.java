package com.s3628594.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.s3628594.controller.AddTracking;
import com.s3628594.controller.CreateDateDialog;
import com.s3628594.controller.CreateTimeDialog;
import com.s3628594.controller.SearchTracking;
import com.s3628594.controller.SelectTrackableSpinner;
import com.s3628594.controller.SuggestTracking;
import com.s3628594.geotracking.R;
import com.s3628594.model.TrackableImplementation;
import com.s3628594.model.MatchedTrackingAdapter;
import com.s3628594.model.TrackingService;

import java.util.ArrayList;
import java.util.List;

public class TrackingFinder extends AppCompatActivity {

    private String selectedTrackable = "";
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList = new ArrayList<>();
    private ArrayList<ArrayList<String>> trackingMatchedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_finder);

        // Set up spinner
        Spinner selectTrackable = findViewById(R.id.find_trackable);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                                                          TrackableImplementation.getSingletonInstance().getFoodTruckTitle());
        selectTrackable.setAdapter(spinnerAdapter);
        selectTrackable.setOnItemSelectedListener(new SelectTrackableSpinner(this));

        // Set up date dialog
        EditText dateEdit = findViewById(R.id.edit_date);
        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new CreateDateDialog(dateEdit, this));

        // Set up time dialog
        EditText timeEdit = findViewById(R.id.edit_time);
        timeEdit.setInputType(InputType.TYPE_NULL);
        timeEdit.setOnClickListener(new CreateTimeDialog(timeEdit, this));

        // Set up tracking found
        MatchedTrackingAdapter trackingAdapter = new MatchedTrackingAdapter(this, trackingMatchedList);
        ListView trackingMatched = findViewById(R.id.tracking_available);
        trackingMatched.setAdapter(trackingAdapter);
        trackingMatched.setOnItemLongClickListener(new AddTracking(this, trackingInfoList));

        // Search button for tracking
        Button search = findViewById(R.id.search_tracking);
        search.setOnClickListener(new SearchTracking(this, timeEdit, dateEdit, trackingAdapter));

        // Suggest button for tracking
        Button suggest = findViewById(R.id.suggest_tracking);
        suggest.setOnClickListener(new SuggestTracking(this, trackingAdapter));
    }

    // Set selected trackable from the spinner
    public void setSelectedTrackable(String trackable) {
        selectedTrackable = trackable;
    }

    // Get selected trackable from the spinner
    public String getSelectedTrackable() {
        return selectedTrackable;
    }

    // Lists all tracking information per tracking
    public void setTrackingMatchedInfo(List<TrackingService.TrackingInfo> trackingInfo) {
        trackingInfoList.add(trackingInfo);
    }

    public void clearTrackingMatchedInfo() {
        trackingInfoList.clear();
    }

    // Lists only tracking information of a tracking when stationary
    public void setTrackingMatchedList(ArrayList<String> trackingMatched) {
        trackingMatchedList.add(trackingMatched);
    }

    public void clearTrackedMatchedList() {
        trackingMatchedList.clear();
    }
}
