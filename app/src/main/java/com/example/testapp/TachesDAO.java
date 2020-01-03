package com.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

    public void addTache(Taches tache) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", tache.getNom());
        contentValues.put("ID", tache.getId());
        contentValues.put("dateDeb", tache.getDateDebut());
        contentValues.put("dateFin", tache.getDateFin());
        contentValues.put("heure", tache.getHeure());
        db.insert("Taches",null, contentValues);
    }

    public Taches getTache(int id){

        Taches tache = null;

        Cursor c = db.rawQuery("SELECT * FROM Taches WHERE ID = " + id, null);
        if (c.moveToFirst()) {
            tache = new Taches(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        }
        c.close();
        return tache;

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
