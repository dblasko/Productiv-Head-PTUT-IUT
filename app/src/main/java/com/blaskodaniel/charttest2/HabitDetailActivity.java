package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HabitDetailActivity extends AppCompatActivity {

    private LineData getData(String habit, String year, String month) {
        // Prepares and returns the monthly data of an habit for the LineChart
        // TODO - use the DB

        List<Entry> entries = new ArrayList<>();
        for (int i = 1; i<31; i++) {
            int number = new Random().nextInt()%100;
            if (number < 0) number = -1 * number;
            entries.add(new Entry(i, number));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Lds1");
        // Styling
        lineDataSet.setLineWidth(1.75f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(2.5f);
        lineDataSet.setColor(Color.parseColor("#FFEB3B"));
        lineDataSet.setCircleColor(Color.parseColor("#FFEB3B"));
        lineDataSet.setHighLightColor(Color.parseColor("#FFEB3B"));
        lineDataSet.setDrawValues(false);

        // Create a data object with the values
        return new LineData(lineDataSet);
    }

    private void setupLineChart(LineChart lineChart, LineData data) {
        // Prepares the line chart display

        // Set circle hole same color as background
        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(Color.WHITE);
        lineChart.setBackgroundColor(Color.WHITE);
        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable / disable grid background
        // lineChart.setDrawHorizontalGrid(true);
        lineChart.setDrawGridBackground(false);
        //lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        //lineChart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        //lineChart.setViewPortOffsets(10, 0, 10, 0);

        // set chart data
        lineChart.setData(data);

        // get the legend (only possible after setting data) and deactivate it
        Legend l = lineChart.getLegend();
        l.setEnabled(false);


        // Set X axis
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(true);

        // Set left Y axis
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        // Hide right Y axis
        lineChart.getAxisRight().setEnabled(false);
        // -> Uncomment to show all Xs
        //lineChart.getXAxis().setLabelCount(30, true);
        
        lineChart.animateX(2500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent intent = getIntent();
        String habit = intent.getStringExtra("habit");

        // Display icon in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.app_icon_round);
        getSupportActionBar().setSubtitle("Détail sur l'habitude " + habit);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        // Get the lineChart reference
        LineChart chartDetail = findViewById(R.id.chartDetail);

        LineData data = getData("","","");
        setupLineChart(chartDetail, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
