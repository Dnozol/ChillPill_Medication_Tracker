package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class CurrentRxFragment extends Fragment implements dialog_newRx.OnInputSelected {
    private ArrayList<Medication> medicationList;

    private RecyclerView rv;
    private RxAdapter mAdapter;
    private RecyclerView.LayoutManager rvlm;
    private Button button;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String MEDICATION = "new_rx";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_rx, container, false);
        button = view.findViewById(R.id.new_rx_button);
        loadCurrentRx();

        rv = (RecyclerView) view.findViewById(R.id.current_rx_list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAdapter = new RxAdapter(getActivity(), medicationList);
        rv.setAdapter(mAdapter);
        //rv.setHasFixedSize(true);


        mAdapter.setOnItemClickListener(new RxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // this should open a new fragment to show details on the selected rx
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
                saveData();
                addToPastRx(removedRx);
            }
        });

        //dialog box method
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "opening dialog");
                dialog_newRx dialog = new dialog_newRx();
                dialog.setTargetFragment(CurrentRxFragment.this, 1);
                dialog.show(getFragmentManager(), "dialog_newRx");
            }
        });

        return view;
    }

    @Override
    public void sendInput(String name, String instructions) {
        Log.d("sendInput", "in send input" + name + " " + instructions);
        Medication newRx = new Medication(name, instructions);
        medicationList.add(medicationList.size(), newRx);
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
    private void addToPastRx(Medication removedRx) {
        Log.d("sendToPastRx", "Sending...");

        ArrayList<Medication> pastRxList = new ArrayList<>();

        // get what is already stored in shared preferences
        SharedPreferences sp = getActivity().getSharedPreferences("past_rx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();

        //try and get the past_rx_list
        String presentPastRxJson = sp.getString("past_rx_list", null);
        Type type = new TypeToken<ArrayList<Medication>>() {}.getType();

        if(presentPastRxJson != null) {
            Log.d("presentPastRxJson", presentPastRxJson);
            // it does already exist so we need to append the new removedRx to it

            pastRxList = gson.fromJson(presentPastRxJson, type);
            pastRxList.add(removedRx);


            String updatedList = gson.toJson(pastRxList);
            editor.putString("past_rx_list", updatedList);
            editor.apply();


        }else {
            Log.d("presentPastRxJson", "NULL");
            pastRxList.add(removedRx);
            presentPastRxJson = gson.toJson(pastRxList);
            editor.putString("past_rx_list", presentPastRxJson);
            editor.apply();
        }
    }
}
