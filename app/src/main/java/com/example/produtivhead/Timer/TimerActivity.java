package com.example.produtivhead.Timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.produtivhead.DB.TimerStatisticsDAO;
import com.example.produtivhead.Habits.HabitListActivity;
import com.example.produtivhead.Home.HomeActivity;
import com.example.produtivhead.Notifications.NotificationActivity;
import com.example.produtivhead.R;
import com.example.produtivhead.WorkMode.WorkModeManager;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int debut = 8000;           // en millis    1500000  exemple 8000

    private int nbSession = 0;
    private int nbTravail = 0;
    private int tpsTravailPerso = 0;
    private int tpsPausePerso = 0;
    private int nbTpsGrandePause = 0;

    private Button bStatistique;

    private CountDownTimer decrementation;

    private ImageButton bStartTravail;
    private ImageButton bStartRepos;
    private ImageButton bInitTpsTravail;
    private ImageButton bPauseTravail;
    private ImageButton bPauseRepos;
    private ImageButton bReset;
    private ImageButton bPersonnaliser;


    private boolean finSessionTravail = true;
    private boolean sonActive = false;
    private boolean resetPossible = false;               //pas cancel au debut
    private boolean sessionTravail = false;           //pr le dialog
    private boolean sessionRepos = false;
    private boolean sessionPersonnaliser = false;
    private boolean modif = false;


    private long tempsRestant = debut;

    private MediaPlayer son;
    private AlertDialog dialog;


    private EditText etNbSession;
    private EditText etTpsTravail;
    private EditText etTpsPause;
    private EditText etTpsGrandePause;

    private String affichageNbSession = "";
    private String affichageTpsTravail = "";
    private String affichageTpsPause = "";
    private String affichageTpsGrandePause = "";


    private TextView tNomTvl;
    private TextView tNomRep;
    private TextView tNbSessionPersonnalise;
    private TextView timer;
    private TextView tSessionTravail;

    TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    TimerStatistics tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));

    private Toolbar toolbar;
    private WorkModeManager wmm;

    private ImageView iMStartTravail;
    private ImageView iMInitTpsTravail;
    private ImageView iMStartRepos;
    private ImageView iMPauseTravail;
    private ImageView iMPauseRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timer_activity);
        //customizeActionBar();

        /* BARRE */

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Timer");

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

        tsDAO.open();

        timer = findViewById(R.id.timer);
        bStartTravail = findViewById(R.id.buttonStartTravail);
        bInitTpsTravail = findViewById(R.id.buttonInitTpsTravail);
        bReset = findViewById(R.id.buttonReset);
        bStartRepos = findViewById(R.id.buttonStartRepos);
        bPauseTravail = findViewById(R.id.buttonPauseTravail);
        bPauseRepos = findViewById(R.id.buttonPauseRepos);
        tSessionTravail = findViewById(R.id.sessionTravail);
        tNomTvl = findViewById(R.id.nomTvl);
        tNomRep = findViewById(R.id.nomRep);
        tNbSessionPersonnalise = findViewById(R.id.nbSessionPersonnalise);
        bStatistique = findViewById(R.id.buttonStatistique);
        bPersonnaliser = findViewById(R.id.buttonPersonnaliser);
        iMStartTravail = findViewById(R.id.imageViewStartTravail);
        iMInitTpsTravail = findViewById(R.id.imageViewInitTpsTravail);
        iMStartRepos = findViewById(R.id.imageViewStartRepos);
        iMPauseTravail = findViewById(R.id.imageViewPauseTravail);
        iMPauseRepos = findViewById(R.id.imageViewPauseRepos);

        bPauseTravail.setVisibility(View.INVISIBLE);
        iMPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        iMPauseRepos.setVisibility(View.INVISIBLE);
        bStartRepos.setVisibility(View.INVISIBLE);
        iMStartRepos.setVisibility(View.INVISIBLE);
        bStartTravail.setVisibility(View.VISIBLE);
        iMStartTravail.setVisibility(View.VISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
        iMInitTpsTravail.setVisibility(View.INVISIBLE);
        bReset.setVisibility(View.VISIBLE);
        tNomTvl.setVisibility(View.VISIBLE);
        tNomRep.setVisibility(View.INVISIBLE);

        bStartTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTravail(view);
            }
        });

        bStartRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRepos(view);
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });

        bPauseTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTravail(view);
            }
        });

        bPauseRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseRepos(view);
            }
        });

        bInitTpsTravail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTpsTravail(view);
            }
        });

        bStatistique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TimerActivity.this,StatisticsActivity.class);
                startActivity(myIntent);
            }
        });

        bPersonnaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personnaliser(view);
            }
        });

        if(nbSession==0) tNbSessionPersonnalise.setText("∞");
        else tNbSessionPersonnalise.setText(String.valueOf(nbSession));
        actualisationTimer();

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
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(TimerActivity.this).create();
                alertDialog.setTitle("Module timer");
                alertDialog.setMessage("Ce module vous permet d’organiser vos sessions de travail afin d’être le plus productif possible.\n" +
                        "Il applique la méthode Pomodoro : des sessions de travail de 25 min entrecoupées de pause de 5 min – toutes les 4 sessions de 25 min, la pause dure 20 min. \n" +
                        "Cette méthode a déjà fait ses preuves et augmente l’efficacité et l’endurance au travail.\n" +
                        "Vous pouvez modifier la durée des sessions de travail, de pause ainsi que le nombre de session à effectuer selon vos besoins, en appuyant sur le bouton paramètre. \n" +
                        "Chaque session pourra être mise en pause voire remise à zéro. A chaque début et fin de pause, une sonnerie retentit vous notifier.\n" +
                        "En allant dans « STATISTIQUES », vous pourrez voir les statistiques du jour et du mois (chiffre et en graphique) pour un jour sélectionner à partir du calendrier.");
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

    public void saveStatistics() {
        tsDAO.saveSessionStatistics(tsInser);
        tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date())); // IMPORTANT -> réinitialise le tjr après la sauvegarde
    }


    public void startTravail(View view) {
        sessionTravail = true;
        resetPossible = true;
        tNomTvl.setVisibility(View.VISIBLE);
        tNomRep.setVisibility(View.INVISIBLE);
        tSessionTravail.setText(String.valueOf(nbTravail));
        if(nbSession==0) tNbSessionPersonnalise.setText("∞");
        else tNbSessionPersonnalise.setText(String.valueOf(nbSession));

        decrementation = new CountDownTimer(tempsRestant, 1000) {          //compte à rembourd
            @Override
            public void onTick(long tempsNecessaire) {
                tempsRestant = tempsNecessaire;
                actualisationTimer();

                bStartRepos.setVisibility(View.INVISIBLE);
                iMStartRepos.setVisibility(View.INVISIBLE);
                bStartTravail.setVisibility(View.INVISIBLE);
                iMStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.VISIBLE);
                iMPauseTravail.setVisibility(View.VISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                iMPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
                iMInitTpsTravail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                alarme();
                finSessionTravail = true;

                bStartTravail.setVisibility(View.INVISIBLE);
                iMStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                iMPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                iMPauseRepos.setVisibility(View.INVISIBLE);
                bStartRepos.setVisibility(View.VISIBLE);
                iMStartRepos.setVisibility(View.VISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
                iMInitTpsTravail.setVisibility(View.INVISIBLE);

                nbTravail = nbTravail + 1;
                tSessionTravail.setText(String.valueOf(nbTravail));

                // TODO -        BDD TEMPS TRAVAIL avec var debut
                tsInser.setTpsTravail(tsInser.getTpsTravail() + debut);
                saveStatistics();

                // TODO -       TESTS
                System.out.println("temps du debut: " + debut);
                System.out.println("temps de travail : " + tsInser.getTpsTravail());

                // TODO -       BDD NB SESSIONS
                tsInser.setNbSessionsTravail(tsInser.getNbSessionsTravail() + 1);
                saveStatistics();

                // TODO -       TESTS
                System.out.println("nombre de session : " + tsInser.getNbSessionsTravail());
            }

        }.start();
    }


    public void startRepos(View view) {
        sessionRepos = true;
        tNomRep.setVisibility(View.VISIBLE);
        tNomTvl.setVisibility(View.INVISIBLE);
        resetPossible = true;
        if (finSessionTravail) initTpsRepos(view);
        decrementation = new CountDownTimer(tempsRestant, 1000) {          //compte à rembourd
            @Override
            public void onTick(long tempsNecessaire) {
                tempsRestant = tempsNecessaire;
                actualisationTimer();

                bStartTravail.setVisibility(View.INVISIBLE);
                iMStartTravail.setVisibility(View.INVISIBLE);
                bStartRepos.setVisibility(View.INVISIBLE);
                iMStartRepos.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.VISIBLE);
                iMPauseRepos.setVisibility(View.VISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                iMPauseTravail.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
                iMInitTpsTravail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                alarme();

                bStartRepos.setVisibility(View.INVISIBLE);
                iMStartRepos.setVisibility(View.INVISIBLE);
                bStartTravail.setVisibility(View.INVISIBLE);
                iMStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                iMPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                iMPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.VISIBLE);
                iMInitTpsTravail.setVisibility(View.VISIBLE);

                //TODO-         BDD TEMPS DE PAUSE
                tsInser.setTpsPause(tsInser.getTpsPause() + debut);
                saveStatistics();
                tsInser.setTpsPause(tsInser.getTpsPause() + nbTpsGrandePause);
                saveStatistics();

                //TODO-         TESTS
                System.out.println("temps de pause : " + tsInser.getTpsPause());
            }

        }.start();
    }

    public void alarme() {
        son = MediaPlayer.create(TimerActivity.this, R.raw.alarme);
        son.start();
        sonActive = true;
    }

    public void initTpsRepos(View view) {
        // debut=4000; // 300000 millis exemple 4000
        resetPossible = true;
        if (nbTravail == 4) {
            if (sessionPersonnaliser && nbTpsGrandePause != -1) {
                debut = nbTpsGrandePause * 60000;
                //nbPause=0;
            } else {
                debut = 4000;    // 1200000
                //nbPause =0;
            }
        } else {
            if (sessionPersonnaliser && tpsPausePerso != -1)
                debut = tpsPausePerso * 60000;
            else
                debut = 3000;   //300000
        }
        tempsRestant = debut;
        decrementation.cancel();
        actualisationTimer();
        son.reset();
        sonActive = false;
    }

    public void initTpsTravail(View view) {
        if (nbSession > 0 && nbTravail == nbSession) {
            resetPossible = true;
            reset(view);
        } else {
            if (sessionPersonnaliser && tpsTravailPerso != -1) debut = tpsTravailPerso * 60000;
            else debut = 1500000;
            resetPossible = true;
            tSessionTravail.setText(String.valueOf(nbTravail));
            decrementation.cancel();
            tempsRestant = debut;
            actualisationTimer();
            son.reset();
            sonActive = false;
            startTravail(view);
        }
    }

    public void pauseTravail(View view) {
        resetPossible = true;
        decrementation.cancel();
        bStartTravail.setVisibility(View.VISIBLE);
        iMStartTravail.setVisibility(View.VISIBLE);
        bStartRepos.setVisibility(View.INVISIBLE);
        iMStartRepos.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        iMPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        iMPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
        iMInitTpsTravail.setVisibility(View.INVISIBLE);
    }

    public void pauseRepos(View view) {
        resetPossible = true;
        decrementation.cancel();
        finSessionTravail = false;
        bStartRepos.setVisibility(View.VISIBLE);
        iMStartRepos.setVisibility(View.VISIBLE);
        bStartTravail.setVisibility(View.INVISIBLE);
        iMStartTravail.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        iMPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        iMPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
        iMInitTpsTravail.setVisibility(View.INVISIBLE);
    }

    public void reset(View view) {
        if (resetPossible) {
            nbTravail = 0;
            tSessionTravail.setText(String.valueOf(nbTravail));
            if(nbSession==0) tNbSessionPersonnalise.setText("∞");
            else tNbSessionPersonnalise.setText(String.valueOf(nbSession));
            if (sessionTravail || sessionRepos) decrementation.cancel();
            if (modif) {
                if (tpsTravailPerso != -1) debut = tpsTravailPerso * 60000;
                else debut = 1500000;
            } else debut = 1500000;
            tempsRestant = debut;
            actualisationTimer();

            bStartRepos.setVisibility(View.INVISIBLE);
            iMStartRepos.setVisibility(View.INVISIBLE);
            bStartTravail.setVisibility(View.VISIBLE);
            iMStartTravail.setVisibility(View.VISIBLE);
            bPauseRepos.setVisibility(View.INVISIBLE);
            iMPauseRepos.setVisibility(View.INVISIBLE);
            bPauseTravail.setVisibility(View.INVISIBLE);
            iMPauseTravail.setVisibility(View.INVISIBLE);
            bInitTpsTravail.setVisibility(View.INVISIBLE);
            iMInitTpsTravail.setVisibility(View.INVISIBLE);
            iMInitTpsTravail.setVisibility(View.INVISIBLE);
            if (sonActive) son.reset();
            tNomTvl.setVisibility(View.VISIBLE);

            tNomRep.setVisibility(View.INVISIBLE);
        }
    }

    public void actualisationTimer() {
        int minutes = (int) (tempsRestant / 1000) / 60;
        int secondes = (int) (tempsRestant / 1000) % 60;

        String timerFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, secondes);
        timer.setText(timerFormat);
    }


    public void personnaliser(View view) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        View dView = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        Button buttonConfirm = dView.findViewById(R.id.buttonConfirm);
        Button buttonCancel = dView.findViewById(R.id.buttonCancel);
        etNbSession = dView.findViewById(R.id.edit_nbSession);
        etTpsTravail = dView.findViewById(R.id.edit_tpsTravail);
        etTpsPause = dView.findViewById(R.id.edit_tpsPause);
        etTpsGrandePause = dView.findViewById(R.id.edit_tpsGrandePause);
        sessionPersonnaliser = true;

        if (modif) {
            // affichage dans le dialog
            etNbSession.setText(affichageNbSession);
            etTpsTravail.setText(affichageTpsTravail);
            etTpsPause.setText(affichageTpsPause);
            etTpsGrandePause.setText(affichageTpsGrandePause);
        }

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recupNbSession = etNbSession.getText().toString();
                if (recupNbSession.equals("")) {
                    nbSession = 0;
                    affichageNbSession = "";
                } else {
                    nbSession = Integer.parseInt(recupNbSession);
                    affichageNbSession = etNbSession.getText().toString();
                    modif = true;
                }

                String recupTpsTravail = etTpsTravail.getText().toString();
                if (recupTpsTravail.equals("")) {
                    tpsTravailPerso = -1;
                    affichageTpsTravail = "";
                } else {
                    tpsTravailPerso = Integer.parseInt(recupTpsTravail);
                    affichageTpsTravail = etTpsTravail.getText().toString();
                    modif = true;
                }

                String recupTpsPause = etTpsPause.getText().toString();
                if (recupTpsPause.equals("")) {
                    tpsPausePerso = -1;
                    affichageTpsPause = "";
                } else {
                    tpsPausePerso = Integer.parseInt(recupTpsPause);
                    affichageTpsPause = etTpsPause.getText().toString();
                    modif = true;
                }

                String recupTpsGrandePause = etTpsGrandePause.getText().toString();
                if (recupTpsGrandePause.equals("")) {
                    nbTpsGrandePause = -1;
                    affichageTpsGrandePause = "";
                } else {
                    nbTpsGrandePause = Integer.parseInt(recupTpsGrandePause);
                    affichageTpsGrandePause = etTpsGrandePause.getText().toString();
                    modif = true;
                }

                if (tpsTravailPerso != -1) {
                    debut = tpsTravailPerso * 60000;
                    tempsRestant = debut;
                    actualisationTimer();
                    resetPossible = true;
                    //reset(view);
                } else {
                    debut = 8000;
                    System.out.println("d else");
                    resetPossible = true;
                    reset(view);
                }

                if(nbSession==0) tNbSessionPersonnalise.setText("  ∞");
                else tNbSessionPersonnalise.setText(String.valueOf(nbSession));
                actualisationTimer();
                dialog.cancel();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionTravail) {
                    dialog.cancel();
                    startTravail(view);
                } else if (sessionRepos) {
                    dialog.cancel();
                    startRepos(view);
                } else dialog.cancel();
            }
        });

        dBuilder.setView(dView);
        dialog = dBuilder.create();

        if (sessionTravail) {
            pauseTravail(view);
            dialog.show();
        } else if (sessionRepos) {
            pauseRepos(view);
            dialog.show();
        } else dialog.show();
    }

    /*public void customizeActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setSubtitle("Timer");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }*/

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
            //Intent i = new Intent(this, ConfigActivity.class);
            //i.putExtra("notificationId", 101);
            this.startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}