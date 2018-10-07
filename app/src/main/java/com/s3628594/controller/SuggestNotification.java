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

import com.s3628594.geotracking.R;
import com.s3628594.model.PreferenceSettings;
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
        String TimeSetting = PreferenceSettings.Notification_period;
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

    private Notification buildNotification(Context context, int i) {
        Log.d("notification", Integer.toString(i));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SuggestionPublisher.id);
        builder.setContentTitle(context.getString(R.string.notification_title));
        if (i <= Suggestions.SuggestionList.size() -1 ){
            builder.setContentText(Suggestions.SuggestionList.get(i).toString());
            Intent notificationIntent = new Intent(context, AddTrackingReceiver.class);
            notificationIntent.putExtra("integer", i);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent addService = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(android.R.drawable.ic_menu_add, context.getString(R.string.add_tracking), addService);

            Intent nextIntent = new Intent(context, SuggestionService.class);
            nextIntent.setAction(SuggestionService.Next);
            PendingIntent nextService = PendingIntent.getService(context, 0, nextIntent, 0);
            builder.addAction(android.R.drawable.ic_menu_view, context.getString(R.string.next_tracking), nextService);

            Intent cancelIntent = new Intent(context, SuggestionService.class);
            cancelIntent.putExtra("counter", i);
            cancelIntent.setAction(SuggestionService.Cancel);
            PendingIntent cancelPendingIntent = PendingIntent.getService(context, 0, cancelIntent, 0);
            builder.addAction(android.R.drawable.ic_menu_view, context.getString(R.string.cancel), cancelPendingIntent);
        } else {
            builder.setContentText(context.getString(R.string.no_tracking));
            builder.setAutoCancel(true);
        }
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return builder.build();
    }











}
