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

import java.sql.DatabaseMetaData;
import java.util.Calendar;

public class BootReceiverActivity extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //Accès à la base de données
        NotificationDatabaseManager notificationDatabaseManager = new NotificationDatabaseManager(context);
        notificationDatabaseManager.open();

        //Initialisation des notifications au redémarrage de l'appareil
        for(int i = 1; i <= 101; i++) {
            Notification notification = notificationDatabaseManager.getNotification(i);
            if(notification.getActive() == 1) {
                NotificationManager.scheduleNotification(context, i, notification.getContent());
            }
        }

        notificationDatabaseManager.close();
    }

}
