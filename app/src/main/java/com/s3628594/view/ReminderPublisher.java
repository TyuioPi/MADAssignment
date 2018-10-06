package com.s3628594.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ReminderPublisher extends BroadcastReceiver {

    //ToDo : run test for reminder

    public static String id = "Reminder_channel";
    public static String notificationid = "ReminderId";
    public static String Reminder = "reminder";
    private String title = "ReminderNotification";
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("reminder", "set");
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
            Notification notification = intent.getParcelableExtra(Reminder);
            int id = intent.getIntExtra(notificationid, 0);
            notificationManager.notify(id, notification);
            Log.d("notify", "hi");
        }else{
            Notification notification = intent.getParcelableExtra(Reminder);
            int id = intent.getIntExtra(notificationid, 0);
            notificationManager.notify(id, notification);
        }
    }
}
