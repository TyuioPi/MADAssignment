package com.s3628594.model;

import android.content.SharedPreferences;

public class Settings {

    private static Settings INSTANCE = null;

    public SharedPreferences preferences;

    private Settings(){}

    public static Settings getSingleton(){
        if (INSTANCE == null){
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }
}
