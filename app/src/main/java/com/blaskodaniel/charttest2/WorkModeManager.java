package com.blaskodaniel.charttest2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AlertDialog;
import es.dmoral.toasty.Toasty;

public class WorkModeManager {
    private Context context;

    public WorkModeManager(Context context) {
        this.context = context;
    }

    /* */

    public void promptUser(String content){
        /*int length = (longLength)? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(getApplicationContext(), content, length).show();*/
        Toasty.custom(context, content, R.drawable.ic_work_mode, R.color.colorAccent, 2000, true,
                true).show();
    }

    public void enableWifi(boolean enable) {
        // Disables / enables the WiFi of the device if needed
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (enable && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!enable && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void doNotDisturb(boolean enable) {
        final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(enable) {
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public void accessCellularDataSettings() {
        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        context.startActivity(intent);
    }

    public boolean isCellularDataEnabled() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return (Settings.Global.getInt(context.getApplicationContext().getContentResolver(), "mobile_data", 1) == 1);
        }
        return false;
    }

    public void showCellularDataDialog(String message) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setTitle("Données cellulaires")
                .setMessage(message)  // Pour s'adapter à l'activation/désactivation
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accessCellularDataSettings();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Non", null)
                .show();
    }

    public void enableWorkMode(boolean enable) {
        askForNotificationPermission();
        if (enable) { // checked - activate work mode
            enableWifi(false);
            doNotDisturb(true);
            if (isCellularDataEnabled()) showCellularDataDialog("Voulez-vous aussi désactiver vos données mobiles ?");
            promptUser("Activation du mode travail...");
        } else { // unchecked - deactivate work mode
            enableWifi(true);
            try {
                doNotDisturb(false);
            } catch (Exception e) {
                Toasty.error(context, "Permissions non données.");
            }
            if (!isCellularDataEnabled()) showCellularDataDialog("Voulez-vous activer vos données mobiles aussi ?");
            promptUser("Désactivation du mode travail...");
        }
    }

    public void askForNotificationPermission() {
        NotificationManager notificationManager =
                (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            context.startActivity(intent);
        }

    }
}
