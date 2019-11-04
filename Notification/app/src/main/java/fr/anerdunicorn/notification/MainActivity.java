package fr.anerdunicorn.notification;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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

    private Toolbar toolbar; // à intégrer !
    WorkModeManager wmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intégration de la toolbar
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

        //Initialisation de la base de données
        NotificationDatabaseManager notificationDatabaseManager = new NotificationDatabaseManager(this);
        notificationDatabaseManager.open();

        //Initialisation d'un calendrier à l'instant t
        Calendar now = Calendar.getInstance();
        if(notificationDatabaseManager.getNotification(100) == null) {
            notificationDatabaseManager.addNotification(new Notification(notificationIdSuiviHabitudes, "Notification", "Suivi des habitudes", 1, 8, 0, 0, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0));
        }
        if(notificationDatabaseManager.getNotification(101) == null) {
            notificationDatabaseManager.addNotification(new Notification(notificationIdHorairesTravail, "Notification", "Horaires de travail", 1, 8, 0, 0, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0));
        }

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
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                intent.putExtra("notificationId", notificationIdSuiviHabitudes);
                startActivity(intent);
            }
        });

        blocHorairesTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                intent.putExtra("notificationId", notificationIdHorairesTravail);
                startActivity(intent);
            }
        });

        //Passage à l'activité RappelsActivity en cliquant sur le bloc
        blocRappels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RappelsActivity.class);
                startActivity(intent);
            }
        });

        //Activation ou annulation de la notification en fonction du changement d'état des switch
        switchSuiviHabitudes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    NotificationManager.scheduleNotification(getApplicationContext(), notificationIdSuiviHabitudes);
                else
                    NotificationManager.cancelNotification(getApplicationContext(), notificationIdSuiviHabitudes);
            }
        });

        switchHorairesTravail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    NotificationManager.scheduleNotification(getApplicationContext(), notificationIdHorairesTravail);
                else
                    NotificationManager.cancelNotification(getApplicationContext(), notificationIdHorairesTravail);
            }
        });

        //Setup de l'état des switchs en fonction de si la notification est activée ou non
        switchSuiviHabitudes.setChecked((notificationDatabaseManager.getNotification(notificationIdSuiviHabitudes).getActive()) == 1);
        switchHorairesTravail.setChecked((notificationDatabaseManager.getNotification(notificationIdHorairesTravail).getActive()) == 1);

        //Fermeture de la base de données
        notificationDatabaseManager.close();
    }

    //Getters
    public static int getNotificationIdSuiviHabitudes() {
        return notificationIdSuiviHabitudes;
    }

    public static int getNotificationIdHorairesTravail() {
        return notificationIdHorairesTravail;
    }

}
