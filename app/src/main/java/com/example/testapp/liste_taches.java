package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import static android.R.layout.simple_list_item_1;
import static android.R.layout.simple_spinner_item;

public class liste_taches extends AppCompatActivity {


    private ListView listeTaches;
    private ArrayList<String> liste;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_taches);

        //Intialisation de l'accès à la BD pour récupérer toutes les tâches créées
        final TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Instanciation de la liste
        liste = new ArrayList<>();

        //Ajout de toutes les tâches à la liste
        for (Taches tache : tachesDAO.getAllTaches()) {
            liste.add(tache.getNom());
        }

        //Création de l'adapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, simple_list_item_1, liste);

        //Ajout de l'adapter a la ListView
        ListView list = findViewById(R.id.listeT);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent affichage = new Intent(liste_taches.this, affichage_tache.class);

                String nomItemChoisi = liste.get(i);
                Taches tache = tachesDAO.getTache(nomItemChoisi);
                affichage.putExtra("idTache", tache.getId());

                startActivity(affichage);

            }

        });
    }

    /*protected ListView getListView() {
        if (listeTaches == null) {
            listeTaches = (ListView) findViewById(R.id.listeT);
        }
        return listeTaches;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }*/
}