package com.s3628594.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.s3628594.model.Suggestions;

public class RequestLocation extends Thread {

    private static RequestLocation INSTANCE = null;
    public static Location deviceLocation;
    private  Context context;

    private RequestLocation(){ }

    public static RequestLocation getSingletonInstance(){
        if (INSTANCE == null){
            INSTANCE = new RequestLocation();
        }
        return INSTANCE;
    }

    public void getLocation(Context context){
        this.context = context;
        LocationUpdate update = new LocationUpdate();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = manager.getBestProvider(criteria, true);
            manager.requestSingleUpdate(provider,update, null);
            if (deviceLocation != null){
                manager.removeUpdates(update);
            }
        } else {
            Toast.makeText(context, "To use this service, enable location permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private class LocationUpdate implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            RequestLocation.deviceLocation = location;
            Suggestions.getSingletonInstance().getSuggestionList(context);
            SuggestNotification.getSingletonInstance().ScheduleNotification(context, 30000);
            Log.d("location", deviceLocation.toString());
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
