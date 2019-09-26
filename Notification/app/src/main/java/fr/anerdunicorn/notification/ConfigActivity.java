package fr.anerdunicorn.notification;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class ConfigActivity extends AppCompatActivity {

    private ConstraintLayout layoutRepeatable;
    private ConstraintLayout layoutDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        layoutRepeatable = findViewById(R.id.layoutRepeatable);
        layoutDate = findViewById(R.id.layoutDate);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                final RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if(radioButton.getText().equals("Date")){
                    final Calendar calendar = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(ConfigActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            layoutRepeatable.setVisibility(View.INVISIBLE);
                            TextView t = findViewById(R.id.textViewDate);
                            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                            t.setText("Date choisie : " + datePicker.getDayOfMonth() + " " + month + " " + datePicker.getYear());
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            RadioButton r = findViewById(R.id.radioButtonRepeatable);
                            r.setChecked(true);
                        }
                    });
                    dpd.show();
                }
                else{
                    layoutDate.setVisibility(View.INVISIBLE);
                    layoutRepeatable.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
