package com.blaskodaniel.modetravail;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
            IDEES
                1. Au lieu toast, belle animation chargement?
                2. Ajouter partout à la navbar, créer une navbar pour tlm?
                3. Personnaliser l'activation/désactivation ?
     */

    public void promptUser(String content, boolean longLength){
        int length = (longLength)? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(getApplicationContext(), content, length).show();
    }

    public void enableWifi(boolean enable) {
        // Disables / enables the WiFi of the device if needed
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (enable && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!enable && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void doNotDisturb(boolean enable) {
        final AudioManager mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(enable) {
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }



        Switch switchWorkMode = findViewById(R.id.switch_work_mode);

        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) { // checked - activate work mode
                    enableWifi(false);
                    doNotDisturb(true);
                    promptUser("Activation du mode travail...", true);
                } else { // unchecked - deactivate work mode
                    enableWifi(true);
                    doNotDisturb(false);
                    promptUser("Désactivation du mode travail...", true);
                }
            }
        });
    }

    // wifi : https://stackoverflow.com/questions/8863509/how-to-programmatically-turn-off-wifi-on-android-device

}
