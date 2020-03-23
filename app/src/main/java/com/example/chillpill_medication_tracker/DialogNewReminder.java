package com.example.chillpill_medication_tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DialogNewReminder extends DialogFragment{
    /**
     * Not sure if this actually works yet, we don't have it inflating yet.
     */
    TextView time;
    TextView textHour;
    TextView textMinute;
    TextView textAmpm;

    String nHour;
    String nMinute;
    String nAmpm;

    // 50 medications should be more than enough
    int alarmIds[] = new int[50];
    ArrayList<Medication> rx_list;
    ArrayList<String> title_list = new ArrayList<>();
    ReminderItem newReminder;

    public interface OnInputSelected {
        void sendInput(String title, ReminderItem newReminder);
    }
    public OnInputSelected inputSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_reminder, container, false);
        Button cancel_button = view.findViewById(R.id.dialog_cancel_reminder);
        Button add_reminder_button = view.findViewById(R.id.dialog_add_reminder);
        final EditText reminderName = view.findViewById(R.id.dialog_reminder_title);

        loadCurrentRx();

        final ListView check_list = view.findViewById(R.id.pick_rx_list);
        check_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final ArrayAdapter<String> clAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_multiple_choice, title_list);
        check_list.setAdapter(clAdapter);

        Spinner hours = view.findViewById(R.id.spinner_hour);
        Spinner minutes = view.findViewById(R.id.spinner_minute);
        Spinner ampm = view.findViewById(R.id.spinner_ampm);

        ArrayAdapter<CharSequence> hour_adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> minute_adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.minutes, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> ampm_adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.ampm, android.R.layout.simple_spinner_item);

        hour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minute_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ampm_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        textHour = view.findViewById(R.id.reminder_hour);
        textMinute = view.findViewById(R.id.reminder_minute);
        textAmpm = view.findViewById(R.id.reminder_ampm);

        hours.setAdapter(hour_adapter);
        hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nHour = parent.getItemAtPosition(position).toString();
                textHour.setText(nHour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        minutes.setAdapter(minute_adapter);
        minutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nMinute  = parent.getItemAtPosition(position).toString();
                textMinute.setText(nMinute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ampm.setAdapter(ampm_adapter);
        ampm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nAmpm = parent.getItemAtPosition(position).toString();
                textAmpm.setText(nAmpm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        add_reminder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the time
                int newHour = Integer.parseInt(textHour.getText().toString());
                int newMinute = Integer.parseInt(textMinute.getText().toString());
                String newAmpm = textAmpm.getText().toString();
                String newTime = textHour.getText().toString() + ":" +
                                 textMinute.getText().toString() + " " +
                                 textAmpm.getText().toString();
                // get the name of the reminder
                String newTitle = reminderName.getText().toString();
                // get the list of medications
                SparseBooleanArray checked = check_list.getCheckedItemPositions();
                ArrayList<Medication> checked_rx = new ArrayList<>();
                for(int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);

                    if(checked.valueAt(i)) {
                        checked_rx.add(rx_list.get(position));
                    }

                }

                // generate new alarm ID
                // create random number generator, save numbers in an array to avoid duplicating IDs
                Random rand = new Random();

                int newAlarmId = 0;
                // make sure we don't add a number that already exists
                Boolean found = false;
                while(found == false && newAlarmId == 0) {

                    // TODO: CHECK FOR REPEATING VALUE
                }

                // save the array in sharedPreferences
                // create the new reminder object
                newReminder = new ReminderItem(checked_rx, newTime);
                if(newReminder.getMedications().size() != 0 && !newTitle.equals("")) {
                    inputSelected.sendInput(newTitle, newReminder);

                    // create a unique request code
                    startAlarm(newHour, newMinute, newAmpm);
                }

                getDialog().dismiss();

            }
        });
        return view;
    }

    private void startAlarm(int hour, int min, String ampm) {
        // min = 00, the value will actually 0
        Log.d("Time", hour + ":" + min + ampm);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);

        Calendar c = Calendar.getInstance();
        int numAmPm = ampm == "AM" ? 0 : 1;
        if(numAmPm == 1) {
            hour = hour + 12;
        }
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    // get the list of all of the current prescriptions to add to the reminder
    private void loadCurrentRx(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("new_rx", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("current_rx_list", null);
        Type type = new TypeToken<ArrayList<Medication>>() {}.getType();
        rx_list = gson.fromJson(json, type);

        for(int i = 0; i < rx_list.size(); i++) {
            String name = rx_list.get(i).getName();
            title_list.add(name);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            inputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("error", "onAttach: class cast exception" + e.getMessage());
        }
    }
}
