package com.example.chillpill_medication_tracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Medication implements Parcelable, Serializable {
    private String name, instructions;
    // private String description;
//    private int quantity;
//    private int image;
    public Medication () {
        name = "";
        instructions = "";
//        description = "";
//        quantity = -1;
    }
    public Medication (String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    protected Medication(Parcel in) {
        name = in.readString();
        instructions = in.readString();
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(instructions);
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public int getImage() {
//        return image;
//    }
}
