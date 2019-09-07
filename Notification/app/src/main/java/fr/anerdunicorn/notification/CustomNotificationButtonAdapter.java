package fr.anerdunicorn.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomNotificationButtonAdapter extends ArrayAdapter<CustomNotificationButton> {

    private Activity activity;
    private List<CustomNotificationButton> customNotificationButtons;
    private String[] days;

    public CustomNotificationButtonAdapter(Activity activity, Context context, List<CustomNotificationButton> customNotificationButtons) {
        super(context, 0, customNotificationButtons);
        this.activity = activity;
        this.customNotificationButtons = customNotificationButtons;
        days = new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CustomNotificationButton customNotificationButton = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_notification_button, parent, false);
        }

        CustomNotificationButtonHolder viewHolder = (CustomNotificationButtonHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new CustomNotificationButtonHolder();
            viewHolder.content = convertView.findViewById(R.id.textViewNotificationButton);
            viewHolder.aSwitch = convertView.findViewById(R.id.switchNotificationButton);
            viewHolder.deleteButton = convertView.findViewById(R.id.imageButtonNotificationButton);
            convertView.setTag(viewHolder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ConfigDialog(activity, customNotificationButton.getId()).show();
                }
            });
            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        NotificationManager.scheduleNotification(getContext(), customNotificationButton.getId(), customNotificationButton.getContent());
                    else
                        NotificationManager.cancelNotification(getContext(), customNotificationButton.getId());
                }
            });
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationManager.cancelNotification(getContext(), customNotificationButton.getId());

                    SharedPreferences settings = getContext().getSharedPreferences("notification", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("notificationButton" + customNotificationButton.getId(), false);
                    for(Days day : Days.values())
                        editor.putBoolean("notification" + customNotificationButton.getId() + day, false);
                    editor.putLong("alarmTime" + customNotificationButton.getId(), 0);
                    editor.commit();

                    customNotificationButtons.remove(position);
                    CustomNotificationButtonAdapter.this.notifyDataSetChanged();
                }
            });
        }

        SharedPreferences settings = getContext().getSharedPreferences("notification", 0);
        viewHolder.aSwitch.setChecked(settings.getBoolean("alarm" + customNotificationButton.getId(), false));
        viewHolder.content.setText(customNotificationButton.getContent());

        return convertView;
    }

    private class CustomNotificationButtonHolder {
        public TextView content;
        public Switch aSwitch;
        public ImageButton deleteButton;
    }

}
