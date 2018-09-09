package com.s3628594.controller;

import android.content.DialogInterface;

import java.util.ArrayList;

public class SetCategoryFilter implements DialogInterface.OnMultiChoiceClickListener {

    private ArrayList mSelectedCategories;

    public SetCategoryFilter(ArrayList mSelectedCategories) {
        this.mSelectedCategories = mSelectedCategories;
    }

    // Add checked items to our list otherwise remove it
    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
        if (isChecked) {
            mSelectedCategories.add(i);
        } else if (mSelectedCategories.contains(i)) {
            mSelectedCategories.remove(Integer.valueOf(i));
        }
    }
}
