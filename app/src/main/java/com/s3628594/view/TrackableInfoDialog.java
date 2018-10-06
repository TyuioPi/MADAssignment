package com.s3628594.view;

import android.app.AlertDialog;

import com.s3628594.controller.Close;
import com.s3628594.controller.ViewTrackable;
import com.s3628594.geotracking.R;

public class TrackableInfoDialog {

    // Private variables
    private TrackableTab trackableTab;
    private ViewTrackable viewTrackable;

    public TrackableInfoDialog(TrackableTab trackableTab, ViewTrackable viewTrackable) {
        this.trackableTab = trackableTab;
        this.viewTrackable = viewTrackable;
    }

    public void createInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(trackableTab.getActivity());
        builder.setTitle(viewTrackable.getSelectedItem().getTrackableName());
        builder.setMessage(String.format(trackableTab.getContext().getString(R.string.description) + ": %s \n" +
                                         trackableTab.getContext().getString(R.string.category) + ": %s \n" +
                                         trackableTab.getContext().getString(R.string.website) + ": %s",
                viewTrackable.getSelectedItem().getTrackableDesc(), viewTrackable.getSelectedItem().getTrackableCategory(),
                viewTrackable.getSelectedItem().getTrackableUrl()))
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_selection_close, new Close(trackableTab.getActivity()));
        builder.create();
        builder.show();
    }
}
