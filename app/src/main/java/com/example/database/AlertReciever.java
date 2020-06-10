package com.example.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReciever extends BroadcastReceiver {

    // klasa odpowiadająca za wysłanie powiadomienia o konkretnej godzinie
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("nazwaLeku");
        String ktoryLek = intent.getStringExtra("ktoryLek1");
        String zmniejszZapas = intent.getStringExtra("zmniejszZapas");
        NotificationHelper notification = new NotificationHelper(context);
        NotificationCompat.Builder nb = notification.getChannel1Notification(title, "Przypominamy o wzięciu leku.",  ktoryLek, zmniejszZapas);
        notification.getManager().notify(1, nb.build());
    }
}

