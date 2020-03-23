package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PastRxFragment extends Fragment{

    private ArrayList<Medication> pastRxList;

    private RecyclerView rv;
    private RxAdapter mAdapter;

    public static final String PASTRX = "past_rx";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_rx, container, false);
        loadPastRx();

        rv = view.findViewById(R.id.past_rx_list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RxAdapter(getActivity(), pastRxList);
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), Rx_Details.class);
                intent.putExtra("PastRx", (Parcelable) pastRxList.get(position));
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                pastRxList.remove(position);
                mAdapter.notifyItemRemoved(position);
                saveData();
            }
        });
        return view;
    }

    private void saveData() {
        SharedPreferences sp = getActivity().getSharedPreferences(PASTRX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pastRxList);
        editor.putString("past_rx_list", json);
        editor.apply();
    }
    private void loadPastRx() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(PASTRX, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("past_rx_list", null);
        Type type = new TypeToken<ArrayList<Medication>>() {}.getType();

        if (json == null) {
            pastRxList = new ArrayList<Medication>();
        } else {
            pastRxList = gson.fromJson(json, type);
        }
    }
}
