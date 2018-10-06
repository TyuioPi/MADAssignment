package com.s3628594.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import com.s3628594.controller.SetDate;

import java.util.Calendar;

public class DateDialog {

    private Context context;
    private EditText dateEdit;

    public DateDialog(Context context, EditText dateEdit) {
        this.context = context;
        this.dateEdit = dateEdit;
    }

    public void createDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(context, new SetDate(dateEdit), year, month, day);
        datePicker.show();
    }
}
