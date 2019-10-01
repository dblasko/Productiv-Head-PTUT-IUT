package fr.anerdunicorn.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationReceiverActivity extends BroadcastReceiver {

    //Variables
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Initialisation des SharedPreferences
        settings = context.getSharedPreferences("notification", 0);
        editor = settings.edit();

        //Récupération de l'id de la notification
        notificationId = intent.getIntExtra("notificationId", 0);

        if(settings.getBoolean("notificationRepeatable" + notificationId, false))
            handleRepeatingNotification(context);
        else
            handleNonRepeatingNotification(context);
    }

    private void handleRepeatingNotification(Context context) {
        //Instanciation d'un calendrier
        Calendar calendar = Calendar.getInstance();

        //Récupération du jour actuel
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

        //Instanciation d'un intent
        Intent repeatingIntent;

        //Création de l'intent en fonction de l'id de la notification
        if(notificationId == MainActivity.getNotificationIdSuiviHabitudes()) {
            repeatingIntent = new Intent(context, SuiviHabitudesActivity.class);
        }
        else if(notificationId == MainActivity.getNotificationIdHorairesTravail()) {
            repeatingIntent = new Intent(context, HorairesTravailActivity.class);
        }
        else
            repeatingIntent =  new Intent(context, RappelsActivity.class);

        //Création de la notification si elle est plannifiée pour ce jour
        if(settings.getBoolean("notification" + notificationId + jour, false)) {
            PendingIntent repeatingPendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager.createNotificationChannel(context);
            NotificationManager.createNotification(context, notificationId, repeatingPendingIntent);
        }
    }

    private void handleNonRepeatingNotification(Context context) {

        //Instanciation d'un intent
        Intent intent;

        //Création de l'intent en fonction de l'id de la notification
        if(notificationId == MainActivity.getNotificationIdSuiviHabitudes()) {
            intent = new Intent(context, SuiviHabitudesActivity.class);
        }
        else if(notificationId == MainActivity.getNotificationIdHorairesTravail()) {
            intent = new Intent(context, HorairesTravailActivity.class);
        }
        else
            intent =  new Intent(context, RappelsActivity.class);

        //Création de la notification
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager.createNotificationChannel(context);
        NotificationManager.createNotification(context, notificationId, pendingIntent);

        //Suppression de la notification dans les rappels
        editor.putBoolean("notificationButton" + notificationId, false);
    }

}
