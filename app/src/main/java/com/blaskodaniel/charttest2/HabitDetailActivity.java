package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HabitDetailActivity extends AppCompatActivity {

    PieChart chart;
    AlertDialog dialog;
    final String textToday = "Aujourd'hui";

    public double getAveragePercentage(String year, String month, String habit) {
        // Connects to the database and calculates & returns that average advancement %age

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

        return avg;
    }

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
        // Prepares & sets the pie chart display up

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

        double avg = getAveragePercentage(year, month, habit);

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
        chart.setCenterText(Double.toString(Math.round(avg)) + "%");
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
        // Moves the pie chart down the screen

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        int offset = (int)(height*0.20); /* percent to move */

        LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0,0,0,-offset);
        chart.setLayoutParams(rlParams);
    }

    public void setupAdvice(String year, String month, String habit) {
        // Sets up the advice card of the layout
        // By calculating the avg advancement & selecting a random advice based on that %age

        String[] low = new String[]{
                "Un petit coup de mou? Ne lâchez pas !",
                "Peut être avez-vous fixé un objectif trop haut ?",
                "Pensez à votre habitude tous les jours,\nla régualité est la clé de la réussite !",
                "Vous pouvez le faire ! Essayez d'être plus réguliers dorénavant.",
                "Pensez à régler une heure de rappel quotidien qui vous convient !",
                "Essayez de réfléchir à ce qui vous motive vraiment à suivre cette habitude.",
                "Accrochez vous ! Vous en êtes capables.",
                "Visualisez-vous suivant votre habitude idéalement : à vous d'en faire une réalité."
        };
        String[] mid = new String[]{
                "Pas mal! Vous êtes sur la bonne voie, accrochez-vous.",
                "Regardez le progrès que vous avez déjà fait ! Tenez bon.",
                "Quelle régularité ! Et si vous essayiez de passer au niveau supérieur ?",
                "C'est bien parti ! Restez sur cette lancée !",
                "Au vu de votre régularité, c'est bien parti pour intégrer cette habitude à votre vie !"
        };
        String[] high = new String[]{
                "Quelle assiduité ! Impressionnant.",
                "Continuez comme ça, c'est parfait !",
                "Votre régularité sur cette habitude est exemplaire.",
                "Si vous continuez comme ça, vous ne pourrez que vous améliorer !",
                "En étant si rigoureux, vous allez atteindre vos objectifs sans aucun doute !",
                "Essayez de maintenir ce score !",
                "Et si vous tentiez d'améliorer votre score de quelques pourcents encore ?"
        };

        double avg = getAveragePercentage(year, month, habit); // get avg value
        TextView adviceTextView = findViewById(R.id.tw_conseil); // get the textView to display the text
        Random r = new Random(); // nextInt gives int between 0 and param-1

        if (avg <= 40) {
            int i = r.nextInt(low.length);
            adviceTextView.setText(low[i]);
        } else if (avg <= 70) {
            int i = r.nextInt(mid.length);
            adviceTextView.setText(high[i]);
        } else {
            int i= r.nextInt(high.length);
            adviceTextView.setText(high[i]);
        }
    }

    public int getMonthlyDaysCount(String month, String year) {
        int monthInt = Integer.parseInt(month);
        int yearInt = Integer.parseInt(year);

        if (monthInt == 2) {
            if ((yearInt%4 == 0 && yearInt%100 != 0) || yearInt%400 == 0) return 29; // bissextile
            return 28;
        } else if (monthInt <= 7) {
            if (monthInt%2 == 0) return 30;
            return 31;
        } else {
            if (monthInt%2 == 0) return 31;
            return 30;
        }
    }

    public void showHabitInputDialog(String year, String month, String habit) {
        // Shows the dialog for habit input

        // Final vars for the inner class
        final String yearData = year;
        final String monthData = month;
        final String habitData = habit;

        // Prepare variables
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.input_habit_dialog, null);
        final Spinner daySpinner = mView.findViewById(R.id.daySpinner);
        Button buttonConfirm = mView.findViewById(R.id.buttonConfirm);
        Button buttonCancel = mView.findViewById(R.id.buttonCancel);
        TextView twHabitName = mView.findViewById(R.id.textViewAdv);
        TextView twUnit = mView.findViewById(R.id.textViewUnit);
        final EditText etAdvancement = mView.findViewById(R.id.editTextAdv);

        final HabitDao habitDao = new HabitDao(this);
        habitDao.open();
        // Get the daily unit
        String unit = habitDao.getMonthlyHabitUnit(year, month, habit);
        twUnit.setText(unit);
        twHabitName.setText("Habitude : " + habit);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - get values, insert OR update to the DB if for the day exists !
                String adv = etAdvancement.getText().toString();
                if (adv.isEmpty()) {
                    dialog.cancel(); // Exit if empty
                    return;
                }
                adv = adv.replace(',', '.');
                double advDouble = Double.parseDouble(adv);
                String when = daySpinner.getSelectedItem().toString();
                if (when.equals(textToday)) {
                    // Get current day
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    String day = date.substring(8);
                    when = day;
                }

                Habit insertion = new Habit(habitData, yearData, monthData, when, advDouble, null);
                habitDao.insertDailyAdv(insertion);

                // Update the UI with the new data
                setupUI(habitData, yearData, monthData);

                dialog.cancel();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        // Prepare data for the spinner
        int daysCount = getMonthlyDaysCount(month, year);
        String[] items = new String[daysCount+1];
        items[0] = textToday;
        for (int i = 1; i <= daysCount; i++){
            items[i] = Integer.toString(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        daySpinner.setAdapter(adapter);

        // Build the dialog
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public void setupUI(String habit, String year, String month ) {
        // Sets up the activity UI with the data

        // Draws the line chart widget
        // Get the lineChart reference
        LineChart chartDetail = findViewById(R.id.chartDetail);

        // Get the extras from the intent to get the data
        LineData data = getData(habit, year, month);
        setupLineChart(chartDetail, data);

        // Draws the pie chart widget
        setupPieChart(year, month, habit);
        moveOffScreen();

        // Fill the advice widget
        setupAdvice(year, month, habit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        // Get intent data from previous activity
        final Intent intent = getIntent();
        final String habit = intent.getStringExtra("habit");
        final String year = intent.getStringExtra("year");
        final String month = intent.getStringExtra("month");

        // Display icon in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.app_icon_round);
        getSupportActionBar().setSubtitle("Détail sur l'habitude " + habit);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Draw the line chart
        setupUI(habit, year, month);

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.detailFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHabitInputDialog(year, month, habit);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Enables going back from the activty
        onBackPressed();
        return true;
    }
}
