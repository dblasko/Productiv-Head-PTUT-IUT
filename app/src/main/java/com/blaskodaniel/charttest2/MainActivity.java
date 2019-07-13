package com.blaskodaniel.charttest2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.room.Room;


public class MainActivity extends AppCompatActivity {

    public void createChart(HabitDao dao, String month, String habit){
        CombinedChart chart = findViewById(R.id.chart2);//new CombinedChart(this/*getApplicationContext()*/);
        /*LinearLayout layout = findViewById(R.id.chart);
        layout.addView(chart);*/
        List<Habit> data = dao.getMonthlyDataOfHabit("2019","01", "Manger");

        List<Entry> liste = new ArrayList<>();
        for (Habit hab : data){
            // Convert string to int with parseInt
            liste.add(new Entry(Integer.parseInt(hab.getDay()), (int)hab.getAdvancement()));
        }

        LineDataSet lds = new LineDataSet(liste, "Avancée du jour");

        lds.setColor(Color.rgb(240, 238, 70));
        lds.setLineWidth(2.5f);
        lds.setCircleColor(Color.rgb(240, 238, 70));
        lds.setCircleRadius(5f);
        lds.setFillColor(Color.rgb(240, 238, 70));
        lds.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lds.setDrawValues(true);
        lds.setValueTextSize(10f);
        lds.setValueTextColor(Color.rgb(240, 238, 70));

        LineData lineData = new LineData(lds);

        List<BarEntry> listeBarre = new ArrayList<>();
        listeBarre.add(new BarEntry(0, 65));
        listeBarre.add(new BarEntry(1, 65));
        listeBarre.add(new BarEntry(2, 65));
        listeBarre.add(new BarEntry(3, 65));
        BarDataSet bds = new BarDataSet(listeBarre, "Moyenne à ce jour");

        bds.setColor(Color.rgb(60, 220, 78));
        bds.setValueTextColor(Color.rgb(60, 220, 78));
        bds.setValueTextSize(10f);
        bds.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData barData = new BarData(bds);
        barData.setBarWidth(0.45f);

        CombinedData cd = new CombinedData();
        cd.setData(barData);
        cd.setData(lineData);

        /* */
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);




        chart.setData(cd);
        chart.invalidate();


    }

    public void createSecondChart(){
        LineChart chart = new LineChart(this);
        LinearLayout layout = findViewById(R.id.chart);
        layout.addView(chart);

        chart.setMinimumHeight(500);

        List<Entry>  liste = new ArrayList<>();
        liste.add(new Entry(1, 30));
        liste.add(new Entry(2, 50));
        liste.add(new Entry(3,45));

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HabitDao dao = new HabitDao(this);
        dao.open();
        dao.insertDailyAdv(new Habit("Manger", "2019", "01", "01", 70));
        dao.insertDailyAdv(new Habit("Manger", "2019", "01", "02", 10));
        dao.insertDailyAdv(new Habit("Manger", "2019", "01", "03", 30));
        dao.insertDailyAdv(new Habit("Manger", "2019", "01", "04", 90));
        dao.insertDailyAdv(new Habit("Manger", "2019", "01", "05", 20));

        createChart(dao, "Janv", "Manger");

        String date = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(new Date());
        System.out.println("DATE : " + date);
        // Donne : 2019-04-13
        String an = date.substring(0, 3);
        String mois = date.substring(5, 6);
        String jour = date.substring(8);
    }
}

