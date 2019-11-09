package com.example.produtivhead.Notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.produtivhead.DB.NotificationDAO;
import com.example.produtivhead.R;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationManager {

    public static void scheduleNotification(Context context, int notificationId){

        //Connexion à la base de données et récupération de la notification
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();
        Notification notification = notificationDAO.getNotification(notificationId);

        Intent receiverIntent = new Intent(context, NotificationReceiverActivity.class);
        Calendar alarm = Calendar.getInstance();
        alarm.set(notification.getYear(), notification.getMonth(), notification.getDay(), notification.getHour(), notification.getMinute());

        receiverIntent.putExtra("notificationId", notificationId);
        PendingIntent receiverPendingIntent = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Si la notification est de type répétable
        if(notification.getRepeatable() == 1)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, receiverPendingIntent);
        //Sinon
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), receiverPendingIntent);

        //Sauvegarde de l'état de la notification dans la base de données
        notification.setActive(1);
        notificationDAO.updateNotification(notification);

        //Fermeture de l'accès à la base de données
        notificationDAO.close();
    }

    public static void cancelNotification(Context context, int notificationId) {
        //Initialisation variables
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent receiverIntent = new Intent(context.getApplicationContext(), NotificationReceiverActivity.class);

        //Annulation de la notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        //Sauvegarde de l'état de la notification
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();
        Notification notification = notificationDAO.getNotification(notificationId);
        notification.setActive(0);
        notificationDAO.updateNotification(notification);
        notificationDAO.close();
    }

    //Création d'un channel de notification (obligatoire sur les dernières versions android), a appeler avant de créer une notification
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription("Description du channel");
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNotification(Context context, int notificationId, PendingIntent repeatingPendingIntent){
        //Récupération du contenu de la notification
        NotificationDAO notificationDAO = new NotificationDAO(context);
        notificationDAO.open();
        Notification notification = notificationDAO.getNotification(notificationId);
        notificationDAO.close();
        String notificationContent = notification.getContent();

        //Contenu par défaut si notificationContent est vide
        if(notificationContent.length() == 0) {
            notificationContent = "This is the default notification content";
        }

        //Création de la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Notification")
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(repeatingPendingIntent)
                .setSmallIcon(R.mipmap.productiv_head_round)
                .setAutoCancel(true);

        //Envoi de la notification
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
