package com.example.chillpill_medication_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReminderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        Intent intent = getIntent();
        String json = intent.getStringExtra("Reminder");

        Gson gson = new Gson();

        Type type = new TypeToken<ReminderItem>() {}.getType();
        ReminderItem reminder;

        reminder = gson.fromJson(json, type);

        String title = intent.getStringExtra("ReminderTitle");

        String time = reminder.getTime();
        ArrayList<Medication> rx = reminder.getMedications();
        ArrayList<String> adaptedRx = new ArrayList<>();
        for(int i = 0; i < rx.size(); i++) {
            Log.d("rx", rx.get(i).getName());
            adaptedRx.add(rx.get(i).getName());
        }

        TextView tTitle = findViewById(R.id.reminder_details_title);
        TextView tTime = findViewById(R.id.reminder_details_time);

        ListView lv = findViewById(R.id.reminder_details_medication);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adaptedRx);
        lv.setAdapter(adapter);

        tTitle.setText(title);
        tTime.setText(time);
    }
}
