package com.example.chillpill_medication_tracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import static com.example.chillpill_medication_tracker.App.CHANNEL;

public class CareProvidersFragment extends Fragment {

    private NotificationManagerCompat notificationManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_care_providers, container, false);
        Button alertButton = view.findViewById(R.id.alert_button);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert(view);
            }
        });


        notificationManager = NotificationManagerCompat.from(this.getContext());
        return view;
    }

    public void sendAlert(View v) {
        Notification notification = new NotificationCompat.Builder(this.getContext(), App.CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alert Title")
                .setContentText("Alert Content")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(1, notification);
    }
}
