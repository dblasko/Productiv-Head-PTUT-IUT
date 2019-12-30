package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

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


        //Intent intent = getIntent();
        //String nomTt = intent.getStringExtra(acceuil_todolist.nom);

        /*if (intent != null) {

            if (intent.hasExtra("nomTache")) {
                str = intent.getStringExtra("nomTache");
            }}

            liste.add(str);*/


        //Création de l'adapter pour la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, simple_list_item_1, liste);
        ListView list = (ListView) findViewById(R.id.listeT);
        list.setAdapter(adapter);

        //Intialisation de l'accès à la BD pour récupérer toutes les tâches créées
        TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Ajout de toutes les tâches à la liste
        for(String tache : tachesDAO.getAllTaches()) {
            liste.add(tache);
        }

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




