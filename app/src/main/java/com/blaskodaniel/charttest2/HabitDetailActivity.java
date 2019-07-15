package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HabitDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent intent = getIntent();
        String habit = intent.getStringExtra("habit");

        // Display icon in actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.app_icon_round);
        getSupportActionBar().setSubtitle("Détail sur l'habitude " + habit);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }
}
