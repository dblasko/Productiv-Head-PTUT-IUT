package com.dblasko.productivhead.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerActionReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        TimerActivity timerActivity = TimerActivity.getTimerActivity();

        if(action.equals("pause")) {
            if(timerActivity.getNotificationAction() == 1) {
                timerActivity.pauseTravail(null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPause, null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPlay, timerActivity.getPendingIntentPlay());
                timerActivity.updateTimerNotification(null);
            }
            else if (timerActivity.getNotificationAction() == 3) {
                timerActivity.pauseRepos(null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPause, null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPlay, timerActivity.getPendingIntentPlay());
                timerActivity.updateTimerNotification(null);
            }
        } else if(action.equals("play")) {
            if (timerActivity.getNotificationAction() == 0) {
                timerActivity.startTravail(null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPause, timerActivity.getPendingIntentPause());
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPlay, null);
            }
            else if (timerActivity.getNotificationAction() == 2) {
                timerActivity.startRepos(null);
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPause, timerActivity.getPendingIntentPause());
                timerActivity.getNotificationLayout().setOnClickPendingIntent(com.dblasko.productivhead.R.id.imageViewPlay, null);
            }
        }

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

}
