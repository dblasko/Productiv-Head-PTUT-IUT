package com.example.produtivhead.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// CF https://fr.jeffprod.com/blog/2015/utilisation-d-une-base-sqlite-sous-android/
public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite";
    // TODO : UPDATE CHAQUE MISE EN PROD OU CRASH !!!!!
    private static final int DATABASE_VERSION = 1;
    // Static -> pattern Singleton, instancie que si pas encore instancié
    private static MySQLite sInstance;

    private MySQLite(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MySQLite getInstance(Context context){
        // CF pattern singleton
        if (sInstance == null) sInstance = new MySQLite(context);
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // S'exécute à la création de la BD
        // => Donc on exécute la/les requêtes pour créer la table
        sqLiteDatabase.execSQL(HabitDao.CREATE_TABLE_HABITS); // Exécute create table habit
        sqLiteDatabase.execSQL(TimerStatisticsDAO.CREATE_TABLE_TIMERSTATISTIQUE);
        sqLiteDatabase.execSQL("Create table Notifications (ID integer primary key autoincrement, TITLE text, CONTENT text, REPEATABLE integer, HOUR integer, MINUTE integer, DAYS integer, YEAR integer, MONTH integer, DAY integer, ACTIVE integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // S'exécute à la màj de la BD
        // => Appelée chaque fois que DATABASE_VERSION est incr°
        /* Ici on recrée la base àc haque fois */
        onCreate(sqLiteDatabase);
    }
}
