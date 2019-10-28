package com.blaskodaniel.modetravail;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
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

    public void accessCellularDataSettings() {
        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        startActivity(intent);
    }

    public boolean isCellularDataEnabled() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return (Settings.Global.getInt(getApplicationContext().getContentResolver(), "mobile_data", 1) == 1);
        }
        return false;
    }

    public void showCellularDataDialog(String message) {
        new AlertDialog.Builder(this)
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
        if (enable) { // checked - activate work mode
            enableWifi(false);
            doNotDisturb(true);
            if (isCellularDataEnabled()) showCellularDataDialog("Voulez-vous aussi désactiver vos données mobiles ?");
            promptUser("Activation du mode travail...");
        } else { // unchecked - deactivate work mode
            enableWifi(true);
            doNotDisturb(false);
            if (!isCellularDataEnabled()) showCellularDataDialog("Voulez-vous activer vos données mobiles aussi ?");
            promptUser("Désactivation du mode travail...");
        }
    }

    public void askForNotificationPermission() {
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

    }
}

public class MainActivity extends AppCompatActivity {

    /*
            ON NE PEUT PAS ETEINDRE LA 4G : https://stackoverflow.com/questions/31120082/latest-update-on-enabling-and-disabling-mobile-data-programmatically
            Depuis lollipop (Android 5.0)
            IDEM mode avion : https://stackoverflow.com/questions/13766909/how-to-programmatically-enable-and-disable-flight-mode-on-android-4-2

            IDEES
                2. Ajouter partout à la navbar, créer une navbar pour tlm?
                    // https://stackoverflow.com/questions/31231609/creating-a-button-in-android-toolbar
                3. Bottom bar
                // Fonctions DAO laulau !
                3. Personnaliser l'activation/désactivation ? -> un temps donné, dans la config° d'olivier? Avec son timer? Activer auto avec le timer de laurine?
     */

        // TODO MAVEN PREREQUISITS POUR FUSION : https://github.com/GrenderG/Toasty




    private Toolbar toolbar; // à intégrer !
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // à intégrer !
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Productiv'Head");
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        askForNotificationPermission();

        Switch switchWorkMode = findViewById(R.id.switch_work_mode);

        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableWorkMode(b);
            }
        });
    }

}
