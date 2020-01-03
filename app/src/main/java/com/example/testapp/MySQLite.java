package com.example.testapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

    private static MySQLite mySQLite;

    public MySQLite(Context context) {
        super(context, "Productivhead.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table Taches (ID integer primary key autoincrement, CONTENT text,heure text,dateDeb text,dateFin text,resume text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists Taches");
        onCreate(db);
    }

    public static synchronized MySQLite getInstance(Context context) {
        if (mySQLite == null) {
            mySQLite = new MySQLite(context);
        }
        return mySQLite;
    }
}
