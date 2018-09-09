package com.s3628594.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import com.s3628594.controller.SetTime;

import java.util.Calendar;

public class TimeDialog {

    private EditText timeEdit;
    private Context context;

    public TimeDialog(EditText timeEdit, Context context) {
        this.timeEdit = timeEdit;
        this.context = context;
    }

    public void createTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(context, new SetTime(timeEdit), hour, min, false);
        timePicker.show();
    }
}
