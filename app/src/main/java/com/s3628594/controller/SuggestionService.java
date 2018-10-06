package com.s3628594.controller;

import android.app.IntentService;
import android.content.Intent;

import com.s3628594.view.SuggestionPublisher;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SuggestionService extends IntentService {


    public static final String Next = "Suggest_next";
    public static final String Cancel = "Schedule_next";

    public SuggestionService() {
        super("SuggestionService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(Next)){
                SuggestNotification.getSingletonInstance().ScheduleNotification(this, 5000);
                SuggestionPublisher.notificationManager.cancel(1);
            }else if (action.equals(Cancel)){
                SuggestNotification.getSingletonInstance().ScheduleNotification(this, 30000);
                SuggestionPublisher.notificationManager.cancel(1);
            }

        }
    }





}
