package fr.anerdunicorn.notification;

import android.app.AlertDialog;

import android.content.Intent;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import android.widget.Switch;
import android.widget.TimePicker;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Variables
    private Switch switchSuiviHabitudes;
    private Switch switchHorairesTravail;

    private CustomNotificationButton notificationButtonSuiviHabitudes;
    private CustomNotificationButton notificationButtonHorairesTravail;

    private ConfigDialog dialogSuiviHabitudes;
    private ConfigDialog dialogHorairesTravail;

    private ConstraintLayout blocSuiviHabitudes;

    private TimePicker choixHeureNotificationsSuiviHabitudes;
    private View layoutSuiviHabitudes;
    private AlertDialog.Builder dialogBuilderSuiviHabitudes;
    private static ArrayList<CheckBox> joursSuiviHabitudes;

    private static final int notificationIdSuiviHabitudes = 100;

    //Variables horaires travail
    private ConstraintLayout blocHorairesTravail;
    private TimePicker choixHeureNotificationsHorairesTravail;
    private View layoutHorairesTravail;
    private AlertDialog.Builder dialogBuilderHorairesTravail;
    private static ArrayList<CheckBox> joursHorairesTravail;

    private static final int notificationIdHorairesTravail = 101;

    //Autres variables
    private ConstraintLayout blocRappels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSharedPreferences("notification", 0).edit().clear().commit();

        switchSuiviHabitudes = findViewById(R.id.switchSuiviHabitudes);
        switchHorairesTravail = findViewById(R.id.switchHorairesTravail);

        notificationButtonSuiviHabitudes = new CustomNotificationButton(100, "Suivi habitudes");
        notificationButtonHorairesTravail = new CustomNotificationButton(101, "Horaires travail");

        dialogSuiviHabitudes = new ConfigDialog(MainActivity.this, notificationButtonSuiviHabitudes, null);
        dialogHorairesTravail = new ConfigDialog(MainActivity.this, notificationButtonHorairesTravail, null);


        blocSuiviHabitudes = findViewById(R.id.blocSuiviHabitudes);
        blocHorairesTravail = findViewById(R.id.blocHorairesTravail);
        blocRappels = findViewById(R.id.blocRappels);
        joursSuiviHabitudes = new ArrayList<>();
        joursHorairesTravail = new ArrayList<>();
        layoutSuiviHabitudes = getLayoutInflater().inflate(R.layout.notification_configuration_layout, null);
        layoutHorairesTravail = getLayoutInflater().inflate(R.layout.notification_configuration_layout, null);
        choixHeureNotificationsSuiviHabitudes = layoutSuiviHabitudes.findViewById(R.id.choixHeureNotifications);
        choixHeureNotificationsHorairesTravail = layoutHorairesTravail.findViewById(R.id.choixHeureNotifications);
        choixHeureNotificationsSuiviHabitudes.setIs24HourView(true);
        choixHeureNotificationsHorairesTravail.setIs24HourView(true);

        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxLundi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxMardi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxMercredi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxJeudi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxVendredi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxSamedi));
        joursSuiviHabitudes.add((CheckBox) layoutSuiviHabitudes.findViewById(R.id.checkBoxDimanche));

        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxLundi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxMardi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxMercredi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxJeudi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxVendredi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxSamedi));
        joursHorairesTravail.add((CheckBox) layoutHorairesTravail.findViewById(R.id.checkBoxDimanche));

        blocSuiviHabitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.isFinishing())
                    dialogSuiviHabitudes.show();
            }
        });

        blocHorairesTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHorairesTravail.show();
            }
        });

        blocRappels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RappelsActivity.class);
                startActivity(intent);
            }
        });

        switchSuiviHabitudes.setChecked(getSharedPreferences("notification", 0).getBoolean("alarm" + notificationIdSuiviHabitudes, false));
        switchHorairesTravail.setChecked(getSharedPreferences("notification", 0).getBoolean("alarm" + notificationIdHorairesTravail, false));


        for (CheckBox jour : joursSuiviHabitudes) {
            jour.setChecked(getSharedPreferences("notification", 0).getBoolean("notification" + notificationIdSuiviHabitudes + jour.getTag().toString(), false));
        }
        for (CheckBox jour : joursHorairesTravail) {
            jour.setChecked(getSharedPreferences("notification", 0).getBoolean("notification" + notificationIdHorairesTravail + jour.getTag().toString(), false));
        }

    }

    public static int getNotificationIdSuiviHabitudes() {
        return notificationIdSuiviHabitudes;
    }

    public static int getNotificationIdHorairesTravail() {
        return notificationIdHorairesTravail;
    }

}
