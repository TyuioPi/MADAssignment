package com.s3628594.controller;

import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Locale;

public class SetTime implements TimePickerDialog.OnTimeSetListener {

    private EditText timeEdit;

    public SetTime(EditText timeEdit) {
        this.timeEdit = timeEdit;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
        int maxHour = 12;

        if (selectedHour > maxHour) {
            int hour = selectedHour - maxHour;
            timeEdit.setText(String.format(Locale.getDefault(),"%d:%02d PM", hour, selectedMin));
        } else if (selectedHour == maxHour) {
            timeEdit.setText(String.format(Locale.getDefault(),"%d:%02d PM", selectedHour, selectedMin));
        } else {
            timeEdit.setText(String.format(Locale.getDefault(),"%d:%02d AM", selectedHour, selectedMin));
        }
    }
}
