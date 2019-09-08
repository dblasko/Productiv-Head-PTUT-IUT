package fr.anerdunicorn.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomNotificationButtonAdapter extends ArrayAdapter<CustomNotificationButton> {

    //Variables
    private Activity activity;
    private List<CustomNotificationButton> customNotificationButtons;

    public CustomNotificationButtonAdapter(Activity activity, Context context, List<CustomNotificationButton> customNotificationButtons) {
        super(context, 0, customNotificationButtons);
        this.activity = activity;
        this.customNotificationButtons = customNotificationButtons;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        //Initialisation des SharedPreferences
        final SharedPreferences settings = getContext().getSharedPreferences("notification", 0);
        final SharedPreferences.Editor editor = settings.edit();

        //Récupération du CustomNotificationButton
        final CustomNotificationButton customNotificationButton = getItem(position);

        //Création d'une view si elle n'existe pas encore
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_notification_button, parent, false);
        }

        CustomNotificationButtonHolder viewHolder = (CustomNotificationButtonHolder) convertView.getTag();

        if(viewHolder == null){

            //Instanciation d'un CustomNotificationButtonHolder
            viewHolder = new CustomNotificationButtonHolder();
            viewHolder.content = convertView.findViewById(R.id.textViewNotificationButton);
            viewHolder.aSwitch = convertView.findViewById(R.id.switchNotificationButton);
            viewHolder.menuButton = convertView.findViewById(R.id.imageButtonNotificationButton);


            convertView.setTag(viewHolder);

            //Affichage du dialogue de configuration si on clique sur le boutton
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ConfigDialog(activity, customNotificationButton.getId(), (Switch) view.findViewById(R.id.switchNotificationButton)).show();
                }
            });

            //Activation ou annulation de la notification en fonction du changement d'état du switch
            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        NotificationManager.scheduleNotification(getContext(), customNotificationButton.getId(), customNotificationButton.getContent());
                    else
                        NotificationManager.cancelNotification(getContext(), customNotificationButton.getId());
                }
            });

            //Instanciation des menus
            final PopupMenu popupMenu = new PopupMenu(getContext(), viewHolder.menuButton, Gravity.START);
            popupMenu.inflate(R.menu.notification_button_menu);

            //Actions effectuées lorsqu'on clique sur un item du menu
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    //En fonction de l'id de l'item, l'action sera différente
                    switch (menuItem.getItemId()) {

                        //Action = modifier
                        case R.id.actionModifier:

                            //Initialisation du EditText
                            final EditText input = new EditText(getContext());
                            input.setHint("Entrez le contenu de la notification");

                            //Création du dialogue permettant d'entrer le contenu de la notification
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            //Création du CustomNotificationButton lorsqu'on valide
                            builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Sauvegarde des données dans les SharedPreferences
                                    editor.putString("notificationContent" + customNotificationButton.getId(), customNotificationButton.getContent());
                                    editor.commit();
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
                            break;

                        //Action = supprimer
                        case R.id.actionSupprimer:

                            //Annulation de la notification
                            NotificationManager.cancelNotification(getContext(), customNotificationButton.getId());

                            //Sauvegarde de l'état de la notification
                            editor.putBoolean("notificationButton" + customNotificationButton.getId(), false);
                            for(Days day : Days.values())
                                editor.putBoolean("notification" + customNotificationButton.getId() + day, false);
                            editor.putLong("alarmTime" + customNotificationButton.getId(), 0);
                            editor.commit();

                            //Suppression du boutton dans la liste
                            customNotificationButtons.remove(position);

                            //Actualisation de la ListView
                            CustomNotificationButtonAdapter.this.notifyDataSetChanged();

                            break;
                    }
                    return false;
                }
            });

            //Affichage du menu lorsqu'on clique sur le boutton
            viewHolder.menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu.show();
                }
            });

        }

        //Setup de l'état du switch en fonction de si la notification est activée ou non
        viewHolder.aSwitch.setChecked(settings.getBoolean("alarm" + customNotificationButton.getId(), false));

        //Mise en place du texte du boutton
        viewHolder.content.setText(customNotificationButton.getContent());

        return convertView;
    }

    private class CustomNotificationButtonHolder {
        public TextView content;
        public Switch aSwitch;
        public ImageButton menuButton;
    }

}
