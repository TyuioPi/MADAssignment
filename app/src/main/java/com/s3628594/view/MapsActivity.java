package com.s3628594.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.s3628594.geotracking.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String LOG_TAG = MapsActivity.class.getName();
    private GoogleMap mMap;
    private PolylineOptions polylineOptions = new PolylineOptions();
    private Date currentTime = Calendar.getInstance().getTime();
    private Date trackingTime;
    private Boolean trackingExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (isServicesOk()) {
            mapFragment.getMapAsync(this);
        }
    }

    // Check if device has google play services
    public boolean isServicesOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(LOG_TAG, "GooglePlayServices is available");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(LOG_TAG, "GooglePlayServices is unavailable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001);
            dialog.show();
        } else {
            Toast.makeText(this, "Failed request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_NORMAL);
        setMapPoints();
        Polyline polyline = mMap.addPolyline(polylineOptions);
        polyline.setWidth(3);
        polyline.setColor(Color.BLUE);
    }

    private void setMapPoints() {
        LatLng point = null, previousPoint;

        // Retrieve passed data through intent
        Intent intent = getIntent();
        ArrayList<String> routeInfoList = intent.getStringArrayListExtra("routeInfoList");

        // Extract the route information of the tracking and plot markers and draw route
        for (int i = 0; i < routeInfoList.size(); i++) {
            String[] route = routeInfoList.get(i).split(",");
            Date previousTrackingTime = trackingTime;
            trackingTime = convertStringToDate(route[0]);
            Double latitude = Double.parseDouble(route[1]);
            Double longitude = Double.parseDouble(route[2]);
            int stopTime = Integer.parseInt(route[3]);
            previousPoint = point;
            point = new LatLng(latitude, longitude);
            polylineOptions.add(point);

            if (i == routeInfoList.size() - 1) {
                setMeetLocTracking(point, trackingTime, stopTime);
            }else if (i < routeInfoList.size() -1){
                setwaypoints(point, trackingTime);
            }else {
                setCurrLocTracking(previousPoint, point, previousTrackingTime, trackingTime);
            }
        }
    }

    // Plot a marker on map for the current location of the tracking
    private void setCurrLocTracking(LatLng previousPoint, LatLng newPoint, Date previousTrackingTime, Date trackingTime) {
        if (previousPoint != null && newPoint != null) {
            if (currentTime.after(previousTrackingTime) && currentTime.before(trackingTime)) {
                mMap.addMarker(new MarkerOptions().position(previousPoint).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(previousPoint, 16));
                trackingExist = true;
            } else if (currentTime.compareTo(previousTrackingTime) == 0) {
                mMap.addMarker(new MarkerOptions().position(previousPoint).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(previousPoint, 16));
                trackingExist = true;
            } else if (currentTime.compareTo(trackingTime) == 0) {
                mMap.addMarker(new MarkerOptions().position(newPoint).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
                trackingExist = true;
            }
        } else if (previousPoint == null && newPoint != null) {
            if (currentTime.compareTo(trackingTime) == 0){
                mMap.addMarker(new MarkerOptions().position(newPoint).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
                trackingExist = true;
            }
        }
    }

    // Plot a marker on map for the meet location of the tracking
    private void setMeetLocTracking(LatLng newPoint, Date trackingTime, int stopTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trackingTime);
        calendar.add(Calendar.MINUTE, stopTime);
        Date trackingEndTime = calendar.getTime();

        if (currentTime.after(trackingTime) && currentTime.before(trackingEndTime)) {
            mMap.addMarker(new MarkerOptions().position(newPoint).title("Tracking At Meet Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
        } else {
            mMap.addMarker(new MarkerOptions().position(newPoint).title("Meet Location"));
            if (!trackingExist) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 16));
            }
        }
    }

    private void setwaypoints(LatLng newPoint, Date trackingTime){
        mMap.addCircle(new CircleOptions()
                .center(newPoint)
                .radius(10)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
    }



    private Date convertStringToDate(String date) {
        Date convertedDate = null;
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
