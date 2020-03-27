package com.example.chillpill_medication_tracker;

import java.util.ArrayList;

public class ReminderItem {

    private ArrayList<Medication> rx;
    private String time;
    private int alarmId;

    public ReminderItem(ArrayList<Medication> listOfItems, String newTime, int newId) {
        rx = listOfItems;
        time = newTime;
        alarmId = newId;
    }

    public ArrayList<Medication> getMedications() {
        return rx;
    }
    public String getTime() {
        return time;
    }
    public int getAlarmId() { return alarmId; }

}
