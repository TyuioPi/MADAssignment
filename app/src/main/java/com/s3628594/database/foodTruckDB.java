package com.s3628594.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.s3628594.model.FoodTruck;
import com.s3628594.model.Tracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class foodTruckDB {


    private static foodTruckDB INSTANCE = null;

    private foodTruckDB() {}

    public static foodTruckDB getSingletonInstance() {
        if (INSTANCE == null) {
            INSTANCE = new foodTruckDB();
        }
        return INSTANCE;
    }

    private static final String DEBUG_TAG = foodTruckDB.class.getName();
    private static final String DATABASE_NAME = "foodtruck.db";
    private static final String TABLE_FOODTRUCK = "tbl_foodtruck";
    private static final String TABLE_TRACKING = "tbl_tracking";

    private SQLiteDatabase mDatabase;

    private static final String CREATE_FOODTRUCK_TABLE = "CREATE TABLE tbl_foodtruck (id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT " +
            ", foodtruckname TEXT, description TEXT, url TEXT, category TEXT);";

    private static final String CREATE_TRACKING_TABLE = "CREATE TABLE tbl_tracking (id INTEGER PRIMARY KEY" +
            " AUTOINCREMENT " +
            ", trackingId TEXT, title TEXT, starttime TEXT, endtime TEXT, meettime TEXT, meetlocation TEXT);";


    public void createdB(ArrayList<FoodTruck> foodtruckList, Context context){
        if (Arrays.binarySearch(context.databaseList(), DATABASE_NAME) >= 0){
            context.deleteDatabase(DATABASE_NAME);
        }
        mDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        mDatabase.setLocale(Locale.getDefault());
        mDatabase.setVersion(1);
        Log.i(DEBUG_TAG, "Created database: " + mDatabase.getPath());
        mDatabase.execSQL(CREATE_FOODTRUCK_TABLE);
        mDatabase.execSQL(CREATE_TRACKING_TABLE);
        for (int i = 0; i < foodtruckList.size(); i++){
            addfoodtruckItems(foodtruckList.get(i));
        }
        Cursor c = mDatabase.query(TABLE_FOODTRUCK, null, null, null, null, null
                , null);
        LogCursorInfo(c);
        c.close();

    }

    private void addfoodtruckItems(FoodTruck foodTruck){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("foodtruckname", foodTruck.getTrackableName());
            values.put("description", foodTruck.getTrackableDesc());
            values.put("url", foodTruck.getTrackableUrl());
            values.put("category", foodTruck.getTrackableCategory());
            mDatabase.insertOrThrow(TABLE_FOODTRUCK, null, values);
            mDatabase.setTransactionSuccessful();
        }catch (Exception e ){
            Log.i(getClass().getName(), "Transaction failed. Exception: " + e.getMessage());
        }
        finally {
            mDatabase.endTransaction();
        }
    }

    public void addItemtoTracking(Tracking tracking){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("trackingId", tracking.getTrackingId());
            values.put("title", tracking.getTitle());
            values.put("starttime", tracking.getStartTime());
            values.put("endtime", tracking.getEndTime());
            values.put("meettime", tracking.getMeetTime());
            values.put("meetlocation",tracking.getMeetLoc());
            mDatabase.insertOrThrow(TABLE_TRACKING, null, values);
            mDatabase.setTransactionSuccessful();
        }catch (Exception e ){
            Log.i(getClass().getName(), "Transaction failed. Exception: " + e.getMessage());
        }
        finally {
            mDatabase.endTransaction();
        }

    }

    public void viewTrackingData(){
        Cursor c = mDatabase.query(TABLE_TRACKING, null, null, null, null, null
                , null);
        LogCursorInfo(c);
        c.close();
    }




    private void LogCursorInfo(Cursor c)
    {
        Log.i(DEBUG_TAG, "*** Cursor Begin *** " + " Results:" + c.getCount()
                + " Columns: " + c.getColumnCount());

        // Print column names
        String rowHeaders = "|| ";
        for (int i = 0; i < c.getColumnCount(); i++)
        {

            rowHeaders = rowHeaders.concat(c.getColumnName(i) + " || ");
        }
        Log.i(DEBUG_TAG, "COLUMNS " + rowHeaders);

        // Print records
        c.moveToFirst();
        while (c.isAfterLast() == false)
        {
            String rowResults = "|| ";
            for (int i = 0; i < c.getColumnCount(); i++)
            {
                rowResults = rowResults.concat(c.getString(i) + " || ");
            }
            Log.i(DEBUG_TAG, "Row " + c.getPosition() + ": " + rowResults);

            c.moveToNext();
        }
        Log.i(DEBUG_TAG, "*** Cursor End ***");
    }





}
