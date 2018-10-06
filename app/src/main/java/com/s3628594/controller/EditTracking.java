package com.s3628594.controller;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.s3628594.database.foodTruckDB;
import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingImplementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class EditTracking implements View.OnClickListener {

    private Activity activity;
    private EditText titleEdit, meetDateEdit, meetTimeEdit;
    private String trackingId, trackingStartTime, trackingEndTime;

    public EditTracking(Activity activity, EditText titleEdit, EditText meetDateEdit, EditText meetTimeEdit,
                        String trackingId, String trackingStartTime, String trackingEndTime) {
        this.activity = activity;
        this.titleEdit = titleEdit;
        this.meetDateEdit = meetDateEdit;
        this.meetTimeEdit = meetTimeEdit;
        this.trackingId = trackingId;
        this.trackingStartTime = trackingStartTime;
        this.trackingEndTime = trackingEndTime;
    }

    @Override
    public void onClick(View view) {
        final Tracking tracking = TrackingImplementation.getSingletonInstance().getTrackingById(trackingId);

        // Edit title
        String newTitle = titleEdit.getText().toString();
        TrackingImplementation.getSingletonInstance().editTrackingTitle(tracking, newTitle);

        // Edit meet time
        Date startTime = formatTime(trackingStartTime);
        fixMeetDateFormat(meetDateEdit);
        Date meetTime = formatMeetTime(meetDateEdit, meetTimeEdit);
        Date endTime = formatTime(trackingEndTime);

        if (validateMeetTime(startTime, endTime, meetTime)) {
            String validMeetTime = String.format("%s %s", meetDateEdit.getText().toString(), meetTimeEdit.getText().toString());
            TrackingImplementation.getSingletonInstance().editTrackingMeetTime(tracking, validMeetTime);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodTruckDB.getSingletonInstance().updateTracking(tracking);
            }
        }).start();
        activity.finish();
    }

    // Validate if startTime < meetTime < endTime
    private boolean validateMeetTime(Date trackingStartTime, Date trackingEndTime, Date trackingMeetTime) {
        if (trackingMeetTime.before(trackingEndTime) && trackingMeetTime.after(trackingStartTime)) {
            return true;
        }
        return false;
    }

    // Convert time from string to Date variable
    private Date formatTime(String time) {
        Date date;
        String newTime, formattedTime;
        int hour = Integer.parseInt(time.substring(12, 14));
        int min = Integer.parseInt(time.substring(15, 17));

        int maxHour = 12;

        if (hour > maxHour) {

            int newHour = hour - maxHour;
            newTime = String.format(Locale.getDefault(),"%d:%02d:00 PM", newHour, min);
            formattedTime = time.substring(1, 12) + newTime;

        } else if (hour == maxHour) {

            newTime = String.format(Locale.getDefault(),"%d:%02d:00 PM", hour, min);
            formattedTime = time.substring(1, 12) + newTime;

        } else {

            newTime = String.format(Locale.getDefault(),"%d:%02d:00 AM", hour, min);
            formattedTime = time.substring(1, 12) + newTime;
        }

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        try {
            date = dateFormat.parse(formattedTime);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Convert meetDate and meetTime into a single Date variable
    private Date formatMeetTime(EditText meetDateEdit, EditText meetTimeEdit) {
        Date date;
        String meetTime = meetTimeEdit.getText().toString();
        meetTime = new StringBuilder(meetTime).insert(meetTime.length() - 3, ":00").toString();

        String meetDateTime = meetDateEdit.getText().toString() + " " + meetTime;

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        try {
            date = dateFormat.parse(meetDateTime);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Correct the format for the meetDate variable
    private void fixMeetDateFormat(EditText meetDateEdit) {

        String meetDateYear = meetDateEdit.getText().toString().substring(6, 10);
        String meetDateMonth = meetDateEdit.getText().toString().substring(3, 5);
        String meetDateDay = meetDateEdit.getText().toString().substring(0, 2);

        String fixedMeetDate = String.format("%s/%s/%s", meetDateMonth, meetDateDay, meetDateYear);
        meetDateEdit.setText(fixedMeetDate);
    }
}
