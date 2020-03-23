package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/****************************************
 * For the reminding notifications the user can specify choose multiple medications
 * for specific times. For example they may have multiple medications to take in the
 * morning, with others in the evening. Grouping medications in this way reduces the
 * number of alerts and notifications to the phone so multiple notifications are
 * received at the same time.
 */


public class RemindersListFragment extends Fragment implements DialogNewReminder.OnInputSelected{

    // The collection of lists of medications, each collection will have it's own
    // notification time

    private ArrayMap reminderList = new ArrayMap();
    private ArrayList<Medication> oneItem = new ArrayList<>();

    private RecyclerView rv;
    private ReminderAdapter reminderAdapter;
    private Button newReminderButton;

    public static final String COLLECTION_LIST = "collection";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reminders_list, container, false);

        loadReminders();

        newReminderButton = view.findViewById(R.id.new_reminder_button);
        rv = view.findViewById(R.id.reminders_list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // ADAPTER FOR COLLECTION OF LISTS
        reminderAdapter = new ReminderAdapter(getActivity(), reminderList);
        rv.setAdapter(reminderAdapter);

        // This will open an activity showing all medications in this list
        reminderAdapter.setOnItemClickListener(new ReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("RemList", "CLICK");
            }

            @Override
            public void OnDeleteClick(int position) {
                Log.d("RemList", "DELETE");
                reminderList.remove(reminderList.keyAt(position));
                reminderAdapter.notifyItemRemoved(position);
                saveData();

                // TODO: ACTUALLY DELETE THE ALERT
                // update stored ids in shared preferences

            }
        });

        newReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogNewReminder dialog = new DialogNewReminder();
                dialog.setTargetFragment(RemindersListFragment.this, 1);
                dialog.show(getFragmentManager(), "reminder_dialog");
            }
        });

        return view;
    }

    // saving the data
    private void saveData() {
        SharedPreferences sp = getActivity().getSharedPreferences(COLLECTION_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(reminderList);
        editor.putString("reminders_list", json);
        editor.apply();
    }

    // loading the data
    private void loadReminders() {
        SharedPreferences sp = this.getActivity().getSharedPreferences(COLLECTION_LIST, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("reminders_list", null);
        Type type = new TypeToken<ArrayMap<String, ReminderItem>>() {}.getType();

        reminderList = gson.fromJson(json, type);

        if (reminderList == null) {
            reminderList = new ArrayMap();
        }
    }

    @Override
    public void sendInput(String title, ReminderItem newReminder) {

        // check if a reminder with the same name exists
        if(reminderList.indexOfKey(title) > -1) {
            Toast.makeText(getContext(), title + " already exists", Toast.LENGTH_LONG).show();
        } else {
            reminderList.put(title, newReminder);
            reminderAdapter.notifyItemInserted(reminderList.indexOfKey(title));
            saveData();
        }
    }
}
