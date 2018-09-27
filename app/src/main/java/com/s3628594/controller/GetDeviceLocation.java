package com.s3628594.controller;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class GetDeviceLocation implements LocationListener {

    private SuggestTracking suggestTracking;

    public GetDeviceLocation(SuggestTracking suggestTracking) {
        this.suggestTracking = suggestTracking;
    }

    @Override
    public void onLocationChanged(Location location) {
        suggestTracking.setDeviceLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
