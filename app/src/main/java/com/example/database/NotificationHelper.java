package com.example.database;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channel_ID = "channel_ID", channel2_ID = "channel2_ID";
    public static final String channel_name = "channel_name", channel2_name = "channel2_name";

    private NotificationManager manager;

    public NotificationHelper(Context base){
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannels();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channel_ID, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);

        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(channel2_ID, channel2_name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);

        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    //powiadomienia dotyczące godzin przyjmowania leków
    public NotificationCompat.Builder getChannel1Notification(String title, String message, String ktoryLek, String zmniejszZapas){
        Intent intent = new Intent(this, Dzisiaj.class);
        Bundle b = new Bundle();
        b.putString("nazwa", title);
        b.putString("kLek", ktoryLek);
        b.putString("zZapas", zmniejszZapas);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 201, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channel_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification_foreground)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    //powiadomienia dotyczące kończącego się zapasu
    public NotificationCompat.Builder getChannel2Notification(String nazwa, String zapas){
        return new NotificationCompat.Builder(getApplicationContext(), channel2_ID)
                .setContentTitle("Uwaga!")
                .setContentText("Zapas leku " + nazwa + " wynosi " + zapas + "!")
                .setSmallIcon(R.drawable.ic_notification_foreground);
    }

}
