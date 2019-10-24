package com.example.timer;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TimerActivity extends AppCompatActivity {

    private int debut= 8000;           // en millis    1500000  exemple 8000


    private Button bStatistique;

    private CountDownTimer decrementation;

    private ImageButton bStartTravail;
    private ImageButton bStartRepos;
    private ImageButton bInitTpsTravail;
    private ImageButton bPauseTravail;
    private ImageButton bPauseRepos;
    private ImageButton bReset;
    private ImageButton bPersonnaliser;

    private TextView timer;
    private TextView tSessionTravail;

    private boolean finSessionTravail =true;
    private boolean sonActive =false;
    private boolean resetPossible=false;               //pas cancel au debut
    private boolean sessionTravail=false;           //pr le dialog
    private boolean sessionRepos = false;
    private boolean sessionPersonnaliser= false;

    private int nbPause =0;
    private int nbTravail=1;

    private long tempsRestant=debut; //****************************************************private long tempsRestant = debut;

    private MediaPlayer son;
    private AlertDialog dialog;

    private TextView tNomTvl;
    private TextView tNomRep;

    private int nbSession=0;
    private TextView tNbSession;
    private int nbTpsTravail=0;
    private int nbTpsPause=0;
    private int nbTpsGrandePause =0;


    private EditText etNbSession;
    private EditText etTpsTravail;
    private EditText etTpsPause;
    private EditText etTpsGrandePause;
    private String affichageNbSession ="";
    private String affichageTpsTravail="";
    private String affichageTpsPause="";
    private String affichageTpsGrandePause ="";
    private boolean modif =false;
    TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    TimerStatistics tsInser = new TimerStatistics(0f, 0f, 0, sdf.format(new Date()));






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customizeActionBar();




//***************************************************************************************************************************



       // TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
        tsDAO.open();
        // TODO - laulau précise explicitement l'unité de tpsTravail/tpsPause
        // EXEMPLE POUR TES DATES LAULAU :
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());








