package com.example.timer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class TimerActivity extends AppCompatActivity {

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
    //private int nbPause =0;


    private long tempsRestant = debut; //****************************************************private long tempsRestant = debut;

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

    private TextView tStatSessionJour;
    private TextView tStatTravailJour;
    private TextView tStatReposJour;

    private TextView tStatDate;
    private TextView tStatDateMois;

    private TextView tStatSessionMois;
    private TextView tStatTravailMois;
    private TextView tStatReposMois;

    private Button bChangerDate;
    Calendar calendar;
    DatePickerDialog datePicker;
    TextView tdatePicker;
    String recupDate;

    private boolean changeDate = false;

    TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    TimerStatistics tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timer_activity);
        customizeActionBar();


//***************************************************************************************************************************


        // TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
        tsDAO.open();
        // TODO - laulau précise explicitement l'unité de tpsTravail/tpsPause
        // EXEMPLE POUR TES DATES LAULAU :
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //sdf.setTimeZone(TimeZone.getDefault());


//***************************************************************************************************************************

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

        //*****
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
                statistiques(view);
            }
        });

        bPersonnaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personnaliser(view);
            }
        });


        tNbSessionPersonnalise.setText(String.valueOf(nbSession));
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
        tNbSessionPersonnalise.setText(String.valueOf(nbSession));


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
                debut = 6000;
                //nbPause =0;
            }

        } else {
            //nbPause++;
            if (sessionPersonnaliser && tpsPausePerso != -1) {
                System.out.println("dans la boucle if ");
                debut = tpsPausePerso * 60000;
            } else {
                debut = 4000;
                System.out.println("dans la boucle else");
            }
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
            //nbTravail = nbTravail + 1;
            if (sessionPersonnaliser && tpsTravailPerso != -1) debut = tpsTravailPerso * 60000;
            else debut = 8000;
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

            //nbPause = 0;
            tSessionTravail.setText(String.valueOf(nbTravail));
            tNbSessionPersonnalise.setText(String.valueOf(nbSession));
            if (sessionTravail || sessionRepos) decrementation.cancel();
            if (modif) {
                if (tpsTravailPerso != -1) debut = tpsTravailPerso * 60000;
                else debut = 8000;
            } else debut = 8000;
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

                tNbSessionPersonnalise.setText(String.valueOf(nbSession));
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


    public void statistiques(View view) {


        View sView = getLayoutInflater().inflate(R.layout.layout_stat, null);
        setContentView(sView);
        tStatSessionJour = sView.findViewById(R.id.texteSessionJour);
        tStatTravailJour = sView.findViewById(R.id.texteTravailJour);
        tStatReposJour = sView.findViewById(R.id.texteReposJour);

//        tStatDate = sView.findViewById(R.id.texteDate);

        tStatSessionMois = sView.findViewById(R.id.texteSessionMois);
        tStatTravailMois = sView.findViewById(R.id.texteTravailMois);
        tStatReposMois = sView.findViewById(R.id.texteReposMois);




// TODO -       Dialog avec le DatePicker
        bChangerDate = sView.findViewById(R.id.buttonChangerDate);
        tdatePicker = sView.findViewById(R.id.texteDatePicker);


        bChangerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate=true;
                calendar = Calendar.getInstance();
                int cJour = calendar.get(Calendar.DAY_OF_MONTH);
                int cMois = calendar.get(Calendar.MONTH);
                int cAnnee = calendar.get(Calendar.YEAR);


                datePicker = new DatePickerDialog(TimerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        tdatePicker.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, cAnnee, cMois, cJour);
                datePicker.show();

               

            }

        });

        if(!changeDate)
            tdatePicker.setText(getDate());



            recupDate = tdatePicker.getText().toString();



//        tStatDate.setText(getDate());
        //tsDAO.open();         On ouvre la BDD en haut

        DecimalFormat dfs = new DecimalFormat("###.##");
        DecimalFormat df = new DecimalFormat("###.##");

        System.out.println("date " + tdatePicker);


        PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        PieChartData pieChartData = new PieChartData(pieData);
