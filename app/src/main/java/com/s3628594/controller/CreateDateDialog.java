package com.s3628594.controller;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.s3628594.view.DateDialog;

public class CreateDateDialog implements View.OnClickListener{

    private Context context;
    private EditText dateEdit;

    public CreateDateDialog(EditText dateEdit, Context context) {
        this.dateEdit = dateEdit;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        DateDialog dateDialog = new DateDialog(context, dateEdit);
        dateDialog.createDateDialog();
    }
}
