package com.example.produtivhead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.produtivhead.DB.NotificationDAO;
import com.example.produtivhead.Notifications.Notification;
import com.example.produtivhead.Notifications.NotificationManager;

public class BootReceiverActivity extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //Accès à la base de données
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();

        //Initialisation des notifications au redémarrage de l'appareil
        for(int i = 1; i <= 101; i++) {
            Notification notification = notificationDAO.getNotification(i);
            if(notification.getActive() == 1) {
                NotificationManager.scheduleNotification(context, i);
            }
        }

        //notificationDAO.close();
    }

}
