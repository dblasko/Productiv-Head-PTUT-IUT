package com.example.produtivhead.Notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.produtivhead.DB.NotificationDAO;
import com.example.produtivhead.Habits.HabitListActivity;
import com.example.produtivhead.R;
import com.example.produtivhead.Timer.TimerActivity;
import com.example.produtivhead.WorkMode.WorkModeManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.notification_activity);

        //Intégration de la toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Notifications");
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

        // PARTIE DRAWER
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ImageView icone = findViewById(R.id.toolbarIcon);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        /* */

        //Initialisation de la base de données
        NotificationDAO notificationDAO = new NotificationDAO(this);
        notificationDAO.open();

        //Initialisation d'un calendrier à l'instant t
        Calendar now = Calendar.getInstance();
        if(notificationDAO.getNotification(100) == null) {
            notificationDAO.addNotification(new Notification(notificationIdSuiviHabitudes, "Notification", "Suivi des habitudes", 1, 8, 0, 0, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0));
        }
        if(notificationDAO.getNotification(101) == null) {
            notificationDAO.addNotification(new Notification(notificationIdHorairesTravail, "Notification", "Horaires de travail", 1, 8, 0, 0, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 0));
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
        switchSuiviHabitudes.setChecked((notificationDAO.getNotification(notificationIdSuiviHabitudes).getActive()) == 1);
        switchHorairesTravail.setChecked((notificationDAO.getNotification(notificationIdHorairesTravail).getActive()) == 1);

        /*//Fermeture de la base de données
        notificationDAO.close();*/
    }

    //Getters
    public static int getNotificationIdSuiviHabitudes() {
        return notificationIdSuiviHabitudes;
    }

    public static int getNotificationIdHorairesTravail() {
        return notificationIdHorairesTravail;
    }

    // POUR DRAWER
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_timer) {
            Intent i = new Intent(this, TimerActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_habits) {
            Intent i = new Intent(this, HabitListActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_todo_list) {

        } else if (id == R.id.nav_notifications) {
            Intent i = new Intent(this, NotificationActivity.class);
            //Intent i = new Intent(this, ConfigActivity.class);
            //i.putExtra("notificationId", 101);
            this.startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
