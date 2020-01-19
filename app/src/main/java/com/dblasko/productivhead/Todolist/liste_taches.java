package com.dblasko.productivhead.Todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dblasko.productivhead.DB.TachesDAO;
import com.dblasko.productivhead.R;
import com.dblasko.productivhead.WorkMode.WorkModeManager;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class liste_taches extends AppCompatActivity {


    private ArrayList<Taches> listeTaches;
    private ArrayList<String> liste;
    private Toolbar toolbar;
    private WorkModeManager wmm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_taches);

        /* BARRE */

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Liste des tâche");

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });

        /* FIN BARRE */

        //Intialisation de l'accès à la BD pour récupérer toutes les tâches créées
        final TachesDAO tachesDAO = new TachesDAO(this);
        tachesDAO.open();

        //Instanciation des listes
        listeTaches = new ArrayList<>();
        liste = new ArrayList<>();

        //Ajout de toutes les tâches aux listes
        for (Taches tache : tachesDAO.getAllTaches()) {
            listeTaches.add(tache);
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

                Taches tache = listeTaches.get(i);
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