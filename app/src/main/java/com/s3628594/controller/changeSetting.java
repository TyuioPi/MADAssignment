package com.s3628594.controller;

import android.content.SharedPreferences;
import android.util.Log;

import com.s3628594.model.Settings;

public class changeSetting implements SharedPreferences.OnSharedPreferenceChangeListener {

    //ToDO : implement the onSharedPreferenceChange and create a notification model to save all information

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("listener ", "set");
        Settings.getSingleton().setPreferences(sharedPreferences);

    }
}
