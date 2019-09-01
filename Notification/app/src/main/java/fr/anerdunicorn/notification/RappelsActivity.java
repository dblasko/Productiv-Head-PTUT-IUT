package fr.anerdunicorn.notification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RappelsActivity extends AppCompatActivity {

    public ListView listView;
    private CustomNotificationButtonAdapter adapter;
    public int id;
    public List<CustomNotificationButton> customNotifications;
    public List<Integer> notificationsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rappels);

        final SharedPreferences settings = getApplicationContext().getSharedPreferences("notification", 0);
        final SharedPreferences.Editor editor = settings.edit();

        notificationsId = new ArrayList<>();
        listView = findViewById(R.id.listView);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        customNotifications = new ArrayList<>();
        for(int i = 1; i < 100; i++){
            if(settings.getBoolean("notificationButton" + i, false))
                customNotifications.add(new CustomNotificationButton(i, settings.getString("notificationContent" + i, "")));
        }
        adapter = new CustomNotificationButtonAdapter(this, getApplicationContext(), customNotifications);
        RappelsActivity.this.listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(getApplicationContext());
                input.setHint("Entrez le contenu de la notification");

                AlertDialog.Builder builder = new AlertDialog.Builder(RappelsActivity.this);
                builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notificationsId.clear();
                        for(CustomNotificationButton customNotificationButton : customNotifications)
                            notificationsId.add(customNotificationButton.getId());
                        id = 1;
                        while(notificationsId.contains(id))
                            id++;
                        CustomNotificationButton customNotificationButton = new CustomNotificationButton(id, input.getText().toString());
                        customNotifications.add(customNotificationButton);
                        editor.putBoolean("notificationButton" + id, true);
                        editor.putString("notificationContent" + id, customNotificationButton.getContent());
                        editor.commit();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.setView(input);
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
