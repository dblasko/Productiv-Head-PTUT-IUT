package com.example.timer;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StatisticsActivity extends AppCompatActivity {


    private TextView tStatSessionJour;
    private TextView tStatTravailJour;
    private TextView tStatReposJour;
    private TextView tStatSessionMois;
    private TextView tStatTravailMois;
    private TextView tStatReposMois;
    private Button bChangerDate;
    Calendar calendar;
    TextView tdatePicker;
    String recupDate;
    String recupJour;
    DatePickerDialog datePicker;

    TimerStatisticsDAO tsDAO = new TimerStatisticsDAO(this);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private Toolbar toolbar;
    private WorkModeManager wmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stat);

        /* BARRE */

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Statistiques");

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });

        /* Flèche retour */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /* Fin flèche */

        /* FIN BARRE */

        tsDAO.open();

        tStatSessionJour = findViewById(R.id.texteSessionJour);
        tStatTravailJour = findViewById(R.id.texteTravailJour);
        tStatReposJour = findViewById(R.id.texteReposJour);
        tStatSessionMois = findViewById(R.id.texteSessionMois);
        tStatTravailMois = findViewById(R.id.texteTravailMois);
        tStatReposMois = findViewById(R.id.texteReposMois);
        tdatePicker = findViewById(R.id.texteDatePicker);
        bChangerDate = findViewById(R.id.buttonChangerDate);

        statistiques();


    }

    public void statistiques() {

        tdatePicker.setText(getDate());
        affichageStats(getDate());

        // TODO -       Dialog avec le DatePicker

        bChangerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int cJour = calendar.get(Calendar.DAY_OF_MONTH);
                int cMois = calendar.get(Calendar.MONTH);
                int cAnnee = calendar.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(StatisticsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if(day<10) recupJour=("0" + String.valueOf(day));
                        else recupJour=String.valueOf(day);
                        tdatePicker.setText(year + "-" + (month + 1) + "-" + recupJour);
                        recupDate = (String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + recupJour);
                        affichageStats(recupDate);

                    }
                }, cAnnee, cMois, cJour);
                datePicker.show();
            }
        });


    }

    public void affichageStats(String date) {
        DecimalFormat dfs = new DecimalFormat("###.##");
        DecimalFormat df = new DecimalFormat("###.##");

        System.out.println("essaie "+date);

        //TODO -        STATISTIQUES DU JOUR

        PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        PieChartData pieChartData = new PieChartData(pieData);

        tStatSessionJour.setText(String.valueOf(df.format(tsDAO.getNbSession(date))));
        tStatTravailJour.setText(String.valueOf((df.format(tsDAO.getTpsTravail(date)/60000))));
        tStatReposJour.setText(String.valueOf((df.format(tsDAO.getTpsPause(date)/60000))));

        if (tsDAO.getTpsTravail(date) == 0.0 && tsDAO.getTpsPause(date) == 0.0)
            pieData.add(new SliceValue(100, Color.GRAY).setLabel("Aucune sesssion effectuée"));
        else if (tsDAO.getTpsTravail(date) == 0.0 && tsDAO.getTpsPause(date) != 0.0)
            pieData.add(new SliceValue(100, Color.YELLOW).setLabel("Pause [100%]"));
        else if (tsDAO.getTpsPause(date) == 0.0 && tsDAO.getTpsTravail(date) != 0.0)
            pieData.add(new SliceValue(100, Color.GREEN).setLabel("Travail [100%]"));
        else {
            float pourcentageTvail = (100*(tsDAO.getTpsTravail(date)/60000))/((tsDAO.getTpsTravail(date)/60000)+(tsDAO.getTpsPause(date)/60000));
            float pourcentageRepos = (100*(tsDAO.getTpsPause(date)/60000))/((tsDAO.getTpsTravail(date)/60000)+(tsDAO.getTpsPause(date)/60000));
            pieData.add(new SliceValue(tsDAO.getTpsTravail(date) / 60000, Color.GREEN).setLabel("Travail " + "[" + String.valueOf(df.format(pourcentageTvail))+ "%]"));
            pieData.add(new SliceValue(tsDAO.getTpsPause(date) / 60000, Color.YELLOW).setLabel("Pause " + "[" + String.valueOf(df.format(pourcentageRepos))+ "%]"));
        }
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);

        //TODO -        STATISTIQUES DU MOIS

        tStatSessionMois.setText(String.valueOf(dfs.format(tsDAO.getNbSessionsMoisAnnee(date.substring(0, 7)))));
        tStatTravailMois.setText(String.valueOf((df.format(tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7)) / 60000))));
        tStatReposMois.setText(String.valueOf((df.format(tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7)) / 60000))));

        PieChartView pieChartView2 = findViewById(R.id.chart2);
        List<SliceValue> pieData2 = new ArrayList<>();
        PieChartData pieChartData2 = new PieChartData(pieData2);

        if (tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7)) == 0 && tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7)) == 0)
            pieData2.add(new SliceValue(100, Color.GRAY).setLabel("Aucune sesssion effectuée"));
        else if (tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7)) == 0 && tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7)) != 0)
            pieData2.add(new SliceValue(100, Color.YELLOW).setLabel("Pause [100%]"));
        else if (tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7)) == 0 && tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7)) != 0)
            pieData2.add(new SliceValue(100, Color.GREEN).setLabel("Travail [100%]"));
        else {
            float pourcentageTravailMois = (100*(tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7))/60000))/((tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7))/60000)+(tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7))/60000));
            float pourcentageReposMois = (100*(tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7))/60000))/((tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7))/60000)+(tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7))/60000));
            pieData2.add(new SliceValue((tsDAO.getTpsTravailMoisAnnee(date.substring(0, 7))) / 60000, Color.GREEN).setLabel("Travail " + "[" + String.valueOf(df.format(pourcentageTravailMois))+ "%]"));
            pieData2.add(new SliceValue((tsDAO.getTpsPauseMoisAnnee(date.substring(0, 7))) / 60000, Color.YELLOW).setLabel("Pause " + "[" + String.valueOf(df.format(pourcentageReposMois))+ "%]"));
        }

        pieChartData2.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData2.setHasCenterCircle(true);
        pieChartView2.setPieChartData(pieChartData2);
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

}
