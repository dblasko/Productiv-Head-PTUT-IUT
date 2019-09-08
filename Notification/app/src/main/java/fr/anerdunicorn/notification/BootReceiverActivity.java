package fr.anerdunicorn.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Calendar;

public class BootReceiverActivity extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //Initialisation des SharedPreferences
        SharedPreferences settings = context.getSharedPreferences("notification", 0);

        //Initialisation des notifications au redémarrage de l'appareil
        for(int i = 1; i <= 101; i++) {
            if(settings.getBoolean("alarm" + i, false)) {
                NotificationManager.scheduleNotification(context, i, settings.getString("notificationContent" + i, ""));
            }
        }
    }

}
