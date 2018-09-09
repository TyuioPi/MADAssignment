package com.s3628594.controller;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.s3628594.view.TimeDialog;

public class CreateTimeDialog implements View.OnClickListener {

    private EditText timeEdit;
    private Context context;

    public CreateTimeDialog(EditText timeEdit, Context context) {
        this.timeEdit = timeEdit;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        TimeDialog timeDialog = new TimeDialog(timeEdit, context);
        timeDialog.createTimeDialog();
    }
}
