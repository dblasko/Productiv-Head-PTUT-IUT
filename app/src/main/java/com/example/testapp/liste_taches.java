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
     ArrayList<String> liste = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_taches);


        
        //Intialisation de l'accès à la BD pour récupérer toutes les tâches créées
        final TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Ajout de toutes les tâches à la liste
        for(Taches tache : tachesDAO.getAllTaches()) {
            liste.add(tache.getNom());
        }
        //Création de l'adapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, simple_list_item_1, liste);
        ListView list = (ListView) findViewById(R.id.listeT);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Toast.makeText(liste_taches.this, "ça marche", Toast.LENGTH_SHORT).show();
                Intent affichage= new Intent(liste_taches.this, affichage_tache.class);

                String nomItemChoisi= liste.get(i);
                Taches tache;
                tache = tachesDAO.getTache(nomItemChoisi);
                int identifiantTacheSelectionne=tache.getId();
                //affichage.putExtra("nomTache", tache.getNom());

                Toast.makeText(liste_taches.this,tache.getNom(), Toast.LENGTH_SHORT).show();

                startActivity(affichage);

            }

        });
        adapter.notifyDataSetChanged();





    }
}
/*
    }



    protected ListView getListView() {
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
            return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }

        */



