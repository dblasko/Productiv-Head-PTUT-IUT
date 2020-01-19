package com.dblasko.productivhead.Notifications;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dblasko.productivhead.DB.NotificationDAO;
import com.dblasko.productivhead.Todolist.liste_taches;

import java.util.Calendar;

public class NotificationReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Récupération de l'id de la notification
        int notificationId = intent.getIntExtra("notificationId", 0);

        //Récupération de la notification dans la base de données
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();
        Notification notification = notificationDAO.getNotification(notificationId);
        //notificationDAO.close();

        if(notification.getRepeatable() == 1)
            handleRepeatingNotification(context, notification);
        else
            handleNonRepeatingNotification(context, notification);
    }

    private void handleRepeatingNotification(Context context, Notification notification) {
        //Instanciation d'un calendrier
        Calendar calendar = Calendar.getInstance();

        //Récupération du jour actuel
        int jour = 0;
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                jour = 1;
                break;
            case Calendar.TUESDAY:
                jour = 2;
                break;
            case Calendar.WEDNESDAY:
                jour = 3;
                break;
            case Calendar.THURSDAY:
                jour = 4;
                break;
            case Calendar.FRIDAY:
                jour = 5;
                break;
            case Calendar.SATURDAY:
                jour = 6;
                break;
            case Calendar.SUNDAY:
                jour = 7;
                break;
        }

        //Instanciation d'un intent
        Intent repeatingIntent;

        //Création de l'intent en fonction de l'id de la notification
        if(notification.getId() == NotificationActivity.getNotificationIdSuiviHabitudes()) {
            repeatingIntent = new Intent(context, NotificationActivity.class);
        }
        else if(notification.getId() == NotificationActivity.getNotificationIdHorairesTravail()) {
            repeatingIntent = new Intent(context, NotificationActivity.class);
        }
        else if(notification.getId() >= 200) {
            repeatingIntent = new Intent(context, liste_taches.class);
        }
        else
            repeatingIntent =  new Intent(context, RappelsActivity.class);

        //Création de la notification si elle est plannifiée pour ce jour
        if(Notification.isDayChosen(notification, jour)) {
            PendingIntent repeatingPendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager.createNotificationChannel(context);
            NotificationManager.createNotification(context, notification.getId(), repeatingPendingIntent);
        }
    }

    private void handleNonRepeatingNotification(Context context, Notification notification) {

        //Instanciation d'un intent
        Intent intent;

        //Création de l'intent en fonction de l'id de la notification
        if(notification.getId() == NotificationActivity.getNotificationIdSuiviHabitudes()) {
            intent = new Intent(context, NotificationActivity.class);
        }
        else if(notification.getId() == NotificationActivity.getNotificationIdHorairesTravail()) {
            intent = new Intent(context, NotificationActivity.class);
        }
        else
            intent =  new Intent(context, RappelsActivity.class);

        //Création de la notification
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager.createNotificationChannel(context);
        NotificationManager.createNotification(context, notification.getId(), pendingIntent);

        //Suppression de la notification dans la base de données si c'est un rappel
        if(notification.getId() != NotificationActivity.getNotificationIdSuiviHabitudes() && notification.getId() != NotificationActivity.getNotificationIdHorairesTravail() && notification.getId() >= 200 && notification.getId() < 300) {
            NotificationDAO notificationDAO = new NotificationDAO(context);
            notificationDAO.open();
            notificationDAO.deleteNotification(notification.getId());
            //notificationDAO.close();

            //Update la RappelsActivity lorsque on supprime la notification
            if(RappelsActivity.customNotifications != null) {
                for (CustomNotificationButton customNotificationButton : RappelsActivity.customNotifications) {
                    if (customNotificationButton.getId() == notification.getId())
                        RappelsActivity.customNotifications.remove(customNotificationButton);
                }
                RappelsActivity.adapter.notifyDataSetChanged();
            }
        }
    }

}
