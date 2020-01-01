package com.example.produtivhead.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.produtivhead.Habits.HabitListActivity;
import com.example.produtivhead.Notifications.NotificationActivity;
import com.example.produtivhead.Timer.TimerActivity;
import com.example.produtivhead.WorkMode.WorkModeManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.produtivhead.R;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public void onClickHabits(View v) {
        Intent i = new Intent(this, HabitListActivity.class);
        this.startActivity(i);
    }

    public void onClickNotifications(View v) {
        Intent i = new Intent(this, NotificationActivity.class);
        this.startActivity(i);
    }

    public void onClickTimer(View v) {
        Intent i = new Intent(this, TimerActivity.class);
        this.startActivity(i);
    }

    public void onClickTodo(View v) {
        Toasty.error(this, "Fonctionnalité en cours de développement");
    }


    private Toolbar toolbar;
    private WorkModeManager wmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /* */
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Accueil");

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });


        /* PARTIE BOUTON INFO */

        /* */

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

    }

    /* POUR BOUTON INFO */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_habit_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Allows us to show info dialog on click on the info menu item
        switch (item.getItemId()) {
            case R.id.mInfo:
                // Build & show information dialog
                AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                alertDialog.setTitle("Interrupteur mode travail.");
                alertDialog.setMessage("Le mode travail vous aide à vous concentrer pour booster votre productivité.\n" +
                        "Afin de vous permettre de vous concentrer sur vos objectifs et de vous couper de votre " +
                        "plus grande source de distraction, vous n'avez qu'à activer ce mode accessible à tout endroit de l'application.\n\n" +
                        "Productiv'Head s'occupera alors de couper tout son, d'activer le mode ne pas déranger, de désactiver le WiFi " +
                        "et même de vous mettre en mode avion si vous le souhaitez (proposé à chaque activation).\n" +
                        "Pour que le mode soit pleinement fonctionnel, merci de donner les droits d'accès aux paramètres de notification, comme demandé au lancement de l'application si elle n'a pas les droits.\n\n" +
                        "Pour vous reconnecter, appuyez sur l'interrupteur n'importe où dans l'application et nous rétablissons le son, les notifications & les connexions !");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    /* */

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
            Intent i = new Intent(this, HomeActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_timer) {
            Intent i = new Intent(this, TimerActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_habits) {
            Intent i = new Intent(this, HabitListActivity.class);
            this.startActivity(i);
        } else if (id == R.id.nav_todo_list) {

        } else if (id == R.id.nav_notifications) {
            Intent i = new Intent(this, NotificationActivity.class);
            this.startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
