package com.s3628594.controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.s3628594.model.HTTPRequest;

public class SuggestTracking implements View.OnClickListener {

    private static final String LOG_TAG = SuggestTracking.class.getName();
    private Activity context;
    private Location myLocation;

    public SuggestTracking(Activity context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        String myLat = null;
        String myLng = null;

        // TODO: Fix location coordinates returning null
        // User has provided location permissions to allow for tracking suggestions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                myLat = Double.toString(myLocation.getLatitude());
                myLng = Double.toString(myLocation.getLongitude());
            } catch (SecurityException e) {
                Log.e(LOG_TAG, "getDeviceLocation Security Exception: " + e.getMessage());
            }
            HTTPRequest httpRequest = new HTTPRequest(context, myLat, myLng, "-37.820666", "144.958277");
            httpRequest.printStuff();
        }
    }
}
