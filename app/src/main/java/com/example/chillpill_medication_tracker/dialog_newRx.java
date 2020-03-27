package com.example.chillpill_medication_tracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class dialog_newRx extends DialogFragment {

    private EditText rx_name;
    private EditText rx_instructions;

    public interface OnInputSelected {
        void sendInput(String name, String instructions);
    }
    private OnInputSelected inputSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_prescription, container, false);
        rx_name = view.findViewById(R.id.dialog_rx_name);
        rx_instructions = view.findViewById(R.id.dialog_rx_instructions);
        Button ok_button = view.findViewById(R.id.dialog_add_rx);
        Button cancel_button = view.findViewById(R.id.dialog_cancel_rx);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dialog", "closing dialog");
                getDialog().dismiss();
            }
        });
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dialog", "ok button");
                String newRxName = rx_name.getText().toString();
                String newRxInstructions = rx_instructions.getText().toString();
                Log.d("new data", newRxName + ": " + newRxInstructions );
                if(!newRxName.equals("") && !newRxInstructions.equals("")) {
                    inputSelected.sendInput(newRxName, newRxInstructions);
                }
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            inputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e) {
            Log.e("error", "onAttach: ClassCastException" + e.getMessage());
        }
    }
}
