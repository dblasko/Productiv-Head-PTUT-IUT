package com.example.produtivhead.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.produtivhead.Habits.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// CF https://fr.jeffprod.com/blog/2015/utilisation-d-une-base-sqlite-sous-android/
public class HabitDao {
    /*
        Lien entre classe modèle & base de données - on définit la table, récupère base et méthodes accès
        CLASSE DAO EN SOMME
     */

    // TODO - si problèmes/crash - changer constructeurs où unit demandé de null à ""

    public static final String TABLE_NAME = "habits";
    public static final String KEY_HABIT = "habit";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MONTH = "month";
    public static final String KEY_DAY = "day";
    public static final String KEY_ADVANCEMENT = "advancement";
    public static final String KEY_UNIT = "unit";
    public static final String CREATE_TABLE_HABITS = "CREATE TABLE " + TABLE_NAME
            + " (" +
            " " + KEY_HABIT + " TEXT," +
            " " + KEY_YEAR + " TEXT," +
            " " + KEY_MONTH + " TEXT," +
            " " + KEY_DAY + " TEXT," +
            " " + KEY_ADVANCEMENT + " REAL," +
            " " + KEY_UNIT + " TEXT, " +
            " " + " CONSTRAINT pk_habits PRIMARY KEY(habit, year, month, day)"
            + ");";
    private MySQLite mySQLiteBase; // Notre gestionnaire de bd
    private SQLiteDatabase db; // Notre instance de bd

    public HabitDao(Context context){
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

    public void insertDailyAdv(Habit habit){
        // Insérer une ligne dans habit

        ContentValues values = new ContentValues();
        values.put(KEY_HABIT, habit.getHabit());
        values.put(KEY_YEAR, habit.getYear());
        values.put(KEY_MONTH, habit.getMonth());
        values.put(KEY_DAY, habit.getDay());
        values.put(KEY_ADVANCEMENT, habit.getAdvancement());
        values.put(KEY_UNIT, habit.getUnit());

        db.insert(TABLE_NAME, null, values);
    }

    public boolean shouldUpdate(Habit habit){
        // Returns true if an update should be performed and not an insert for given line

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " +
                KEY_HABIT + " = '" + habit.getHabit() + "' AND " + KEY_YEAR + " = '" + habit.getYear() +
                "' AND " + KEY_MONTH + " = '" + habit.getMonth() + "' AND " + KEY_DAY + " = '" +
                habit.getDay() + "'", null);

        c.moveToFirst();
        int count = c.getInt(0); // first and only column + line

        c.close();

        return !(count==0);
    }

