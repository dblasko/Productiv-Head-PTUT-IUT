package com.blaskodaniel.modetravail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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
    WorkModeManager wmm;

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

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });
    }

}
