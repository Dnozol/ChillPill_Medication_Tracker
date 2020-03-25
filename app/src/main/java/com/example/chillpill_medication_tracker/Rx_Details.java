package com.example.chillpill_medication_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Rx_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_details);

        Intent intent = getIntent();
        Medication rx = intent.getParcelableExtra("Rx");

        String name = rx.getName();
        String instruct = rx.getInstructions();

        TextView t1 = findViewById(R.id.details_name);
        TextView t2 = findViewById(R.id.details_instructions);

        t1.setText(name);
        t2.setText(instruct);
    }
}
