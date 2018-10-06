package com.s3628594.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.s3628594.controller.Close;
import com.s3628594.controller.CreateDateDialog;
import com.s3628594.controller.CreateTimeDialog;
import com.s3628594.controller.EditTracking;
import com.s3628594.controller.RemoveTracking;
import com.s3628594.geotracking.R;

public class EditTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracking);

        // Retrieve passed data through intent
        Intent intent = getIntent();
        String trackingId = intent.getStringExtra("trackingId");
        String trackingStartTime = intent.getStringExtra("trackingStartTime");
        String trackingEndTime = intent.getStringExtra("trackingEndTime");

        // Set up title
        EditText titleEdit = findViewById(R.id.edit_title);

        // Set up date dialog
        EditText meetDateEdit = findViewById(R.id.edit_meet_date);
        meetDateEdit.setInputType(InputType.TYPE_NULL);
        meetDateEdit.setOnClickListener(new CreateDateDialog(meetDateEdit, this));

        // Set up time dialog
        EditText meetTimeEdit = findViewById(R.id.edit_meet_time);
        meetTimeEdit.setInputType(InputType.TYPE_NULL);
        meetTimeEdit.setOnClickListener(new CreateTimeDialog(meetTimeEdit, this));

        // Set up buttons
        Button edit = findViewById(R.id.confirm_edit);
        edit.setOnClickListener(new EditTracking(this, titleEdit, meetDateEdit, meetTimeEdit,
                                                  trackingId, trackingStartTime, trackingEndTime));
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new Close(this));

        Button remove = findViewById(R.id.remove);
        remove.setOnClickListener(new RemoveTracking(this, trackingId));
    }
}