/*
        System.out.println("d mois " + tsDAO.getDateMois("2019-10"));
        System.out.println("d mois " + tsDAO.getDateMois(getDate().substring(0, 7)));

         String recupNbSession = etNbSession.getText().toString();
                if (recupNbSession.equals("")) {
                    nbSession = 0;
                    affichageNbSession = "";
                } else {
                    nbSession = Integer.parseInt(recupNbSession);
                    affichageNbSession = etNbSession.getText().toString();
                    modif = true;
                }
*/
        String recupDateDuJour = getDate();
        System.out.println("j " + recupDateDuJour + " " + recupDate);
       if(recupDate!=recupDateDuJour && recupDate!=null){
           System.out.println("j dans if");
        tStatSessionJour.setText(String.valueOf(df.format(tsDAO.getNbSession(recupDate))));
        tStatTravailJour.setText(String.valueOf((df.format(tsDAO.getTpsTravail(recupDate) / 60000))));
        tStatReposJour.setText(String.valueOf((df.format(tsDAO.getTpsPause(recupDate) / 60000))));

        if (tsDAO.getTpsTravail(recupDate) == 0.0 && tsDAO.getTpsPause(recupDate) == 0.0)
            pieData.add(new SliceValue(100, Color.GRAY).setLabel("Aucune sesssion effectuée"));
        else if (tsDAO.getTpsTravail(recupDate) == 0.0 && tsDAO.getTpsPause(recupDate) != 0.0)
            pieData.add(new SliceValue(100, Color.YELLOW).setLabel("Pause"));
        else if (tsDAO.getTpsPause(recupDate) == 0.0 && tsDAO.getTpsTravail(recupDate) != 0.0)
            pieData.add(new SliceValue(100, Color.GREEN).setLabel("Travail"));
        else {
            pieData.add(new SliceValue(tsDAO.getTpsTravail(getDate()) / 60000, Color.GREEN).setLabel("Travail"));
            pieData.add(new SliceValue(tsDAO.getTpsPause(getDate()) / 60000, Color.YELLOW).setLabel("Pause"));
        }
    }else {
        tStatSessionJour.setText(String.valueOf(dfs.format(tsDAO.getNbSession(getDate()))));
        tStatTravailJour.setText(String.valueOf((df.format(tsDAO.getTpsTravail(getDate()) / 60000))));
        tStatReposJour.setText(String.valueOf((df.format(tsDAO.getTpsPause(getDate()) / 60000))));

        if (tsDAO.getTpsTravail(getDate()) == 0.0 && tsDAO.getTpsPause(getDate()) == 0.0)
            pieData.add(new SliceValue(100, Color.GRAY).setLabel("Aucune sesssion effectuée"));
        else if (tsDAO.getTpsTravail(getDate()) == 0.0 && tsDAO.getTpsPause(getDate()) != 0.0)
            pieData.add(new SliceValue(100, Color.YELLOW).setLabel("Pause"));
        else if (tsDAO.getTpsPause(getDate()) == 0.0 && tsDAO.getTpsTravail(getDate()) != 0.0)
            pieData.add(new SliceValue(100, Color.GREEN).setLabel("Travail"));
        else {
            pieData.add(new SliceValue(tsDAO.getTpsTravail(getDate()) / 60000, Color.GREEN).setLabel("Travail"));
            pieData.add(new SliceValue(tsDAO.getTpsPause(getDate()) / 60000, Color.YELLOW).setLabel("Pause"));
        }
    }
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);

       // System.out.println("date : " + getDate().substring(0, 7) + tsDAO.getDate(getDate().substring(0, 7)));
        //System.out.println("date :  " + tsDAO.getDateMois(getDate().substring(0,7)));
//*****************************todo - FAIRE AVEC MOIS*************************************************************************************
        tStatSessionMois.setText(String.valueOf(dfs.format(tsDAO.getNbSessionsMoisAnnee(getDate().substring(0,7)))));
        tStatTravailMois.setText(String.valueOf((df.format(tsDAO.getTpsTravailMoisAnnee(getDate().substring(0,7))/ 60000))));
        tStatReposMois.setText(String.valueOf((df.format(tsDAO.getTpsPauseMoisAnnee(getDate().substring(0,7)) / 60000))));

        PieChartView pieChartView2 = findViewById(R.id.chart2);
        List<SliceValue> pieData2 = new ArrayList<>();
        PieChartData pieChartData2 = new PieChartData(pieData2);

        if (tsDAO.getTpsTravailMoisAnnee(getDate().substring(0,7)) == 0 && tsDAO.getTpsPauseMoisAnnee(getDate().substring(0,7)) == 0)
            pieData2.add(new SliceValue(100, Color.GRAY).setLabel("Aucune sesssion effectuée"));
        else if (tsDAO.getTpsTravailMoisAnnee(getDate().substring(0,7)) == 0 && tsDAO.getTpsPauseMoisAnnee(getDate().substring(0,7)) != 0)
            pieData2.add(new SliceValue(100, Color.YELLOW).setLabel("Pause"));
        else if (tsDAO.getTpsPauseMoisAnnee(getDate().substring(0,7)) == 0 && tsDAO.getTpsTravailMoisAnnee(getDate().substring(0,7)) != 0)
            pieData2.add(new SliceValue(100, Color.GREEN).setLabel("Travail"));
        else {
            pieData2.add(new SliceValue((tsDAO.getTpsTravailMoisAnnee(getDate().substring(0,7))) / 60000, Color.GREEN).setLabel("Travail"));
            pieData2.add(new SliceValue((tsDAO.getTpsPauseMoisAnnee(getDate().substring(0,7))) / 60000, Color.YELLOW).setLabel("Pause"));
        }

        pieChartData2.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData2.setHasCenterCircle(true);
        pieChartView2.setPieChartData(pieChartData2);




    }

    public void customizeActionBar() {
        // Customizes the actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setSubtitle("Timer");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

}


