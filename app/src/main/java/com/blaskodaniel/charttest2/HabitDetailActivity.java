package com.blaskodaniel.charttest2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import oxim.digital.rxanim.RxAnimationBuilder;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class HabitDetailActivity extends AppCompatActivity {

    PieChart chart;
    AlertDialog dialog;
    final String textToday = "Aujourd'hui";
    // We store the current habit data for the share functionnality
    double shareHabitAvgPerf;
    String shareHabitName;


    public int getMonthlyDaysCount(String month, String year) {
        // Returns monthly days count
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

    public String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public double getAveragePercentage(String year, String month, String habit) {
        // Calculates & returns the average advancement %age for given habit + month/year
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
        shareHabitAvgPerf = avg;

        return avg;
    }

    private LineData getLineChartData(String habit, String year, String month) {
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
        lineChart.getAxisLeft().setValueFormatter(new PercentFormatter());
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
        if (avg > 100) { // To fix the bug if the avg is > 100% the graph makes multiple cycles
            values.add(new PieEntry(100f));
            values.add(new PieEntry(0f));
        } else {
            values.add(new PieEntry((float) avg));
            values.add(new PieEntry((float) (100 - avg)));
        }
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

        // Setup view animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        adviceTextView.startAnimation(fadeOut);
        fadeIn.setDuration(1200);
        fadeIn.setFillAfter(true);

        // Set textView content based on the percentage
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

        // Start view animation
        adviceTextView.startAnimation(fadeIn);
        fadeOut.setDuration(1200);
        fadeOut.setFillAfter(true);
    }

    public void showHabitInputDialog(String year, String month, String habit) {
        // Builds & shows the dialog for habit input

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

        // Animate buttons
        new Boom((View)buttonCancel);
        new Boom((View)buttonConfirm);

        final HabitDao habitDao = new HabitDao(this);
        habitDao.open();

        // Get & set the daily unit
        String unit = habitDao.getMonthlyHabitUnit(year, month, habit);
        twUnit.setText(unit);

        twHabitName.setText("Habitude : " + habit);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String adv = etAdvancement.getText().toString();
                if (adv.isEmpty()) {
                    dialog.cancel(); // Exit if empty
                    return;
                }
                adv = adv.replace(',', '.'); // Doubles use . or else throw an exception
                double advDouble = Double.parseDouble(adv);
                String when = daySpinner.getSelectedItem().toString();
                if (when.equals(textToday)) {
                    // Get current day
                    String date = getCurrentDate();
                    when = date.substring(8); // day part
                }

                // Insert the advancement to the DB
                Habit insertion = new Habit(habitData, yearData, monthData, when, advDouble, null);
                if (habitDao.shouldUpdate(insertion)){ // Returns true if a line for given habit+date already exists, so we update
                    habitDao.updateDailyAdv(insertion);
                } else {
                    habitDao.insertDailyAdv(insertion); // Else we insert the new line into the DB
                }

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

        // Prepare & set the data for the spinner
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
        RxAnimationBuilder.animate(mView, 800).fadeIn();
        dialog.show();
    }

    public void setupUI(String habit, String year, String month ) {
        // Sets up the activity UI with the data (fills the 3 cardViews)

        // Draw the line chart card
        LineChart chartDetail = findViewById(R.id.chartDetail);
        LineData data = getLineChartData(habit, year, month);
        setupLineChart(chartDetail, data);

        // Draw the pie chart card
        setupPieChart(year, month, habit);
        moveOffScreen();

        // Fill the advice card
        setupAdvice(year, month, habit);
    }

    public void customizeActionBar(String habitName) {
        // Customizes the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle("Détail sur l'habitude " + habitName);
        TextView tw = findViewById(R.id.nav_bar_title);
        tw.setText(habitName);
    }

    private Toolbar toolbar;
    private WorkModeManager wmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        /* */
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TextView titre_barre = findViewById(R.id.nav_bar_title);
        titre_barre.setText("Productiv'Head");

        wmm = new WorkModeManager(this);
        wmm.askForNotificationPermission();
        Switch switchWorkMode = findViewById(R.id.switch_work_mode);
        switchWorkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wmm.enableWorkMode(b);
            }
        });

        /* */

        // Get intent data from previous activity
        final Intent intent = getIntent();
        final String habit = intent.getStringExtra("habit");
        final String year = intent.getStringExtra("year");
        final String month = intent.getStringExtra("month");
        shareHabitName = habit; // TODO - cleanup and only use this and not params?

        customizeActionBar(habit);

        setupUI(habit, year, month);

        // Animate the cards
        LinearLayout layout = findViewById(R.id.linearLayout);
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(layout.getContext(), R.anim.layout_animation_fall_up);
        layout.setLayoutAnimation(controller);
        layout.scheduleLayoutAnimation();

        // Setup FAB to show the add progress dialog
        final FloatingActionButton fab = findViewById(R.id.detailFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofFloat(fab, "rotation", 0f, 720f).setDuration(1200).start();
                showHabitInputDialog(year, month, habit);
            }
        });

        // Re-generate a random advice on click on the cardView
        CardView adviceCard = findViewById(R.id.messageCard);
        new Boom((View)adviceCard);
        adviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupAdvice(year, month, habit);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_habit_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Allows us to share on click on the share menu item
        switch (item.getItemId()) {
            case R.id.mShare:
                Intent  i = new Intent(
                        android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(
                        android.content.Intent.EXTRA_TEXT, "Ce mois-ci, j'ai atteint mon objectif pour l'habitude " + shareHabitName + " à " + Double.toString(shareHabitAvgPerf) + "%!\n Téléchargez Productiv'Head sur le PlayStore et améliorez vous aussi.");
                startActivity(Intent.createChooser(
                        i,
                        "Partager votre avancée..."));
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        // Enables going back from the activty
        onBackPressed();
        return true;
    }
}
