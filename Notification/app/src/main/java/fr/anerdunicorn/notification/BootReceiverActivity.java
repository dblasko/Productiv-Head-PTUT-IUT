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

import java.util.Calendar;

public class BootReceiverActivity extends BroadcastReceiver {

    private long alarmTime;
    private int notificationId;
    private String notificationContent;

    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("notification", 0);
        if(settings.getBoolean("alarm" + MainActivity.getNotificationIdSuiviHabitudes(), false)) {
            notificationId = MainActivity.getNotificationIdSuiviHabitudes();
            alarmTime = settings.getLong("alarmTime" + notificationId, 0);
            notificationContent = settings.getString("notificationContent" + notificationId, "");
            scheduleNotification(context);
        }
        else if(settings.getBoolean("alarm" + MainActivity.getNotificationIdHorairesTravail(), false)) {
            notificationId = MainActivity.getNotificationIdHorairesTravail();
            alarmTime = settings.getLong("alarmTime" + notificationId, 0);
            notificationContent = settings.getString("notificationContent" + notificationId, "");
            scheduleNotification(context);
        }
    }

    public void scheduleNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(alarmTime);
        Intent receiverIntent = new Intent(context, NotificationReceiverActivity.class);
        receiverIntent.putExtra("notificationContent", notificationContent);
        receiverIntent.putExtra("notificationId", notificationId);
        PendingIntent receiverPendingIntent = PendingIntent.getBroadcast(context, notificationId, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, receiverPendingIntent);
    }

}
