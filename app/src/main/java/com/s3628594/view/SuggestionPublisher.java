package com.s3628594.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.s3628594.controller.SuggestNotification;

public class SuggestionPublisher extends BroadcastReceiver {

    public static String id = "SuggestionChannel";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private String title = "SuggestionNotification";
    private NotificationManager notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = notificationManager.getNotificationChannel(id);
            if (channel == null) {
                channel = new NotificationChannel(id, title, importance);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(id, notification);
            Log.d("notify", "hi");
        }else{
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(id, notification);
        }

    }





}
