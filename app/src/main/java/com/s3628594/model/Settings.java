package com.s3628594.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.s3628594.controller.changeSetting;

public class Settings {

    private static Settings INSTANCE = null;
    public static boolean Notification_turnOn;
    public static String Notification_period;
    public static boolean Reminder_turnOn;
    public static SharedPreferences preferences;

    private Settings(){}

    public static Settings getSingleton(){
        if (INSTANCE == null){
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    public void setPreferences(SharedPreferences preferences) {
        Notification_turnOn = preferences.getBoolean("notification_turnon", false);
        Notification_period = preferences.getString("notification_time", null);
        Reminder_turnOn = preferences.getBoolean("Reminder", false);
        Log.d("sharedPreferences", Boolean.toString(Notification_turnOn));

    }

    public void getPreferences(){
        preferences.registerOnSharedPreferenceChangeListener(new changeSetting());

    }
}
