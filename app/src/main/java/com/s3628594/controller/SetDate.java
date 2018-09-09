package com.s3628594.controller;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Locale;

public class SetDate implements DatePickerDialog.OnDateSetListener {

    private EditText dateEdit;

    public SetDate(EditText dateEdit) {
        this.dateEdit = dateEdit;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dateEdit.setText(String.format(Locale.getDefault(),"%02d/%02d/%d", day, month + 1, year));
    }
}
