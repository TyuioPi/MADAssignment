package com.s3628594.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.s3628594.database.foodTruckDB;
import com.s3628594.model.Suggestions;
import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingImplementation;
import com.s3628594.view.SuggestionPublisher;

public class AddTrackingReceiver extends BroadcastReceiver {

    private ReminderNotification reminder = new ReminderNotification();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Display message
        Toast.makeText(context, "broadcast received", Toast.LENGTH_SHORT).show();

        int i = intent.getIntExtra("integer", 0);
        final Tracking newTracking = Suggestions.SuggestionList.get(i);
        newTracking.setReminder(true);
        TrackingImplementation.getSingletonInstance().addTracking(newTracking);
        reminder.scheduleReminder(context, newTracking);

        // Add tracking to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodTruckDB.getSingletonInstance().addItemtoTracking(newTracking);
            }
        }).start();

        // Sort the tracking list
        TrackingImplementation.getSingletonInstance().setDateSortedTrackingList();
        SuggestionPublisher.notificationManager.cancel(1);
    }
}

