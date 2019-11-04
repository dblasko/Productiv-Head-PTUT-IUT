package com.example.timer;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimerActivity extends AppCompatActivity {

    private int debut = 1500000;           // en millis    1500000  exemple 8000

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
        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bStartRepos.setVisibility(View.INVISIBLE);
        bStartTravail.setVisibility(View.VISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
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


        /* TESTS BD */

/*

        TimerStatistics tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));
        TimerStatistics tsInser2 = new TimerStatistics(20.5f, 3.4f, 10, sdf.format(new Date()));
        tsInser.setNbSessionsTravail(tsInser.getNbSessionsTravail()+1);
        tsDAO.saveSessionStatistics(tsInser);
        tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));

        tsDAO.saveSessionStatistics(tsInser);
        tsDAO.saveSessionStatistics(tsInser2); // devrait update et pas insert
        tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date())); // IMPORTANT -> réinitialise le tjr après la sauvegarde
        tsInser2 = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));

        TimerStatistics donneesDuJour = tsDAO.getTimerStatistics(sdf.format(new Date()));
        System.out.println("FLAG RECHERCHE " + donneesDuJour.getTpsTravail() + " " + donneesDuJour.getDate()); // devrait être date ajd et temps travail somme des 2
*/
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
                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.VISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                alarme();
                finSessionTravail = true;

                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bStartRepos.setVisibility(View.VISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);

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
                bStartRepos.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.VISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                alarme();

                bStartRepos.setVisibility(View.INVISIBLE);
                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.VISIBLE);

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
                debut = 1200000;
                //nbPause =0;
            }
        } else {
            if (sessionPersonnaliser && tpsPausePerso != -1)
                debut = tpsPausePerso * 60000;
            else
                debut = 300000;
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
        bStartRepos.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
    }

    public void pauseRepos(View view) {
        resetPossible = true;
        decrementation.cancel();
        finSessionTravail = false;
        bStartRepos.setVisibility(View.VISIBLE);
        bStartTravail.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
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
            bStartTravail.setVisibility(View.VISIBLE);
            bPauseRepos.setVisibility(View.INVISIBLE);
            bPauseTravail.setVisibility(View.INVISIBLE);
            bInitTpsTravail.setVisibility(View.INVISIBLE);
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

                if(nbSession==0) tNbSessionPersonnalise.setText("∞");
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

}