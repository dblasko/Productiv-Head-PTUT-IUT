package com.dblasko.productivhead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dblasko.productivhead.DB.NotificationDAO;
import com.dblasko.productivhead.Notifications.Notification;
import com.dblasko.productivhead.Notifications.NotificationManager;

public class BootReceiverActivity extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //Accès à la base de données
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();

        //Initialisation des notifications au redémarrage de l'appareil
        for(Notification notification : notificationDAO.getAllNotifications()) {
            if(notification.getActive() == 1) {
                NotificationManager.scheduleNotification(context, notification.getId());
            }
        }

        //notificationDAO.close();
    }

}
