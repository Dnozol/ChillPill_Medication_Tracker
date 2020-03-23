package com.example.chillpill_medication_tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper helper = new NotificationHelper(context);
        NotificationCompat.Builder nb = helper.getChannelNotification();
        helper.getManager().notify(1, nb.build());
    }
}
