package com.s3628594.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.s3628594.model.Settings;
import com.s3628594.view.SuggestionPublisher;

public class SuggestNotification {

    private long time = 60000;

    public void ScheduleNotification(Context context){
        if (Settings.Notification_turnOn){
            getTime();
            Intent intent = new Intent(context, SuggestionPublisher.class);
            intent.putExtra(SuggestionPublisher.NOTIFICATION_ID, 1);
            intent.putExtra(SuggestionPublisher.NOTIFICATION, buildNotification(context));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            long schedule = SystemClock.elapsedRealtime() + time;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,schedule,time, pendingIntent);
        }
    }


    private void getTime(){
        String TimeSetting = Settings.Notification_period;
        if (TimeSetting.equals("5 minutes")){
            time = 300000;
        }else if (TimeSetting.equals("10 minutes")) {
            time = 600000;
        }
    }

    private Notification buildNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SuggestionPublisher.id);
        builder.setContentTitle("New Tracking");
        builder.setContentText("There is a Trackable near you would you like to start tracking");
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return builder.build();
    }









}
