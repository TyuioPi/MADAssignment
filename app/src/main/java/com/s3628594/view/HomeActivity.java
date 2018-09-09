package com.s3628594.view;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.s3628594.geotracking.R;
import com.s3628594.model.SectionsPageAdapter;
import com.s3628594.model.FileLoader;

public class HomeActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private CategoryFilter categoryFilter;
    private FileLoader fileLoader = new FileLoader();
    private TrackableTab trackableTab = new TrackableTab();
    private TrackingTab trackingTab = new TrackingTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections
        ViewPager mViewPager = findViewById(R.id.container);
        setViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Loads food_truck_data.txt and tracking_data.txt
        fileLoader.loadFoodTruckFile(this);

        // Set up Filters
        categoryFilter = new CategoryFilter(this, trackableTab);
        categoryFilter.populateCategoryFilter();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
