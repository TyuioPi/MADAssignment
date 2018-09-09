package com.s3628594.view;

import android.support.v7.app.AlertDialog;

import com.s3628594.controller.FilterByCategory;
import com.s3628594.controller.RemoveFilter;
import com.s3628594.controller.SetCategoryFilter;
import com.s3628594.geotracking.R;
import com.s3628594.model.TrackableImplementation;

import java.util.ArrayList;

public class CategoryFilter {

    private HomeActivity homeActivity;
    private TrackableTab trackableTab;
    private String[] categoryFilter;

    public CategoryFilter(HomeActivity homeActivity, TrackableTab trackableTab) {
        this.homeActivity = homeActivity;
        this.trackableTab = trackableTab;
    }

    public void showCategoryDialog() {
        ArrayList mSelectedCategories = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
        builder.setTitle(R.string.dialog_title_1)
                .setMultiChoiceItems(categoryFilter, null, new SetCategoryFilter(mSelectedCategories))
                .setPositiveButton(R.string.dialog_selection_add, new FilterByCategory(mSelectedCategories, categoryFilter, trackableTab))
                .setNegativeButton(R.string.cancel, new RemoveFilter(trackableTab));
        builder.show();
    }

    public void populateCategoryFilter() {
        categoryFilter = new String[TrackableImplementation.getSingletonInstance().getCategoryList().size()];

        for (int i = 0; i < TrackableImplementation.getSingletonInstance().getCategoryList().size(); i++) {
            categoryFilter[i] = TrackableImplementation.getSingletonInstance().getCategoryList().get(i);
        }
    }
}
