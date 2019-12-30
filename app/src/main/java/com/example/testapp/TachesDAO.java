package com.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TachesDAO {

    private MySQLite mySQLite;
    private SQLiteDatabase db;

    public TachesDAO(Context context) {
        mySQLite = MySQLite.getInstance(context);
    }

    public void open() {
        db = mySQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void addTache(String content) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("content", content);

        db.insert("Taches",null, contentValues);
    }

    public List<String> getAllTaches() {
        ArrayList<String> taches = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM Taches", null);
        while(c.moveToNext()) {
            taches.add(c.getString(1));
        }
        c.close();
        return taches;
    }

}
