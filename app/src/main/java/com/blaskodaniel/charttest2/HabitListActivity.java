package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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

    public static void deleteHabit(String habit) {
        // Deletes the habit at the current year & month - for the HabitListAdapter
        habitDao.delMonthlyHabit(habit, year, month);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Get the recyclerView
        habitRecView = (RecyclerView)findViewById(R.id.habitRecyclerView);
        // Create the linear layout manager => positions the views in our list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        habitRecView.setLayoutManager(layoutManager);

        /*String date = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(new Date());
        String year = date.substring(0, 3);
        String month = date.substring(5, 6);*/

        year = "2019";
        month = "01";

        habitDao = new HabitDao(this);
        habitDao.open();

        habitDao.insertDailyAdv(new Habit("Manger", "2019", "01", "01", 70));
        habitDao.insertDailyAdv(new Habit("Manger", "2019", "01", "02", 10));
        habitDao.insertDailyAdv(new Habit("Manger", "2019", "01", "03", 30));
        habitDao.insertDailyAdv(new Habit("Manger", "2019", "01", "04", 90));
        habitDao.insertDailyAdv(new Habit("Manger", "2019", "01", "05", 20));
        habitDao.insertDailyAdv(new Habit("Boire", "2019", "01", "01", 70));

        List<String> monthlyHabits = habitDao.getMonthlyHabits("2019", "01");

        HabitListAdapter adapter = new HabitListAdapter(monthlyHabits);
        habitRecView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.listFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD HABIT FOR THE MONTH
                // Get date
                String date = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(new Date());
                String year = date.substring(0, 4);
                System.out.println("DATE :" + year);
                String month = date.substring(5, 7);
                // Insert habit
                String habitTitle = "Test"; // TODO - add dialog to get the habit
                // TODO - change force 01 to real month
                habitDao.insertDailyAdv(new Habit(habitTitle, year, "01", "00", 0));
                ((HabitListAdapter)habitRecView.getAdapter()).habitsData = habitDao.getMonthlyHabits(year, "01"); // TODO - change month to real
                habitRecView.getAdapter().notifyDataSetChanged();
            }
        });

    }
}
