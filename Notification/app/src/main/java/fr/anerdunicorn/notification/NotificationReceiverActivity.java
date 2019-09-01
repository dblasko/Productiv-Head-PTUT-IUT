package fr.anerdunicorn.notification;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationReceiverActivity extends BroadcastReceiver {

    private Intent repeatingIntent;
    private PendingIntent repeatingPendingIntent;
    private int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("notification", 0);
        notificationId = intent.getIntExtra("notificationId", 0);
        Calendar calendar = Calendar.getInstance();
        String jour = "";
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                jour = "Lundi";
                break;
            case Calendar.TUESDAY:
                jour = "Mardi";
                break;
            case Calendar.WEDNESDAY:
                jour = "Mercredi";
                break;
            case Calendar.THURSDAY:
                jour = "Jeudi";
                break;
            case Calendar.FRIDAY:
                jour = "Vendredi";
                break;
            case Calendar.SATURDAY:
                jour = "Samedi";
                break;
            case Calendar.SUNDAY:
                jour = "Dimanche";
                break;
        }
        if(notificationId == MainActivity.getNotificationIdSuiviHabitudes()) {
            repeatingIntent = new Intent(context, SuiviHabitudesActivity.class);
        }
        else if(notificationId == MainActivity.getNotificationIdHorairesTravail()) {
            repeatingIntent = new Intent(context, HorairesTravailActivity.class);
        }
        else
            repeatingIntent =  new Intent(context, RappelsActivity.class);
        if(settings.getBoolean("notification" + notificationId + jour, false)) {
            repeatingPendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager.createNotificationChannel(context);
            NotificationManager.createNotification(context, notificationId, repeatingPendingIntent);
            repeatingIntent = null;
        }

    }

}
