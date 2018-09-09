package com.s3628594.model;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

// Creates our fragments (Used for our tabs)
public class SectionsPageAdapter extends FragmentPagerAdapter {

    // Private variables
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    // Create the fragment
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    // Return a fragment
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    // Return amount of fragments
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    // Return title of fragment
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
