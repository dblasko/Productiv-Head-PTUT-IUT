package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                Intent intentListe = new Intent(affichage_tache.this, liste_taches.class);
                startActivity(intentListe);
            }
        });
        Button modifications= (Button) findViewById(R.id.enregistrer);
        modifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TachesDAO tachesDAO = new TachesDAO(affichage_tache.this);
                tachesDAO.open();
                int id = getIntent().getIntExtra("idTache", 0);
                tachesDAO.supprimerTache(id);
                //je recrée la tâche
                EditText editText = (EditText) findViewById(R.id.nomTache);
                String nomTt= editText.getText().toString();
                //date de début
                TextView dateD=(TextView) findViewById(R.id.dateDeb) ;
                String dateDS=dateD.getText().toString();
                //date de fin
                TextView dateF=(TextView) findViewById(R.id.dateFin) ;
                String dateFS=dateF.getText().toString();
                //heure de début
                TextView heure=(TextView) findViewById(R.id.heureDeb);
                String heureS=heure.getText().toString();

                EditText resume= (EditText) findViewById(R.id.resume);
                String resumeS= resume.getText().toString();

                Taches uneTache= new Taches(acceuil_todolist.compteurIdentifiant,dateDS,dateFS,heureS,nomTt,resumeS);
                acceuil_todolist.compteurIdentifiant=acceuil_todolist.compteurIdentifiant+1;
                //Initialisation de l'accès à la base de données pour sauvegarder la tâche créée


                //Sauvegarde de la tâche dans la bd
                tachesDAO.addTache(uneTache);
                Intent intentListe = new Intent(affichage_tache.this, liste_taches.class);
                startActivity(intentListe);
            }
        });

    }

}
