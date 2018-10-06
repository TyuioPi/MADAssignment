package com.s3628594.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.s3628594.model.Tracking;
import com.s3628594.model.TrackingService;
import com.s3628594.view.MapsActivity;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DisplayRouteInfo implements AdapterView.OnItemClickListener {

    private Activity context;

    public DisplayRouteInfo(Activity context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final String LOG_TAG = DisplayRouteInfo.class.getName();
        Tracking tracking = (Tracking) adapterView.getAdapter().getItem(i);
        Toast.makeText(context, tracking.getRouteInfo().toString(), Toast.LENGTH_LONG).show();
        ArrayList<String> routeInfoList = new ArrayList<>();

        // Get route information of clicked tracking
        for (TrackingService.TrackingInfo routeInfo : tracking.getRouteInfo()) {
            Log.i(LOG_TAG, routeInfo.toString());
            Date fixedRouteDate = correctDateFormat(routeInfo.date);
            String route = String.format("%s,%s,%s,%s",fixedRouteDate, routeInfo.latitude, routeInfo.longitude, routeInfo.stopTime);
            routeInfoList.add(route);
        }


        Intent intent = new Intent(context, MapsActivity.class);

        // Pass data through intent
        intent.putStringArrayListExtra("routeInfoList", routeInfoList);

        context.startActivity(intent);
    }

    private Date correctDateFormat(Date date) {
        DateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        DateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat temp = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());

        // Switch day and month to correct format
        String day = monthFormat.format(date);
        String month = dayFormat.format(date);

        // Convert month int to string name
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        String fixedMonth = months[Integer.parseInt(month) - 1];

        // Correct the day and month
        StringBuilder stringBuilder = new StringBuilder(date.toString());
        stringBuilder.replace(4,7, fixedMonth.substring(0,3));
        stringBuilder.replace(8,10, day);

        // Correct the date
        String tempDate = stringBuilder.substring(4);
        Date fixedDate = null;

        try {
            fixedDate = temp.parse(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fixedDate;
    }
}
