package com.example.chillpill_medication_tracker;

import java.util.ArrayList;

public class ReminderItem {

    private ArrayList<Medication> rx;
    private String time;

    public ReminderItem(ArrayList<Medication> listOfItems, String newTime) {
        rx = listOfItems;
        time = newTime;
    }

    public ArrayList<Medication> getMedications() {
        return rx;
    }
    public String getTime() {
        return time;
    }

    public void setRx(ArrayList<Medication> rxList) {
        rx = rxList;
    }

    public void setTime(String newTime) {
        time = newTime;
    }


}
