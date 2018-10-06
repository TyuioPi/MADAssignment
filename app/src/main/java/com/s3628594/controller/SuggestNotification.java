package com.s3628594.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.s3628594.model.Settings;
import com.s3628594.model.Suggestions;
import com.s3628594.view.SuggestionPublisher;

public class SuggestNotification {

    private long time = 30000;
    private AlarmManager alarmManager;

    private static SuggestNotification INSTANCE = null;
    private int counter = 0;

    private SuggestNotification(){}

    public static SuggestNotification getSingletonInstance(){
        if (INSTANCE == null){
            INSTANCE = new SuggestNotification();
        }
        return INSTANCE;
    }


    private void getTime(){
        String TimeSetting = Settings.Notification_period;
        if (TimeSetting.equals("5 minutes")){
            time = 300000;
        }else if (TimeSetting.equals("10 minutes")) {
            time = 600000;
        }
    }

    public void ScheduleNotification(Context context, long time){
        Intent intent = new Intent(context, SuggestionPublisher.class);
        intent.putExtra(SuggestionPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(SuggestionPublisher.NOTIFICATION, buildNotification(context, counter ));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long schedule = SystemClock.elapsedRealtime() + time;
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,schedule, pendingIntent);
        counter++;
    }




    private Notification buildNotification(Context context, int i){
        Log.d("notification", Integer.toString(i));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SuggestionPublisher.id);
        builder.setContentTitle("New Tracking");
        if (i <= Suggestions.SuggestionList.size() -1 ){
            builder.setContentText(Suggestions.SuggestionList.get(i).toString());
            Intent notificationintent = new Intent(context, addTrackingReceiver.class);
            notificationintent.putExtra("integer", i);
            notificationintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent addService = PendingIntent.getBroadcast(context, 0, notificationintent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(android.R.drawable.ic_menu_add, "Add Tracking", addService);

            Intent nextIntent = new Intent(context, SuggestionService.class);
            nextIntent.setAction(SuggestionService.Next);
            PendingIntent nextService = PendingIntent.getService(context, 0, nextIntent, 0);
            builder.addAction(android.R.drawable.ic_menu_view, "Next Tracking", nextService);

            Intent cancelIntent = new Intent(context, SuggestionService.class);
            cancelIntent.putExtra("counter", i);
            cancelIntent.setAction(SuggestionService.Cancel);
            PendingIntent canelIntent = PendingIntent.getService(context, 0, cancelIntent, 0);
            builder.addAction(android.R.drawable.ic_menu_view, "Cancel", canelIntent);
        }else {
            builder.setContentText("No trackable available");
            builder.setAutoCancel(true);
        }
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return builder.build();
    }











}
