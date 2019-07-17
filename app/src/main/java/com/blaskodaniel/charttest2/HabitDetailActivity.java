package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HabitDetailActivity extends AppCompatActivity {

    PieChart chart;

    private LineData getData(String habit, String year, String month) {
        // Prepares and returns the monthly data of an habit for the LineChart

        HabitDao habitDao = new HabitDao(this);
        habitDao.open();
        // Retrieve the data for the graph from the DB
        Map<Integer, Double> dbData = habitDao.getMonthlyHabitAdvancementPercentages(year, month, habit);

        List<Entry> entries = new ArrayList<>();
        // Iterate map and create entries for the graph
        for (Map.Entry<Integer, Double> mapEntry : dbData.entrySet()) {
            entries.add(new Entry(mapEntry.getKey(), mapEntry.getValue().floatValue())); // entry takes a float and not a double as a parameter
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
        lineChart.getXAxis().setAxisLineColor(Color.parseColor("#AC9F29")); // give more width, dashed?
        //lineChart.getXAxis().enableAxisLineDashedLine(1, 1, );
        // fixed intervals
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1);
        // Set left Y axis
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(true);
        lineChart.getAxisLeft().setAxisMinimum(0); // to start at 90%
        lineChart.getAxisLeft().setAxisLineColor(Color.parseColor("#AC9F29")); // give more width, dashed?
        // Hide right Y axis
        lineChart.getAxisRight().setEnabled(false);
        // -> Uncomment to show all Xs
        //lineChart.getXAxis().setLabelCount(30, true);

        lineChart.animateX(2500);
    }

    public void setupPieChart(String year, String month, String habit) {
        chart = findViewById(R.id.chartSumup);
        chart.setBackgroundColor(Color.WHITE);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false); // éventuellement maj et afficher description

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        //chart.setMinimumHeight(90);

        // interactions
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        chart.setMaxAngle(180f); // HALF CHART
        chart.setRotationAngle(180f);
        chart.setCenterTextOffset(0, -20);

        // TODO - set data
        HabitDao habitDao = new HabitDao(this);
        habitDao.open();
        Map<Integer, Double> mData = habitDao.getMonthlyHabitAdvancementPercentages(year, month, habit);
        // Calculate avg advancement percentage
        List<Double> percentages = new ArrayList<>(mData.values());
        double sum = 0;
        for (double d : percentages) {
            sum += d;
        }
        double avg = sum/percentages.size();
        // Prepare pie data
        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry((float)avg));
        values.add(new PieEntry((float)(100 - avg)));
        PieDataSet dataSet = new PieDataSet(values, "Moyenne du mois");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // Couleur en fonction de la moyenne TODO - switch et juste variable couleur - mettre %age à cette couleur aussi
        int color;
        if (avg <= 40) color = Color.parseColor("#ef5350");
        else if (avg <= 70) color = Color.parseColor("#AC9F29");
        else color = Color.parseColor("#00e676");

        dataSet.setColors(color, Color.WHITE); // set the 2 colors,
        chart.setCenterText(Double.toString(avg) + "%");
        chart.setDrawCenterText(true);
        chart.setCenterTextSize(21);
        chart.setCenterTextColor(color);


        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        chart.setData(data);

        chart.invalidate();

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    public void moveOffScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        int offset = (int)(height*0.20); /* percent to move */

        LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0,0,0,-offset);
        chart.setLayoutParams(rlParams);
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


        // TODO - remove - for testing purposes
        HabitDao habitDao = new HabitDao(this);
        habitDao.open();
        //habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "00", 5, null));
        habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "01", 3, null));
        habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "02", 5, null));
        habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "03", 2, null));
        habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "04", 3, null));
        habitDao.insertDailyAdv(new Habit("Testage", "2019", "07", "06", 6, null));

        // Get the lineChart reference
        LineChart chartDetail = findViewById(R.id.chartDetail);

        // Get the extras from the intent to get the data
        LineData data = getData(habit, intent.getStringExtra("year"), intent.getStringExtra("month"));
        setupLineChart(chartDetail, data);

        // Display pie chart
        setupPieChart(intent.getStringExtra("year"), intent.getStringExtra("month"), habit);
        moveOffScreen();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
