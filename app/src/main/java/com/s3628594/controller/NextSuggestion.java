package com.s3628594.controller;

import android.content.DialogInterface;

import com.s3628594.view.SuggestionDialog;

import java.util.ArrayList;

public class NextSuggestion implements DialogInterface.OnClickListener {

    private SuggestionDialog suggestionDialog;
    private ArrayList<ArrayList<String>> trackingMatchedList;
    private int position;

    public NextSuggestion(SuggestionDialog suggestionDialog, ArrayList<ArrayList<String>> trackingMatchedList, int position) {
        this.suggestionDialog = suggestionDialog;
        this.trackingMatchedList = trackingMatchedList;
        this.position = position;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        // Dismiss dialog when end of suggestion list reached otherwise get next suggestion
        if (position == trackingMatchedList.size() - 1) {
            dialogInterface.dismiss();
        } else {
            suggestionDialog.updatePosition();
            suggestionDialog.createInfoDialog();
        }
    }
}
