package com.s3628594.controller;

import android.app.Activity;
import android.view.View;

import com.s3628594.model.TrackingService;
import com.s3628594.view.SuggestionDialog;

import java.util.ArrayList;
import java.util.List;

public class CreateSuggestionDialog implements View.OnClickListener {

    private Activity activity;
    private ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList;
    private ArrayList<ArrayList<String>> trackingMatchedList;

    public CreateSuggestionDialog(Activity activity, ArrayList<List<TrackingService.TrackingInfo>> trackingInfoList,
                                  ArrayList<ArrayList<String>> trackingMatchedList) {
        this.activity = activity;
        this.trackingInfoList = trackingInfoList;
        this.trackingMatchedList = trackingMatchedList;
    }

    @Override
    public void onClick(View view) {
        SuggestionDialog suggestionDialog = new SuggestionDialog(activity, trackingInfoList, trackingMatchedList);
        suggestionDialog.createInfoDialog();
    }
}
