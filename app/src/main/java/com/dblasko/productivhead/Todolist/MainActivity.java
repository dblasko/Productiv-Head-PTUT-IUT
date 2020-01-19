package com.dblasko.productivhead.Todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dblasko.productivhead.Habits.HabitListActivity;
import com.dblasko.productivhead.Home.HomeActivity;
import com.dblasko.productivhead.Notifications.NotificationActivity;
import com.dblasko.productivhead.R;
import com.dblasko.productivhead.Timer.TimerActivity;
import com.dblasko.productivhead.WorkMode.WorkModeManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private WorkModeManager wmm;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* BARRE */

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Todo List");

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });

        /* FIN BARRE */

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


    public void acceuil_todolist(View view){
        startActivity(new Intent(this,acceuil_todolist.class));
    }

    public void liste_taches(View view)
    {

        startActivity(new Intent(this,liste_taches.class));
    }




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
                // TODO - extract to function? idk if needed
                // Build & show information dialog
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Module todo list");
                alertDialog.setMessage("Vous êtes tous le temps débordé par vos activités quotidiennes et l’expression ”je n’ai pas le temps” est devenue votre meilleure excuse ? \n" +
                        "Inutile de chercher plus longtemps une solution à cette situation ! Une meilleure gestion du temps est indispensable. \n" +
                        "Alors, pourquoi ne pas vous essayer à la to do list ?\n" +
                        "En effet, la liste de tâches aide à atteindre les différents objectifs fixés, en mettant l’accent sur les tâches importantes qu’on ne veut surtout pas oublier.\n" +
                        "Comment l'utiliser ? Tout d'abord,chargez vous de sélectionner les dates de début et de fin de votre tâche mais aussi l'heure de début afin d'être notifié en temps réel et le tour est joué !\u200B");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
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
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
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
