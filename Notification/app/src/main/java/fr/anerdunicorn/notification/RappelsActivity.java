package fr.anerdunicorn.notification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RappelsActivity extends AppCompatActivity {

    //Variables
    public ListView listView;
    public CustomNotificationButtonAdapter adapter;
    public int id;
    public List<CustomNotificationButton> customNotifications;
    public List<Integer> notificationsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rappels);

        //Connexion à la base de données
        final NotificationDatabaseManager notificationDatabaseManager = new NotificationDatabaseManager(this);
        notificationDatabaseManager.open();

        //Initialisation des variables
        notificationsId = new ArrayList<>();
        listView = findViewById(R.id.listView);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        //Initialisation de la lisre des CustomNotificationButtons
        customNotifications = new ArrayList<>();

        //Création d'un adapter pour la ListView
        adapter = new CustomNotificationButtonAdapter(this, getApplicationContext(), customNotifications);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(!customNotifications.isEmpty())
                    RappelsActivity.this.getWindow().getDecorView().findViewById(R.id.textViewNoCustomNotification).setVisibility(View.INVISIBLE);
                else
                    RappelsActivity.this.getWindow().getDecorView().findViewById(R.id.textViewNoCustomNotification).setVisibility(View.VISIBLE);
            }
        });
        listView.setAdapter(adapter);

        //Initialisation des CustomNotificationButton déjà existants
        for(int i = 1; i < 100; i++){
            Notification notification = notificationDatabaseManager.getNotification(i);
            if(notification != null)
                customNotifications.add(new CustomNotificationButton(i, notification.getContent()));
        }
        adapter.notifyDataSetChanged();

        //Création d'un CustomNotificationButton quand on clique sur le bouton d'ajout
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialisation du EditText
                final EditText input = new EditText(getApplicationContext());
                input.setHint("Entrez le contenu de la notification");
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.requestFocus();

                //Affiche le clavier à l'ouverture de la fenetre de dialogue


                //Création du dialogue permettant d'entrer le contenu de la notification
                AlertDialog.Builder builder = new AlertDialog.Builder(RappelsActivity.this);

                //Création du CustomNotificationButton lorsqu'on valide
                builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Récupération de la liste des ids déja utilisés
                        notificationsId.clear();
                        for(CustomNotificationButton customNotificationButton : customNotifications)
                            notificationsId.add(customNotificationButton.getId());

                        //Récupération du premier id non utilisé
                        id = 1;
                        while(notificationsId.contains(id))
                            id++;

                        //Instanciation du CustomNotificationButton
                        CustomNotificationButton customNotificationButton = new CustomNotificationButton(id, input.getText().toString());

                        //Ajout du CustomNotificationButton à la liste
                        customNotifications.add(customNotificationButton);

                        //Sauvegarde des données dans la base de données
                        Notification notification = new Notification(id, "Notification", customNotificationButton.getContent(), 1, 8, 0, 0, -1, -1, -1, -1);
                        NotificationDatabaseManager notificationDatabaseManager1 = new NotificationDatabaseManager(getApplicationContext());
                        notificationDatabaseManager.open();
                        notificationDatabaseManager.addNotification(notification);
                        notificationDatabaseManager.close();

                        //Refresh de l'adapter
                        adapter.notifyDataSetChanged();
                    }
                });

                //Annulation de la création du CustomNotificationButton
                builder.setNegativeButton("Annuler", null);

                //Setup de la View du dialogue
                builder.setView(input);

                //Création du dialogue
                Dialog dialog = builder.create();

                //Affichage du dialogue
                dialog.show();
            }
        });

        //Fermeture de l'accès à la base de données
        notificationDatabaseManager.close();
    }

}
