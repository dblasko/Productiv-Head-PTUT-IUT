package fr.anerdunicorn.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TimePicker;
import java.util.Calendar;

public class ConfigDialog {

    private Dialog dialog;

    public ConfigDialog(final Activity activity, final int notificationId, final Switch aSwitch) {

        //Initialisation du layout de configuration
        final View configView = activity.getLayoutInflater().inflate(R.layout.notification_configuration_layout, null);

        //Initialisation des SharedPreferences
        final SharedPreferences settings = activity.getApplicationContext().getSharedPreferences("notification", 0);
        final SharedPreferences.Editor editor = settings.edit();

        //Setup de l'état des CheckBox du layout de configuration en fonction de leur valeur dans les SharedPreferences
        for(Days day : Days.values()) {
            CheckBox checkBox = configView.findViewWithTag(day.toString());
            checkBox.setChecked(settings.getBoolean("notification" + notificationId + day, false));
        }

        //Initialisation d'un calendrier a l'heure de la notification
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(settings.getLong("alarmTime" + notificationId, 0));

        //Initialisation du TimePicker du layout de configuration
        TimePicker timePicker = configView.findViewById(R.id.choixHeureNotifications);
        timePicker.setIs24HourView(true);

        //Si l'heure du calendier est a 0 ( = pas de notification prévue)
        if(time.getTimeInMillis() == 0){

            //Changement de l'heure du TimePicker a 08:00
            timePicker.setHour(8);
            timePicker.setMinute(0);
        }
        else{

            //Sinon, changement de l'heure du TimePicker a celle du calendrier
            timePicker.setHour(time.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(time.get(Calendar.MINUTE));
        }

        //Initialisation du dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //Sauvegarde de l'heure de création de la notification + planification de la notification
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Récupération du contenu de la notification dans les SharedPreferences
                String notificationContent = settings.getString("notificationContent" + notificationId, "");

                //Création d'un intent vers la NotificationReceiverActivity
                Intent receiverIntent = new Intent(activity.getApplicationContext(), NotificationReceiverActivity.class);


                for(Days day : Days.values()) {
                    CheckBox checkBox = configView.findViewWithTag(day.toString());
                    editor.putBoolean("notification" + notificationId + day, checkBox.isChecked());
                    receiverIntent.putExtra("notification" + notificationId + day, checkBox.isChecked());
                }

                //Initialisation d'un calendrier à l'heure choisi avec le TimePicker
                Calendar alarm = Calendar.getInstance();
                alarm.set(Calendar.HOUR_OF_DAY, ((TimePicker) configView.findViewById(R.id.choixHeureNotifications)).getHour());
                alarm.set(Calendar.MINUTE, ((TimePicker) configView.findViewById(R.id.choixHeureNotifications)).getMinute());
                alarm.set(Calendar.SECOND, 0);

                //Sauvegarde des données dans les SharedPreferences
                editor.putLong("alarmTime" + notificationId, alarm.getTimeInMillis());
                editor.putString("notificationContent" + notificationId, notificationContent);
                editor.putBoolean("notificationRepeatable" + notificationId, true);
                editor.commit();

                //Changement de l'état du switch (annulation et plannification automatique de la notification)
                aSwitch.setChecked(false);
                aSwitch.setChecked(true);
            }
        });
        //Annulation de la configuration de la notification
        builder.setNegativeButton("Annuler", null);

        //Setup de la View du dialogue
        builder.setView(configView);

        //Création du dialogue
        dialog = builder.create();
    }


    /* PLANIFICATION DE LA NOTIFICATION UN JOUR DONNÉ

        //Récupération du DatePicker
        DatePicker datePicker = configView.findViewById(R.id.datePicker);

        //Initialisation d'un calendrier à la date et a l'heure choisies
        Calendar alarm = Calendar.getInstance();
        alarm.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute(), 0);

        //Sauvegarde des données dans les SharedPreferences
        editor.putLong("alarmTime" + notificationId, alarm.getTimeInMillis());
        editor.putBoolean("notificationRepeatable" + notificationId, false);
        editor.putString("notificationContent" + notificationId, notificationContent);
        editor.commit();

        //Changement de l'état du switch (annulation et plannification automatique de la notification)
        aSwitch.setChecked(false);
        aSwitch.setChecked(true);

     */

    //Affichage du dialogue
    public void show() {
        dialog.show();
    }

}
