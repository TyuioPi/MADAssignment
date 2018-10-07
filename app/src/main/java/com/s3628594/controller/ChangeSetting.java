package com.s3628594.controller;

import android.content.SharedPreferences;

import com.s3628594.model.PreferenceSettings;

public class ChangeSetting implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceSettings.getSingleton().setPreferences(sharedPreferences);
    }
}