    public void updateDailyAdv(Habit habit){
        // Mettre à jour une ligne dans habit

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + KEY_ADVANCEMENT +  " = " + habit.getAdvancement() + " WHERE " +
                KEY_HABIT + " = '" + habit.getHabit() + "' AND " + KEY_YEAR + " = '" + habit.getYear() +
                "' AND " + KEY_MONTH + " = '" + habit.getMonth() + "' AND " + KEY_DAY + " = '" +
                habit.getDay() + "'");
    }

    public void modifNomHabit(String ancien, String nouveau){
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + KEY_HABIT + " = '" + nouveau + "' WHERE "
         + KEY_HABIT + " = '" + ancien + "'");
    }

    public void modifAdv(String habit, String year, String month, String day, double newAdv){
        // Modif l'avancement d'une habitude à un jour

        ContentValues values = new ContentValues();
        values.put(KEY_ADVANCEMENT, newAdv);

        String where = KEY_HABIT+" = ? AND "+KEY_MONTH+" = ? AND "+KEY_DAY+" = ? AND "+KEY_YEAR+" = ?";
        String[] whereArgs = {habit, month, day, year};

        db.update(TABLE_NAME, values, where, whereArgs);
    }

    public void delMonthlyHabit(String habit, String year, String month){
        // Suppr une habitude sur un mois

        String where = KEY_HABIT+" = ? AND "+KEY_MONTH+" = ? AND "+KEY_YEAR+" = ?";
        String[] whereArgs = {habit, month, year};

        db.delete(TABLE_NAME, where, whereArgs);
    }

    public List<String> getMonthlyHabits(String year, String month){
        Cursor c = db.rawQuery("SELECT DISTINCT " + KEY_HABIT + " FROM " + TABLE_NAME
                + " WHERE " + KEY_MONTH + " = '" + month + "' AND " + KEY_YEAR + " = '" + year + "'", null);

        List<String> monthlyHabits = new ArrayList<>();
        // Tant qu'il y a des lignes à traîter
        while (c.moveToNext()) {
            monthlyHabits.add(c.getString(c.getColumnIndex(KEY_HABIT)));
        }

        return monthlyHabits;
    }

    public Cursor getMonthlyTable(String year, String month){
        // Récupère curseur avec données du mois

        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE " + KEY_MONTH +" = '" + month + "' AND " + KEY_YEAR + " = '" + year + "'", null);
    }

    public Habit getDailyAdv(String habit, String year, String month, String day){
        // Récup° une ligne

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_HABIT + " = '" + habit
                + "' AND " + KEY_MONTH + " = '" + month + "' AND " + KEY_DAY + " = '" + day + "' AND " + KEY_YEAR + " = '"+year+"'", null);

        // Récupère données du curseur et construit nouveau habit
        String habit_data = c.getString(c.getColumnIndex(KEY_HABIT));
        String year_data = c.getString(c.getColumnIndex(KEY_YEAR));
        String month_data = c.getString(c.getColumnIndex(KEY_MONTH));
        String day_data = c.getString(c.getColumnIndex(KEY_DAY));
        double adv_data = c.getDouble(c.getColumnIndex(KEY_ADVANCEMENT));

        c.close();

        Habit result = new Habit(habit_data, year_data, month_data, day_data, adv_data, null);
        return result;
    }


    public List<Habit> getMonthlyDataOfHabit(String year, String month, String habit) {
        // Données sur un mois pour une habitude

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_MONTH + " = '" + month
                + "' AND " + KEY_HABIT + " = '" + habit + "' AND "+ KEY_YEAR+ " = '" + year + "' ORDER BY DAY asc", null);

        List<Habit> result = new ArrayList<>();
        while (c.moveToNext()){
            String habit_data = c.getString(c.getColumnIndex(KEY_HABIT));
            String year_data = c.getString(c.getColumnIndex(KEY_YEAR));
            String month_data = c.getString(c.getColumnIndex(KEY_MONTH));
            String day_data = c.getString(c.getColumnIndex(KEY_DAY));
            double adv_data = c.getDouble(c.getColumnIndex(KEY_ADVANCEMENT));
            String unit_data = c.getString(c.getColumnIndex(KEY_UNIT));

            Habit ajout = new Habit(habit_data, year_data, month_data, day_data, adv_data, unit_data);

            result.add(ajout);
        }
        return result;
    }

    public double getObjective(String year, String month, String habit) {
        // Gets the user's daily objective for the habit for the month

        Cursor c = db.rawQuery("SELECT advancement FROM " + TABLE_NAME +
                " WHERE year = '"+year+"' AND month='"+month+"' AND habit='"+habit+"' AND day == '00'", null );
        c.moveToFirst();
        String advancementString = c.getString(c.getColumnIndex(KEY_ADVANCEMENT));
        return Double.parseDouble(advancementString);
    }

    public Map<Integer, Double> getMonthlyHabitAdvancementPercentages(String year, String month, String habit) {
        // Returns the needed data for the line chart

        Cursor c = db.rawQuery("SELECT day, advancement FROM " + TABLE_NAME +
                " WHERE year = '"+year+"' AND month='"+month+"' AND habit='"+habit+"' AND day != '00' ORDER BY day ASC", null);

        Map<Integer, Double> result = new TreeMap<>(); // ! Treemap to avoid plotting in the bad y order issue
        while (c.moveToNext()) {
            String dayString = c.getString(c.getColumnIndex(KEY_DAY));
            int day = Integer.parseInt(dayString);
            String advancementString = c.getString(c.getColumnIndex(KEY_ADVANCEMENT));
            double advancement = Double.parseDouble(advancementString);
            double objective = getObjective(year, month, habit);

            // %age
            result.put(day, advancement/objective * 100);
        }

        return result;

    }

    public String getMonthlyHabitUnit(String year, String month, String habit) {
        // Returns the unit of a given habit at a given month+year

        Cursor c = db.rawQuery("SELECT unit FROM " + TABLE_NAME + " WHERE " + KEY_MONTH + " = '" + month + "' AND " + KEY_YEAR + " = '" + year + "' AND " +
                KEY_HABIT + " = '" + habit + "' AND " + KEY_DAY + " = '00'", null);
        String result;
        if (c.moveToNext()) result = c.getString(c.getColumnIndex(KEY_UNIT));
        else result = " ";
        return result;
    }





}