//***************************************************************************************************************************

        timer=findViewById(R.id.timer);
        bStartTravail =findViewById(R.id.buttonStartTravail);
        bInitTpsTravail =findViewById(R.id.buttonInitTpsTravail);
        bReset=findViewById(R.id.buttonReset);
        bStartRepos =findViewById(R.id.buttonStartRepos);
        bPauseTravail =findViewById(R.id.buttonPauseTravail);
        bPauseRepos=findViewById(R.id.buttonPauseRepos);
        tSessionTravail=findViewById(R.id.sessionTravail);
        tNomTvl =findViewById(R.id.nomTvl);
        tNomRep =findViewById(R.id.nomRep);

        //*****
        tNbSession=findViewById(R.id.nbSessionPersonnalise);

        bStatistique=findViewById(R.id.buttonStatistique);
        bPersonnaliser=findViewById(R.id.buttonPersonnaliser);

        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bStartRepos.setVisibility(View.INVISIBLE);
        bStartTravail.setVisibility(View.VISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
        bReset.setVisibility(View.VISIBLE);
        tNomTvl.setVisibility(View.INVISIBLE);
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
                statistque(view);
            }
        });

        bPersonnaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personnaliser(view);
            }
        });

        tNbSession.setText(String.valueOf(nbSession));
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




    public void startTravail(View view){

        tsInser.setTpsPause(tsInser.getTpsPause()+nbTpsPause);
        tsInser.setTpsPause(tsInser.getTpsPause()+nbTpsGrandePause);
        sessionTravail=true;
        resetPossible=true;
        tNomTvl.setVisibility(View.VISIBLE);
        tNomRep.setVisibility(View.INVISIBLE);
        tSessionTravail.setText(String.valueOf(nbTravail));
        tNbSession.setText(String.valueOf(nbSession));




        decrementation= new CountDownTimer(tempsRestant,1000){          //compte à rembourd
            @Override
            public void onTick(long tempsNecessaire){




                tempsRestant= tempsNecessaire;
                actualisationTimer();


                bStartRepos.setVisibility(View.INVISIBLE);
                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.VISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish(){
                alarme();
                finSessionTravail =true;


                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bStartRepos.setVisibility(View.VISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);
            }

        }.start();

    }


    public void startRepos(View view){
        sessionRepos =true;
        tNomRep.setVisibility(View.VISIBLE);
        tNomTvl.setVisibility(View.INVISIBLE);
        resetPossible=true;
        if (finSessionTravail)initTpsRepos(view);
        decrementation= new CountDownTimer(tempsRestant,1000){          //compte à rembourd
            @Override
            public void onTick(long tempsNecessaire){


                tempsRestant= tempsNecessaire;
                actualisationTimer();


                bStartTravail.setVisibility(View.INVISIBLE);
                bStartRepos.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.VISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFinish(){

                alarme();

                bStartRepos.setVisibility(View.INVISIBLE);
                bStartTravail.setVisibility(View.INVISIBLE);
                bPauseTravail.setVisibility(View.INVISIBLE);
                bPauseRepos.setVisibility(View.INVISIBLE);
                bInitTpsTravail.setVisibility(View.VISIBLE);
            }

        }.start();
    }


    public void alarme(){
        son = MediaPlayer.create(TimerActivity.this,R.raw.alarme);
        son.start();
        sonActive =true;
    }

    public void initTpsRepos(View view){

        // debut=4000; // 300000 millis exemple 4000
        resetPossible=true;
        if (nbPause ==3) {
            if(sessionPersonnaliser && nbTpsGrandePause!=1){
                debut=nbTpsGrandePause*60000;
                nbPause=0;
            }
            else {
                debut=6000;
                nbPause =0;
            }

        }
        else{
            nbPause++;
            if(sessionPersonnaliser && nbTpsPause!=-1){
                System.out.println("dans la boucle if ");
                debut= nbTpsPause*60000;
            }
            else{
                debut=4000;
                System.out.println("dans la boucle else");
            }
        }
        tempsRestant= debut;
        decrementation.cancel();
        actualisationTimer();
        son.reset();
        sonActive =false;
    }

    public void initTpsTravail(View view){
        // TODO-        BDD TRAVAIL 
        tsInser.setTpsTravail(tsInser.getTpsTravail() + debut);
        tsInser.setNbSessionsTravail(tsInser.getNbSessionsTravail()+nbSession);
        System.out.println("tps t" + debut);
        System.out.println("tps tvail : " +  tsInser.getTpsTravail());

        if(nbSession>0 && nbTravail==nbSession){
            resetPossible=true;
            reset(view);
        }
        else{
            nbTravail = nbTravail + 1;
            if(sessionPersonnaliser && nbTpsTravail!=-1) debut= nbTpsTravail*60000;
            else debut=8000;
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


    public void pauseTravail(View view){
        resetPossible=true;
        decrementation.cancel();
        bStartTravail.setVisibility(View.VISIBLE);
        bStartRepos.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
    }

    public void pauseRepos(View view){
        resetPossible=true;
        decrementation.cancel();
        finSessionTravail =false;
        bStartRepos.setVisibility(View.VISIBLE);
        bStartTravail.setVisibility(View.INVISIBLE);
        bPauseTravail.setVisibility(View.INVISIBLE);
        bPauseRepos.setVisibility(View.INVISIBLE);
        bInitTpsTravail.setVisibility(View.INVISIBLE);
    }

    public void reset(View view) {
        if (resetPossible) {
            nbTravail = 1;
            nbPause = 0;
            tSessionTravail.setText(String.valueOf(nbTravail));
            tNbSession.setText(String.valueOf(nbSession));
            if(sessionTravail || sessionRepos) decrementation.cancel();
            //debut=8000;
            tempsRestant = debut;
            actualisationTimer();
            bStartRepos.setVisibility(View.INVISIBLE);
            bStartTravail.setVisibility(View.VISIBLE);
            bPauseRepos.setVisibility(View.INVISIBLE);
            bPauseTravail.setVisibility(View.INVISIBLE);
            bInitTpsTravail.setVisibility(View.INVISIBLE);
            if (sonActive) son.reset();


        }
    }

    public void actualisationTimer(){


        int minutes= (int) (tempsRestant/1000)/60;
        int secondes= (int) (tempsRestant/1000)%60;

        String timerFormat= String.format(Locale.getDefault(),"%02d:%02d",minutes,secondes);
        timer.setText(timerFormat);
    }


    public void personnaliser(View view){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        View dView = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        Button buttonConfirm = dView.findViewById(R.id.buttonConfirm);
        Button buttonCancel = dView.findViewById(R.id.buttonCancel);
        etNbSession= dView.findViewById(R.id.edit_nbSession);
        etTpsTravail = dView.findViewById(R.id.edit_tpsTravail);
        etTpsPause = dView.findViewById(R.id.edit_tpsPause);
        etTpsGrandePause= dView.findViewById(R.id.edit_tpsGrandePause);
        sessionPersonnaliser=true;

        if(modif){
            etNbSession.setText(affichageNbSession);
            etTpsTravail.setText(affichageTpsTravail);
            etTpsPause.setText(affichageTpsPause);
            etTpsGrandePause.setText(affichageTpsGrandePause);
        }

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recupNbSession = etNbSession.getText().toString();
                if (recupNbSession.equals("")){
                    nbSession=0;
                    affichageNbSession="";
                }
                else {
                    nbSession= Integer.parseInt(recupNbSession);
                    affichageNbSession =etNbSession.getText().toString();
                    modif=true;
                }

                String recupTpsTravail = etTpsTravail.getText().toString();
                if (recupTpsTravail.equals("")){
                    nbTpsTravail=-1;
                    affichageTpsTravail="";
                }
                else {
                    nbTpsTravail = Integer.parseInt(recupTpsTravail);
                    affichageTpsTravail =etTpsTravail.getText().toString();
                    modif=true;
                    debut=nbTpsTravail*6000;
                }

                String recupTpsPause = etTpsPause.getText().toString();
                if (recupTpsPause.equals("")){
                    nbTpsPause=-1;
                    affichageTpsPause="";
                }
                else {
                    nbTpsPause = Integer.parseInt(recupTpsPause);
                    affichageTpsPause =etTpsPause.getText().toString();
                    modif=true;
                }

                String recupTpsGrandePause = etTpsGrandePause.getText().toString();
                if (recupTpsGrandePause.equals("")){
                    nbTpsGrandePause =-1;
                    affichageTpsGrandePause ="";
                }
                else {
                    nbTpsGrandePause = Integer.parseInt(recupTpsGrandePause);
                    affichageTpsGrandePause =etTpsGrandePause.getText().toString();
                    modif=true;
                }

                if(nbTpsTravail!=-1) {
                    debut=nbTpsTravail*60000;
                    resetPossible=true;
                    reset(view);
                }
                else {
                    debut=8000;
                    resetPossible=true;
                    reset(view);
                }

                tNbSession.setText(String.valueOf(nbSession));
                actualisationTimer();
                dialog.cancel();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionTravail){
                    dialog.cancel();
                    startTravail(view);
                }
                else if(sessionRepos){
                    dialog.cancel();
                    startRepos(view);
                }
                else dialog.cancel();
            }
        });

        dBuilder.setView(dView);
        dialog = dBuilder.create();


        if(sessionTravail){
            pauseTravail(view);
            dialog.show();
        }
        else if (sessionRepos){
            pauseRepos(view);
            dialog.show();
        }
        else dialog.show();

    }

    //*************************************************************************************

    public void statistque(View view){

        View sView = getLayoutInflater().inflate(R.layout.layout_stat, null);
        setContentView(sView);
    }

    public void customizeActionBar(){
        // Customizes the actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setSubtitle("Timer");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }


}
