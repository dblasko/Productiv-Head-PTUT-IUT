package fr.anerdunicorn.notification;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    //Variables
    private Switch switchSuiviHabitudes;
    private Switch switchHorairesTravail;

    private CustomNotificationButton notificationButtonSuiviHabitudes;
    private CustomNotificationButton notificationButtonHorairesTravail;

    private ConstraintLayout blocSuiviHabitudes;
    private ConstraintLayout blocHorairesTravail;
    private ConstraintLayout blocRappels;

    private static final int notificationIdSuiviHabitudes = 100;
    private static final int notificationIdHorairesTravail = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSharedPreferences("notification", 0).edit().clear().commit(); //Pour delete toutes les SharedPreferences

        //Initialisation des variables
        switchSuiviHabitudes = findViewById(R.id.switchSuiviHabitudes);
        switchHorairesTravail = findViewById(R.id.switchHorairesTravail);

        notificationButtonSuiviHabitudes = new CustomNotificationButton(notificationIdSuiviHabitudes, "Suivi habitudes");
        notificationButtonHorairesTravail = new CustomNotificationButton(notificationIdHorairesTravail, "Horaires travail");

        blocSuiviHabitudes = findViewById(R.id.blocSuiviHabitudes);
        blocHorairesTravail = findViewById(R.id.blocHorairesTravail);
        blocRappels = findViewById(R.id.blocRappels);

        //Affichage du dialogue de config en cliquant sur les blocs
        blocSuiviHabitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfigDialog(MainActivity.this, notificationButtonSuiviHabitudes.getId(), switchSuiviHabitudes).show();
            }
        });

        blocHorairesTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfigDialog(MainActivity.this, notificationButtonHorairesTravail.getId(), switchHorairesTravail).show();
            }
        });

        //Passage à l'activité RappelsActivity en cliquant sur le bloc
        blocRappels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                startActivity(intent);
            }
        });

        //Activation ou annulation de la notification en fonction du changement d'état des switch
        switchSuiviHabitudes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    NotificationManager.scheduleNotification(getApplicationContext(), notificationIdSuiviHabitudes, "Suivi habitudes");
                else
                    NotificationManager.cancelNotification(getApplicationContext(), notificationIdSuiviHabitudes);
            }
        });

        switchHorairesTravail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    NotificationManager.scheduleNotification(getApplicationContext(), notificationIdHorairesTravail, "Horaires travail");
                else
                    NotificationManager.cancelNotification(getApplicationContext(), notificationIdHorairesTravail);
            }
        });

        //Setup de l'état des switchs en fonction de si la notification est activée ou non
        switchSuiviHabitudes.setChecked(getSharedPreferences("notification", 0).getBoolean("alarm" + notificationIdSuiviHabitudes, false));
        switchHorairesTravail.setChecked(getSharedPreferences("notification", 0).getBoolean("alarm" + notificationIdHorairesTravail, false));
    }

    //Getters
    public static int getNotificationIdSuiviHabitudes() {
        return notificationIdSuiviHabitudes;
    }

    public static int getNotificationIdHorairesTravail() {
        return notificationIdHorairesTravail;
    }

}
