package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TimerStatisticsDAO {
      /*
        Lien entre classe modèle & base de données - on définit la table, récupère base et méthodes accès
        CLASSE DAO EN SOMME
     */

    public static final String TABLE_NAME = "timerStatistique";
    public static final String KEY_TPS_TRAVAIL = "tpsTravail";
    public static final String  KEY_MONTH = "mois";
    public static final String KEY_JOUR = "jour";
    public static final String KEY_NB_SESSION_TVAIL = "nbSessionTvail";
    public static final String KEY_ANNEE = "annee";
    public static final String CREATE_TABLE_TIMERSTATISTIQUE = "CREATE TABLE " + TABLE_NAME
            + " (" +
            " " + KEY_TPS_TRAVAIL + " TEXT," +
            " " + KEY_MONTH + " TEXT," +
            " " + KEY_JOUR + " TEXT," +
            " " + KEY_NB_SESSION_TVAIL + " TEXT," +
            " " + KEY_ANNEE + " TEXT," +
            " " + " CONSTRAINT pk_timerStatistique PRIMARY KEY(tpsTravail, month, day)"
            + ");";
    private MySQLite mySQLiteBase; // Notre gestionnaire de bd
    private SQLiteDatabase db; // Notre instance de bd

    public TimerStatisticsDAO(Context context){
        // On récupère le manager SQLite
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        // Ouverture de la base en lecture/écriture - instance de la bd
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close(){
        // Fermeture de l'accès à la BD
        db.close();
    }

  /*
    public void modifNomHabit(String ancien, String nouveau){
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + KEY_TPS_TRAVAIL + " = '" + nouveau + "' WHERE "
                + KEY_TPS_TRAVAIL + " = '" + ancien + "'");
    }




    public List<String> getMonthlyHabits(String month){
        Cursor c = db.rawQuery("SELECT DISTINCT " + KEY_TPS_TRAVAIL + " FROM " + TABLE_NAME
                + " WHERE " + KEY_MONTH + " = '" + month + "'", null);

        List<String> monthlyHabits = new ArrayList<>();
        // Tant qu'il y a des lignes à traîter
        while (c.moveToNext()) {
            monthlyHabits.add(c.getString(c.getColumnIndex(KEY_TPS_TRAVAIL)));
        }

        return monthlyHabits;
    }

    public Cursor getMonthlyTable(String month){
        // Récupère curseur avec données du mois

        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE " + KEY_MONTH +" = '" + month + "'", null);
    }

    public Habit getDailyAdv(String habit, String month, int day){
        // Récup° une ligne

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_TPS_TRAVAIL + " = '" + habit
                + "' AND " + KEY_MONTH + " = '" + month + "' AND " + KEY_JOUR + " = '" + Integer.toString(day) + "'", null);

        // Récupère données du curseur et construit nouveau habit
        String habit_data = c.getString(c.getColumnIndex(KEY_TPS_TRAVAIL));
        String month_data = c.getString(c.getColumnIndex(KEY_MONTH));
        int day_data = c.getInt(c.getColumnIndex(KEY_JOUR));
        double adv_data = c.getDouble(c.getColumnIndex(KEY_NB_SESSION_TVAIL));

        Habit result = new Habit(habit_data, month_data, day_data, adv_data);
        return result;
    }


    public List<Habit> getMonthlyDataOfHabit(String month, String habit) {
        // Données sur un mois pour une habitude

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_MONTH + " = '" + month
                + "' AND " + KEY_TPS_TRAVAIL + " = '" + habit + "' ORDER BY DAY asc", null);

        List<Habit> result = new ArrayList<>();
        while (c.moveToNext()){
            String habit_data = c.getString(c.getColumnIndex(KEY_TPS_TRAVAIL));
            String month_data = c.getString(c.getColumnIndex(KEY_MONTH));
            int day_data = c.getInt(c.getColumnIndex(KEY_JOUR));
            double adv_data = c.getDouble(c.getColumnIndex(KEY_NB_SESSION_TVAIL));

            Habit ajout = new Habit(habit_data, month_data, day_data, adv_data);

            result.add(ajout);
        }
        return result;
    }
    */
}
