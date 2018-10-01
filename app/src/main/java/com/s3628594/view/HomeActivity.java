package com.s3628594.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.s3628594.controller.SuggestNotification;
import com.s3628594.controller.changeSetting;
import com.s3628594.geotracking.R;
import com.s3628594.model.SectionsPageAdapter;
import com.s3628594.model.FileLoader;
import com.s3628594.model.Settings;

public class HomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private SectionsPageAdapter mSectionsPageAdapter;
    private CategoryFilter categoryFilter;
    private FileLoader fileLoader = new FileLoader();
    private TrackableTab trackableTab = new TrackableTab();
    private TrackingTab trackingTab = new TrackingTab();
    private Context context = this;
    private SuggestNotification notification = new SuggestNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(new changeSetting());
        Settings.getSingleton().setPreferences(preferences);
        notification.ScheduleNotification(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections
        ViewPager mViewPager = findViewById(R.id.container);
        setViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Loads food_truck_data.txt and tracking_data.txt
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileLoader.loadFoodTruckFile(context);
            }
        }).start();

        // Set up Filters
        categoryFilter = new CategoryFilter(this, trackableTab);
        categoryFilter.populateCategoryFilter();

        // Ask user to provide location permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void setViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(trackableTab, getString(R.string.tab_text_1));
        mSectionsPageAdapter.addFragment(trackingTab, getString(R.string.tab_text_2));
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    // Create our options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Show view for selected option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_action_1) {
            categoryFilter.showCategoryDialog();
            return true;
        }else if (id == R.id.setting){
            Intent i = new Intent(this, SettingView.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
