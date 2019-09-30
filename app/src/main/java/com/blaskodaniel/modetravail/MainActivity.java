package com.blaskodaniel.modetravail;

import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch switchWorkMode = findViewById(R.id.switch_work_mode);

        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                AudioManager mode = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                if (b) { // checked - activate work mode
                    if (wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);
                    mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    // if (mode.getRingerMode() == AudioManager.RINGER_MODE_NORMAL || mode.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
                    Toast.makeText(getApplicationContext(), "Activation du mode travail...", Toast.LENGTH_LONG).show();
                } else { // unchecked - deactivate work mode
                    if (!wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(true);
                    Toast.makeText(getApplicationContext(), "Désactivation du mode travail...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // wifi : https://stackoverflow.com/questions/8863509/how-to-programmatically-turn-off-wifi-on-android-device

}
