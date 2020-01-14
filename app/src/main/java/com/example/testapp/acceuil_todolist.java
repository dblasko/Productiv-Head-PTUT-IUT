package com.example.testapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class acceuil_todolist extends AppCompatActivity{

    public static int compteurIdentifiant=1;
    public static final String nom ="rien pour linstant";
    private EditText text;
    private DatePickerDialog.OnDateSetListener dateurDeNotifDeb;
    private DatePickerDialog.OnDateSetListener dateurDeNotifFin;
    private TextView dateDebut;

    /*private ListView listeTache;
    private ArrayAdapter<String> adapter;
    private ArrayList<String>  arrayList;
    private EditText RecupnomTache;
    private Button btn;*/
    private TimePickerDialog.OnTimeSetListener timeurNotif;
    private TextView heure;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil_todolist);





//-------------TIMEUR DE DATE DE DEBUT !!!

        heure = (TextView) findViewById(R.id.heure);
        heure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(acceuil_todolist.this, R.style.timePickerDialogS,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        /*String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }*/
                        heure.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });





        dateDebut = (TextView) findViewById(R.id.debut);
        dateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendrier = Calendar.getInstance();
                int annee = calendrier.get(Calendar.YEAR);
                int mois = calendrier.get(Calendar.MONTH);
                int jour = calendrier.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogueur = new DatePickerDialog(acceuil_todolist.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateurDeNotifDeb, annee, mois, jour);
                dialogueur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogueur.show();

            }
        });

        dateurDeNotifDeb = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int annee, int mois, int jour) {
                mois= mois + 1;
                Log.d("MainActivity","onDateSet : jj/mm/yy : " + jour +"/"+mois+"/"+annee);
                String date;
                if(mois <10){
                    date = jour +"/0"+mois+"/"+annee;
                }
                else{
                    date= jour +"/"+mois+"/"+annee;
                }

                dateDebut.setText(date);
            }
        };

        //DEUXIEME DATE QUI EST LA DATE DE FIN DE TACHE
        final TextView dateFin = (TextView) findViewById(R.id.fin);
        dateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendrier = Calendar.getInstance();
                int annee = calendrier.get(Calendar.YEAR);
                int mois = calendrier.get(Calendar.MONTH);
                int jour = calendrier.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogueur = new DatePickerDialog(acceuil_todolist.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateurDeNotifFin, annee, mois, jour);
                dialogueur.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogueur.show();

            }
        });

        dateurDeNotifFin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int annee, int mois, int jour) {
                mois= mois + 1;
                Log.d("MainActivity","onDateSet : jj/mm/yy : " + jour +"/"+mois+"/"+annee);
                String date;
                if(mois <10){
                    date = jour +"/0"+mois+"/"+annee;
                }
                else{
                    date= jour +"/"+mois+"/"+annee;
                }

                dateFin.setText(date);
            }
        };
    }



    public void envoyer(View view){


        Intent intent = new Intent(this,liste_taches.class);
        //nom de la tache
        EditText editText = (EditText) findViewById(R.id.nomTache);
        String nomTt= editText.getText().toString();
        //date de début
        TextView dateD=(TextView) findViewById(R.id.debut) ;
        String dateDS=dateD.getText().toString();
        //date de fin
        TextView dateF=(TextView) findViewById(R.id.fin) ;
        String dateFS=dateF.getText().toString();
        //heure de début
        TextView heure=(TextView) findViewById(R.id.heure);
        String heureS=heure.getText().toString();
        //résumé de la tâche
        TextView resume=(TextView) findViewById(R.id.resumeA);
        String resumeA= resume.getText().toString();
        Taches uneTache= new Taches(compteurIdentifiant,dateDS,dateFS,heureS,nomTt,resumeA);
        compteurIdentifiant++;
        //Initialisation de l'accès à la base de données pour sauvegarder la tâche créée
        TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Sauvegarde de la tâche dans la bd
        tachesDAO.addTache(uneTache);

        Toast.makeText(getApplicationContext(), "C'est ajouté !", Toast.LENGTH_SHORT).show();

        startActivity(intent);

    }



    public void obtenirCalend(View v) {

        RadioButton notificationb = (RadioButton) findViewById(R.id.notificationb);
        boolean coche = ((RadioButton) v).isChecked();

        switch (v.getId()) {

            case R.id.notificationb:
                if (coche) {
                    Toast.makeText(getApplicationContext(), "Vous serez notifié !", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }



    public void liste_taches(View view){
        startActivity(new Intent(this,liste_taches.class));
    }
    }

