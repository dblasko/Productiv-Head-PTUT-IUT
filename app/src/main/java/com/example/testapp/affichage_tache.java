package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class affichage_tache extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_tache);

        EditText nomTache = findViewById(R.id.nomTache);
        EditText dateDeb = findViewById(R.id.dateDeb);
        EditText dateFin = findViewById(R.id.dateFin);
        EditText heureDeb = findViewById(R.id.heureDeb);
        EditText resume = findViewById(R.id.resume);

        //Récupération de l'id de la tache
        int id = getIntent().getIntExtra("idTache", 0);

        //Intialisation de l'accès à la BD pour récupérer toutes les tâches créées
        final TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Récupération de la Tache en BD
        Taches tache = tachesDAO.getTache(id);

        nomTache.setText(tache.getNom());
        dateDeb.setText(tache.getDateDebut());
        dateFin.setText(tache.getDateFin());
        heureDeb.setText(tache.getHeure());
        resume.setText(tache.getResume());
    }
}
