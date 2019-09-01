package fr.anerdunicorn.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConfigDialog {

    private AlertDialog.Builder builder;
    private Dialog dialog;

    public ConfigDialog(final Activity activity, final CustomNotificationButton customNotificationButton, final View notificationButtonView) {
        final View configView = activity.getLayoutInflater().inflate(R.layout.notification_configuration_layout, null);

        final SharedPreferences settings = activity.getApplicationContext().getSharedPreferences("notification", 0);

        builder = new AlertDialog.Builder(activity);
        for(Days day : Days.values()) {
            CheckBox checkBox = configView.findViewWithTag(day.toString());
            checkBox.setChecked(settings.getBoolean("notification" + customNotificationButton.getId() + day, false));
        }

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(settings.getLong("alarmTime" + customNotificationButton.getId(), 0));
        TimePicker timePicker = configView.findViewById(R.id.choixHeureNotifications);
        timePicker.setIs24HourView(true);
        if(time.getTimeInMillis() == 0){
            timePicker.setHour(8);
            timePicker.setMinute(0);
        }
        else{
            timePicker.setHour(time.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(time.get(Calendar.MINUTE));
        }


        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = settings.edit();
                Intent receiverIntent = new Intent(activity.getApplicationContext(), NotificationReceiverActivity.class);
                Calendar alarm = Calendar.getInstance();
                for(Days day : Days.values()) {
                    CheckBox checkBox = configView.findViewWithTag(day.toString());
                    editor.putBoolean("notification" + customNotificationButton.getId() + day, checkBox.isChecked());
                    receiverIntent.putExtra("notification" + customNotificationButton.getId() + day, checkBox.isChecked());
                }
                alarm.set(Calendar.HOUR_OF_DAY, ((TimePicker) configView.findViewById(R.id.choixHeureNotifications)).getHour());
                alarm.set(Calendar.MINUTE, ((TimePicker) configView.findViewById(R.id.choixHeureNotifications)).getMinute());
                alarm.set(Calendar.SECOND, 0);

                editor.putLong("alarmTime" + customNotificationButton.getId(), alarm.getTimeInMillis());
                editor.putString("notificationContent" + customNotificationButton.getId(), customNotificationButton.getContent());
                editor.commit();

                Switch aSwitch = notificationButtonView.findViewById(R.id.switchNotificationButton);
                aSwitch.setChecked(false);
                aSwitch.setChecked(true);
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.setView(configView);
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

}
