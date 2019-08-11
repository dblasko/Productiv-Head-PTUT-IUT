package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import oxim.digital.rxanim.RxAnimationBuilder;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
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
        // Returns the corresponding month name as a String
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

    public String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public void refreshView(String year, String month) {
        // Populates the view with given year+month pairs data

        // Get the list of the month's habits
        List<String> monthlyHabits = habitDao.getMonthlyHabits(year, month);
        // Replace the adapter with one holding the new data
        HabitListAdapter adapter = new HabitListAdapter(monthlyHabits, this, year, month);
        habitRecView.setAdapter(adapter);
        // Re-animate view
        animateRecyclerView();
        // Update the month+year textView
        TextView dateText = findViewById(R.id.textViewMonthYear);
        dateText.setText(getLiteralMonth(month) + " " + year);
    }

    public void createHabitDialog() {
        // Creates and displays the dialog to create a new habit

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView = getLayoutInflater().inflate(R.layout.new_habit_dialog, null);

        final EditText et1 = mView.findViewById(R.id.editText1);
        final EditText et2 = mView.findViewById(R.id.editText2);
        final EditText et3 = mView.findViewById(R.id.editText3);
        Button buttonValider = mView.findViewById(R.id.button_valider);
        Button buttonAnnuler = mView.findViewById(R.id.button_annuler);

        // Add boom animation to both buttons
        new Boom(buttonValider);
        new Boom(buttonAnnuler);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert habit
                String habitTitle = et1.getText().toString();
                String advText = et2.getText().toString();
                if (advText.isEmpty() || habitTitle.isEmpty() ) {
                    dialog.cancel(); // Exit if empty
                    return;
                }
                // => Double uses ".", parsing with , results in an app crash
                advText = advText.replace(',', '.');
                double adv = Double.parseDouble(advText);
                String unit = et3.getText().toString();

                habitDao.insertDailyAdv(new Habit(habitTitle, year, month, "00", adv, unit)); // Day 0 is here to remember the habit + to know its unit
                ((HabitListAdapter) habitRecView.getAdapter()).setHabitsData(habitDao.getMonthlyHabits(year, month)); // We update the adapters data once the new habit is added, this is enough since there is no need to refresh the entire view, only the list is changing
                habitRecView.getAdapter().notifyDataSetChanged();
                // Re animate
                animateRecyclerView();

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
        RxAnimationBuilder.animate(mView, 800).fadeIn();
        dialog.show();
    }

    public void animateRecyclerView(){
        // Re animates the recyclerview
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(habitRecView.getContext(), R.anim.layout_animation_fall_down);
        habitRecView.setLayoutAnimation(controller);
        habitRecView.scheduleLayoutAnimation();
    }

    public void customizeActionBar(){
        // Customizes the actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.app_icon_round);
        getSupportActionBar().setSubtitle("Liste des habitudes");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void setupMonthNavigation(){

        // Sets up the month navigation on click on the arrows
        ImageView leftArrow = findViewById(R.id.leftArrow);
        ImageView rightArrow = findViewById(R.id.rightArrow);
        // Add boom animation
        new Boom((View)leftArrow);
        new Boom((View)rightArrow);

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
                // Update the view with new data
                refreshView(year, month);
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
                // Update the view with new data
                refreshView(year, month);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        customizeActionBar();

        setupMonthNavigation();

        // Get a DAO instance & open DB
        habitDao = new HabitDao(this);
        habitDao.open();

        // Create the linear layout manager => positions the views in our list
        habitRecView = findViewById(R.id.habitRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        habitRecView.setLayoutManager(layoutManager);

        // Get current month/year and init view at these values
        String date = getCurrentDate();
        year = date.substring(0, 4);
        month = date.substring(5, 7);
        refreshView(year, month);

        // Setup floating action button -> displays new habit dialog
        final FloatingActionButton fab = findViewById(R.id.listFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD HABIT FOR THE MONTH
                ObjectAnimator.ofFloat(fab, "rotation", 0f, 720f).setDuration(1200).start();
                createHabitDialog();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        animateRecyclerView();
    }
}
