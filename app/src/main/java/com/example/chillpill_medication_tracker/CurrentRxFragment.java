package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CurrentRxFragment extends Fragment implements dialog_newRx.OnInputSelected {
    private ArrayList<Medication> medicationList;

    private RecyclerView rv;
    private RxAdapter mAdapter;

    private static final String MEDICATION = "new_rx";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_rx, container, false);
        Button button = view.findViewById(R.id.new_rx_button);
        // Get the prescriptions from shared preferences
        loadCurrentRx();

        rv = view.findViewById(R.id.current_rx_list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // tie the list of prescriptions to the recycler view
        mAdapter = new RxAdapter(getActivity(), medicationList);
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // this opens a new fragment to show details on the selected rx
                Intent intent = new Intent(getActivity(), Rx_Details.class);
                intent.putExtra("Rx", (Parcelable) medicationList.get(position));
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                Medication removedRx = medicationList.get(position);
                // removedRx needs to be sent to old prescriptions
                medicationList.remove(position);
                mAdapter.notifyItemRemoved(position);
                // update sharedpreferences
                saveData();
                addToPastRx(removedRx);
            }
        });

        // open dialog to add new prescription
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_newRx dialog = new dialog_newRx();
                dialog.setTargetFragment(CurrentRxFragment.this, 1);
                dialog.show(getFragmentManager(), "dialog_newRx");
            }
        });

        return view;
    }

    // this is how we get the information from the dialog
    @Override
    public void sendInput(String name, String instructions) {
        Medication newRx = new Medication(name, instructions);
        medicationList.add(medicationList.size(), newRx);
        // update the list with the newly added medication
        mAdapter.notifyItemInserted(medicationList.size());
        saveData();
    }

    private void saveData() {
        SharedPreferences sp = getActivity().getSharedPreferences(MEDICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(medicationList);
        editor.putString("current_rx_list", json);
        editor.apply();
    }
    private void loadCurrentRx(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MEDICATION, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("current_rx_list", null);
        Type type = new TypeToken<ArrayList<Medication>>() {}.getType();
        medicationList = gson.fromJson(json, type);

        if (medicationList == null) {
            medicationList = new ArrayList<Medication>();
        }

    }

    // This is to move prescriptions the user doesn't need any more to a list
    // where they can see what they used to take
    private void addToPastRx(Medication removedRx) {
        ArrayList<Medication> pastRxList = new ArrayList<>();

        // get what is already stored in shared preferences
        SharedPreferences sp = getActivity().getSharedPreferences("past_rx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();

        //try and get the past_rx_list
        String presentPastRxJson = sp.getString("past_rx_list", null);
        Type type = new TypeToken<ArrayList<Medication>>() {}.getType();

        if(presentPastRxJson != null) {
            // it does already exist so we need to append the new removedRx to it
            pastRxList = gson.fromJson(presentPastRxJson, type);
            pastRxList.add(removedRx);

            String updatedList = gson.toJson(pastRxList);
            editor.putString("past_rx_list", updatedList);
            editor.apply();
        } else {
            pastRxList.add(removedRx);
            presentPastRxJson = gson.toJson(pastRxList);
            editor.putString("past_rx_list", presentPastRxJson);
            editor.apply();
        }
    }
}
