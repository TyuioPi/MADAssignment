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
import com.s3628594.model.Tracking;
import com.s3628594.view.ReminderPublisher;
import com.s3628594.view.SuggestionPublisher;
import com.s3628594.view.TrackingFinder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ReminderNotification {

    private long time = 60000;
    private Calendar calendar = Calendar.getInstance();

    public void scheduleReminder(Context context, Tracking tracking){
        Log.d("reminder", Boolean.toString(tracking.isReminder()));

        if (tracking.isReminder()){
          long currentTime = getTime(tracking.getMeetTime());
            Log.d("tracking", Long.toString(currentTime));
          Log.d("miliseconds", Long.toString(System.currentTimeMillis()));
          long alarm = SystemClock.elapsedRealtime() + time;

          Intent intent = new Intent(context, ReminderPublisher.class);
          intent.putExtra(ReminderPublisher.notificationid, 1);
          intent.putExtra(ReminderPublisher.Reminder, buildNotification(context));
          PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

          AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
          alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarm, pendingIntent);
        }
    }

    public long getTime(String meetTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date;
        try {
            date = dateFormat.parse(meetTime.trim());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = calendar.getTimeInMillis();
        return time;

    }


    private Notification buildNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ReminderPublisher.id);
        builder.setContentTitle("Reminder");
        builder.setContentText("Your tracking is starting soon");
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return builder.build();
    }








}
