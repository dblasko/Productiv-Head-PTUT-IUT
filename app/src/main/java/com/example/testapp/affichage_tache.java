package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        Button boutonSupp= (Button) findViewById(R.id.button2);
        boutonSupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "C'est ajouté !", Toast.LENGTH_SHORT).show();
                final TachesDAO tachesDAO = new TachesDAO(affichage_tache.this);
                tachesDAO.open();
                int id = getIntent().getIntExtra("idTache", 0);
                tachesDAO.supprimerTache(id);
            }
        });

    }

}
