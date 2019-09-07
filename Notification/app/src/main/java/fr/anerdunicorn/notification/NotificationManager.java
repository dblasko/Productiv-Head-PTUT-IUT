package fr.anerdunicorn.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationManager {

    public static void scheduleNotification(Context context, int notificationId, String notificationContent){
        SharedPreferences settings = context.getSharedPreferences("notification", 0);
        SharedPreferences.Editor editor = settings.edit();
        Intent receiverIntent = new Intent(context, NotificationReceiverActivity.class);
        long time = settings.getLong("alarmTime" + notificationId, 0);
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(time);
        Calendar now = Calendar.getInstance();
        if(alarm.before(now)){
            time += 86400000L;
        }
        alarm.setTimeInMillis(time);

        receiverIntent.putExtra("notificationId", notificationId);
        receiverIntent.putExtra("notificationContent", notificationContent);
        PendingIntent receiverPendingIntent = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, receiverPendingIntent);

        editor.putLong("alarmTime" + notificationId, alarm.getTimeInMillis());
        editor.putString("notificationContent" + notificationId, notificationContent);
        editor.putBoolean("alarm" + notificationId, true);
        editor.commit();
    }

    public static void cancelNotification(Context context, int notificationId) {
        //Initialisation variables
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent receiverIntent = new Intent(context.getApplicationContext(), NotificationReceiverActivity.class);

        //Annulation de la notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        //Sauvegarde de l'état de la notification
        SharedPreferences settings = context.getSharedPreferences("notification", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("alarm" + notificationId, false);
        editor.commit();
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
        SharedPreferences settings = context.getSharedPreferences("notification", 0);
        String notificationContent = settings.getString("notificationContent" + notificationId, "");

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
