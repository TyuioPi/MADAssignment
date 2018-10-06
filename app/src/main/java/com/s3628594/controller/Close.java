package com.s3628594.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

public class Close implements DialogInterface.OnClickListener, View.OnClickListener{

    private Activity activity;

    public Close(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {}

    @Override
    public void onClick(View view) {
        activity.finish();
    }
}
