package com.example.chillpill_medication_tracker;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL = "reminder";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }
    public void createChannel() {
        // Check if API level in 26 or greater
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL,
                    "Rx Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Take your medicine");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
