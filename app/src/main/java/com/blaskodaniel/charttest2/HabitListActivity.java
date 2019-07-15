package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitListActivity extends AppCompatActivity {

    RecyclerView habitRecView;
    public static String month;
    public static String year;
    public static HabitDao habitDao;
    AlertDialog dialog;

    public static void deleteHabit(String habit) {
        // Deletes the habit at the current year & month - for the HabitListAdapter
        habitDao.delMonthlyHabit(habit, year, month);
    }

    public static String getLiteralMonth(String month) {
        // Returns the corresponding month name
        switch (month) {
            case "00" : return "Mois sauvegarde";
            case "01" : return "Janvier";
            case "02" : return "Février";
            case "03" : return "Mars";
            case "04" : return "Avril";
            case "05" : return "Mai";
            case "06" : return "Juin";
            case "07" : return "Juillet";
            case "08" : return "Août";
            case "09" : return "Septembre";
            case "10" : return "Octobre";
            case "11" : return "Novembre";
            case "12" : return "Décembre";
        }
        return "Erreur conversion mois";
    }

    public void updateView(String year, String month) {
        // Updates the view for given year/month

        // Get monthly data
        List<String> monthlyHabits = habitDao.getMonthlyHabits(year, month);
        // Change the adapter with monthly data - IF CRASH TRY REPLACE THE DATA IN ACTUAL ADAPTER OR NO DATA CHANGE
        HabitListAdapter adapter = new HabitListAdapter(monthlyHabits, this, year, month);
        habitRecView.setAdapter(adapter);
        // Set textview
        TextView dateText = findViewById(R.id.textViewMonthYear);
        dateText.setText(getLiteralMonth(month) + " " + year);
    }

    public void createHabitDialog() {
        // Prompts the user to create a habit

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.new_habit_dialog, null);
        final EditText et1 = mView.findViewById(R.id.editText1);
        final EditText et2 = mView.findViewById(R.id.editText2);
        final EditText et3 = mView.findViewById(R.id.editText3);
        Button buttonValider = mView.findViewById(R.id.button_valider);
        Button buttonAnnuler = mView.findViewById(R.id.button_annuler);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //  TODO
                // Insert habit
                String habitTitle = et1.getText().toString();
                String advText = et2.getText().toString();
                // => Double uses ".", parsing with , results in an app crash
                advText = advText.replace(',', '.');
                double adv = Double.parseDouble(advText);
                String unit = et3.getText().toString();

                habitDao.insertDailyAdv(new Habit(habitTitle, year, month, "00", adv, unit));
                ((HabitListAdapter)habitRecView.getAdapter()).habitsData = habitDao.getMonthlyHabits(year, month);
                habitRecView.getAdapter().notifyDataSetChanged();

                dialog.cancel();
            }
        });

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Display icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.app_icon_round);
        getSupportActionBar().setSubtitle("Liste des habitudes");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Get the recyclerView
        habitRecView = (RecyclerView)findViewById(R.id.habitRecyclerView);
        // Create the linear layout manager => positions the views in our list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        habitRecView.setLayoutManager(layoutManager);

        // Open a DB
        habitDao = new HabitDao(this);
        habitDao.open();

        // Get current month/year and init view at these values
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        year = date.substring(0, 4);
        month = date.substring(5, 7);

        updateView(year, month);

        // Change month with the arrows
        ImageView leftArrow = findViewById(R.id.leftArrow);
        ImageView rightArrow = findViewById(R.id.rightArrow);
        // Go back a month
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int monthInt = Integer.parseInt(month);
                if (monthInt == 1) {
                    // Go back a year and set to december if jan
                    int yearInt = Integer.parseInt(year);
                    yearInt--;
                    year = Integer.toString(yearInt);
                    month = "12";
                } else {
                    monthInt--;
                    if (monthInt < 10) month = "0" + Integer.toString(monthInt);
                    else month = Integer.toString(monthInt);
                }
                // Update the view
                updateView(year, month);
            }
        });

        // Forward a month
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int monthInt = Integer.parseInt(month);
                if (monthInt == 12) {
                    // Forward a year and set to jan if december
                    int yearInt = Integer.parseInt(year);
                    yearInt++;
                    year = Integer.toString(yearInt);
                    month = "01";
                } else {
                    monthInt++;
                    if (monthInt < 10) month = "0" + Integer.toString(monthInt);
                    else month = Integer.toString(monthInt);
                }
                // Update the view
                updateView(year, month);
            }
        });


        // Floating action button adds a new habit
        FloatingActionButton fab = findViewById(R.id.listFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD HABIT FOR THE MONTH
                createHabitDialog();
            }
        });

    }
}
